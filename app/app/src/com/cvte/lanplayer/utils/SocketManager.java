package com.cvte.lanplayer.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;

public class SocketManager {

	private final String TAG = "SocketManager";

	private static SocketManager instance = null;

	private ServerSocket server;
	private Handler handler = null;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private SocketManager() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized SocketManager getInstance() {

		if (instance == null) {
			instance = new SocketManager();
		}

		return instance;
	}

	void SendMessage(int what, Object obj) {
		if (handler != null) {
			Message.obtain(handler, what, obj).sendToTarget();
		}
	}

	public void StartRecv() {

		// ����socket����
		try {
			server = new ServerSocket(
					GlobalData.TranPort.SOCKET_FILE_TRANSMIT_PORT);
		} catch (Exception e) {
		}

		// ��ѯ���ռ���
		Thread receiveFileThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {// �����ļ�
					ReceiveFile();
				}
			}
		});
		receiveFileThread.start();
	}

	// �����ļ�
	public void ReceiveFile() {
		try {
			// �����ļ���
			Socket name = server.accept();
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
			Socket data = server.accept();
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
		} catch (Exception e) {
			// SendMessage(0, "���մ���:\n" + e.getMessage());
			Log.d(TAG, "���մ���:\n" + e.getMessage());
		}
	}

	public void SendFile(ArrayList<String> fileName, ArrayList<String> path,
			String ipAddress, int port) {
		try {
			for (int i = 0; i < fileName.size(); i++) {
				Socket name = new Socket(ipAddress, port);
				OutputStream outputName = name.getOutputStream();
				OutputStreamWriter outputWriter = new OutputStreamWriter(
						outputName);
				BufferedWriter bwName = new BufferedWriter(outputWriter);
				bwName.write(fileName.get(i));
				bwName.close();
				outputWriter.close();
				outputName.close();
				name.close();
				// SendMessage(0, "���ڷ���" + fileName.get(i));
				Log.d(TAG, "���ڷ���" + fileName.get(i));

				Socket data = new Socket(ipAddress, port);
				OutputStream outputData = data.getOutputStream();
				FileInputStream fileInput = new FileInputStream(path.get(i));
				int size = -1;
				byte[] buffer = new byte[1024];
				while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
					outputData.write(buffer, 0, size);
				}
				outputData.close();
				fileInput.close();
				data.close();
				// SendMessage(0, fileName.get(i) + " �������");
				Log.d(TAG, fileName.get(i) + " �������");
			}
			// SendMessage(0, "�����ļ��������");
			Log.d(TAG, "�����ļ��������");
		} catch (Exception e) {
			// SendMessage(0, "���ʹ���:\n" + e.getMessage());
			Log.d(TAG, "���ʹ���:\n" + e.getMessage());
		}
	}
}
