package com.cvte.lanplayer.view;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;

import com.cvte.lanplayer.R;
import com.cvte.lanplayer.adapter.IpListAdapter;
import com.cvte.lanplayer.service.RecvLanDataService;
import com.cvte.lanplayer.service.SendLanDataService;

public class ScanLanDeviceFragment extends Fragment {

	private final String TAG = "ScanLanDevice";

	// ͨ����Կ
	private final String KEY = "welcome to cvte";

	// ����IP
	private String mLocalIp = null;
	// ����IPͷ���磺192.168.1
	private String mIpAddressHead = null;
	// ɨ�������IP�б�
	private List<String> mIpList = new ArrayList<String>();
	// Ĭ�϶˿ں�
	private final int port = 9598;

	private Activity activity;

	private boolean start = true;
	private String address;
	public static final int DEFAULT_PORT = 9598;
	private static final int MAX_DATA_PACKET_LENGTH = 40;
	private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];

	// �ؼ�
	// private TextView tv_ip;
	private Button btn_scan;
	private Button btn_scan_stop;
	private ListView lv_iplist;

	private IpListAdapter mIpList_adapter;
	private final int STARE_SCAN = 1;

	private MyReceiver receiver;

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

		View view = inflater.inflate(R.layout.fragment_scan_landevice,
				container, false);

		// tv_ip = (TextView) view.findViewById(R.id.tv_ip);
		btn_scan = (Button) view.findViewById(R.id.btn_scan);
		btn_scan_stop = (Button) view.findViewById(R.id.btn_scan_stop);

		lv_iplist = (ListView) view.findViewById(R.id.lv_iplist);

		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ScanLanDevice();

				// �رս��յķ���
				activity.stopService(new Intent(activity,
						RecvLanDataService.class));

				// ����ɨ�裬��ʼ����
				activity.startService(new Intent(activity,
						SendLanDataService.class));

				Intent intent = new Intent();
				intent.putExtra("int", STARE_SCAN);

				intent.setAction("android.intent.action.recv_contrl");// action���������ͬ
				activity.sendBroadcast(intent);

			}
		});

		btn_scan_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.stopService(new Intent(activity,
						SendLanDataService.class));

				activity.startService(new Intent(activity,
						RecvLanDataService.class));
			}
		});

		mIpList_adapter = new IpListAdapter(mIpList, activity);
		lv_iplist.setAdapter(mIpList_adapter);

		mLocalIp = getIpAddress();
		mIpAddressHead = getIpAddressHead();
		Log.d(TAG, mLocalIp);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		// ע�������
		receiver = new MyReceiver();

		IntentFilter filter = new IntentFilter();

		filter.addAction("android.intent.action.recvip");
		activity.registerReceiver(receiver, filter);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			// ��������
			mIpList_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * ��ȡ������IP��ַ
	 */
	private String getIpAddress() {
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(activity.WIFI_SERVICE);

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
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(activity.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + ".");

	}

	private String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}

	public class MyReceiver extends BroadcastReceiver {

		// �Զ���һ���㲥������

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub

			System.out.println("OnReceiver");

			Bundle bundle = intent.getExtras();

			String str = bundle.getString("str");

			// progressBar.setProgress(a);

			// label.setText(String.valueOf(str));
			for (int i = 0; i < mIpList.size(); i++) {
				if (str.equals(mIpList.get(i))) {
					// ����Ѿ����ˣ��Ͳ����
					return;
				}
			}

			mIpList.add(str);
			// ��������
			mIpList_adapter.notifyDataSetChanged();

			// ������յ�������

		}

		public MyReceiver() {

			System.out.println("MyReceiver");

			// ���캯������һЩ��ʼ�����������������κ�����

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ��ʼ����
		activity.stopService(new Intent(activity, SendLanDataService.class));
	}
}
