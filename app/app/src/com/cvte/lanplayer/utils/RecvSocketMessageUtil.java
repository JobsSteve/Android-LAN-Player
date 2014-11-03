package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;
import com.cvte.lanplayer.service.RecvLanDataService;

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

	private static RecvSocketMessageUtil instance = null;

	private static Thread mRecvThread = null;

	ServerSocket mServerSocket = null;
	List<Socket> mSocketList = new ArrayList<Socket>();

	// �����յ���Ϣ��ص�
	private static RecvLanDataService mRkdService;

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
	public static synchronized RecvSocketMessageUtil getInstance(
			RecvLanDataService rkdService) {

		// mContext = context;
		mRkdService = rkdService;

		if (instance == null) {
			instance = new RecvSocketMessageUtil();
		}

		return instance;
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
							GlobalData.TranPort.SOCKET_TRANSMIT_PORT);
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

						// �ص�service�����յ�������
						mRkdService.handleSocketCommand(songList, socket
								.getInetAddress().getHostAddress());

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

	

	

}
