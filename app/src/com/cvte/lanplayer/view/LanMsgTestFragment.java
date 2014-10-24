package com.cvte.lanplayer.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cvte.lanplayer.GlobalData;
import com.cvte.lanplayer.R;
import com.cvte.lanplayer.utils.RecvSocketMessageUtil;
import com.cvte.lanplayer.utils.SendSocketMessageUtil;
import com.cvte.lanplayer.view.LanDeviceControlActivity.RecvScoketMsgReceiver;

public class LanMsgTestFragment extends Fragment {

	private Button btn_test_send;

	private TextView tv_recv;
	private EditText et_ip;
	private EditText et_context;

	private Activity mActivity;

	private RecvSocketReceiver mRecvScoketMsgReceiver;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_lanmsg_test, container,
				false);
		btn_test_send = (Button) view.findViewById(R.id.btn_test_send);

		tv_recv = (TextView) view.findViewById(R.id.tv_recv_data);
		et_ip = (EditText) view.findViewById(R.id.et_ip);
		et_context = (EditText) view.findViewById(R.id.et_context);

		btn_test_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// ���Ϸ���ͷ���ٷ�����Ϣ
				SendSocketMessageUtil.getInstance(mActivity).SendMessage(
						GlobalData.COMMAND_SEND_MSG
						+ et_context.getText().toString(),
						et_ip.getText().toString());
			}
		});

		// ע�������
		mRecvScoketMsgReceiver = new RecvSocketReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalData.RECV_SOCKET_FROM_SERVICE_ACTION);
		mActivity.registerReceiver(mRecvScoketMsgReceiver, filter);

		return view;
	}

	// ��ȡɨ�������IP��ַ�Ľ�����
	private class RecvSocketReceiver extends BroadcastReceiver {

		// �Զ���һ���㲥������
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle bundle = intent.getExtras();

			//��ȡָ��
			int commant = bundle.getInt(GlobalData.GET_BUNDLE_COMMANT);

			//����ָ�������д���
			if (commant == GlobalData.RECV_MSG) {
				String str = bundle.getString(GlobalData.GET_BUNDLE_DATA);

				// ���յ���������ʾ����
				tv_recv.setText(str);
			}

		}
	}

}
