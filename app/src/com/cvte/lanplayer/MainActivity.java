package com.cvte.lanplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cvte.lanplayer.adapter.MusicListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private final String TAG = "MainActivity";
	
	// �����б�
	private List<String> myMusicList = new ArrayList<String>();
	
	// ���ֵ�·��
	private static final String MUSIC_PATH = new String("/sdcard/");

	private ListView lv_songlist;
	private MusicListAdapter music_lv_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ʵ�����ؼ�
		lv_songlist = (ListView) findViewById(R.id.lv_songlist);

		// ��ʼ�������ļ�
		InitMusicList();
	}

	private void InitMusicList() {
		
		File home = new File(MUSIC_PATH);
		if (home.listFiles(new MusicFilter()).length > 0) {
			for (File file : home.listFiles(new MusicFilter())) {
				myMusicList.add(file.getName());
				Log.d(TAG,"find:" + file.getName());
			}
			
		}
		music_lv_adapter = new MusicListAdapter(myMusicList,
				getApplicationContext());
		lv_songlist.setAdapter(music_lv_adapter);
		
	}

}
