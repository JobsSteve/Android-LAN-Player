package com.cvte.lanplayer.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * ��ʾTOAST��Ϣ
 * 
 * @author JHYin
 * 
 */
public class ShowToastMsgUtil {

	private final String TAG = "ShowToastMsgUtil";
	private static ShowToastMsgUtil instance = null;

	// �����յ���Ϣ��ص�
	private static Context mContext;

	/**
	 * ˽��Ĭ�Ϲ�����
	 */
	private ShowToastMsgUtil() {

	}

	/**
	 * ��̬��������
	 */
	public static synchronized ShowToastMsgUtil getInstance(Context context) {

		// mContext = context;
		mContext = context;

		if (instance == null) {
			instance = new ShowToastMsgUtil();
		}

		return instance;
	}

	/**
	 * ��ʾToast ��Ϣ
	 * 
	 * @param msg
	 */
	public void ShowToastMsg(final String msg) {

		new Thread(new Runnable() {
			public void run() {
				// Log.d(TAG, "Service in Thread: " + "\n" + "��ǰ�߳����ƣ�"
				// + Thread.currentThread().getName() + "," + "��ǰ�߳����ƣ�"
				// + Thread.currentThread().getId());

				Looper.prepare();

				Toast.makeText(mContext.getApplicationContext(), msg,
						Toast.LENGTH_SHORT).show();

				Looper.loop();
			}
		}).start();

	}
}
