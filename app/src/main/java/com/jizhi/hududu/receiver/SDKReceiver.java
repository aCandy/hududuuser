package com.jizhi.hududu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.jizhi.hududu.uclient.util.CommonMethod;

/**
 * 百度地图KEY验证以及网络验证
 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
 */
public class SDKReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		String s = intent.getAction();
		if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
			CommonMethod.makeNoticeShort(context, "百度地图key 验证出错");
		} else if (s
				.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
			CommonMethod.makeNoticeShort(context, "网络出错");
		}
	}
}