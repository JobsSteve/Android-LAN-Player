package com.cvte.lanplayer.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.R;
import com.cvte.lanplayer.adapter.IpListAdapter;
import com.cvte.lanplayer.service.RecvLanDataService;
import com.cvte.lanplayer.service.SendLanDataService;

public class ScanLanDeviceFragment extends Fragment {

	private final String TAG = "ScanLanDeviceFragment";
	// ͨ����Կ
	private final String KEY = "welcome to cvte";

	// ����IP
	private String mLocalIp = null;

	// ɨ�������IP�б�
	private List<String> mIpList = new ArrayList<String>();
	private Activity activity;

	// �ؼ�
	private TextView tv_local_ip;
	private Button btn_scan;
	private Button btn_scan_stop;
	private ListView lv_iplist;

	private IpListAdapter mIpList_adapter;

	private ScanDataReceiver mScanDataReceiver;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_scan_landevice,
				container, false);

		tv_local_ip = (TextView) view.findViewById(R.id.tv_local_ip);
		btn_scan = (Button) view.findViewById(R.id.btn_scan);
		btn_scan_stop = (Button) view.findViewById(R.id.btn_scan_stop);
		lv_iplist = (ListView) view.findViewById(R.id.lv_iplist);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		InitListener();
		Init();

	}

	/**
	 * ��ʼ����������
	 */
	private void Init() {
		mIpList_adapter = new IpListAdapter(mIpList, activity);
		lv_iplist.setAdapter(mIpList_adapter);

		// ע�������
		mScanDataReceiver = new ScanDataReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.GET_SCAN_DATA_ACTION);
		activity.registerReceiver(mScanDataReceiver, filter);

		mLocalIp = getIpAddress();
		tv_local_ip.setText("����IP��" + String.valueOf(mLocalIp));
		Log.d(TAG, mLocalIp);
	}

	private void InitListener() {
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ScanLanDevice();

				// // �رս��յķ���
				// activity.stopService(new Intent(activity,
				// RecvLanDataService.class));
				//
				// // ����ɨ�裬��ʼ����
				// activity.startService(new Intent(activity,
				// SendLanDataService.class));

				Intent intent = new Intent();
				// intent.putExtra(GlobalData.GET_BUNDLE_COMMANT,
				// GlobalData.STARE_SCAN);

				Bundle data = new Bundle();
				data.putInt(GlobalData.GET_BUNDLE_COMMANT,
						GlobalData.STARE_SCAN);

				intent.putExtras(data);

				intent.setAction(GlobalData.CTRL_SCAN_ACTION);// action���������ͬ
				activity.sendBroadcast(intent);

				Log.d(TAG, "on click scan button");

			}
		});

		btn_scan_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// activity.stopService(new Intent(activity,
				// SendLanDataService.class));
				//
				// activity.startService(new Intent(activity,
				// RecvLanDataService.class));

				Intent intent = new Intent();

				// intent.putExtra(GlobalData.GET_BUNDLE_COMMANT,
				// GlobalData.STOP_SCAN);

				Bundle data = new Bundle();
				data.putInt(GlobalData.GET_BUNDLE_COMMANT, GlobalData.STOP_SCAN);

				intent.putExtras(data);

				intent.setAction(GlobalData.CTRL_SCAN_ACTION);// action���������ͬ
				activity.sendBroadcast(intent);

				Log.d(TAG, "on click canel scan button");
			}
		});

		lv_iplist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent startIntent = new Intent(activity,
						LanDeviceControlActivity.class);

				startIntent.putExtra("ip", mIpList.get(position));
				startActivity(startIntent);

			}
		});

	}

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

	// ��ȡɨ�������IP��ַ�Ľ�����
	public class ScanDataReceiver extends BroadcastReceiver {

		// �Զ���һ���㲥������
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle bundle = intent.getExtras();
			String str = bundle.getString("str");

			// ����Ѿ����ˣ��Ͳ����
			for (int i = 0; i < mIpList.size(); i++) {
				if (str.equals(mIpList.get(i))) {
					return;
				}
			}

			mIpList.add(str);
			// ��������
			mIpList_adapter.notifyDataSetChanged();
			// ������յ�������
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ֹͣ����
		// activity.stopService(new Intent(activity, SendLanDataService.class));

		// ���ע�������
		activity.unregisterReceiver(mScanDataReceiver);
	}
}
