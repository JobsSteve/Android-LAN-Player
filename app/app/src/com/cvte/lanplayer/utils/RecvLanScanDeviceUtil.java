package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * ���������豸�ھ�����ɨ��ʱ�����Ӧ
 * 
 * ���ñ�־λ�������̵߳��ж� ����socket���й���ֻ���߳̽�����ʱ�����һЩ�������
 * 
 * 
 * 
 * 
 * @author JHYin
 * 
 * 
 * 
 */
public class RecvLanScanDeviceUtil {

	private static String TAG = "RecvLanScanDeviceUtil";

	private static Service mService;
	private static RecvLanScanDeviceUtil instance = null;

	// private Socket socket = null;
	private DatagramSocket udpSocket = null;
	private DatagramPacket udpPacket = null;

	private Thread mRecvThread;

	// �����̵߳ı�־λ
	private boolean isRunThread = false;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private RecvLanScanDeviceUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized RecvLanScanDeviceUtil getInstance(Service service) {

		mService = service;
		if (instance == null) {
			instance = new RecvLanScanDeviceUtil();

		}

		return instance;
	}

	// private void SendMessage(String str) {
	// Intent intent = new Intent();
	//
	// Bundle data = new Bundle();
	// data.putString(GlobalData.RECV_SCAN, str);
	//
	// intent.putExtras(data);
	// intent.setAction(GlobalData.IS_SCANED_ACTION);// action���������ͬ
	//
	// mService.sendBroadcast(intent);
	// }

	/**
	 * ��ʼ��������
	 */
	public void StartRecv() {

		// ��������߳�û�г�ʼ��������г�ʼ��
		if (mRecvThread == null) {
			InitRecvThread();

			Log.d(TAG, "InitRecvThread");
		}

		// ����߳��Ѿ������ˣ������¿����߳�
		if (mRecvThread.getState() == Thread.State.NEW) {

			isRunThread = true;
			mRecvThread.start();

			Log.d(TAG, "StartRecv");
		}

	}

	/**
	 * ��ʼ�������߳�
	 */
	private void InitRecvThread() {
		mRecvThread = new Thread(new Runnable() {

			@Override
			public void run() {

				/*
				 * ʹ��ͬ���������߳�ͬ��
				 * 
				 * ����ʹ��wait()��nodify()
				 * ��Ϊ�ڽ���synchronized()�Ĵ�����ʱ�򣬾ʹ�����������Ȼ�����û������
				 * ��ô��Ҫ���еȴ���������wait();
				 * ��synchronized()�Ĵ����ִ����ϣ��ͻ��Զ��ͷ�����������nodify()֪ͨ
				 */
				synchronized (GlobalData.UDP_SOCKET_LOCK) {

					byte[] data = new byte[256];
					try {
						udpSocket = new DatagramSocket(
								GlobalData.TranPort.UDP_PORT);
						udpPacket = new DatagramPacket(data, data.length);
					} catch (SocketException e1) {
						e1.printStackTrace();
					}
					while (isRunThread) { // �����̵߳ı�־λ

						try {
							udpSocket.receive(udpPacket);
						} catch (Exception e) {
						}
						if (null != udpPacket.getAddress()) {
							final String quest_ip = udpPacket.getAddress()
									.toString();
							final String codeString = new String(data, 0,
									udpPacket.getLength());

							Log.d(TAG, "�յ�IP��ַ��" + quest_ip + "��UDP����\n"
									+ "��ַ���룺" + codeString);

							// SendMessage("�յ�IP��ַ��" + quest_ip + "��UDP����\n"
							// + "��ַ���룺" + codeString + "\n\n");

							final String ip = udpPacket.getAddress().toString()
									.substring(1);

							// SendMessage("����socketͨ�ţ�" + ip + "\n");
							// Log.d(TAG, "����socketͨ�ţ�" + ip + "\n");

							// ����ͨ����Կ
							// socket = new Socket(ip,
							// GlobalData.TranPort.SOCKET_TRANSMIT_PORT);

							// ʵ�����������
							SocketTranEntity msg = new SocketTranEntity();
							msg.setmCommant(GlobalData.SocketTranCommand.COMMAND_LAN_ASK);
							// ���÷��ͷ�IP��ַ
							msg.setmSendIP(getIpAddress());
							// ���ý��շ�IP
							msg.setmRecvIP(ip);

							// ����ͨ����Կ
							// msg.setmMessage(et_context.getText().toString());

							// ֱ�ӷ�����Ϣ
							SendSocketMessageUtil.getInstance(mService)
									.SendMessage(msg, ip);

							Log.d(TAG, "��IP��" + ip + "����ȷ�����ݰ�");
						}
					}

				}

			}
		});
	}

	/**
	 * ֹͣ�������� ����̲߳������ֳ�
	 * 
	 * @throws IOException
	 */
	public void StopRecv() throws IOException {

		Log.d(TAG, "StopRecv");

		/*
		 * ���ֿ����߳���ֹ�ķ��������� 1.ʹ��interrupt 2.ʹ�ñ�־λ�����̵߳�whileѭ�������жϸñ�־λ��ͨ�����ı�־λ���߳�ͣ��
		 * 
		 * �������߳�����ӱ�־λ�ķ�������Ϊ�������������socket�رյȴ���
		 */

		/* ����1 */
		// if (mRecvThread != null) {
		// mRecvThread.interrupt();
		//
		// mRecvThread = null;
		// }

		/* ����2 */

		// ���Ʊ�־λ��ֹͣ�̵߳�ѭ��
		isRunThread = false;
		// ����ֱ�Ӹ�ֵΪnull��֪���ò���
		mRecvThread = null;

		// if (socket != null) {
		// socket.close();
		// }

		if (udpSocket != null) {
			udpSocket.close();

			// ����ֱ�Ӹ�ֵΪnull��֪���ò���
			udpSocket = null;
		}

	}

	/**
	 * ����LANɨ��ȷ�ϰ�
	 */
	private void SendLanAskPackbag(String targetIP) {

	}

	/**
	 * ��ȡ������IP��ַ
	 */
	private String getIpAddress() {
		WifiManager wifiManager = (WifiManager) mService
				.getSystemService(mService.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));

	}

}
