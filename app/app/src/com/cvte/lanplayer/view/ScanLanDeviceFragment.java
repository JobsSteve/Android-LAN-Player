package com.cvte.lanplayer.view;

import java.io.IOException;
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
import com.cvte.lanplayer.adapter.MyListAdapter;
import com.cvte.lanplayer.utils.ScanLanDeviceUtil;

public class ScanLanDeviceFragment extends Fragment {

	private final String TAG = "ScanLanDeviceFragment";
	// ͨ����Կ
	private final String KEY = "welcome to cvte";

	// ����IP
	private String mLocalIp = null;

	// ɨ�������IP�б�
	private List<String> mIpList = new ArrayList<String>();
	private Activity mActivity;

	// �ؼ�
	private TextView tv_local_ip;
	private Button btn_scan;
	private Button btn_scan_stop;
	private ListView lv_iplist;

	private MyListAdapter mIpList_adapter;

	private ScanDataReceiver mScanDataReceiver;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
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
		mIpList_adapter = new MyListAdapter(mIpList, mActivity);
		lv_iplist.setAdapter(mIpList_adapter);

		// ע�������
		mScanDataReceiver = new ScanDataReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.SocketTranCommand.RECV_SOCKET_FROM_SERVICE_ACTION);
		mActivity.registerReceiver(mScanDataReceiver, filter);

		mLocalIp = getIpAddress();
		tv_local_ip.setText("����IP��" + String.valueOf(mLocalIp));
		Log.d(TAG, mLocalIp);
	}

	private void InitListener() {
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d(TAG, "on click scan button");

				// ���ù������ɨ�跽��
				ScanLanDeviceUtil.getInstance(mActivity).StartScan();

			}
		});

		btn_scan_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Log.d(TAG, "on click canel scan button");

				// ���ù������ֹͣɨ�跽��
				try {
					ScanLanDeviceUtil.getInstance(mActivity).StopScan();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		lv_iplist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent startIntent = new Intent(mActivity,
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
		WifiManager wifiManager = (WifiManager) mActivity
				.getSystemService(mActivity.WIFI_SERVICE);

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

			// ��ȡָ��
			int commant = bundle
					.getInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT);

			// ����ָ�������д���
			if (commant == GlobalData.SocketTranCommand.COMMAND_LAN_ASK) {
				String str = bundle
						.getString(GlobalData.SocketTranCommand.GET_BUNDLE_COMMON_DATA);

				// ����Ѿ����ˣ��Ͳ����
				for (int i = 0; i < mIpList.size(); i++) {
					if (str.equals(mIpList.get(i))) {
						return;
					}
				}

				mIpList.add(str);
				// ��������
				mIpList_adapter.notifyDataSetChanged();
				// ������յ�������........
			}

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ֹͣ����
		// activity.stopService(new Intent(activity, SendLanDataService.class));

		// ���ע�������
		mActivity.unregisterReceiver(mScanDataReceiver);
	}
}
