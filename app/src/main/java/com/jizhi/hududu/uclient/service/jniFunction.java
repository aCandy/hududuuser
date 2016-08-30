package com.jizhi.hududu.uclient.service;
/**
 * 未知父类
 * 尚未发现应用场景
 * */
public class jniFunction {
	
	static {
        System.loadLibrary("hdd");
    }
	
	public static native String hello();
	public static native String getmessage(String lid);
	public static native String getorderanswerd(String url,String body);

}
