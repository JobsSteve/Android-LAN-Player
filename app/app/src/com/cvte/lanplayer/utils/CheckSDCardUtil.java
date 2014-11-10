package com.cvte.lanplayer.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


/**
 * ���SD���Ƿ�װ��
 * 
 * @author JHYin
 *
 */
public class CheckSDCardUtil {

	private static final String TAG = "CheckSDCardUtil";
	
	/**
	 * �ж�SD���Ƿ����
	 * 
	 * @return
	 */
	public static boolean IsHaveSDcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			Log.d(TAG, "����SD��");

			return true;
		} else {
			Log.d(TAG, "������SD��");
			
			return false;
		}
	}
	
}
