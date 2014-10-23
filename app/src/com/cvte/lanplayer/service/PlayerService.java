package com.cvte.lanplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.widget.Toast;

import com.cvte.lanplayer.constant.AppConstant;
import com.cvte.lanplayer.constant.AppFunction;

public class PlayerService extends Service implements Runnable,MediaPlayer.OnCompletionListener {

	/*����һ����ý�����*/
	private static MediaPlayer mMediaPlayer = null;
	/*
	 * ���ղ��� 
	 * 1-���� 
	 * 2-��ͣ 
	 * 3-��һ��
	 * 4-��һ��
	 * 5-����������¼�
	 */
	private int MSG;
	private int state;
	private MyReceiver serviceReceiver;
	private AssetManager am;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		if(mMediaPlayer != null)
		{
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		
		am = getAssets();
		//����BroadcastReceiver
		serviceReceiver = new MyReceiver();
		//����IntentFilter
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.MusicPlayVariate.CTL_ACTION);
		registerReceiver(serviceReceiver, filter);
		mMediaPlayer = new MediaPlayer();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if(mMediaPlayer != null)
		{
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	/*����service��ִ�еķ���*/
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public class MyReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int control = intent.getIntExtra("control", -1);
			switch(control)
			{
			case AppConstant.MusicPlayControl.MUSIC_CONTROL_PLAY://��������
				playMusic();
				break;
			case AppConstant.MusicPlayControl.MUSIC_CONTROL_PAUSE://��ͣ����
				playPause();
				break;
			case AppConstant.MusicPlayControl.MUSIC_CONTROL_PREVIOUS:
				//��һ�׸���
				playPreviousMusic();
				break;
			case AppConstant.MusicPlayControl.MUSIC_CONTROL_NEXT:
				//��һ�׸���
				playNextMusic();
				break;
			case AppConstant.MusicPlayControl.MUSIC_CONTROL_SEEKBAR:
				//����������
				SeekBarChange();
				break;
			}
		}
	}
	
	/*
	 * ���ص�ǰ���Ÿ�����ʱ��
	 */
	public static int getMusicDuration()
	{
		return mMediaPlayer.getDuration();
	}
	
	/*
	 * ���ص�ǰ���Ÿ�������λ��
	 */
	public static int getMusicCurrPosition()
	{
		return mMediaPlayer.getCurrentPosition();
	}
	
	/*
	 * ��������
	 */
	private void playMusic()
	{
		try
		{
			//��ȡ��ǰ���������������ж�
			int checkIndex = checkNowSong();
			Intent sendIntent = new Intent(AppConstant.MusicPlayVariate.UPDATE_ACTION);
			//�ж��Ƿ��ǲ����¸�
			if(AppConstant.MusicPlayData.IS_PLAY_NEW)
			{
				//���ö�ý��
				mMediaPlayer.reset();
				//���õ�ǰ���ڲ��ŵ����ֶ���
				//AppConstant.mNowSong = AppConstant.mySongList.get(AppConstant.mCurrentIndex);
				//װ�������ļ�
				mMediaPlayer.setDataSource(AppConstant.MusicPlayData.myMusicList.get(AppConstant.MusicPlayData.CURRENT_PLAY_INDEX).getFilePath());
				//׼������
				mMediaPlayer.prepare();
				AppConstant.MusicPlayData.IS_PLAY_NEW = false;
				//DisplayToast(AppConstant.mNowSong.getFileName());
				sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_PLAY_DATA, AppConstant.MusicPlayVariate.MUSIC_PALY_DATA_INT);
			}
			else
			{
				if(AppConstant.MusicPlayState.CURRENT_PLAY_STATE == AppConstant.MusicPlayState.PLAY_STATE_PAUSE)
				{
					DisplayToast("��������");
				}
			}
			//���õ�ǰ״̬Ϊ����״̬
			AppConstant.MusicPlayState.CURRENT_PLAY_STATE = AppConstant.MusicPlayState.PLAY_STATE_PLAYING;
			//��ʼ����
			mMediaPlayer.start();
			sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_PLAY_STATE_STR, AppConstant.MusicPlayState.PLAY_STATE_PLAYING);
			sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_INDEX_STR, AppConstant.MusicPlayData.CURRENT_PLAY_INDEX);
			//���͹㲥֪ͨActivity
			sendBroadcast(sendIntent);
			//���������Ƿ񲥷����
			mMediaPlayer.setOnCompletionListener(new MediaPlayerOnCompletionListener());
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	
	
	/*
	 * ���������Ƿ񲥷���ϣ�������Զ�������һ��
	 */
	private class MediaPlayerOnCompletionListener implements OnCompletionListener
	{

		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			//������һ������
			//�жϲ���ģʽ�Ƿ���˳�򲥷��Ҳ��ŵ����һ��
			if(AppConstant.MusicPlayData.CURRENT_PLAY_INDEX == AppConstant.MusicPlayData.myMusicList.size() - 1 && AppConstant.MusicPlayMode.CURRENT_PLAY_MODE == AppConstant.MusicPlayMode.PLAY_MODE_ORDER)
			{
				DisplayToast("�Ѿ������һ�׸���");
				//onDestroy();
				mMediaPlayer.pause();
				//��ͣ״̬
				AppConstant.MusicPlayState.CURRENT_PLAY_STATE = AppConstant.MusicPlayState.PLAY_STATE_PAUSE;
				//�������¸�
				AppConstant.MusicPlayData.IS_PLAY_NEW = false;
				//�㲥����״̬
				Intent sendIntent = new Intent(AppConstant.MusicPlayVariate.UPDATE_ACTION);
				sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_PLAY_STATE_STR, AppConstant.MusicPlayState.PLAY_STATE_PAUSE);
				sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_INDEX_STR, AppConstant.MusicPlayData.CURRENT_PLAY_INDEX);
				//���͹㲥֪ͨActivity
				sendBroadcast(sendIntent);
			}
			else
			{
				//������һ��
				playNextMusic();
			}
		}
	}
	
	/*
	 * ��ͣ����
	 */
	private void playPause()
	{
		//��ͣ�����²����¸�
		AppConstant.MusicPlayData.IS_PLAY_NEW = false;
		//����Ϊ��ͣ״̬
		AppConstant.MusicPlayState.CURRENT_PLAY_STATE = AppConstant.MusicPlayState.PLAY_STATE_PAUSE;
		mMediaPlayer.pause();
		//
		Intent sendIntent = new Intent(AppConstant.MusicPlayVariate.UPDATE_ACTION);
		sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_PLAY_STATE_STR, AppConstant.MusicPlayState.PLAY_STATE_PAUSE);
		sendIntent.putExtra(AppConstant.MusicPlayVariate.MUSIC_INDEX_STR, AppConstant.MusicPlayData.CURRENT_PLAY_INDEX);
		//���͹㲥֪ͨActivity
		sendBroadcast(sendIntent);
		DisplayToast("��ͣ����");
	}
	
	/*
	 * ������һ��
	 */
	private void playPreviousMusic()
	{
		//��ȡ��ǰ���������������ж�
		int checkIndex = checkNowSong();
		if(checkIndex == 0)
		{
			DisplayToast("�����������ļ�");
			return;
		}
		else if(checkIndex == 1)
		{
			//�������ж�����
			//���ݲ���ģʽ���õ�ǰҪ���ŵ�������
			AppConstant.MusicPlayData.CURRENT_PLAY_INDEX = 
					setCurrIndexByPlayType(AppConstant.MusicPlayState.CURRENT_PLAY_STATE, 
											AppConstant.MusicPlayControl.MUSIC_CONTROL_PREVIOUS, 
											AppConstant.MusicPlayData.CURRENT_PLAY_INDEX, 
											AppConstant.MusicPlayData.myMusicList.size());
		}
		else if(checkIndex == 2)
		{
			//�������쳣��Ĭ�ϼ��ص�һ�׸�����Ϣ��ֱ�Ӳ���
		}
		else 
		{
			//�ж��쳣
			return;
		}
		//�����¸��ʶ
		AppConstant.MusicPlayData.IS_PLAY_NEW = true;
		//����״̬
		AppConstant.MusicPlayState.CURRENT_PLAY_STATE = AppConstant.MusicPlayState.PLAY_STATE_PLAYING;
		//��ʼ����
		playMusic();
	}
	
	/*
	 * ������һ��
	 */
	private void playNextMusic()
	{
		//��ȡ��ǰ���������������ж�
		int checkIndex = checkNowSong();
		if(checkIndex == 0)
		{
			DisplayToast("�����������ļ�");
			return;
		}
		else if(checkIndex == 1)
		{
			//�������ж�����
			//���ݲ���ģʽ���õ�ǰҪ���ŵ�������
			AppConstant.MusicPlayData.CURRENT_PLAY_INDEX = setCurrIndexByPlayType(AppConstant.MusicPlayState.CURRENT_PLAY_STATE, AppConstant.MusicPlayControl.MUSIC_CONTROL_NEXT, AppConstant.MusicPlayData.CURRENT_PLAY_INDEX, AppConstant.MusicPlayData.myMusicList.size());
		}
		else if(checkIndex == 2)
		{
			//�������쳣��Ĭ�ϼ��ص�һ�׸�����Ϣ��ֱ�Ӳ���
		}
		else 
		{
			//�ж��쳣
			return;
		}
		//�����¸��ʶ
		AppConstant.MusicPlayData.IS_PLAY_NEW = true;
		//����״̬
		AppConstant.MusicPlayState.CURRENT_PLAY_STATE = AppConstant.MusicPlayState.PLAY_STATE_PLAYING;
		//��ʼ����
		playMusic();
	}
	
	/*
	 * ��������������¼�
	 */
	private void SeekBarChange()
	{
		//���Ž�����ת����ǰλ��
		mMediaPlayer.seekTo(AppConstant.MusicPlayData.CURRENT_PLAY_POSITION);
	}
	
	
	/*
	 * ���ݲ���ģʽ���õ�ǰ��������
	 * playmode
	 * 	AppConstant.MusicPlayMode.MUSIC_ORDER_PLAY-˳�򲥷�
	 * 	AppConstant.MusicPlayMode.MUSIC_LIST_LOOP_PLAY-ѭ������
	 * 	AppConstant.MusicPlayMode.MUSIC_RANDOM_PLAY-�������
	 * 	AppConstant.MusicPlayMode.MUSIC_SINGLE_LOOP_PLAY-��������
	 * playDirection
	 *  AppConstant.MusicPlayControl.MUSIC_CONTROL_PREVIOUS-ǰһ��
	 *  AppConstant.MusicPlayControl.MUSIC_CONTROL_NEXT-��һ��
	 * index��ǰ��������������
	 * allCount�����б�����
	 * Ĭ��Ϊ0
	 */
	private int setCurrIndexByPlayType(int playmode, int playDirection, int index, int allCount)
	{
		int newIndex = index;
		switch(playmode)
		{
		case AppConstant.MusicPlayMode.PLAY_MODE_ORDER:
			//˳�򲥷�
			if(playDirection == AppConstant.MusicPlayControl.MUSIC_CONTROL_PREVIOUS)
			{
				//ǰһ��
				//�ж��Ƿ��ǵ�һ�׸���
				if(index == 0)
				{
					DisplayToast("�Ѿ��ǵ�һ�׸���");
					newIndex = index;
				}
				else
				{
					newIndex = index - 1;
				}
			}
			else if(playDirection == AppConstant.MusicPlayControl.MUSIC_CONTROL_NEXT)
			{
				//��һ��
				//�ж��Ƿ������һ�׸���
				if(index == allCount - 1)
				{
					DisplayToast("�Ѿ������һ�׸���");
					newIndex = index;
				}
				else
				{
					newIndex = index + 1;
				}
			}
			break;
		case AppConstant.MusicPlayMode.PLAY_MODE_LIST_LOOP:
			//ѭ������
			if(playDirection == AppConstant.MusicPlayControl.MUSIC_CONTROL_PREVIOUS)
			{
				//�ж��Ƿ��ǵ�һ�׸���
				if(index == 0)
				{
					newIndex = allCount - 1;
				}
				else
				{
					newIndex = index - 1;
				}
			}
			else if(playDirection == AppConstant.MusicPlayControl.MUSIC_CONTROL_NEXT)
			{
				//�ж��Ƿ������һ�׸���
				if(index == allCount - 1)
				{
					newIndex = 0;
				}
				else
				{
					newIndex = index + 1;
				}
			}
			break;
		case AppConstant.MusicPlayMode.PLAY_MODE_RANDOM:
			//�������
			newIndex = AppFunction.GenerateRandom(allCount - 1, index);
			break;
		case AppConstant.MusicPlayMode.PLAY_MODE_SINGLE:
			//��������
			newIndex = index;
			break;
		default:break;
		}
		//����Ҫ���ŵ�������
		return newIndex;
	}
	
	/*
	 * �жϵ�ǰ���ڲ��ŵ����ֶ���
	 * 0-����������
	 * 1-����������
	 * 2-�����Ŵ������õ�һ�������ļ�
	 */
	private int checkNowSong()
	{
		//Ĭ�ϲ���������
		int isCorrect = 0;
		try
		{
			//�ж�������
			if(AppConstant.MusicPlayData.CURRENT_PLAY_INDEX >= 0 && AppConstant.MusicPlayData.CURRENT_PLAY_INDEX < AppConstant.MusicPlayData.myMusicList.size())
			{
				//AppConstant.mNowSong = AppConstant.mySongList.get(AppConstant.mCurrentIndex);
				//����������
				isCorrect = 1;
			}
			else
			{
				//�����Ŵ����ж������б��Ƿ����������Ϣ
				if(AppConstant.MusicPlayData.myMusicList.size() > 0)
				{
					//���õ�ǰ���ڲ��ŵ����ֵ�������
					AppConstant.MusicPlayData.CURRENT_PLAY_INDEX = 0;
					//�����Ŵ���װ�ص�һ������
					isCorrect = 2;
				}
				else
				{
					//����Ĭ��ֵ
					AppConstant.MusicPlayData.CURRENT_PLAY_INDEX = -1;
					//�����б����ڸ���
					isCorrect = 0;
				}
			}
			return isCorrect;
		}
		catch(Exception e)
		{
			isCorrect = 0;
			return isCorrect;
		}
	}
	
	/*
	 * Toast��ʾ
	 * 
	 */
	private void DisplayToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
