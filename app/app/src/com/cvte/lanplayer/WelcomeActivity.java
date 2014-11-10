package com.cvte.lanplayer;

import java.util.Timer;
import java.util.TimerTask;

import com.cvte.lanplayer.utils.CheckSDCardUtil;
import com.cvte.lanplayer.utils.CheckWIFIConnectUtil;
import com.cvte.lanplayer.utils.ShowToastMsgUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class WelcomeActivity extends Activity {

	private boolean mIsHaveWIFI = false;
	private boolean mIsHaveSDCard = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ����Title
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);

		mIsHaveWIFI = CheckWIFIConnectUtil.isWifiConnected(this);
		mIsHaveSDCard = CheckSDCardUtil.IsHaveSDcard();

		Timer mTimer = new Timer();
		// ����
		TimerTask mTask = new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		};

		// ����ѽ���WIFI�ʹ���SD��������תҳ��
		if (mIsHaveWIFI == true) {
			if (mIsHaveSDCard == true) {
				// ��ʱ2m֮����ת����ҳ��
				mTimer.schedule(mTask, 2000);
			} else {
				//ʹ��Toast��ʾSD��δ����
		        ShowToastMsgUtil.getInstance(this).ShowToastMsg("SD��δ����,���˳�Ӧ��");
			}
		} else {
			//ʹ��Toast��ʾWIFIδ����
	        ShowToastMsgUtil.getInstance(this).ShowToastMsg("WIFIδ����,���˳�Ӧ��");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
