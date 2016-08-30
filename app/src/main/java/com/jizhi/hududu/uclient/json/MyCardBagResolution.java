package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.jizhi.hududu.uclient.bean.CashCouponState;
import com.jizhi.hududu.uclient.bean.MyCardBagState;
import com.jizhi.hududu.uclient.bean.OrderState;
import com.jizhi.hududu.uclient.util.UtilConn;
import com.umeng.socialize.utils.Log;

/**
 * 优惠券查询
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class MyCardBagResolution {
	public MyCardBagState resolution(Activity activity, String url,List<NameValuePair> params) {
		MyCardBagState card_state = new MyCardBagState();
		String content = UtilConn.getContent(activity, url, params);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			card_state.setState(state);
			if (state == 1) {
				Gson gson = new Gson();
				card_state = gson.fromJson(content,MyCardBagState.class);
			}else{
				card_state.setState(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card_state;
	}
	
	/**
	 * 添加优惠券
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public CashCouponState add(Activity activity, String url,List<NameValuePair> params) {
		CashCouponState card_state = new CashCouponState();
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			card_state.setState(state);
			if (state == 1) {
				Gson gson = new Gson();
				card_state = gson.fromJson(content,CashCouponState.class);
			}else{
				card_state.setErrormsg(jsonObject.getString("errormsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card_state;
	}
	
	
	
	/**
	 * 查询抵扣券
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public CashCouponState search_cash_coupons(Activity activity, String url,List<NameValuePair> params) {
		CashCouponState card_state = null;
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content_ooo",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			Gson gson = new Gson();
			card_state = gson.fromJson(content,CashCouponState.class);
			return card_state;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
