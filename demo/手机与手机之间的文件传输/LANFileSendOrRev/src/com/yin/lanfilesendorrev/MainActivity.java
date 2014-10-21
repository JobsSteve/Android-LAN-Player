package com.yin.lanfilesendorrev;

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

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private EditText et_ip; // ����IP��ַ
	private TextView tv_progress; // ��ʾ����
	private Button btn_send; // ���Ͱ�ť

	private ServerSocket server;
	int port = 9503;
	String ip = "172.18.54.68";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ʵ�����ؼ�
		et_ip = (EditText) findViewById(R.id.et_ip);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		btn_send = (Button) findViewById(R.id.btn_send);

		// ��ʼ�����������ServerSocket
		try {
			server = new ServerSocket(port);
		} catch (Exception e) {

		}

		// �����̣߳��ȴ������ļ�
		new Thread() {
			public void run() {
				while (true) { // �ǵü�����ѭ��
					RecvFile();
				}
			};
		}.start();

		
		//���ð�ť����
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//String ipAddress = null;
				//ipAddress = et_ip.getText().toString();
				//System.out.println("IP:"+ipAddress);
				
				
				SendFile(ip,port);
			}
		});
	}

	/**
	 * �����ļ�
	 */
	private void SendFile(String ipAddress, int port) {
		try {
			Socket name = new Socket(ipAddress, port);
			OutputStream outputName = name.getOutputStream();
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
			BufferedWriter bwName = new BufferedWriter(outputWriter);
			bwName.write("yuenanyueai.mp3");
			bwName.close();
			outputWriter.close();
			outputName.close();
			name.close();
			SetStatus("���ڷ���" + "yuenanyueai.mp3");

			
		
			
			Socket data = new Socket(ipAddress, port);
			OutputStream outputData = data.getOutputStream();
			//FileInputStream fileInput = new FileInputStream(
			//		"file:///android_asset/yuenanyueai.mp3");
			InputStream fileInput = getAssets().open("yuenanyueai.mp3");
			
			int size = -1;
			byte[] buffer = new byte[1024];
			while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
				outputData.write(buffer, 0, size);
			}
			outputData.close();
			fileInput.close();
			data.close();
			SetStatus("yuenanyueai.mp3" + " �������");

			SetStatus("�����ļ��������");
		} catch (Exception e) {
			SetStatus("���ʹ���:\n" + e.getMessage());
		}
	}

	/**
	 * �����ļ�
	 */
	private void RecvFile() {
		// �ǵü���д��SD����Ȩ��

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
			SetStatus("���ڽ���:" + fileName);
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
			SetStatus(fileName + " �������");
		} catch (Exception e) {
			SetStatus("���մ���:\n" + e.getMessage());
		}
	}

	// ����״̬����ʾ��״̬
	private void SetStatus(String str) {
		tv_progress.setText(str);
	}
}
