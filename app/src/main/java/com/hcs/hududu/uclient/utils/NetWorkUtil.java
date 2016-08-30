package com.hcs.hududu.uclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 
 * <p>
 * 网络
 * </p>
 * 
 * @version 1.0 2015-1-8
 * @author huChangSheng
 */
public class NetWorkUtil {

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			if (null != context) {
				ConnectivityManager cm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (cm != null) {
					NetworkInfo[] info = cm.getAllNetworkInfo();
					if (info != null) {
						for (int i = 0; i < info.length; i++) {
							if (info[i].getState() == NetworkInfo.State.CONNECTED) {
								return true;
								
							}
						}
					}
				}
				Toast.makeText(context, "请检查您的网络是否可用!", 0).show();
				return false;
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}
}
