package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.DinnerState;
import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 查询上门做饭厨师
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class EatFoodResolution {
	public DinnerState resolution(Activity activity, String url,List<NameValuePair> params,double lng,double lat) {
		DinnerState food_state = new DinnerState();
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			food_state.setState(state);
			if (state == 1) {
				Gson gson = new Gson();
				food_state = gson.fromJson(content,DinnerState.class);
				for(Dinner bean:food_state.getResp()){
					bean.setFar(DistanceOfTwoPoints(lat,lng,bean.getLocation()[1],bean.getLocation()[0]));
					bean.setBeseCookingData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return food_state;
	}
	
	private final double EARTH_RADIUS = 6378137;
	
	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，
	 * @param lat1 
	 * @param lng1
	 * @param lat2 
	 * @param lng2
	 * @return 距离：单位为米
	 */
	public double DistanceOfTwoPoints(double lat1, double lng1,
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
	
	
	private double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
