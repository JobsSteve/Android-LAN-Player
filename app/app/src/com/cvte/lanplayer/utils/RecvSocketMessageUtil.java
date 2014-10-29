package com.cvte.lanplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;

/**
 * ���������豸��socket��Ϣ
 * 
 * @author JHYin
 * 
 */
public class RecvSocketMessageUtil {

	private final String TAG = "RecvSocketMessageUtil";

	private static Context mContext;
	private static RecvSocketMessageUtil instance = null;

	private static Thread mRecvThread = null;

	ServerSocket mServerSocket = null;
	List<Socket> mSocketList = new ArrayList<Socket>();

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private RecvSocketMessageUtil() {

		// ��������߳�û�г�ʼ��������г�ʼ��
		if (mRecvThread == null) {
			InitRecvThread();
			
			Log.d(TAG,"InitRecvThread");
		}

	}

	/**
	 * ��̬��������
	 */
	public static synchronized RecvSocketMessageUtil getInstance(Context context) {

		mContext = context;
		if (instance == null) {
			instance = new RecvSocketMessageUtil();
		}

		return instance;
	}

	/**
	 * �յ���Ӧ��IP��ַ�����ݣ�����й㲥ת����ȥ
	 * 
	 * @param str
	 */
	private void SendMessage(String str) {
		Intent intent = new Intent();

		Bundle data = new Bundle();
		data.putInt(GlobalData.GET_BUNDLE_COMMANT, GlobalData.RECV_MSG);
		data.putString(GlobalData.GET_BUNDLE_DATA, str);

		intent.putExtras(data);
		intent.setAction(GlobalData.RECV_LAN_SOCKET_MSG_ACTION);// action���������ͬ

		mContext.sendBroadcast(intent);
	}

	public void StartRecv() {

		// ����߳��Ѿ������ˣ������¿����߳�
		if (mRecvThread.getState() == Thread.State.NEW) {
			mRecvThread.start();
			
			Log.d(TAG,"StartRecv");
		}

	}

	/**
	 * ��ʼ�������߳�
	 */
	private void InitRecvThread() {
		mRecvThread = new Thread() {

			@Override
			public void run() {

				try {
					mServerSocket = new ServerSocket(
							GlobalData.SOCKET_TRANSMIT_PORT);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (true) {
					// �����ļ���
					Socket socket;
					try {
						socket = mServerSocket.accept();

						// �������֮�У������պ�����
						mSocketList.add(socket);
						InputStream nameStream = socket.getInputStream();
						InputStreamReader streamReader = new InputStreamReader(
								nameStream);
						BufferedReader br = new BufferedReader(streamReader);
						String recv_data = br.readLine();

						Log.d(TAG, "���Է����������ݣ�" + recv_data);
						HandleMessage(recv_data);

						br.close();
						streamReader.close();
						nameStream.close();

						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}

	public void StopRecv() throws IOException {
		if (mRecvThread != null) {
			mRecvThread.interrupt();
		}

		if (mServerSocket != null) {
			mServerSocket.close();
		}

		// ���������п��ܿ������߳�
		for (int i = 0; i < mSocketList.size(); i++) {
			if (mSocketList.get(i) != null) {
				mSocketList.get(i).close();
			}
		}
		
		Log.d(TAG,"StopRecv");
	}

	/**
	 * ��������������
	 */
	private void HandleMessage(String str) {
		if (str.startsWith(GlobalData.COMMAND_SEND_MSG)) {
			// �ض���Ϣλ�ٷ���
			SendMessage(str.substring(GlobalData.COMMAND_SEND_MSG.length()));
		}
		// else if()
	}

}
