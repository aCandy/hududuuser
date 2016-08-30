package com.jizhi.hududu.uclient.util;

public class Constance {
	public static final String PRICEINFO = "priceinfo";
	public static final String HUDUDUUSER = "hududuuser";
	public final static int REQUESTCODE_NEAYBYADDE = 0X00;
	public final static int RESULTCODE_NEAYBYADDE = 0X01;
	public static final int REQUESTCODE_HOMEMAKE = 0X02;
	public static final int RESULTCODE_HOMEMAKE = 0X03;
	public static final int REQUESTCODE_LOGIN = 0X04;
	public static final int RESULTCODE_LOGIN = 0X05;
	
	//拍照图片回调
	public static final int REQUESTCODE_PHOTOGRSPHIMG = 13;
	
	public static final int REQUESTCODE_CROPIDCARD = 0X09;
	public static final int REQUESTCODE_CROPHEALTH = 0X10;
	// 选择相册图片回调 用来标识请求gallery的activity
	public static final int REQUESTCODE_CHOOSEIMG = 0X11;
	// 裁剪图片回调
	public static final int REQUESTCODE_MODIFY_FINISH = 0X12;
	// 身份证拍照图片回调/ 用来标识请求照相功能的activity */
	public static final int REQUESTCODE_PHOTOCAMERAIDCARD = 0X13;
	// 健康证拍照图片回调/ 用来标识请求照相功能的activity */
	public static final int REQUESTCODE_PHOTOCAMERAHEALTH = 0X14;
	
	
}
