package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.Utils;
import com.jizhi.hududu.uclient.bean.HouseholdCleaningBean;
import com.jizhi.hududu.uclient.json.FeedBackResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 意见反馈页面
 * @author Xuj
 * @date 2015年8月18日 15:59:49
 */
public class FeedBackActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.feed)
	private EditText feed;//意见建议

	@ViewInject(R.id.information)
	private EditText information; //联系方式

	@ViewInject(R.id.submit)
	private Button submit;

	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	private Dialog dialog;
	
	private FeedBackResolution resolution = new FeedBackResolution();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.feedback);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		tv_title.setText(getString(R.string.feedback));
		submit.setOnClickListener(this);
		information.setInputType(InputType.TYPE_CLASS_PHONE);//电话
		information.setInputType(InputType.TYPE_CLASS_NUMBER);//只能输入数字
		information.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); //邮箱地址
	}

	@OnClick(R.id.rb_back)
	public void finishAct(View view) {
		finish();
	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public void onClick(View view) {
		// Intent intent;
		switch (view.getId()) {
		case R.id.submit:
			String content = feed.getText().toString();
			if(TextUtils.isEmpty(content)){
				CommonMethod.makeNoticeShort(FeedBackActivity.this,"请输入您的宝贵意见!");
				return;
			}
			String mobile = information.getText().toString();
			//|| !Utils.checkEmail(in)
			if(Utils.isMobileNum(mobile) || Utils.checkEmail(mobile)){
				submitFeed(content, mobile);
				return;
			}else{
				CommonMethod.makeNoticeShort(FeedBackActivity.this,"请输入正确的联系方式!");
			}
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 提交意见反馈
	 */
	public void submitFeed(final String content,final String mobile) {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(FeedBackActivity.this,getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final boolean access = resolution.resolution(FeedBackActivity.this,CMD.SUBMIT_FEED_BACK, params(content,mobile));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (access) {
							CommonMethod.makeNoticeShort(FeedBackActivity.this,"感谢您反馈的意见!");
							finish();
						}else{
							CommonMethod.makeNoticeShort(FeedBackActivity.this,"意见提交失败!");
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
	}
	
	public List<NameValuePair> params(String content,String mobile) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid","13880280091")); 
		params.add(new BasicNameValuePair("contents",content)); //意见内容
		params.add(new BasicNameValuePair("contactway",mobile)); //联系方式
		
		return params;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	

}
