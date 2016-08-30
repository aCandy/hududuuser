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
 * 读取网页数据
 * @author Xuj
 * @time 2015年8月31日 10:46:33
 * @version 1.0
 */
public class LoadUrlActivity extends Activity {
	@ViewInject(R.id.rb_back)
	private RadioButton rb_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	private ProgressWebView webView;
	
	private String loadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agressment);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this);
		initView();
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
		webView.loadUrl(loadUrl);
		// 设置Web视图
		webView.setWebViewClient(new webViewClient());

	}

	public void initView() {
		//获取跳转时存入的value
		int type = getIntent().getExtras().getInt("type");
		//根据type的值的不同，请求不同的网页数据。
		switch (type) {
		case 1:
			tv_title.setText("家政服务参考");
			loadUrl = CMD.PROCLEANREFER;
			break;
		case 2:
			tv_title.setText("洗衣服务参考");
			loadUrl = CMD.WHSERVICEREFERENCE;
			break;
		case 3:
			tv_title.setText("推荐有奖");
			loadUrl = CMD.RECOMMENDUSER;
			break;
		case 4:
			tv_title.setText("使用帮助");
			loadUrl = CMD.HELPME;
			break;
		case 5:
			tv_title.setText("服客招募");
			loadUrl = CMD.JOINUS;
			break;
		case 6:
			tv_title.setText("帮我买菜参考");
			loadUrl = CMD.FRESHHELPREFER;
			break;
		case 7:
			tv_title.setText("上门做饭参考");
			loadUrl = CMD.HOME;
			break;
		//多多心语，已作废
		/*case 8:
			tv_title.setText("多多新语");
			loadUrl = CMD.EXTENDREADING;
			break;*/
		case 9:
			tv_title.setText("优惠说明");
			loadUrl = CMD.COUPONSRELES;
			break;
		default:
			break;
		}
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
