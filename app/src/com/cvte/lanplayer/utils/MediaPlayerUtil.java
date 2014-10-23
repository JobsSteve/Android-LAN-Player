package com.cvte.lanplayer.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cvte.lanplayer.R;
import com.cvte.lanplayer.constant.AppConstant;
import com.cvte.lanplayer.service.PlayerService;

public class MediaPlayerUtil {
	private Intent startIntent;
	//private ActivityReceive activityReceive;
	private static Activity mActivity;
	private static MediaPlayerUtil instance = null;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private MediaPlayerUtil() {
//		// ����IntentFilter
//		IntentFilter filter = new IntentFilter();
//		// ָ��BroadcastReceiver������Action
//		filter.addAction(AppConstant.MusicPlayVariate.UPDATE_ACTION);
//		// ע��BroadcastReceiver
//		mActivity.registerReceiver(activityReceive, filter);
//		startIntent = new Intent(mActivity, PlayerService.class);
//		// ������̨Service
//		mActivity.startService(startIntent);

	}

	/**
	 * ��̬��������
	 */
	public static synchronized MediaPlayerUtil getInstance(Activity activity) {

		mActivity = activity;
		if (instance == null) {
			instance = new MediaPlayerUtil();
		}

		return instance;
	}

	public void NextMusic() {

	}

	public void PauseMusic() {

	}

	public void PlayMusic() {

	}

	public void GetMusicList() {

	}
	
	

	

}
