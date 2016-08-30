package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.hcs.hududu.uclient.utils.Toasts;
import com.hcs.hududu.uclient.utils.Utils;
import com.jizhi.hududu.uclient.alipay.AliPay;
import com.jizhi.hududu.uclient.bean.GetCodeBean;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.jizhi.hududu.uclient.json.ParseHttpData;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;
import com.neusoft.huduoduoapp.wxapi.WXPay;

/**
 * 付款方式页面
 * 
 * @author Xuj
 * @date 2015年8月14日 12:41:22
 */
@SuppressLint("NewApi")
public class PayForActivity extends FragmentActivity implements OnClickListener {
	@ViewInject(R.id.hour)
	private TextView hour; // 服务时长
	@ViewInject(R.id.current_price)
	private TextView current_price; // 现价
	@ViewInject(R.id.original_price)
	private TextView original_price;// 原价
	@ViewInject(R.id.weixin_checked)
	private CheckBox weixin_check;
	@ViewInject(R.id.zhifubao_checked)
	private CheckBox zhifubao_check;
	@ViewInject(R.id.service_project)
	private TextView service_project; // 服务项目
	@ViewInject(R.id.hour_price)
	private TextView hour_price; // 每小时 服务金额
	@ViewInject(R.id.tv_actType)
	private TextView tv_actType;
	@ViewInject(R.id.real_price_value)
	private TextView real_price_value;
	private int chekd_state = 0; // 默认选择支付宝支付
	@ViewInject(R.id.sure_pay)
	private Button sure_pay; // 确认支付按钮
	// @ViewInject(R.id.rb_back)
	// private RadioButton rb_back;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	private VegetableOrderhFkInfo fkInfo;
	private double price;
	private String p1, oid;
	private PayForActivity mActivity;
	private String wtype;
	public static PayForActivity payActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.pay_for);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu_client);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		payActivity = PayForActivity.this;
		tv_title.setText(getString(R.string.payorder));
		initView();
		Intent intent = getIntent();
		fkInfo = (VegetableOrderhFkInfo) intent.getSerializableExtra("fkInfo");
		oid = intent.getStringExtra("oid");
		wtype = intent.getStringExtra("wtype");
		if (null == oid || intent.equals("")) {
			CommonMethod.makeNoticeShort(mActivity, "订单编号错误");
			return;
		}
		if (null != fkInfo) {
			// 工作描述：专业保洁：1 简单打扫 ：2 饭后洗碗：3
			int jobDesc = fkInfo.getJobdesc();
			// 家政
			if (fkInfo.getWtype().equals(CMD.HW)) {
				if (jobDesc == 1) {
					p1 = (String) SPUtils.get(mActivity, "dailycleaning", "29",
							Constance.PRICEINFO);
					service_project.setText(getString(R.string.homemake_hours));
					hour.setText(fkInfo.getWorkinghours() + "小时");
					hour_price.setText("(" + p1 + "元/小时)");
				} else if (jobDesc == 2) {
					p1 = (String) SPUtils.get(mActivity, "simpcleaning", "19",
							Constance.PRICEINFO);
					service_project.setText(getString(R.string.homemake_hour));
					hour.setText(fkInfo.getWorkinghours() + "小时");
					hour_price.setText("(" + p1 + "元/小时)");
				} else if (jobDesc == 3) {
					p1 = (String) SPUtils.get(mActivity, "washdishes", "15",
							Constance.PRICEINFO);
					service_project.setText(getString(R.string.homemake_min));
					int p = Integer.parseInt(p1) / 2;
					hour.setText(fkInfo.getWorkinghours() + "小时");
					hour_price.setText("(" + p + "元/半小时)");
				}
				// 手洗衣服
			} else if (fkInfo.getWtype().equals(CMD.WH)) {
				p1 = (String) SPUtils.get(mActivity, "washclothes", "19",
						Constance.PRICEINFO);
				service_project.setText(getString(R.string.main_chloth));
				hour.setText(fkInfo.getWorkinghours() + "小时");
				hour_price.setText("(" + p1 + "元/小时)");
				// // 帮我买菜
			} else if (fkInfo.getWtype().equals(CMD.FH)) {
				hour_price.setVisibility(View.GONE);
				service_project.setText(getString(R.string.main_grenns));
				String frash_Desc = fkInfo.getFresh_Desc();
				if (null != frash_Desc) {
					String[] s = frash_Desc.split(";");
					hour.setText("生鲜数量：" + s.length + "种");
				}else{
					hour.setVisibility(View.GONE);
				}

			} else if (fkInfo.getWtype().equals(CMD.HD)) {
				hour_price.setVisibility(View.GONE);
				service_project.setText(getString(R.string.main_food));
				hour.setText("菜品数量：" + fkInfo.getDishcount() + "种");
			}
			double wage = fkInfo.getWage();
			double discount = fkInfo.getDiscount();
			price = wage - discount;
			if ((int) discount == 0) {
				current_price.setVisibility(View.GONE);
				original_price.setText("￥" + Utils.m2(price));
			} else {
				original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				original_price.setVisibility(View.VISIBLE);
				original_price.setText("￥" + Utils.m2(wage));

				current_price.setVisibility(View.VISIBLE);
				current_price.setText("￥" + Utils.m2(price));
			}
			if (null != fkInfo && fkInfo.getActivityType().equals("")) {
				tv_actType.setText(fkInfo.getActivityType() + "");
			}
			real_price_value.setText(("￥" + Utils.m2(price)));
		}

	}

	@OnClick(R.id.rb_back)
	public void finishAct(View view) {
		finish();
	}

	public void initView() {
		sure_pay.setOnClickListener(this);
		mActivity = PayForActivity.this;
		zhifubao_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {
						if (isChecked) {
							if (weixin_check.isChecked()) {
								weixin_check.setChecked(false);
							}
							chekd_state = 0;
						} else {
							if (!weixin_check.isChecked()) {
								zhifubao_check.setChecked(true);
								chekd_state = 0;
							}
						}
					}
				});
		weixin_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				if (isChecked) {
					if (zhifubao_check.isChecked()) {
						zhifubao_check.setChecked(false);
					}
					chekd_state = 1;
				} else {
					if (!zhifubao_check.isChecked()) {
						weixin_check.setChecked(true);
						chekd_state = 1;
					}
				}
			}
		});

	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public void onClick(View view) {
		// Intent intent;
		switch (view.getId()) {
		case R.id.sure_pay:
			if (chekd_state == 0) {
				AliPay aliPay = new AliPay(mActivity, fkInfo.getWtype());
				aliPay.pay(getString(R.string.main_homemake), service_project
						.getText().toString(), Utils.m2(price), oid, fkInfo
						.getLid());
			} else if (chekd_state == 1) {
				CustomProgress.show(mActivity, "", true, null);
				pay();
			} else {
				CommonMethod.makeNoticeShort(PayForActivity.this,
						"请至少选择一种支付方式!");
			}
			break;
		}

	}

	/**
	 * 设置获取个人信息参数
	 * 
	 * @return
	 */
	public List<NameValuePair> params() {
		// lid oid wtype

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String lid = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);

		params.add(new BasicNameValuePair("option", "addlidtoorder"));
		params.add(new BasicNameValuePair("lid", fkInfo.getLid()));
		params.add(new BasicNameValuePair("oid", oid));
		params.add(new BasicNameValuePair("wtype", fkInfo.getWtype()));
		return params;
	}

	public void pay() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				final GetCodeBean codeBean = ParseHttpData.getCode(mActivity,
						wtype, params());
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						CustomProgress.dissmiss();
						if (null != codeBean && codeBean.getState() == 1) {
							WXPay wxPay = new WXPay(mActivity,
									getString(R.string.main_homemake),
									service_project.getText().toString(),
									price, oid, fkInfo.getWtype());
							wxPay.pay();
						} else {
							Toasts.showShort(mActivity, "支付异常");
						}

					}
				});
			}
		}).start();
	}
}
