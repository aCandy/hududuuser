package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.jizhi.hududu.uclient.bean.ShareState;
import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 评价详情解析
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class EvaluateResolution {
	public ShareState resolution(String content) {
		ShareState share_state = new ShareState();
		Log.e("content",content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			if (state == 1) {
				Gson gson = new Gson();
				share_state = gson.fromJson(content,ShareState.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return share_state;
	}
}
