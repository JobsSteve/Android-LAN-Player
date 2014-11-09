package com.cvte.lanplayer.view.device;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.R;

public class LanDeviceControlActivity extends FragmentActivity {

	private final String TAG = "LanDeviceControlActivity";

	private String targetIp = null;

	private TextView tv_target_ip;

	private MyFragmentPagerAdapter adapter;
	private ViewPager vp;

	// ��������
	List<String> titleList = new ArrayList<String>();
	// fragment����
	List<Fragment> fragmentList = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ����Title
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_landevice_ctrl);

		InitData();
		InitView();

	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	/**
	 * ʵ�����ؼ�
	 */
	private void InitView() {

		tv_target_ip = (TextView) findViewById(R.id.tv_target_ip);
		tv_target_ip.setText("Ŀ��IP��" + targetIp);

		vp = (ViewPager) findViewById(R.id.vp);
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

		// ������Ϣ��Fragment����
		LanMusicFragment lanMusicFragment = new LanMusicFragment();
		Bundle bundle = new Bundle();
		bundle.putString(GlobalData.GetBundle.GET_IP, targetIp);
		lanMusicFragment.setArguments(bundle);

		fragmentList.add(lanMusicFragment);
		titleList.add("Ŀ�������б�");

		vp.setAdapter(adapter);

	}

	/**
	 * ��ʼ������
	 */
	private void InitData() {
		Intent intent = getIntent();
		targetIp = (String) intent.getSerializableExtra("ip");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
