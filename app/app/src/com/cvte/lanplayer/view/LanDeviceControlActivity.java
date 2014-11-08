package com.cvte.lanplayer.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.R;
import com.cvte.lanplayer.adapter.MyListAdapter;
import com.cvte.lanplayer.entity.SocketTranEntity;
import com.cvte.lanplayer.utils.SendSocketMessageUtil;

public class LanDeviceControlActivity extends Activity {

	private final String TAG = "LanDeviceControlActivity";

	private String targetIp = null;

	private TextView tv_target_ip;
	private Button btn_send_msg;
	private ListView lv_music_list;

	private List<String> mMusicList = new ArrayList<String>();
	private MyListAdapter mMusicListAdapter;

	private RecvScoketMsgReceiver mRecvScoketMsgReceiver;

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

	private void InitView() {

		tv_target_ip = (TextView) findViewById(R.id.tv_target_ip);
		btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
		lv_music_list = (ListView) findViewById(R.id.lv_music_list);

		tv_target_ip.setText("Ŀ��IP��" + targetIp);

		btn_send_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.d(TAG, "on click button");

				// ���ͻ�ȡ�����б������
				// ʵ�����������
				SocketTranEntity msg = new SocketTranEntity();
				msg.setmCommant(GlobalData.SocketTranCommand.COMMAND_REQUSET_MUSIC_LIST);

				SendSocketMessageUtil
						.getInstance(LanDeviceControlActivity.this)
						.SendMessage(msg, targetIp);

			}
		});

		// ΪListView����Adapter
		mMusicListAdapter = new MyListAdapter(mMusicList, this);
		lv_music_list.setAdapter(mMusicListAdapter);

		// ����ListView����
		lv_music_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ȷ�������ȡ�Ի���
				ShowRequsetDialog(position);
			}
		});
	}

	private void InitData() {
		Intent intent = getIntent();
		targetIp = (String) intent.getSerializableExtra("ip");

		// ע�������
		mRecvScoketMsgReceiver = new RecvScoketMsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.SocketTranCommand.RECV_SOCKET_FROM_SERVICE_ACTION);
		registerReceiver(mRecvScoketMsgReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ������ʱ��ȡ��ע��㲥�����������򱨴�
		unregisterReceiver(mRecvScoketMsgReceiver);
	}

	/**
	 * �����Ƿ�������ȡ�Ի���
	 * 
	 */
	private void ShowRequsetDialog(final int musicID) {

		String musicName = mMusicList.get(musicID);

		AlertDialog.Builder builder = new Builder(this);

		// ���ñ���
		builder.setTitle("��ȡ����");
		// ����������Ϣ
		builder.setMessage("�Ƿ���ȡ��" + musicName);

		// ȷ��
		builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Log.d(TAG, "on click:�����ȡ���֣�" + String.valueOf(musicID));

				// �����ȡĿ��IP����Ӧ�������ļ�
				// ʵ�����������
				SocketTranEntity msg = new SocketTranEntity();
				msg.setmCommant(GlobalData.SocketTranCommand.COMMAND_REQUSET_MUSIC_FILE);
				// ��Ŀ��IP�������б�ı����Ϊ���
				msg.setmMessage(String.valueOf(musicID));
				msg.setmSendIP(getIpAddress());

				SendSocketMessageUtil
						.getInstance(LanDeviceControlActivity.this)
						.SendMessage(msg, targetIp);

				// �����ļ�
				// SendSocketFileUtil.getInstance().SendFile(
				// AppConstant.MusicPlayData.myMusicList.get(musicID)
				// .getFileName(),
				// AppConstant.MusicPlayData.myMusicList.get(musicID)
				// .getFilePath(), et_ip.getText().toString());

			}
		});

		// ȡ��
		builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		builder.show();

	}

	/**
	 * ��ȡ������IP��ַ
	 * 
	 * �Ժ��ع�������ȥ��
	 * 
	 */
	private String getIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		int ipAddress = wifiInfo.getIpAddress();
		// Log.d("TAG","IP:"+ String.valueOf(ipAddress));

		return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
				+ (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));

	}

	// ��ȡɨ�������IP��ַ�Ľ�����
	private class RecvScoketMsgReceiver extends BroadcastReceiver {

		// �Զ���һ���㲥������
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "RECV:COMMAND");

			Bundle bundle = intent.getExtras();

			// ��ȡָ��
			int commant = bundle
					.getInt(GlobalData.SocketTranCommand.GET_BUNDLE_COMMANT);

			// �����Ƿ��������б�
			if (commant == GlobalData.SocketTranCommand.COMMAND_SEND_MUSIC_LIST) {
				Log.d(TAG, "RECV:COMMAND_SEND_MUSIC_LIST");

				SocketTranEntity data = (SocketTranEntity) bundle
						.getSerializable(GlobalData.SocketTranCommand.GET_BUNDLE_COMMON_DATA);

				// ���յ���������ʾ����
				mMusicList.clear(); // ����б�
				for (int i = 0; i < data.getmMusicList().size(); i++) {
					mMusicList.add(data.getmMusicList().get(i).getFileName());
				}

				// ��������
				mMusicListAdapter.notifyDataSetChanged();

			}

		}
	}
}
