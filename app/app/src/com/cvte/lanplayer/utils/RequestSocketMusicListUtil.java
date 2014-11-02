package com.cvte.lanplayer.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * �������豸���ͻ�������б�����
 * 
 * @author JHYin
 * 
 */
public class RequestSocketMusicListUtil {

	private final String TAG = "GetSocketMusicListUtil";

	private static Context mContext;
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
	public static synchronized RequestSocketMusicListUtil getInstance(
			Context context) {

		mContext = context;
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

//					mSocket = new Socket(mTargetIP,
//							GlobalData.SOCKET_TRANSMIT_PORT);
//					OutputStream outputName = mSocket.getOutputStream();
//					OutputStreamWriter outputWriter = new OutputStreamWriter(
//							outputName);
//					BufferedWriter bwName = new BufferedWriter(outputWriter);
//					
//					//д�������ȡ�����б��ָ��
//					bwName.write(GlobalData.COMMAND_HEAD_REQUSET_MUSIC_LIST);
//					
//					bwName.close();
//					outputWriter.close();
//					outputName.close();
//					mSocket.close();
					
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
