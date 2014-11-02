package com.cvte.lanplayer.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * ���ͱ����������б�
 * 
 * @author JHYin
 * 
 */
public class SendLocalMusicListUtil {

	private final String TAG = "SendLocalMusicListUtil";

	private static Context mContext;
	private static SendLocalMusicListUtil instance = null;

	private Thread mSendThread = null;
	private Socket mSocket = null;

	private String mTargetIP = null;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private SendLocalMusicListUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized SendLocalMusicListUtil getInstance(
			Context context) {

		mContext = context;
		if (instance == null) {
			instance = new SendLocalMusicListUtil();
		}

		return instance;
	}

	/**
	 * �������ָ��IP�豸������Ϣ
	 * 
	 * musicList:Ҫ���͵������б� targetIP��Ŀ��IP
	 */
	public void SendMusicList(final SocketTranEntity musicList, String targetIP) {

		mTargetIP = targetIP;

		mSendThread = new Thread() {
			@Override
			public void run() {
				super.run();
				// �ر���������socket
				try {

					Log.d(TAG, "�����б����ڷ���");

					// ���ӵ���������
					mSocket = new Socket(mTargetIP,
							GlobalData.SOCKET_TRANSMIT_PORT);
					// ʹ��ObjectOutputStream��ObjectInputStream���ж������ݴ���
					ObjectOutputStream out = new ObjectOutputStream(
							mSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(
							mSocket.getInputStream());
					// ���ͻ��˵Ķ����������������������ȥ
					out.writeObject(musicList);
					out.flush();

					out.close();
					in.close();
					mSocket.close();

					Log.d(TAG, "�����б������");

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
