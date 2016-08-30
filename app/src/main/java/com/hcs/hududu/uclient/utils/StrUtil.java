package com.hcs.hududu.uclient.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
	/**按钮点击间隔时间*/
	private static long lastClickTime;

	/**
	 * 判断内容是否为空
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
	 * 判断是否是手机号
	 * @param num
	 * @return
	 */
	public static boolean isMobileNum(String num) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

	/**
	 * 判断是否为邮箱
	 */
	public static boolean checkEmail(String email) {

		String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;

	}
	/**
	 * 判断是否为身份证号码
	 * @param str
	 * @return
	 */
	public static boolean isIDCcard(String str) {
		Pattern pattern = Pattern.compile("([0-9]{17}([0-9]|X))|([0-9]{15})");
		return pattern.matcher(str).matches();
	}

	/**
	 *防止按钮重复点击
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
}
