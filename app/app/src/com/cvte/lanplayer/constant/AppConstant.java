package com.cvte.lanplayer.constant;

import java.util.ArrayList;

import com.cvte.lanplayer.data.MusicData;

public interface AppConstant {
	/*
	 * ������Ϣ
	 */
	public class MusicPlayData
	{
		//������Ϣ�б�
		public static ArrayList<MusicData> myMusicList = new ArrayList<MusicData>();
		//��ǰ���Ÿ���������
		public static int CURRENT_PLAY_INDEX = -1;
		//��ǰ���Ÿ�������
		public static int CURRENT_PLAY_POSITION = 0;
		//�Ƿ�Ҫ���²��ű�ʶ
		public static boolean IS_PLAY_NEW = true;
	}
	/*
	 * ���ֲ���״̬
	 */
	public class MusicPlayState
	{
		//��ͣ״̬
		public static final int PLAY_STATE_PAUSE = 0;
		//����״̬
		public static final int PLAY_STATE_PLAYING = 1;
		//��ǰ����״̬��Ĭ��Ϊ��ͣ״̬
		public static int CURRENT_PLAY_STATE = PLAY_STATE_PAUSE;
	}
	/*
	 * ���ֲ���ģʽ
	 */
	public class MusicPlayMode
	{
		//˳�򲥷�
		public static final int PLAY_MODE_ORDER = 0;
		//�б�ѭ��
		public static final int PLAY_MODE_LIST_LOOP = 1;
		//�������
		public static final int PLAY_MODE_RANDOM = 2;
		//����ѭ��
		public static final int PLAY_MODE_SINGLE = 3;
		//����ģʽ����
		public static final int[] PLAY_MODE_ARRAY = {
				PLAY_MODE_ORDER,
				PLAY_MODE_LIST_LOOP,
				PLAY_MODE_RANDOM,
				PLAY_MODE_SINGLE};
		//��ǰ����ģʽ��Ĭ�ϲ���ģʽΪ˳�򲥷�
		public static int CURRENT_PLAY_MODE = PLAY_MODE_ARRAY[0];
	}
	/*
	 * ����
	 */
	public class MusicPlayControl
	{
		//��������
		public static final int MUSIC_CONTROL_PLAY = 0;         
		//��ͣ����
		public static final int MUSIC_CONTROL_PAUSE = 1;        
		//��һ������
		public static final int MUSIC_CONTROL_PREVIOUS = 2;     
		//��һ������
		public static final int MUSIC_CONTROL_NEXT = 3;         
		//�������������
		public static final int MUSIC_CONTROL_SEEKBAR = 4;      
	}
	
	/*
	 * ��ʶ��
	 */
	public class MusicPlayVariate
	{
		//��ǰ����������ʶ��
		public static final String MUSIC_INDEX_STR = "playIndex";     
		
		public static final String MUSIC_PLAY_DATA = "playdata";
		//����״̬��ʶ��
		public static final String MUSIC_PLAY_STATE_STR = "playState";  
		//���������ʶ��
		public static final String MUSIC_CONTROL_STR = "control";  
		//1-����ʼ�����¸裬���¸��������ܲ���ʱ�䣬����������
		public static final int MUSIC_PALY_DATA_INT = 1;
		
		public static String CTL_ACTION = "org.crazyit.action.CTL_ACTION";
		public static String UPDATE_ACTION = "org.crazyit.action.UPDATE_ACTION";
	}
}
