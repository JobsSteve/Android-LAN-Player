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
import com.cvte.lanplayer.utils.RecvSocketFileUtil;
import com.cvte.lanplayer.utils.RecvSocketMessageUtil;
import com.cvte.lanplayer.utils.SendSocketFileUtil;
import com.cvte.lanplayer.utils.SendSocketMessageUtil;

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
		recvCtrlFilter.addAction(GlobalData.LANScanCtrl.CTRL_LAN_RECV_ACTION);
		registerReceiver(mRecvCtrlReceiver, recvCtrlFilter);

		// �����������������豸ɨ�赽�Ľ��ռ���
		RecvLanScanDeviceUtil.getInstance(this).StartRecv();
		RecvSocketMessageUtil.getInstance(this).StartRecv();

		// �����ļ����ռ���
		RecvSocketFileUtil.getInstance(this).StartRecv();

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
	 * ��Util������յ��ļ�����
	 * 
	 * @param fileName
	 */
	public void RecvFileFromUtil(String fileName) {

	}

	/**
	 * ����RecvSocketMessageUtil����ص�������socket�������Ϣ
	 */
	public void handleSocketCommand(SocketTranEntity data, String targetIP) {
		int command = data.getmCommant();

		switch (command) {
		// �ı�������Ϣ
		case GlobalData.SocketTranCommand.COMMAND_RECV_MSG:
			Log.d(TAG, "�յ����ݰ���COMMAND_RECV_MSG");
			// �յ��ı���Ϣ��
			Log.d(TAG, "�յ��ı�������Ϣ��" + data.getmMessage());

			// ���й㲥������Ϣת����ȥ
			Intent msg_intent = new Intent();

			Bundle msg_bundle = new Bundle();
			msg_bundle.putInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT,
					GlobalData.SocketTranCommand.COMMAND_RECV_MSG);
			msg_bundle.putString(
					GlobalData.SocketTranCommand.GET_BUNDLE_COMMON_DATA,
					data.getmMessage());

			msg_intent.putExtras(msg_bundle);
			msg_intent
					.setAction(GlobalData.SocketTranCommand.RECV_SOCKET_FROM_SERVICE_ACTION);// action���������ͬ

			sendBroadcast(msg_intent);

			break;

		// �����ȡ�����б�
		case GlobalData.SocketTranCommand.COMMAND_REQUSET_MUSIC_LIST:
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

			musicList
					.setmCommant(GlobalData.SocketTranCommand.COMMAND_SEND_MUSIC_LIST);
			musicList.setmMusicList(AppConstant.MusicPlayData.myMusicList);

			// ���������б�
			SendSocketMessageUtil.getInstance(RecvLanDataService.this)
					.SendMessage(musicList, targetIP);

			break;

		// �յ��ش��������б�
		case GlobalData.SocketTranCommand.COMMAND_SEND_MUSIC_LIST:
			Log.d(TAG, "�յ����ݰ���COMMAND_SEND_MUSIC_LIST");
			// ��ʱ��������������б�
			for (int i = 0; i < data.getmMusicList().size(); i++) {
				Log.d(TAG, "�յ�" + data.getmMusicList().get(i).getFileName());
			}

			// ���й㲥������Ϣת����ȥ
			Intent music_list_intent = new Intent();

			Bundle music_list_bundle = new Bundle();
			music_list_bundle.putInt(
					GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT,
					GlobalData.SocketTranCommand.COMMAND_SEND_MUSIC_LIST);
			music_list_bundle.putSerializable(
					GlobalData.SocketTranCommand.GET_BUNDLE_COMMON_DATA, data);

			music_list_intent.putExtras(music_list_bundle);
			music_list_intent
					.setAction(GlobalData.SocketTranCommand.RECV_SOCKET_FROM_SERVICE_ACTION);// action���������ͬ

			sendBroadcast(music_list_intent);

			break;

		// �յ�������ɨ���ȷ�ϰ�
		case GlobalData.SocketTranCommand.COMMAND_LAN_ASK:
			Log.d(TAG, "�յ����ݰ���COMMAND_LAN_ASK");

			// ���й㲥������Ϣת����ȥ
			Intent lan_ask_intent = new Intent();
			Bundle lan_ask_bundle = new Bundle();

			// ���봫������
			lan_ask_bundle.putInt(
					GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT,
					GlobalData.SocketTranCommand.COMMAND_LAN_ASK);

			// ���뷢�ͷ���IP��ַ
			lan_ask_bundle.putString(
					GlobalData.SocketTranCommand.GET_BUNDLE_COMMON_DATA,
					data.getmSendIP());

			lan_ask_intent.putExtras(lan_ask_bundle);
			lan_ask_intent
					.setAction(GlobalData.SocketTranCommand.RECV_SOCKET_FROM_SERVICE_ACTION);// action���������ͬ

			sendBroadcast(lan_ask_intent);

			break;

		//�յ���ȡ�����ļ�����
		case GlobalData.SocketTranCommand.COMMAND_REQUSET_MUSIC_FILE:
			Log.d(TAG, "�յ����ݰ���COMMAND_REQUSET_MUSIC_FILE");
			
			//��ȡ��Ҫ���͵�����ID��
			int musicID = Integer.parseInt(data.getmMessage());
			
			//���������ļ�
			SendSocketFileUtil.getInstance().SendFile(
					AppConstant.MusicPlayData.myMusicList.get(musicID)
							.getFileName(),
					AppConstant.MusicPlayData.myMusicList.get(musicID)
							.getFilePath(), data.getmSendIP());
			
			break;

		}
	}

	// ��������
	private class RecvCtrlReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Bundle bundle = intent.getExtras();
			int control = bundle
					.getInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT);

			switch (control) {
			case GlobalData.LANScanCtrl.STARE_LAN_RECV:// ��ʼɨ��
				Log.d(TAG, "recv StartRecvLAN Broadcast ");

				// ���ù������ɨ�跽��
				RecvLanScanDeviceUtil.getInstance(RecvLanDataService.this)
						.StartRecv();
				break;

			case GlobalData.LANScanCtrl.STOP_LAN_RECV:// ֹͣɨ��

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
