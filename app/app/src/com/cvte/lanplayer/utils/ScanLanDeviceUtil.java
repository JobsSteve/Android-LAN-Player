package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;

/**
 * ���������UDPɨ��
 * 
 * 
 * @author JHYin
 * 
 */
public class ScanLanDeviceUtil {

	private static String TAG = "ScanLanDeviceUtil";
	private static Context mContext;
	private static ScanLanDeviceUtil instance = null;


	private DatagramSocket udpSocket;

	private static final int MAX_DATA_PACKET_LENGTH = 40;
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private ScanLanDeviceUtil() {
	}

	/**
	 * ��̬��������
	 */
	public static synchronized ScanLanDeviceUtil getInstance(Context context) {

		mContext = context;
		if (instance == null) {
			instance = new ScanLanDeviceUtil();
		}

		return instance;
	}

	/**
	 * ֹͣɨ�� ��տ������̺߳�socket
	 * 
	 * @throws IOException
	 */
	public void StopScan() throws IOException {

		if (udpSocket != null) {
			udpSocket.close();
		}

	}

	/**
	 * ��ʼɨ��
	 * 
	 * ��ʼɨ��֮�󣬽��շ�Ҫ����һ��GlobalData.GET_SCAN_DATA_ACTION �Ĺ㲥���������ܽ�����������IP��ַ
	 */
	public void StartScan() {

		Log.d(TAG, "StartScan");

		// ÿ����һ�Σ��Ϳ���һ��UDP�߳̽���ɨ��
		new BroadCastUdpThread(getLocalIPAddress().toString()).start();

	}

	private String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}

	/**
	 * 
	 * UDPɨ�����
	 * 
	 * @author JHYin
	 * 
	 */
	public class BroadCastUdpThread extends Thread {
		private String dataString;
		private int BroadCastUdpCount = 10; // ������¼����Ĺ㲥����

		public BroadCastUdpThread(String dataString) {
			this.dataString = dataString;
		}

		public void run() {

			/*
			 * �رս��ն˿�ɨ�� �������synchronized()ͬ�������֮ǰ�������޷������
			 */
			StopLANRecv();

			/*
			 * ʹ��ͬ���������߳�ͬ��
			 * 
			 * ����ʹ��wait()��nodify() ��Ϊ�ڽ���synchronized()�Ĵ�����ʱ�򣬾ʹ�����������Ȼ�����û������
			 * ��ô��Ҫ���еȴ���������wait(); ��synchronized()�Ĵ����ִ����ϣ��ͻ��Զ��ͷ�����������nodify()֪ͨ
			 */
			synchronized (GlobalData.UDP_SOCKET_LOCK) {
				DatagramPacket dataPacket = null;
				try {
					udpSocket = new DatagramSocket(GlobalData.TranPort.UDP_PORT);

					dataPacket = new DatagramPacket(buffer,
							MAX_DATA_PACKET_LENGTH);
					byte[] data = dataString.getBytes();
					dataPacket.setData(data);
					dataPacket.setLength(data.length);
					dataPacket.setPort(GlobalData.TranPort.UDP_PORT);

					InetAddress broadcastAddr;

					broadcastAddr = InetAddress.getByName("255.255.255.255");
					dataPacket.setAddress(broadcastAddr);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

				try {
					// �����ι㲥 -- ���߲��÷�����ι㲥
					for (int i = 0; i < BroadCastUdpCount; i++) {
						udpSocket.send(dataPacket);

						Log.d(TAG, "send udpSocket in BroadCastUdpThread");

						// ����100ms
						sleep(100);
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				} finally {
					if (udpSocket != null) {
						udpSocket.close();
					}

				}

				// �����������ն˿�
				StartLANRecv();
			}
		}
	}

	/**
	 * ����ɨ��
	 */
	private void StartLANRecv() {
		Intent intent = new Intent();

		Bundle data = new Bundle();
		data.putInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT,
				GlobalData.LANScanCtrl.STARE_LAN_RECV);

		intent.putExtras(data);
		intent.setAction(GlobalData.LANScanCtrl.CTRL_LAN_RECV_ACTION);// action���������ͬ

		mContext.sendBroadcast(intent);
	}

	/**
	 * ֹͣɨ��
	 */
	private void StopLANRecv() {
		Intent intent = new Intent();

		Bundle data = new Bundle();
		data.putInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT,
				GlobalData.LANScanCtrl.STOP_LAN_RECV);

		intent.putExtras(data);
		intent.setAction(GlobalData.LANScanCtrl.CTRL_LAN_RECV_ACTION);// action���������ͬ

		mContext.sendBroadcast(intent);
	}

}
