package com.jizhi.hududu.uclient.json;

import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import com.google.gson.Gson;
import com.jizhi.hududu.uclient.bean.ServerDetail;
import com.jizhi.hududu.uclient.bean.ServerDetailState;
import com.jizhi.hududu.uclient.util.UtilConn;
import com.umeng.socialize.utils.Log;

/**
 * 服客资料解析
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class ServerDataResolution {
	public ServerDetailState search(Activity activity, String url,List<NameValuePair> params) {
		ServerDetailState server_state = new ServerDetailState();
		String content = UtilConn.getContent(activity, url, params);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			server_state.setState(state);
			if (state == 1) {
				Gson gson = new Gson();
				server_state = gson.fromJson(content,ServerDetailState.class);
			} else {
				server_state.setState(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server_state;
	}
}
