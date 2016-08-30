package com.jizhi.hududu.uclient.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;

import com.google.gson.Gson;
import com.hcs.hududu.uclient.utils.AppUtils;
import com.hcs.hududu.uclient.utils.DownLoadingApkUtil;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.GetCodeBean;
import com.jizhi.hududu.uclient.bean.LoginBean;
import com.jizhi.hududu.uclient.bean.NearFk;
import com.jizhi.hududu.uclient.bean.NearFkState;
import com.jizhi.hududu.uclient.bean.OrderhMFkInfo;
import com.jizhi.hududu.uclient.bean.OrderhMFkState;
import com.jizhi.hududu.uclient.bean.VegOrderhFkState;
import com.jizhi.hududu.uclient.main.HandWasingMainActivity;
import com.jizhi.hududu.uclient.main.MyCardBagActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.UtilConn;
import com.umeng.socialize.utils.Log;

public class ParseHttpData {
	public static NearFkState getNeatFK(Activity activity, String url,
			List<NameValuePair> params) {
		NearFkState fkState = new NearFkState();
		String content = UtilConn.getContent(activity, url, params);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			fkState.setState(state);
			if (state == 1) {
				List<NearFk> fks = new ArrayList<NearFk>();
				JSONArray jsonArray = jsonObject.getJSONArray("resp");
				for (int i = 0; i < jsonArray.length(); i++) {
					NearFk fk = new NearFk();
					JSONObject object = jsonArray.getJSONObject(i);
					fk.setName(object.optString("name"));
					fk.setOrdertotal(object.optInt("ordertotal"));
					fk.setPic(object.optString("pic"));
					fk.setIcno(object.optString("icno"));
					fk.setAge(object.getString("age"));
					fk.setAvgrate(object.getDouble("avgrate"));
					JSONArray jsonArray2 = object.getJSONArray("location");
					for (int j = 0; j < jsonArray2.length(); j++) {
						double ll = Double.parseDouble(jsonArray2.get(j)
								.toString());
						if (j == 0) {
							fk.setLongitude(ll);
						} else if (j == 1) {
							fk.setLatitude(ll);
						}
					}
					fks.add(fk);
				}
				fkState.setNearFks(fks);
			} else {
				fkState.setCode(jsonObject.optInt("code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fkState;
	}
	public static List<NameValuePair> paramsCommon(Activity mActivity) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("option", "preloading"));
		String priceVer = (String) SPUtils.get(mActivity, "priceVer", "",
				Constance.PRICEINFO);
		params.add(new BasicNameValuePair("priceVer", priceVer));
		return params;
	}

	/**
	 * 获取价格信息
	 * 
	 */
	public static void getPrice(final Activity mActivity) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getCommon(mActivity, CMD.PRICE, paramsCommon(mActivity));
				checkVersion(mActivity);
			}
		}).start();
	}

	/** 价格信息 */
	public static void getCommon(final Activity activity, String url,
			List<NameValuePair> params) {
		//从服务器获取到的关于价格的JSON数据
		String content = UtilConn.getContent(activity, url, params);
		Log.d("TAG","parContent:"+content);
		try {
			//新建JSONObject对象用于解析content这个JSON数据
			JSONObject jsonObject = new JSONObject(content);
			//解析JSON数据中state值。optInt和getInt类似，区别在于opt出现null的时候不会报错。
			Log.d("TAG","obj:"+jsonObject);
			int state = jsonObject.optInt("state");
			if (state == 1) {
				if (!jsonObject.isNull("resp")) {
					JSONObject object = jsonObject.getJSONObject("resp");
					// 日常打扫
					Log.d("TAG","obj:23555");



					SPUtils.put(activity, "dailycleaning",
							object.optString("dailycleaning"),
							Constance.PRICEINFO);


					// 生鲜代购
					SPUtils.put(activity, "freshhelper",
							object.optString("freshhelper1"),
							Constance.PRICEINFO);
					// 饭后洗碗
					SPUtils.put(activity, "washdishes",
							object.optString("washdishes1"),
							Constance.PRICEINFO);
					// 洗衣
					SPUtils.put(activity, "washclothes",
							object.optString("washclothes1"),
							Constance.PRICEINFO);
					// 简单打扫
					SPUtils.put(activity, "simpcleaning",
							object.optString("simpcleaning"),
							Constance.PRICEINFO);
					// 洗衣折扣价
					SPUtils.put(activity, "fhdiscount",
							object.optString("fhdiscount"), Constance.PRICEINFO);
					// 饭后洗碗折扣价
					SPUtils.put(activity, "wddiscount",
							object.optString("wddiscount"), Constance.PRICEINFO);
					// 日常打扫折扣价
					SPUtils.put(activity, "hwdiscount",
							object.optString("hwdiscount"), Constance.PRICEINFO);
					// 价格版本
					SPUtils.put(activity, "priceVer",
							object.optString("priceVer"), Constance.PRICEINFO);
					SPUtils.put(activity, "timeunit",
					// 一个时间单位为60分钟
							object.optString("timeunit"), Constance.PRICEINFO);
					SPUtils.put(activity, "andversion",
							object.optString("andversion"), Constance.PRICEINFO);
					//更新内容
					SPUtils.put(activity, "candversionsmsg",
							object.optString("candversionsmsg"), Constance.PRICEINFO);
					
				} 
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static OrderhMFkState getOrderfkInfo(String str) {
		OrderhMFkState fkState = new OrderhMFkState();
		try {
			JSONObject jsonObject = new JSONObject(str);
			int state = jsonObject.optInt("state");
			fkState.setState(state);
			if (state == 1) {
				OrderhMFkInfo fkInfo = new OrderhMFkInfo();
				JSONObject jo = jsonObject.getJSONObject("resp");
				fkInfo.setSendcount(jo.optInt("sendcount"));
				if (!jo.isNull("info")) {
					JSONObject jo1 = jo.getJSONObject("info");
					fkInfo.setLid(jo1.getString("lid"));
					fkInfo.setName(jo1.getString("name"));
					fkInfo.setWage(jo1.getDouble("wage"));
					fkInfo.setPic(jo1.getString("pic"));
					fkInfo.setGender(jo1.getString("gender"));
					fkInfo.setAge(jo1.getInt("age"));
					fkInfo.setIcno(jo1.getString("icno"));
					fkInfo.setActivityType(jo1.getString("activityType"));
					fkInfo.setDiscount(jo1.getDouble("discount"));
					fkInfo.setJobdesc(jo1.getInt("jobdesc"));
					fkInfo.setAvgrate(jo1.getDouble("avgrate"));
					fkInfo.setOrdertotal(jo1.getInt("ordertotal"));
					fkInfo.setWtype(jo1.getString("wtype"));
					fkInfo.setDistance(jo1.getInt("distance"));
					fkInfo.setWorkinghours(jo1.getDouble("workinghours"));

					fkState.setFkInfo(fkInfo);
				}

			} else {
				fkState.setCode(jsonObject.optInt("code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fkState;
	}

	public static GetCodeBean cancelOrder(Activity activity, String url,
			List<NameValuePair> params) {
		String content = UtilConn.getContent(activity, url, params);
		GetCodeBean bean = new GetCodeBean();
		try {
			Gson gson = new Gson();
			bean = gson.fromJson(content, GetCodeBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 获取验证码
	 * 
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	public static GetCodeBean getCode(Activity activity, String url,
			List<NameValuePair> params) {
		//返回的数据
		String content = UtilConn.getContent(activity, url, params);
		GetCodeBean bean = new GetCodeBean();
		try {
			//利用Gson框架进行数据解析
			Gson gson = new Gson();
			bean = gson.fromJson(content, GetCodeBean.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回数据对象
		return bean;
	}

	/**
	 * 登录,注册
	 * 
	 * @param activity
	 * @param url
	 * @param params
	 * @return
	 */
	/*public static LoginBean register(Activity activity, String url,
			List<NameValuePair> params) {
		String content = UtilConn.getContent(activity, url, params);
		LoginBean bean = new LoginBean();
		try {
			Gson gson = new Gson();
			bean = gson.fromJson(content, LoginBean.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}*/
	//新的验证码验证、登录方法
	public static LoginBean registers(String json){
		LoginBean bean=new LoginBean();
		try {
			Gson gson=new Gson();
			bean=gson.fromJson(json,LoginBean.class);

		}catch (Exception e){
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 解析抢单服客信息
	 * 
	 * @param str
	 * @return
	 */
	public static VegOrderhFkState getVegOrderfkInfo(String str) {
		VegOrderhFkState fkState = new VegOrderhFkState();
		try {
			Gson gson = new Gson();
			fkState = gson.fromJson(str, VegOrderhFkState.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fkState;
	}
	
	
	/**
	 * 检查版本
	 * @param str
	 * @return
	 */
	public static void checkVersion(final Activity activity) {
		int serverVersion = Integer.parseInt((String)SPUtils.get(activity,"andversion","1",Constance.PRICEINFO));
		int localVersion = AppUtils.getVersionCode(activity);
		Log.e("服务器版本",serverVersion+"");
		Log.e("本地版本",localVersion+"");
		
		
		if(serverVersion>localVersion){
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(activity,android.R.style.Theme_Holo_Light_Dialog));
				adb.setTitle("发现新的客户端版本，点击确定更新！");
				adb.setMessage((String)SPUtils.get(activity,"candversionsmsg","更新内容",Constance.PRICEINFO));
				adb.setPositiveButton("确定",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								DownLoadingApkUtil down = new DownLoadingApkUtil(activity);
								down.checkUpdateInfo();
							}
						});
				adb.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int id) {
						dialog.dismiss();
					}
				});
				adb.show();
			}
		});
	  }
	}
}
