package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 解析意见反馈
 * @author xuj
 * @date 2015年8月30日 13:58:37
 */
public class FeedBackResolution {
	public boolean resolution(Activity activity, String url,List<NameValuePair> params) {
		String content = UtilConn.getContent(activity, url, params);
		try {
			Log.e("content",content);
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			if (state == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
