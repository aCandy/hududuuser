package com.jizhi.hududu.uclient.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hcs.hududu.uclient.utils.LUtils;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class UtilConn {
	

	/**
	 * 获取内容
	 */
	public static String getContent(InputStream in, String charset)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048 * 10];
		int len = -1;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		String content = out
				.toString(charset == null || charset.equals("") ? "GBK"
						: charset);

		if (out != null) {
			out.close();
		}
		return content;
	}

	public String getLogin(Activity activity, String url,List<NameValuePair> params) {
		String content = "";
		LUtils.i("手机端发送数据：" + params.toString());
		try {
			AssetManager assetManager = activity.getAssets();
			KeyStore ts = KeyStore.getInstance("BKS");
			ts.load(assetManager.open("android.bks"), "hududu".toCharArray());
			SSLSocketFactory socketFactory = new SSLSocketFactory(ts);
			Scheme sch = new Scheme("https", socketFactory, 443);

			// 1 得到浏览器
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
			// 请求超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			// 读取超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 30000);
			// 2 指定请求方式
			HttpPost httpPost = new HttpPost(url);
			// 3构建实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"utf-8");
			// 4把实体数据设置到请求对象
			httpPost.setEntity(entity);
			// 5 执行请求
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();
			// 6判断请求是否成功
			if (code == 200) {
				content = EntityUtils.toString(httpResponse.getEntity());
				LUtils.i("服务器返回数据：" + content);
				// CookieStore mCookieStore = httpClient.getCookieStore();
				// List<Cookie> cookie = mCookieStore.getCookies();
				// for (int i = 0; i < cookie.size(); i++) {
				// if (cookie.isEmpty()) {
				// LUtils.i("cookie为空");
				// } else {
				// LUtils.i("cookie==");
				// }
				// }
			} else {
				ToastReadTimeout(activity, code);
			}

		} catch (Exception e) {
			ToastConnectTimeout(activity, e.getMessage());
		} finally {
			// dissDialog(activity, dialog, bar);
		}
		return content;
	}
	//网络的POST请求
	public static String getContent(Activity activity, String url,
			List<NameValuePair> params) {
		String content = "";
		try {
//			AssetManager assetManager = activity.getAssets();
//			KeyStore ts = KeyStore.getInstance("BKS");
//			ts.load(assetManager.open("android.bks"), "hududu".toCharArray());
//			SSLSocketFactory socketFactory = new SSLSocketFactory(ts);
//			Scheme sch = new Scheme("https", socketFactory, 443);

			// 1 得到浏览器
			DefaultHttpClient httpClient = new DefaultHttpClient();
//			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
			// 请求超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			// 读取超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 30000);
			// 2 指定请求方式
			HttpPost httpPost = new HttpPost(url);

			String sessid = (String) SPUtils.get(activity, "sessid", "",Constance.HUDUDUUSER);
			LUtils.i("sessid-->" + sessid);

			if (!sessid.equals("")) {
				httpPost.setHeader("Cookie", "PHPSESSID=" + sessid);
				Log.d("TAG","sssID:"+sessid);
				//httpPost.setHeader("Cookie", "PHPSESSID=" + sessid);
				LUtils.i("sessid--->" + sessid);
			}
			// 3构建实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
			// 4把实体数据设置到请求对象
			httpPost.setEntity(entity);
			// 5 执行请求
			HttpResponse httpResponse = httpClient.execute(httpPost);
			LUtils.i("手机端发送数据：" + params.toString());
			int code = httpResponse.getStatusLine().getStatusCode();
			// 6判断请求是否成功
			if (code == 200) {
				content = EntityUtils.toString(httpResponse.getEntity());
				Log.d("TAG","content:"+content);
				LUtils.i("服务器返回数据：" + content);
				CookieStore mCookieStore = httpClient.getCookieStore();
				List<Cookie> cookie = mCookieStore.getCookies();
				//获取验证码环节未执行此循环
				for (int i = 0; i < cookie.size(); i++) {
					if (cookie.isEmpty()) {
						LUtils.i("cookie为空");
					} else {
						LUtils.i("cookie==" + cookie.get(i).getValue());
					}
				}
			} else {
				ToastReadTimeout(activity, code);
			}

		} catch (Exception e) {
			ToastConnectTimeout(activity, e.getMessage());
		} finally {
			// dissDialog(activity, dialog, bar);
		}
		return content;
	}

	public static void ToastConnectTimeout(final Activity activity,
			final String str) {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, "服务器连接异常,请检查网络后重试", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public static void dissDialog(final Activity activity,
			final boolean dialog, final ProgressBar bar) {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (dialog) {
					if (null != bar) {
						bar.setVisibility(View.GONE);
					}

				}
			}
		});
	}

	public static void ToastReadTimeout(final Activity activity, final int code) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity, "服务端异常[错误码:" + code + "]",
						Toast.LENGTH_SHORT).show();
				// CustomProgress.dissmiss();
			}
		});
	}

}
