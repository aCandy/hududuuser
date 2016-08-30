package com.jizhi.hududu.uclient.util;

import com.lidroid.xutils.HttpUtils;
/**
 * http帮助类
 * @author zhaoping
 *
 */
public class SingsHttpUtils {
	private static HttpUtils http=null;
	
	private SingsHttpUtils() {
		super();
	}
	/**
	 * 同步获取httpUtils
	 * @return
	 */
	public synchronized static HttpUtils getHttp() {
		if(http==null){
			http=new HttpUtils();
		}
		return http;
	}
}
