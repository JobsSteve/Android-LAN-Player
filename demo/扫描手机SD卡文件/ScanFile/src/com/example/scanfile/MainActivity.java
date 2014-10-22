package com.example.scanfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	List<String> items = new ArrayList<String>();// ���ڱ���ɨ�����.torrent�ļ�
	private final String TAG = "ScanFile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		new Thread() {
			public void run() {
				// ��ȡ�ⲿ�洢·��
				File exStorageDirectory = Environment
						.getExternalStorageDirectory();
				if(exStorageDirectory == null){	//û��SD��
					throw new NullPointerException("none SD card");
				}
				
				scan(exStorageDirectory);
			};
		}.start();
		
		// ����������
		ArrayAdapter<String> torrentList = new ArrayAdapter<String>(
				this, R.layout.musicitme, items);
		setListAdapter(torrentList);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */

	/**
	 * ɨ���ļ� �����Ŀ¼�ļ��У����������ɨ�� �����ж��Ƿ���.torrent�ļ�
	 * 
	 * @param file
	 *            ��ɨ����ļ�
	 */
	public void scan(File file) {
		if (file.isDirectory())// �Ƿ�Ϊ�ļ���
		{
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				scan(files[i]);
			}
		} else {
			if (isTorrent(file)) {
				items.add(file.toString());
				Log.d(TAG, file.toString());
			}
		}

	}

	/**
	 * �ж��ļ��Ƿ���.mp3�ļ�
	 * 
	 * @param file
	 *            ���жϵ��ļ�
	 * @return ����Ƿ���true,���򷵻�flase
	 */
	public boolean isTorrent(File file) {
		if (file.getName().endsWith(".mp3"))// �Ƿ���.torrent��β
		{
			return true;
		} else {
			return false;
		}
	}

}
