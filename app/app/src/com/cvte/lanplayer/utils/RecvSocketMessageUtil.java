package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * ���������豸��socket��Ϣ
 * 
 * ������շ����ı���Ϣ�������ȡ�����б��socket����ͨ�Ŷ�������
 * 
 * �����ְ������е����⣬�Ժ�Ľ���
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

			Log.d(TAG, "InitRecvThread");
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
		data.putInt(GlobalData.GET_BUNDLE_COMMANT, GlobalData.COMMAND_RECV_MSG);
		data.putString(GlobalData.GET_BUNDLE_DATA, str);

		intent.putExtras(data);
		intent.setAction(GlobalData.RECV_LAN_SOCKET_MSG_ACTION);// action���������ͬ

		mContext.sendBroadcast(intent);
	}

	public void StartRecv() {

		// ����߳��Ѿ������ˣ������¿����߳�
		if (mRecvThread.getState() == Thread.State.NEW) {
			mRecvThread.start();

			Log.d(TAG, "StartRecv");
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

						// �������ݵ��������������Ҫ��ObjectInputStream��ObjectOutputStream����
						ObjectInputStream in = new ObjectInputStream(
								socket.getInputStream());
						ObjectOutputStream out = new ObjectOutputStream(
								socket.getOutputStream());
						// ��ȡ�ͻ��˵Ķ���������
						SocketTranEntity songList = (SocketTranEntity) in
								.readObject();

						// �����յ�������
						HandleMessage(songList, socket.getInetAddress()
								.getHostAddress());

						// List cityList = songList.GetSongList();

						// InputStream nameStream = socket.getInputStream();
						// InputStreamReader streamReader = new
						// InputStreamReader(
						// nameStream);
						// BufferedReader br = new BufferedReader(streamReader);
						// String recv_data = br.readLine();
						//
						// Log.d(TAG, "���ԣ� "
						// + socket.getInetAddress().getHostAddress()
						// + " ����Ϣ" + recv_data);
						//
						// // ������յ�����Ϣ
						// HandleMessage(recv_data, socket.getInetAddress()
						// .getHostAddress());
						//
						// br.close();
						// streamReader.close();
						// nameStream.close();

						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
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

		Log.d(TAG, "StopRecv");
	}

	/**
	 * ��������������
	 * 
	 * str:��������ԭʼ�ı���Ϣ targetIP�����ͷ���IP��ַ
	 */
	private void HandleMessage(SocketTranEntity data, String targetIP) {

		// if (str.startsWith(GlobalData.COMMAND_HEAD_SEND_MSG)) { // �ı���Ϣͨ��
		//
		// // �յ��ı���Ϣ��
		// Log.d(TAG,
		// "�յ��ı�������Ϣ��"
		// + str.substring(GlobalData.COMMAND_HEAD_SEND_MSG
		// .length()));
		//
		// // �ض���Ϣλ�ٷ���
		// SendMessage(str
		// .substring(GlobalData.COMMAND_HEAD_SEND_MSG.length()));
		// }
		// // �����Ҫ�����ȡ�����������б�
		// else if (str.startsWith(GlobalData.COMMAND_HEAD_REQUSET_MUSIC_LIST))
		// {
		// Log.d(TAG, "�յ�IP: " + targetIP + " �����ȡ�����������б�");
		//
		// Intent intent = new Intent();
		//
		// Bundle data = new Bundle();
		// data.putInt(GlobalData.GET_BUNDLE_COMMANT,
		// GlobalData.COMMAND_REQUSET_MUSIC_LIST);
		// data.putString(GlobalData.GET_BUNDLE_DATA, targetIP);
		//
		// intent.putExtras(data);
		// intent.setAction(GlobalData.RECV_LAN_SOCKET_MSG_ACTION);//
		// action���������ͬ
		//
		// mContext.sendBroadcast(intent);
		// }

		int command = data.getmCommant();

		switch (command) {
		case GlobalData.COMMAND_RECV_MSG:
			Log.d(TAG,"�յ����ݰ���COMMAND_RECV_MSG");
			// �յ��ı���Ϣ��
			Log.d(TAG,
					"�յ��ı�������Ϣ��"
							+ data.getmMessage().substring(GlobalData.COMMAND_HEAD_SEND_MSG
									.length()));

			// �ض���Ϣλ�ٷ���
			SendMessage(data.getmMessage());
			
			
			break;

		case GlobalData.COMMAND_REQUSET_MUSIC_LIST:
			Log.d(TAG,"�յ����ݰ���COMMAND_REQUSET_MUSIC_LIST");

			break;

		case GlobalData.COMMAND_SEND_MUSIC_LIST:
			Log.d(TAG,"�յ����ݰ���COMMAND_SEND_MUSIC_LIST");

			break;

		}

	}

}
