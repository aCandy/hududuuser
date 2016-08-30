package com.jizhi.hududu.uclient.json;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import com.jizhi.hududu.uclient.bean.HouseHoldCleaningState;
import com.jizhi.hududu.uclient.util.UtilConn;

/**
 * 提交家庭保洁订单
 * @author xuj
 * @date 2015年8月14日 11:59:59
 */
public class HouseHoldCleningResolution {
	public HouseHoldCleaningState resolution(Activity activity, String url,List<NameValuePair> params) {
		HouseHoldCleaningState clean_state = new HouseHoldCleaningState();
		String content = UtilConn.getContent(activity, url, params);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int state = jsonObject.optInt("state");
			clean_state.setState(state);
			if (state == 1) {
				String oid = jsonObject.getJSONObject("resp").getString("oid");
				if(oid!=null && oid.length()!=0){
					clean_state.setOid(oid);
				}
			} else {
				clean_state.setCode(jsonObject.optInt("code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clean_state;
	}
}
