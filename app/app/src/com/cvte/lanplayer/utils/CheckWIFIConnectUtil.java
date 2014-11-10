package com.cvte.lanplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * ���WIFI�Ƿ�����
 * 
 * @author JHYin
 *
 */
public class CheckWIFIConnectUtil {
	private final static String TAG = "CheckWIFIConnectUtil";
	
	
	//�Ƿ�����WIFI
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
        	Log.d(TAG, "WIFI������");
        	
            return true ;
        }
     
        Log.d(TAG, "WIFIδ����");
        
        return false ;
    }
}
