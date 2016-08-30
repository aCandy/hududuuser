package com.jizhi.hududu.uclient.util;

import java.text.DecimalFormat;

public class DataForMat {
	public static DecimalFormat df = new DecimalFormat("#0.00");
	
	/**
	 * 替换2位小数
	 * @return
	 */
	public static String twoDecimalPlaces(float money){
		return df.format(money);
	}
	
	/**
	 * 替换2位小数
	 * @return
	 */
	public static String twoDecimalPlaces(double money){
		return df.format(money);
	}
}
