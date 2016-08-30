package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.LinearGradient;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jizhi.hududu.uclient.bean.BuyVegetablesState;
import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 解析帮我买菜
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class BuyVegetablesResolution {
	public BuyVegetablesState resolution(Activity activity, String url,
			List<NameValuePair> params,String type) {
		BuyVegetablesState orderState = null;
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			if (state == 1) {
				Gson gson = new Gson();
				orderState = gson.fromJson(content,BuyVegetablesState.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderState;
	}
	
	
	/**
	 * 提交买菜
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public BuyVegetablesState submit(Activity activity, String url,
			List<NameValuePair> params) {
		BuyVegetablesState clean_state = new BuyVegetablesState();
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			clean_state.setState(state);
			if (state == 1) {
				String oid = jsonObject.getJSONObject("resp").getString("oid");
				if(oid!=null && oid.length()!=0){
					clean_state.setOid(oid);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clean_state;
	}
	
	

}
