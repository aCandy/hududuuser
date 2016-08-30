package com.jizhi.hududu.uclient.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.OrderState;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 解析订单数据
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class OrderResolution {
	public OrderState resolution(Activity activity, String url,
			List<NameValuePair> params) {
		OrderState orderState = null;
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.getInt("state");
			if (state == 1) {
				Gson gson = new Gson();
				orderState = gson.fromJson(content,OrderState.class);
				for(Order bean:orderState.getResp()){
					String desc = bean.getDesc();
					String wtype = bean.getWtype();
					if(wtype.equals(CMD.HW)){
						if(desc.equals("1")){
							bean.setProject_name("专业保洁");
						}else if(desc.equals("2")){
							bean.setProject_name("简单打扫");
						}else if(desc.equals("3")){
							bean.setProject_name("饭后洗碗");
						}
						bean.setUnit("时长 : ");
						bean.setWorking_hours_unit("小时");
					}else if(wtype.equals(CMD.FH)){
						int weight = 0;
						String[] cais = desc.split(";");
						bean.setCaiPic(new ArrayList<String>());
						StringBuffer sb = new StringBuffer();
						int i = 0;
						for(String cai:cais){
							String[] c = cai.split(",");
							weight = weight+Integer.parseInt(c[2]);
							if(i<4){
								bean.getCaiPic().add(c[4]);
								if(i == 0){
									sb.append(c[1]);
								}else{
									sb.append(","+c[1]);
								}
							}
							i++;
						}
						bean.setWeight(weight);
						if(cais.length>4){
							bean.setProject_name("帮我买菜("+sb.toString()+"等)");
						}else{
							bean.setProject_name("帮我买菜("+sb.toString()+")");
						}
						
//						1003,土豆,1,1.4,201508181435428071.jpg;1005,米冬瓜,1,1.5,201508181449208949.jpg
						bean.setUnit("重量 : ");
						bean.setWorking_hours_unit("斤");
					}else if(wtype.equals(CMD.HD)){
						bean.setProject_name("吃饭了");
						bean.setUnit("数量 : ");
						bean.setWorking_hours_unit("个");
					}else if(wtype.equals(CMD.WH)){
						bean.setProject_name("手洗衣服");
						bean.setUnit("时长 : ");
						bean.setWorking_hours_unit("小时");
					}else if(wtype.equals(CMD.JS)){
						bean.setProject_name("急事速办");
						bean.setWorking_hours_unit("小时");
					}
				}
			}else if(state==0){
				orderState = new OrderState();
				orderState.setState(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderState;
	}
	

	/**
	 * 提交状态为7的订单
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public OrderState submitStatus7(Activity activity, String url,List<NameValuePair> params) {
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		OrderState order = null;
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.getInt("state");
			if (state == 1) {
				order = new OrderState();
				order.setState(1);
				order.setReceive_time(jsonObject.getJSONObject("resp").getLong("receivetime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}
	
	
	/**
	 * 取消订单
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public OrderState cancelOrder(Activity activity, String url,List<NameValuePair> params) {
		String content = UtilConn.getContent(activity, url, params);
		Log.e("content",content);
		OrderState order = new OrderState();
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.getInt("state");
			if(state == 0){
				order.setError_meesage(jsonObject.getString("errormsg"));
			}
			order.setState(state);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}
}
