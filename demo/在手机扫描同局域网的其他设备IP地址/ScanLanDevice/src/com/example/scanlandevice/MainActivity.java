package com.example.scanlandevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private List<String> IpList = new ArrayList<String>();
	private final int port = 9598; // Ĭ�϶˿ں�
	private final String TAG = "ScanLanDevice";
	private final String KEY = "welcome to cvte";
	private String LocalIp = null;
	private TextView tv_ip;
	private Button btn_scan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_ip = (TextView) findViewById(R.id.tv_ip);
		btn_scan = (Button) findViewById(R.id.btn_scan);

		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ScanLanDevice();
			}
		});

		// �����̵߳ȴ�socket����
		new Thread() {
			public void run() {

				ServerSocket ss = null;
				try {
					ss = new ServerSocket(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ����ѭ�����Ͻ������Կͻ��˵�����
				while (true) {
					// ÿ�����ܵ��ͻ���Socket�����󣬷�������Ҳ��Ӧ����һ��Socket
					Socket s;
					try {
						s = ss.accept();
						OutputStream os = s.getOutputStream();
						os.write(KEY.getBytes("utf-8"));
						// �ر���������ر�Socket
						os.close();
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();

		LocalIp = getIpAddress();
		Log.d(TAG, LocalIp);
	}

	/**
	 * ���߳�ɨ�������������豸
	 */
	static int i;

	private void ScanLanDevice() {
		IpList.clear();
		// ɨ������������IP��-����255���߳̽���ɨ��
		for (i = 1; i < 255;) {
			new Thread() {
				@Override
				public void run() {
					
					synchronized (this) {
						String ipAddress = "172.18.54." + String.valueOf(i);
						i++;

						try {
							Socket socket = new Socket(ipAddress, port);

							// ��ȡͨ����Կ����
							// ��Socket��Ӧ����������װ��BufferedReader
							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											socket.getInputStream()));
							// ������ͨIO����
							String line = br.readLine();
							if (line.equals(KEY)) {	//�ж��Ƿ����ͨ����Կ
								if (!ipAddress.equals(LocalIp)) {	//�жϱ��Ȿ��IP
									for(int j=0;j<IpList.size();j++){
										//��������Ѿ�ɨ�����IP�������ټ���
										if(IpList.get(j).equals(ipAddress)){
											socket.close();
											return;
										}
									}
									IpList.add(ipAddress);
									// �������߳��������UI���
									// tv_ip.setText(tv_ip.getText() +
									// "  172.18.54.68");
									Log.d(TAG, ipAddress);

									handler.sendEmptyMessage(123);
								}

							}

							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			for (int i = 0; i < IpList.size(); i++) {
				Log.d(TAG, IpList.get(i));
				tv_ip.setText(tv_ip.getText() + IpList.get(i) + "\n");
			}
		}
	};

	/**
	 * ��ȡ������IP��ַ
	 */
	private String getIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));

	}

}
