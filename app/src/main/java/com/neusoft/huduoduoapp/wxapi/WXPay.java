package com.neusoft.huduoduoapp.wxapi;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.neusoft.huduoduoapp.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
/**
 * 微信支付请求*/
public class WXPay {

	private static final String TAG = "MicroMsg.SDKSample.PayActivity";
	PayReq req;
	final IWXAPI msgApi;
	Map<String, String> resultunifiedorder;
	StringBuffer sb;
	private Activity activity;
	private String name;
	private String info;
	private double price;
	private String oid;
	private boolean isInstall;
	private String wtype;

	public WXPay(Activity activity, String name, String info, double price,
			String oid, String wtype) {
		this.activity = activity;
		this.name = name;
		this.info = info;
		this.price = price;
		this.oid = oid;
		msgApi = WXAPIFactory.createWXAPI(activity, null);
		msgApi.registerApp(Constants.APP_ID);
		req = new PayReq();
		sb = new StringBuffer();
		this.wtype = getWype(wtype);
	}

	public String getWype(String type) {
		String wType = "";
		// 帮我买菜
		if (type.equalsIgnoreCase("FH")) {
			wType = CMD.cwxpayfh;
			// 家庭保洁
		} else if (type.equalsIgnoreCase("HW")) {
			// 吃饭了
			wType = CMD.cwxpayhw;
		} else if (type.equalsIgnoreCase("HD")) {
			// 手洗衣服
			wType = CMD.cwxpayhd;
		} else if (type.equalsIgnoreCase("WH")) {
			// 急事速办
			wType = CMD.cwxpaywh;
		} else if (type.equalsIgnoreCase("JS")) {
			wType = CMD.cwxpayjs;
		}
		return wType;

	}

	public void pay() {
		if (null == wtype || wtype.equals("")) {
			CommonMethod.makeNoticeShort(activity,
					activity.getString(R.string.pay_fail));
			return;
		} 
		if (msgApi.isWXAppInstalled()) {
			CustomProgress.show(activity,
					activity.getString(R.string.createPay), false, null);
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
		} else {
			CommonMethod.makeNoticeShort(activity,
					activity.getString(R.string.wechatNoInstall));
		}

	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		System.out.println("44" + packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		System.out.println("55" + appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		System.out.println(66 + sb.toString());
		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		@Override
		protected void onPreExecute() {
			// dialog = ProgressDialog.show(WXPay.this,
			// getString(R.string.app_tip),
			// getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			System.out.println("prepay_id-->" + result.get("prepay_id"));
			if (null == result.get("prepay_id")) {
				CommonMethod.makeNoticeShort(activity,
						activity.getString(R.string.pay_err));
				CustomProgress.dissmiss();
			} else {
				resultunifiedorder = result;
				genPayReq();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			//微信支付链接
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "呼多多"));
			packageParams.add(new BasicNameValuePair("detail", "呼多多"));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", wtype));
			packageParams.add(new BasicNameValuePair("out_trade_no", oid));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			System.out.println((int) (price * 100) + "");
			packageParams.add(new BasicNameValuePair("total_fee",
					(int) (price * 100) + ""));
			// packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			// packageParams.add(new BasicNameValuePair("attach",
			// "aaaa".toString()));
			String xmlstring = toXml(packageParams);

			// return xmlstring;
			return new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");
			// return new String(xmlstring.toString().getBytes(), "UTF-8");

		} catch (Exception e) {
			//Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			System.out.println("111" + e.getMessage());
			return null;
		}

	}

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		sendPayReq();
		Log.e("orion", signParams.toString());

	}

	private void sendPayReq() {
		isInstall = msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
		CustomProgress.dissmiss();

	}

}
