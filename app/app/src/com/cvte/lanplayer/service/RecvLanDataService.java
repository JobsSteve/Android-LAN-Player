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
import com.cvte.lanplayer.constant.AppConstant;
import com.cvte.lanplayer.entity.SocketTranEntity;
import com.cvte.lanplayer.utils.RecvLanScanDeviceUtil;
import com.cvte.lanplayer.utils.RecvSocketMessageUtil;
import com.cvte.lanplayer.utils.SendLocalMusicListUtil;

public class RecvLanDataService extends Service {

	private static String TAG = "RecvLanDataService";

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

		// ע����տ��ƵĽ�����
		mRecvCtrlReceiver = new RecvCtrlReceiver();
		IntentFilter recvCtrlFilter = new IntentFilter();
		recvCtrlFilter.addAction(GlobalData.CTRL_RECV_ACTION);
		registerReceiver(mRecvCtrlReceiver, recvCtrlFilter);

		// �����������������豸ɨ�赽�Ľ��ռ���
		RecvLanScanDeviceUtil.getInstance(this).StartRecv();
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
	
	/**
	 * ����RecvSocketMessageUtil����ص�������socket�������Ϣ
	 */
	public void handleSocketCommand(SocketTranEntity data, String targetIP){
		int command = data.getmCommant();

		switch (command) {
		//�ı�������Ϣ
		case GlobalData.COMMAND_RECV_MSG:
			Log.d(TAG, "�յ����ݰ���COMMAND_RECV_MSG");
			// �յ��ı���Ϣ��
			Log.d(TAG, "�յ��ı�������Ϣ��" + data.getmMessage());

			//SendMessage(data.getmMessage());
			
			
			//���й㲥������Ϣת����ȥ
			Intent msg_intent = new Intent();

			Bundle msg_bundle = new Bundle();
			msg_bundle.putInt(GlobalData.GET_BUNDLE_COMMANT, GlobalData.COMMAND_RECV_MSG);
			msg_bundle.putString(GlobalData.GET_BUNDLE_DATA, data.getmMessage());

			msg_intent.putExtras(msg_bundle);
			msg_intent.setAction(GlobalData.RECV_SOCKET_FROM_SERVICE_ACTION);// action���������ͬ

			sendBroadcast(msg_intent);
			
			break;

		//�����ȡ�����б�
		case GlobalData.COMMAND_REQUSET_MUSIC_LIST:
			Log.d(TAG, "�յ����ݰ���COMMAND_REQUSET_MUSIC_LIST");

			Log.d(TAG, "�յ�IP: " + targetIP + " �����ȡ�����������б�");
			
			// ��ȡ�����������б�

			// ��ӡ����ǰ5�׸�������ļ���
			Log.d(TAG, "��ӡǰ5�׸�");
			for (int i = 0; (i < 5)
					&& (i < AppConstant.MusicPlayData.myMusicList.size()); i++) {

				if (AppConstant.MusicPlayData.myMusicList.get(i) != null) {
					Log.d(TAG, AppConstant.MusicPlayData.myMusicList.get(i)
							.getFileName());

				}
			}

			// ���ͱ����������б�

			// ��װһ������ʵ��,�������б�����
			SocketTranEntity musicList = new SocketTranEntity();
			
			musicList.setmCommant(GlobalData.COMMAND_SEND_MUSIC_LIST);
			musicList.setmMusicList(AppConstant.MusicPlayData.myMusicList);

			//���������б�
			SendLocalMusicListUtil.getInstance(RecvLanDataService.this)
					.SendMusicList(musicList, targetIP);
			
			break;

		//�յ������б�
		case GlobalData.COMMAND_SEND_MUSIC_LIST:
			Log.d(TAG, "�յ����ݰ���COMMAND_SEND_MUSIC_LIST");
			// ��ʱ��������������б�
			for (int i = 0; i < data.getmMusicList().size(); i++) {
				Log.d(TAG, "�յ�" + data.getmMusicList().get(i).getFileName());
			}

			break;

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
