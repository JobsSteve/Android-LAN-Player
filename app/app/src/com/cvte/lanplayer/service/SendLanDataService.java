package com.cvte.lanplayer.service;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.utils.ScanLanDeviceUtil;

public class SendLanDataService extends Service {

	private final int STARE_SCAN = 1;

	private static String TAG = "SendLanDataService";
	private ScanCtrlReceiver mScanCtrl;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "onCreate SendLanDataService");

		// ע��ɨ����ƵĽ�����
		mScanCtrl = new ScanCtrlReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.CTRL_SCAN_ACTION);
		registerReceiver(mScanCtrl, filter);
	}

	// ��������
	private class ScanCtrlReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Bundle bundle = intent.getExtras();
			int control = bundle.getInt(GlobalData.GET_BUNDLE_COMMANT);

			switch (control) {
			case GlobalData.STARE_SCAN:// ��ʼɨ��
				Log.d(TAG, "recv StartScanLAN Broadcast ");

				// ���ù������ɨ�跽��
				ScanLanDeviceUtil.getInstance(SendLanDataService.this)
						.StartScan();
				break;

			case GlobalData.STOP_SCAN:// ֹͣɨ��

				Log.d(TAG, "recv StopScanLAN Broadcast ");
				// ���ù������ֹͣɨ�跽��
				try {
					ScanLanDeviceUtil.getInstance(SendLanDataService.this)
							.StopScan();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	}

	public void onDestroy() {
		// ���ù������stopScan()
		try {
			ScanLanDeviceUtil.getInstance(SendLanDataService.this).StopScan();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ���ע�������
		this.unregisterReceiver(mScanCtrl);

		Log.d(TAG, "onDestroy SendLanDataService");
	}

}
