package com.cvte.lanplayer.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.content.Context;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.entity.SocketTranEntity;

/**
 * ���������豸��socket��Ϣ
 * 
 * @author JHYin
 * 
 */
public class SendSocketMessageUtil {

	private final String TAG = "SendSocketMessageUtil";

	private static Context mContext;
	private static SendSocketMessageUtil instance = null;

	private Thread mSendThread = null;
	private Socket mSocket = null;

	private String mTargetIP = null;
	private String mSendData = null;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private SendSocketMessageUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized SendSocketMessageUtil getInstance(Context context) {

		mContext = context;
		if (instance == null) {
			instance = new SendSocketMessageUtil();
		}

		return instance;
	}

	/**
	 * �������ָ��IP�豸������Ϣ
	 * 
	 * @param str
	 */
	public void SendMessage(final SocketTranEntity data, String targetIP) {

		mTargetIP = targetIP;

		mSendThread = new Thread() {
			@Override
			public void run() {
				super.run();

				synchronized (GlobalData.TCP_SOCKET_LOCK) {
					try {

						// ���ӵ���������
						mSocket = new Socket(mTargetIP,
								GlobalData.TranPort.SOCKET_TRANSMIT_PORT);
						// ʹ��ObjectOutputStream��ObjectInputStream���ж������ݴ���
						ObjectOutputStream out = new ObjectOutputStream(
								mSocket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(
								mSocket.getInputStream());
						// ���ͻ��˵Ķ����������������������ȥ
						out.writeObject(data);
						out.flush();

						out.close();
						in.close();
						mSocket.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

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
