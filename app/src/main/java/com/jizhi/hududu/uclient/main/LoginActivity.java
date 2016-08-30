package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.hcs.hududu.uclient.utils.StrUtil;
import com.hcs.hududu.uclient.utils.Utils;
import com.jizhi.hududu.uclient.bean.GetCodeBean;
import com.jizhi.hududu.uclient.bean.LoginBean;
import com.jizhi.hududu.uclient.json.ParseHttpData;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.UtilsCode;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 登陆
 * 
 * @author huChangSheng
 * @time 2015-8-13 下午2:28:13
 * @version 1.0
 * 
 */
public class LoginActivity extends Activity {
	//输入手机号码的EditText
	@ViewInject(R.id.ed_phone)
	private EditText ed_phone;
	//输入验证码的EditText
	@ViewInject(R.id.ed_code)
	private EditText ed_code;
	//标题栏的中间
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	//获取验证码的Button
	@ViewInject(R.id.btn_code)
	private Button btn_code;
	// @ViewInject(R.id.btn_agr)
	// private Button btn_agr;
	//“开始验证”Button
	@ViewInject(R.id.btn_start)
	private Button btn_start;


	private LoginActivity mActivity;
	private String code;// 验证码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//加载登录界面布局
		setContentView(R.layout.activity_login);
		//设置状态栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu_client);
		//Xutils
		ViewUtils.inject(this);

		mActivity = LoginActivity.this;
		//将标题栏中间的文字设置为“验证手机”
		tv_title.setText(R.string.provingmoblie);
		//输入验证码的EditText监听，当EditText里面的内容有变化时，触发该监听
		ed_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			//这里的意思就是当输入验证码时，“开始验证”按钮的背景色就从灰色变为绿色
			@Override
			public void afterTextChanged(Editable s) {
				Resources resources = getResources();
				Drawable drawable;
				if (s.length() <= 0) {
					drawable = resources
							.getDrawable(R.drawable.draw_radius_guide_btn_gray);
				} else {
					drawable = resources.getDrawable(R.drawable.sel_btn_blue);
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					btn_start.setBackground(drawable);
				} else {
					btn_start.setBackgroundDrawable(drawable);
				}

			}
		});
	}
	//标题栏的返回按钮监听，点击此按钮时，finish掉本Activity
	@OnClick(R.id.rb_back)
	public void finishAct(View view) {
		finish();
	}

	//获取验证码Button监听
	@OnClick(R.id.btn_code)
	public void getCode(View view) {
		//如果在规定时间内被重复点击，返回出该方法，不执行任何操作（防止重复点击）
		if (StrUtil.isFastDoubleClick()) {
			return;
		}
		/**
		 * 将输入的电话号码提取
		 * toString().trim()是去除字符串左右的空格
		 */
		String mobile = ed_phone.getText().toString().trim();
		//判断是否有输入电话号码，true代表没有
		if (StrUtil.isNull(mobile)) {
			//提示“请输入手机号码”
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.input_mobile));
			//返回（跳出该监听回调）
			return;
		}
		//点击“获取验证码”按钮后，屏幕出现显示进度的Dialog
		CustomProgress.show(mActivity, "", true, null);
		//执行获取验证码方法
		getCode();
		//“获取验证码”点击之后，进行时间倒计时
		if (null != timer) {
			timer.start();
			//设置“获取验证码”Button点击无效
			btn_code.setEnabled(false);
		}
	}
	//“协议条款监听”,界面跳转到“用户协议”界面
	@OnClick(R.id.btn_agr)
	public void language(View view) {
		startActivity(new Intent(mActivity, AgreementActivity.class));
	}
	//“开始验证”Button
	@OnClick(R.id.btn_start)
	public void start(View view) {
		//防止重复点击
		if (StrUtil.isFastDoubleClick()) {
			return;
		}
		//提取输入的手机号码
		String mobile = ed_phone.getText().toString().trim();
		//提取输入的验证码
		String codes = ed_code.getText().toString().trim();
		//未输入手机号码判断
		if (StrUtil.isNull(mobile)) {
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.input_mobile));
			return;
		}
		//手机号码格式判断
		if (!StrUtil.isMobileNum(mobile)) {
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.mobile_err));
			return;

		}
		//未输入验证码判断
		if (StrUtil.isNull(codes)) {
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.input_code));
			return;
		}
		//验证码判断（改为服务器验证，已作废）
		/*if (!code.equals(codes)) {
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.code_err));
			return;
		}*/
		//屏幕正中显示“正在验证...”Dialog
		CustomProgress.show(mActivity, "正在验证…", true, null);
		//执行登录操作，传入输入的验证码和电话号码
		Login(codes,mobile);
	}

	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			Resources resources = getResources();
			Drawable d = resources
					.getDrawable(R.drawable.draw_radius_guide_btn_gray);
			btn_code.setBackgroundDrawable(d);
			btn_code.setText((millisUntilFinished / 1000) + "秒后重新获取");
		}

		@Override
		public void onFinish() {
			btn_code.setEnabled(true);
			Resources resources = getResources();
			Drawable d = resources.getDrawable(R.drawable.sel_btn_blue);
			btn_code.setBackgroundDrawable(d);
			btn_code.setText("获取验证码");
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		if (null != timer) {
			timer.cancel();
		}
		if (null != btn_code) {
			btn_code.setText("获取验证码");
			Resources resources = getResources();
			Drawable d = resources.getDrawable(R.drawable.sel_btn_blue);
			btn_code.setBackgroundDrawable(d);
			btn_code.setEnabled(true);
		}
		super.onDestroy();
	}
	//“获取验证码”请求头对象
	public List<NameValuePair> paramsCode() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//获取输入的电话号码
		String mobile = ed_phone.getText().toString();
		//获取验证码的请求头
		params.add(new BasicNameValuePair("telph", mobile)); // 顾客ID
		return params;
	}

	//获取验证码
	public void getCode() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ParseHttpData.getCode(mActivity,
						//参数为URL和请求头
						CMD.GETCODE, paramsCode());
				CustomProgress.dissmiss();
				//用于本地验证的逻辑（已作废）
				/*mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {

						if (codetBean == null) {
							CommonMethod.makeNoticeShort(mActivity,
									getString(R.string.data_err));
							return;
						}
						//获取到验证码后进行存储赋值给变量，用于本地验证（后续会改为服务器验证）
						*//*if (codetBean.getState() == 1) {

							CommonMethod.makeNoticeShort(mActivity, codetBean.getResp().getCode() + "<--");
							code = codetBean.getResp().getCode();
							String sessid = codetBean.getResp().getSessid();
							SPUtils.put(mActivity, "sessid", sessid,
									Constance.HUDUDUUSER);
						} else {
							CommonMethod.makeNoticeShort(mActivity,
									"验证码获取失败,请重试");
						}*//*
					}

				});*/
			}
		}).start();
	}
	//“登录”操作请求头对象（已作废）
	/*public List<NameValuePair> paramsLogin() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String mobile = ed_phone.getText().toString();
		params.add(new BasicNameValuePair("telph", mobile)); // 顾客ID
		params.add(new BasicNameValuePair("vcode", code + ""));
		params.add(new BasicNameValuePair("mos", "A"));
		params.add(new BasicNameValuePair("mid", ""));
		return params;
	}*/
	//登录函数
	public void Login(String codes,String mobile) {

		//利用Xutils进行POST请求
		HttpUtils httpUtils=new HttpUtils();
		RequestParams params=new RequestParams();
		/**
		 * 向服务器提交数据
		 * 注意：提交数据的key必须和服务器接收的key一致，否则会导致请求不到相应数据
		 * params.addBodyParameter()是提交数据的方法
		 * 提交电话号码（telph）
		 * 提交mos（未知作用）
		 * 提交mid(手机唯一标识)
		 * 提交验证码（codes）
		 */
		params.addBodyParameter("telph",mobile);
		params.addBodyParameter("mos","A");
		params.addBodyParameter("mid","");
		params.addBodyParameter("codes",codes);
		httpUtils.send(HttpRequest.HttpMethod.POST, CMD.register, params, new RequestCallBack<String>() {

			//请求成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
					String result=responseInfo.result;
				Log.d("TAG","result:"+result);
				LoginBean bean=ParseHttpData.registers(result);
				if (bean.getState()==1){
					CustomProgress.dissmiss();
					CommonMethod.makeNoticeShort(getApplicationContext(),
							getString(R.string.code_bingo));
					String uid = bean.getResp().getUid();
					String name = bean.getResp().getName();
					String pic = bean.getResp().getPic();
					SPUtils.put(mActivity, "uid", uid,Constance.HUDUDUUSER);
					SPUtils.put(mActivity, "name", name,Constance.HUDUDUUSER);
					SPUtils.put(mActivity, "pic", pic,Constance.HUDUDUUSER);
					SPUtils.put(mActivity, "telph", ed_phone.getText().toString().trim(), Constance.HUDUDUUSER);
					SPUtils.put(mActivity,"hide_telph",Utils.mobileReplace(ed_phone.getText().toString().trim()),Constance.HUDUDUUSER);
					setResult(Constance.RESULTCODE_LOGIN, getIntent());
					finish();
				}else {
					CommonMethod.makeNoticeShort(mActivity,
							 getString(R.string.code_err));
				}
			}
			//请求失败
			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
		//登录方法（已作废）
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				//登录数据对象
				final LoginBean bean = ParseHttpData.register(mActivity,
						CMD.register, paramsLogin());
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//取消对话框
						CustomProgress.dissmiss();
						//判断数据对象是否为null
						if (bean == null) {
							CommonMethod.makeNoticeShort(mActivity,
									getString(R.string.data_err));
							return;
						}
						//判断请求状态是否正确
						if (bean.getState() == 1) {


							String uid = bean.getResp().getUid();
							String name = bean.getResp().getName();
							String pic = bean.getResp().getPic();
							SPUtils.put(mActivity, "uid", uid,Constance.HUDUDUUSER);
							SPUtils.put(mActivity, "name", name,Constance.HUDUDUUSER);
							SPUtils.put(mActivity, "pic", pic,Constance.HUDUDUUSER);
							SPUtils.put(mActivity, "telph", ed_phone.getText().toString().trim(), Constance.HUDUDUUSER);
							SPUtils.put(mActivity,"hide_telph",Utils.mobileReplace(ed_phone.getText().toString().trim()),Constance.HUDUDUUSER);
							setResult(Constance.RESULTCODE_LOGIN, getIntent());*//*
							finish();
						} else {

							// CommonMethod.makeNoticeShort(mActivity,
							// "验证码获取失败,请重试");
							UtilsCode.getCodeInfo(bean.getCode());
						}
					}

				});
			}
		}).start();*/
	}
}
