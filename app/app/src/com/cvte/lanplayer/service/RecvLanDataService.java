package com.cvte.lanplayer.service;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.constant.AppConstant;
import com.cvte.lanplayer.utils.MediaPlayerUtil;
import com.cvte.lanplayer.utils.RecvLanScanDeviceUtil;
import com.cvte.lanplayer.utils.RecvSocketMessageUtil;

public class RecvLanDataService extends Service {

	// �յ���Ϣ���ٷַ�����
	private RecvScoketMsgReceiver mRecvScoketMsgReceiver;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// ע�������
		mRecvScoketMsgReceiver = new RecvScoketMsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.RECV_LAN_SOCKET_MSG_ACTION);
		registerReceiver(mRecvScoketMsgReceiver, filter);

		// �����������������豸ɨ�赽�Ľ��ռ���
		RecvLanScanDeviceUtil.getInstance(this).StartRecv();
		RecvSocketMessageUtil.getInstance(this).StartRecv();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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

				//��ȡ�����������б�
				//MediaPlayerUtil.getInstance(RecvLanDataService.this).getMusicList();
				//AppConstant.MusicPlayData.myMusicList;
				
				
				//���ͱ����������б�
				
				
				break;
			}

		}
	}

}
