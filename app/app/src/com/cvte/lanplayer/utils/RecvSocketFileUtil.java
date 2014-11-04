package com.cvte.lanplayer.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.service.RecvLanDataService;

/**
 * ���������豸������ļ�
 * ������ͬʱ�������ļ�����Ҫ������̣߳�
 * 
 * 
 * 
 * @author JHYin
 * 
 */
public class RecvSocketFileUtil {

	private final String TAG = "RecvSocketFileUtil";
	private static RecvSocketFileUtil instance = null;

	private Thread mRecvFileThread;
	ServerSocket mServerSocket = null;

	// �����յ���Ϣ��ص�
	private static RecvLanDataService mRkdService;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private RecvSocketFileUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized RecvSocketFileUtil getInstance(
			RecvLanDataService rkdService) {

		// mContext = context;
		mRkdService = rkdService;

		if (instance == null) {
			instance = new RecvSocketFileUtil();
		}

		return instance;
	}

	/**
	 * �����ļ�����
	 */
	public void StartRecv() {

		// ����socket����
		try {
			mServerSocket = new ServerSocket(
					GlobalData.TranPort.SOCKET_FILE_TRANSMIT_PORT);
		} catch (Exception e) {
		}

		// ��ѯ���ռ���
		mRecvFileThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {// �����ļ�
					ReceiveFile();
				}
			}
		});
		mRecvFileThread.start();
	}

	/**
	 * ֹͣ�ļ�����
	 */
	public void StopRecv() {
		// ֹͣ�߳�
		if (mRecvFileThread != null) {
			mRecvFileThread.interrupt();
		}

	}

	// �����ļ�
	public void ReceiveFile() {
		try {
			// �����ļ���
			Socket name = mServerSocket.accept();
			InputStream nameStream = name.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(nameStream);
			BufferedReader br = new BufferedReader(streamReader);
			String fileName = br.readLine();
			br.close();
			streamReader.close();
			nameStream.close();
			name.close();
			// SendMessage(0, "���ڽ���:" + fileName);
			Log.d(TAG, "���ڽ���:" + fileName);

			// �����ļ�����
			Socket data = mServerSocket.accept();
			InputStream dataStream = data.getInputStream();
			String savePath = Environment.getExternalStorageDirectory()
					.getPath() + "/" + fileName;
			FileOutputStream file = new FileOutputStream(savePath, false);
			byte[] buffer = new byte[1024];
			int size = -1;
			while ((size = dataStream.read(buffer)) != -1) {
				file.write(buffer, 0, size);
			}
			file.close();
			dataStream.close();
			data.close();
			// SendMessage(0, fileName + " �������");
			Log.d(TAG, fileName + " �������");
			
			//�ص�recvService�ķ���
			mRkdService.RecvFileFromUtil(fileName);
			
		} catch (Exception e) {
			// SendMessage(0, "���մ���:\n" + e.getMessage());
			Log.d(TAG, "���մ���:\n" + e.getMessage());
		}
	}

}
