package com.cvte.lanplayer.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cvte.lanplayer.R;

public class ScanLanDeviceFragment extends Fragment {

	private List<String> IpList = new ArrayList<String>();
	private final int port = 9598; // Ĭ�϶˿ں�
	private String mIpAddressHead = null;
	private final String TAG = "ScanLanDevice";
	private final String KEY = "welcome to cvte";
	private String mLocalIp = null;
	private TextView tv_ip;
	private Button btn_scan;
	
	private Activity activity;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_scan_landevice, container,false);

		tv_ip = (TextView) view.findViewById(R.id.tv_ip);
		btn_scan = (Button) view.findViewById(R.id.btn_scan);

		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ScanLanDevice();
			}
		});

		

		mLocalIp = getIpAddress();
		mIpAddressHead = getIpAddressHead();
		Log.d(TAG,mLocalIp);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


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
						String ipAddress = mIpAddressHead + String.valueOf(i);
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
								if (!ipAddress.equals(mLocalIp)) {	//�жϱ��Ȿ��IP
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
		WifiManager wifiManager = (WifiManager) activity.getSystemService(activity.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));

	}
	
	/**
	 * ��ȡ������IP��ַ��ͷ����192.168.1.
	 */
	private String getIpAddressHead() {
		WifiManager wifiManager = (WifiManager) activity.getSystemService(activity.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + ".");

	}
}
