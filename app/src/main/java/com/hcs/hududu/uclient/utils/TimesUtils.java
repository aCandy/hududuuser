package com.hcs.hududu.uclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimesUtils {
	/**
	 * 毫秒换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDateLong(long strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(strDate * 1000));
		return date;
	}

	public static long strToLong(String strDate) {
		long time = 0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = df.parse(strDate);
			time = date.getTime();
			System.out.println(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}

	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
		return time;
	}

	public static String getNowTimeAdd30() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30);
		String time = calendar.getTimeInMillis() / 1000 + "";
		return time;
	}

	public static String dataFormat(String data) {
		// Date nowTime = new Date();
		// System.out.println(nowTime);
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(time.format(data) + "");
		return time.format(data);
	}

	public static String dataFormatMinAndSecond(long time) {
		String minute = (time / 60) + "";
		String second = (time % 60) + "";
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		if (second.length() == 1) {
			second = "0" + second;
		}
		return minute + ":" + second;
	}

	/**
	 * 获取当前年月日
	 * 
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String time = df.format(new Date());
		return time;
	}
}
