package com.jizhi.hududu.uclient.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.widget.ProgressWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 用户协议Activity
 * @author huChangSheng
 * @time 2015-5-28 下午4:40:53
 * @version 1.0
 * 
 */
public class AgreementActivity extends Activity {
	@ViewInject(R.id.rb_back)
	private RadioButton rb_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	private ProgressWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agressment);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu);
		ViewUtils.inject(this);
		tv_title.setText("用户协议");
		rb_back.setText(getString(R.string.back));
		webView = (ProgressWebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		webView.loadUrl(CMD.lread);
		// 设置Web视图
		webView.setWebViewClient(new webViewClient());

	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		finish();// 结束退出程序
		return false;
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@OnClick(R.id.rb_back)
	public void finishAct(View view) {
		finish();
	}

}
