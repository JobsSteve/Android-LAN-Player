package com.cvte.lanplayer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cvte.lanplayer.view.LocalPlayerFragment;
import com.cvte.lanplayer.view.ScanLanDeviceFragment;
import com.cvte.lanplayer.view.fragment1;

public class MainActivity extends FragmentActivity {
	private final String TAG = "MainActivity";

	private final int port = 9598; // Ĭ�϶˿ں�
	private final String KEY = "welcome to cvte";
	
	private MyFragmentPagerAdapter adapter;
	private ViewPager vp;
	// ��������
	List<String> titleList = new ArrayList<String>();
	// fragment����
	List<Fragment> fragmentList = new ArrayList<Fragment>();

	private LocalPlayerFragment mLocalPlayerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		vp = (ViewPager) findViewById(R.id.vp);
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

		fragmentList.add(new LocalPlayerFragment());
		fragmentList.add(new fragment1());
		fragmentList.add(new ScanLanDeviceFragment());
		titleList.add("����");
		titleList.add("���");
		titleList.add("ɨ��");

		vp.setAdapter(adapter);

		// �����̵߳ȴ�socket����
		new Thread() {
			public void run() {

				ServerSocket ss = null;
				try {
					ss = new ServerSocket(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ����ѭ�����Ͻ������Կͻ��˵�����
				while (true) {
					// ÿ�����ܵ��ͻ���Socket�����󣬷�������Ҳ��Ӧ����һ��Socket
					Socket s;
					try {
						s = ss.accept();
						OutputStream os = s.getOutputStream();
						os.write(KEY.getBytes("utf-8"));
						// �ر���������ر�Socket
						os.close();
						s.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();


	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}

	}

	

}
