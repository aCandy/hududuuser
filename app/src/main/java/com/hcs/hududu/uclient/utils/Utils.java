package com.hcs.hududu.uclient.utils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	public static String logStringCache;
	// public static String channelIds;
	private static final double EARTH_RADIUS = 6378137;
	private static long lastClickTime;

	/**
	 * 验证字符串是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(String... obj) {
		for (String s : obj) {
			if (s == null || "".equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是手机
	 * @param num
	 * @return
	 */
	public static boolean isMobileNum(String num) {
		Pattern p = Pattern.compile("1[0-9]{10}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}
	

	/**
	 * 电子邮件
	 */
	public static boolean checkEmail(String email) {
		String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;

	}

	public static boolean isIDCcard(String str) {
		Pattern pattern = Pattern.compile("([0-9]{17}([0-9]|X))|([0-9]{15})");
		return pattern.matcher(str).matches();
	}

	/**
	 * 防止按钮重复点击
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 动态计算listview高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (null != listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}

			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			params.height += 45;// if without this statement,the listview will
								// be a
								// little short
			listView.setLayoutParams(params);
		}

	}

	/**
	 * 获取Mac
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {

		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();

	}

	/**
	 * 获取小数点后两位
	 * 
	 * @param f
	 * @return
	 */
	public static String m2(double f) {
		DecimalFormat df = new DecimalFormat("0.00");
		String ff = df.format(f);
		return ff;
	}

	/**
	 * 获取字符串中数字
	 * 
	 * @param str
	 * @return
	 */
	public static double getNumber(String str) {
		String regEx = "[^0-9|.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		Double num = Double.parseDouble(m.replaceAll("").trim());
		return num;

	}

	//
	// /**
	// * 使用java正则表达式去掉多余的.与0
	// *
	// * @param s
	// * @return
	// */
	// public static String subZeroAndDot(String s) {
	// if (s.indexOf(".") > 0) {
	// s = s.replaceAll("0+?$", "");// 去掉多余的0
	// s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
	// }
	// return s;
	// }

	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.commit();
	}

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 查询应用是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOpenMyApp(Context context) {
		boolean isAppRunning = false;
		String MY_PKG_NAME = getAppPacKageName(context);
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);

		for (RunningTaskInfo info : list) {

			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					&& info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {

				isAppRunning = true;
				break;
			} else {
				isAppRunning = false;
			}

		}
		return isAppRunning;
	}

	/**
	 * 查询应用后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i("后台", appProcess.processName);
					return true;
				} else {
					LUtils.i("前台");
					return false;
				}
			}
		}
		return false;
	}

	public static boolean isTopActivity(Context context) {

		String packageName = getAppPacKageName(context);
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

		if (tasksInfo.size() > 0) {
			// System.out.println("---------------包名-----------"
			// + tasksInfo.get(0).topActivity.getPackageName());

			// 应用程序位于堆栈的顶层

			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {

				return true;

			}

		}

		return false;

	}

	/**
	 * 获取应用包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppPacKageName(Context context) {
		String pkName = context.getPackageName();
		LUtils.i("packageName---->" + pkName);
		return pkName;

	}

	/**
	 * 手机号码中间4为替换为*
	 * 
	 * @param args
	 */
	public static String mobileReplace(String mobile) {
		String m = mobile.substring(0,
				mobile.length() - (mobile.substring(3)).length())
				+ "****" + mobile.substring(7);
		return m;
	}

	public static String changeTime(int time) {
		int hour = time / 60;
		int minute = time % 60;
		StringBuffer buffer = new StringBuffer();

		if (hour != 0) {
			buffer.append(hour + "小时");
		}
		if (minute != 0) {
			buffer.append(minute + "分钟");
		}
		return buffer.toString();

	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return 距离：单位为米
	 */
	public static double DistanceOfTwoPoints(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 电子邮件
	 */
	public boolean checkEmail() {

		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher("dffdfdf@qq.com");
		boolean isMatched = matcher.matches();
		return isMatched;

	}

}
