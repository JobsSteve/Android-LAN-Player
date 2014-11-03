package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * �������豸���ͻ�������б�����
 * 
 * @author JHYin
 * 
 */
public class RequestSocketMusicListUtil {

	private final String TAG = "RequestSocketMusicListUtil";

	private static RequestSocketMusicListUtil instance = null;

	private Thread mSendThread = null;
	private Socket mSocket = null;

	private String mTargetIP = null;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private RequestSocketMusicListUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized RequestSocketMusicListUtil getInstance() {

		if (instance == null) {
			instance = new RequestSocketMusicListUtil();
		}

		return instance;
	}

	/**
	 * �������豸�����ȡ�������б�
	 * 
	 * @param targetIP
	 */
	public void RequestSocketMusicList(String targetIP) {

		mTargetIP = targetIP;

		mSendThread = new Thread() {
			@Override
			public void run() {
				super.run();
				// �ر���������socket
				try {

					// ʵ�����������
					SocketTranEntity msg = new SocketTranEntity();
					msg.setmCommant(GlobalData.COMMAND_REQUSET_MUSIC_LIST);

					// ���ӵ���������
					mSocket = new Socket(mTargetIP,
							GlobalData.SOCKET_TRANSMIT_PORT);
					// ʹ��ObjectOutputStream��ObjectInputStream���ж������ݴ���
					ObjectOutputStream out = new ObjectOutputStream(
							mSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(
							mSocket.getInputStream());
					// ���ͻ��˵Ķ����������������������ȥ
					out.writeObject(msg);
					out.flush();

					out.close();
					in.close();
					mSocket.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		mSendThread.start();

	}

	public void StopSendMsg() throws IOException {
		if (mSendThread != null) {
			mSendThread.interrupt();
		}

		if (mSocket != null) {
			mSocket.close();
		}
	}
}
