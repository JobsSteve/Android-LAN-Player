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
import com.cvte.lanplayer.utils.RecvLanScanDeviceUtil;
import com.cvte.lanplayer.utils.RecvSocketMessageUtil;

public class RecvLanDataService extends Service {

	private static String TAG = "RecvLanDataService";

	// �յ���Ϣ���ٷַ�����
	private RecvScoketMsgReceiver mRecvScoketMsgReceiver;

	// ���ƽ���
	private RecvCtrlReceiver mRecvCtrlReceiver;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate RecvLanDataService");

		// ע��ת����Ϣ�Ľ�����
		mRecvScoketMsgReceiver = new RecvScoketMsgReceiver();
		IntentFilter recvScoketFilter = new IntentFilter();
		recvScoketFilter.addAction(GlobalData.RECV_LAN_SOCKET_MSG_ACTION);
		registerReceiver(mRecvScoketMsgReceiver, recvScoketFilter);

		// ע����տ��ƵĽ�����
		mRecvCtrlReceiver = new RecvCtrlReceiver();
		IntentFilter recvCtrlFilter = new IntentFilter();
		recvCtrlFilter.addAction(GlobalData.CTRL_RECV_ACTION);
		registerReceiver(mRecvCtrlReceiver, recvCtrlFilter);

		// �����������������豸ɨ�赽�Ľ��ռ���
		// RecvLanScanDeviceUtil.getInstance(this).StartRecv();
		RecvSocketMessageUtil.getInstance(this).StartRecv();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.d(TAG, "onDestroy SendLanDataService");

		// ֹͣ�������������豸ɨ�赽�Ľ��ռ���
		try {
			RecvLanScanDeviceUtil.getInstance(this).StopRecv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			RecvSocketMessageUtil.getInstance(this).StopRecv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ���յ�����Ϣת����ȥ
	private class RecvScoketMsgReceiver extends BroadcastReceiver {

		// �Զ���һ���㲥������
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle bundle = intent.getExtras();
			// ��ȡָ��
			int commant = bundle.getInt(GlobalData.GET_BUNDLE_COMMANT);

			switch (commant) {
			case GlobalData.RECV_MSG:

				Intent msg_intent = new Intent();
				msg_intent.putExtras(bundle);
				// action���������ͬ
				msg_intent
						.setAction(GlobalData.RECV_SOCKET_FROM_SERVICE_ACTION);

				sendBroadcast(msg_intent);

				break;
			case GlobalData.REQUSET_MUSIC_LIST:

				// ��ȡ�����������б�
				// MediaPlayerUtil.getInstance(RecvLanDataService.this).getMusicList();
				// AppConstant.MusicPlayData.myMusicList;

				// ���ͱ����������б�

				break;
			}

		}
	}

	// ��������
	private class RecvCtrlReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Bundle bundle = intent.getExtras();
			int control = bundle.getInt(GlobalData.GET_BUNDLE_COMMANT);

			switch (control) {
			case GlobalData.STARE_LAN_RECV:// ��ʼɨ��
				Log.d(TAG, "recv StartRecvLAN Broadcast ");

				// ���ù������ɨ�跽��
				RecvLanScanDeviceUtil.getInstance(RecvLanDataService.this)
						.StartRecv();
				break;

			case GlobalData.STOP_LAN_RECV:// ֹͣɨ��

				Log.d(TAG, "recv StopRecvLAN Broadcast ");
				// ���ù������ֹͣɨ�跽��
				try {
					RecvLanScanDeviceUtil.getInstance(RecvLanDataService.this)
							.StopRecv();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	}

}
