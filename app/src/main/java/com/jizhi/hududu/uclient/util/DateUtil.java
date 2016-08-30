package com.jizhi.hududu.uclient.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.hcs.hududu.uclient.utils.LUtils;

public class DateUtil {

	/**
	 * method 将字符串类型的日期转换为一个timestamp的毫秒数
	 */
	public static String stringToTimeStamp(String dateString)
			throws java.text.ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设定格式
		java.util.Date timeDate = dateFormat.parse(dateString);// util类型
		return (dateFormat.parse(dateString).getTime()/1000)+"";
	}
	
	
	
	
	public static String ChangeData(){
		try {
			return stringToTimeStamp(longToDate(timeTo()));
		} catch (ParseException e) {
			LUtils.e("时间转换出错!");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断是否小于10 如果小于10 则拼接0
	 * 
	 * @param number
	 * @return
	 */
	public static String gtOrGtTen(int number) {
		if (number < 10) {
			return "0" + number;
		} else {
			return "" + number;
		}
	}
	
	
	
	public static long timeTo() {
		long j = 0l;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		java.util.Date curDate = new java.util.Date(System.currentTimeMillis());// 获取当前时间s
		String str = formatter.format(curDate);

		// 2015年09月06日21时59分
		long i = Long.parseLong(str);
		if (i % 10000 >= 2125) {// 超过当天晚上21:25 ，则自动转换到第二天8:00
			return (i / 10000 + 1) * 10000 + 800;
		} else if ((i % 10000 <= 730)) {// 低于当天早上07:30 ，则自动转换到当天8:00
			return (i / 10000) * 10000 + 800;
		} else {
			// 时间加30分钟
			j = i + 30;
			// 对j四舍五入
			if (j % 10 >= 0 && j % 10 < 5) {
				j = j / 10l * 10 + 5;
			} else {
				j = (j / 10l + 1) * 10;
			}
			// 超过60分钟向前进1
			if (j % 100 >= 60) {
				j = (j / 100 + 1) * 100 + (j % 100 - 60);
			}
			return j;
		}
	}

	public static String longToDate(long j) {
		String year = "";
		String month = "";
		String date = "";
		String hour = "";
		String min = "";
		year = String.valueOf(j / 100000000);
		// 月
		if ((j % 100000000) / 1000000 < 10) {
			month = "0" + (j % 100000000) / 1000000;
		} else {
			month = String.valueOf((j % 100000000) / 1000000);
		}
		// 日
		if ((j % 1000000) / 10000 < 10) {
			date = "0" + (j % 1000000) / 10000;
		} else {
			date = String.valueOf((j % 1000000) / 10000);
		}
		// 时
		if ((j % 10000) / 100 < 10) {
			hour = "0" + (j % 10000) / 100;
		} else {
			hour = String.valueOf((j % 10000) / 100);
		}
		// 分
		if (j % 100 < 10) {
			min = "0" + (j % 100);
		} else {
			min = String.valueOf(j % 100);
		}
		return year + "-" + month + "-" + date + " " + hour + ":" + min;
	}
}
