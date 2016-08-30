package com.jizhi.hududu.uclient.main;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.AppUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;


/**
 * 关于呼多多
 * @author Xuj
 * @date 2015年8月30日 11:18:34
 */
public class AboutActivity extends Activity implements OnClickListener{

	//微信布局
	@ViewInject(R.id.weixin)
	private RelativeLayout weixin;
	//微博布局
	@ViewInject(R.id.weibo)
	private RelativeLayout weibo;
	
	
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	@ViewInject(R.id.version)
	private TextView version;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.about);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); //Xutil必须调用的一句话
		tv_title.setText(R.string.about_title);
		
		init();
		
		
	}

	public void finishAct(View view) {
		finish();
	}
	
	public void init(){
		weixin.setOnClickListener(this);
		weibo.setOnClickListener(this);
		String v = getResources().getString(R.string.about_text1); 
		version.setText(String.format(v,AppUtils.getVersionName(this)));
		
	}
	
	
	


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.weixin:

			break;
		case R.id.weibo:
			
			break;
		default:
			break;
		}
	}
	
	
	

	
	
	
	

}
