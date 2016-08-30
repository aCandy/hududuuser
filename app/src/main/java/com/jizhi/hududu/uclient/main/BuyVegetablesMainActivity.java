package com.jizhi.hududu.uclient.main;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.bean.BuyVegetablesState;
import com.jizhi.hududu.uclient.bean.CashCouponState;
import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.jizhi.hududu.uclient.json.BuyVegetablesResolution;
import com.jizhi.hududu.uclient.json.MyCardBagResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.util.DateUtil;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog.ServiceDateInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;
import com.umeng.socialize.utils.Log;

/**
 * 帮我买菜主界面
 * 
 * @author Xuj
 * @date 2015年8月6日 10:20:25
 */
public class BuyVegetablesMainActivity extends Activity implements
		OnClickListener {

	public static final int RETURN_SUCCESS = 100;
	public static final int RETURN_CANCEL = 101;

	private List<BuyVegetables> vegetables_list = new ArrayList<BuyVegetables>(); // 蔬菜数据
	private List<BuyVegetables> fruit_list = new ArrayList<BuyVegetables>(); // 水果数据
	private List<BuyVegetables> fish_list = new ArrayList<BuyVegetables>(); // 水产数据
	private List<BuyVegetables> meet_list = new ArrayList<BuyVegetables>();; // 肉类数据
	private List<BuyVegetables> all_list; // 需要提交的数据

	private Map<String, BuyVegetables> map = new HashMap<String, BuyVegetables>();

	@ViewInject(R.id.door_service_date_value)
	private TextView door_service_date_value; // 上门服务时间值

	@ViewInject(R.id.rea_greens)
	private RelativeLayout rea_greens;
	
	@ViewInject(R.id.service_content_relative)
	private RelativeLayout service_content_relative; // 服务内容及耗时参考

	@ViewInject(R.id.tv_greens)
	private TextView tv_greens; // 选择需要购买的菜

	@ViewInject(R.id.service_address_value)
	private TextView service_address_value; // 服务地址
	
	

	@ViewInject(R.id.submit)
	private TextView submit; // 提交按钮
	@ViewInject(R.id.cost_value)
	private TextView cost_value; // 花费金额
	@ViewInject(R.id.cost_text_money)
	private TextView cost_text_money; // 花费金额
	@ViewInject(R.id.use_cash_value)
	private TextView use_cash_value; //抵扣券
	

	@ViewInject(R.id.littlegoods)
	private EditText littlegoods;// 顺手带

	@ViewInject(R.id.tv_title)
	private TextView title; //标题

	@ViewInject(R.id.service_money)
	private TextView serviceMoney_text; // 服务费文字

	private Dialog dialog;

	private String allNumber = "0";

	private String allPrice = "0.00";

	private int serviceMoney = 0;

	private BuyVegetablesResolution resolution = new BuyVegetablesResolution();

	private List<NearByPoiInfo> poiInfos;

	StringBuffer buffer;

	private String location;

	private String parameter_door_open; // 上门服务时间
	private String parameter_price; // 预计花费

	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.buy_vegetables_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		poiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");
		initData();
		searchCash_coupon();
	}

	public void finishAct(View view) {
		finish();
	}

	@OnClick(R.id.rea_top)
	public void reaTop(View view) {
		Intent intent = new Intent(BuyVegetablesMainActivity.this,
				NearbyAddrActivity.class);
		if (null == poiInfos) {
			poiInfos = new ArrayList<NearByPoiInfo>();
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("poiInfo", (Serializable) poiInfos);
		intent.putExtras(bundle);
		this.startActivityForResult(intent, Constance.REQUESTCODE_NEAYBYADDE);
	}

	@SuppressWarnings("unchecked")
	public void initData() {
		title.setText(R.string.main_grenns);
		Intent intent = getIntent();
		service_address_value.setText(intent.getStringExtra("address")); // 设置服务地址
		location = intent.getStringExtra("location");
		serviceMoney = Integer.parseInt((String) SPUtils.get(
				getApplicationContext(), "freshhelper", "6",
				Constance.PRICEINFO));
		poiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");
		submit.setOnClickListener(this);
		serviceMoney_text.setText("(该服务需支付" + serviceMoney + "元服务费)");
		cost_text_money.setText("(包含服务费" + serviceMoney + "元)");
		cost_value.setText("￥" + DataForMat.twoDecimalPlaces(serviceMoney));
		allPrice = DataForMat.twoDecimalPlaces(serviceMoney);
		littlegoods.setHint(Html.fromHtml("<font color='#444444'>顺手带:如需代买其他小件物品、请在此填写详细信息,该服务需</font>"
								+ "<font color='#ff5418' >线下支付费用给服客</font>"));

	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.door_service_date_relative:
			ServiceDateDialog dialog = new ServiceDateDialog(
					BuyVegetablesMainActivity.this);
			dialog.show();
			dialog.setListener(new ServiceDateInterface() {
				@Override
				public void onClick(String chooseDate) {
					try {
						parameter_door_open = DateUtil
								.stringToTimeStamp(chooseDate);
					} catch (ParseException e) {
						CommonMethod.makeNoticeShort(
								BuyVegetablesMainActivity.this, "日期解析出错!");
						e.printStackTrace();
						return;
					}
					door_service_date_value.setText(chooseDate);
				}
			});
			break;
		case R.id.submit:
			if (TextUtils.isEmpty(parameter_door_open)) {
				CommonMethod.makeNoticeShort(BuyVegetablesMainActivity.this,
						"请选择上门服务时间!");
				return;
			}
			if (all_list != null) {
				buffer = new StringBuffer();
				for (int i = 0; i < all_list.size(); i++) {
					buffer.append(all_list.get(i).getFhnum() + ",");
					buffer.append(all_list.get(i).getName() + ",");
					buffer.append(all_list.get(i).getAllNumber() + ",");
					buffer.append(all_list.get(i).getFee() + ",");
					buffer.append(all_list.get(i).getPic() + ";");
				}
			} else {
				CommonMethod.makeNoticeShort(BuyVegetablesMainActivity.this,
						"请选择需要购买的菜品!");
				return;
			}
			submitBuyVegetables();
			break;
		case R.id.service_content_relative:
			Intent intent = new Intent(this, LoadUrlActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", 6);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 提交订单所需参数
	 * @return
	 */
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", (String) SPUtils.get(this,
				"mobile", "", Constance.HUDUDUUSER))); // 顾客ID
		params.add(new BasicNameValuePair("servertime", parameter_door_open)); // 上门服务的时间
		params.add(new BasicNameValuePair("address", service_address_value
				.getText().toString())); // 服务地址
		params.add(new BasicNameValuePair("location", location)); // 服务地点坐标
		params.add(new BasicNameValuePair("freshdesc", buffer.toString()));
		params.add(new BasicNameValuePair("totalprice", parameter_price));
		params.add(new BasicNameValuePair("option", "cdistribute"));
		String value = littlegoods.getText().toString();
		if (!TextUtils.isEmpty(value)) {
			params.add(new BasicNameValuePair("littlegoods", value));
		}
		return params;
	}
	
	/**
	 * 查询抵扣券所需参数
	 * @return
	 */
	public List<NameValuePair> cash_params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", (String) SPUtils.get(this,"mobile", "", Constance.HUDUDUUSER))); // 顾客电话号码
		params.add(new BasicNameValuePair("wtype",CMD.FH)); //类型
		return params;
	}

	/**
	 * 提交帮我买菜订单
	 */
	public void submitBuyVegetables() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(BuyVegetablesMainActivity.this,
				getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final BuyVegetablesState state = resolution.submit(
						BuyVegetablesMainActivity.this,
						CMD.SUBMIT_VEGETABLES_CLEANING, params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(
									BuyVegetablesMainActivity.this, "提交失败!:"
											+ state.getCode());
							return;
						}
						if (state.getState() == 1) {
							CommonMethod.makeNoticeShort(
									BuyVegetablesMainActivity.this, "提交成功!");
							Intent intent = new Intent();
							intent.putExtra("oid", state.getOid());
							setResult(Constance.RESULTCODE_HOMEMAKE, intent); //
							finish();
						} else {
							CommonMethod.makeNoticeShort(
									BuyVegetablesMainActivity.this, "提交失败!:"
											+ state.getCode());
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constance.REQUESTCODE_NEAYBYADDE
				&& resultCode == Constance.RESULTCODE_NEAYBYADDE) {
			NearByPoiInfo info = (NearByPoiInfo) data
					.getSerializableExtra("info");
			String str = service_address_value.getText().toString().trim();
			String userAddr = info.getAddress() + "," + info.getName();
			String[] s = str.split(",");
			if (s.length == 2) {
				service_address_value.setText(userAddr);
			} else if (s.length == 3) {
				service_address_value.setText(userAddr + "," + s[2]);
			}
			service_address_value.setText(userAddr);
		} else if (resultCode == RETURN_SUCCESS) {
			Bundle bundle = data.getExtras();
			all_list = (List<BuyVegetables>) bundle.getSerializable("all_list");
			vegetables_list = (List<BuyVegetables>) bundle
					.getSerializable("vegetables_list");
			fruit_list = (List<BuyVegetables>) bundle
					.getSerializable("fruit_list");
			fish_list = (List<BuyVegetables>) bundle
					.getSerializable("fish_list");
			meet_list = (List<BuyVegetables>) bundle
					.getSerializable("meet_list");
			map = (Map<String, BuyVegetables>) bundle.getSerializable("map");
			allPrice = bundle.getString("allPrice");
			allNumber = bundle.getString("allNumber");
			cost_value.setText("￥" + (Double.parseDouble(allPrice)));
			parameter_price = Double.parseDouble(allPrice) + "";
			tv_greens.setText("已选");
			tv_greens.setTextColor(getResources().getColor(R.color.money));
		} else if (resultCode == Constance.RESULTCODE_HOMEMAKE) {
			Intent intent = new Intent();
			intent.putExtra("oid", data.getStringExtra("oid"));
			setResult(Constance.RESULTCODE_HOMEMAKE, intent);
			finish();
		} 
	}

	@OnClick(R.id.rea_greens)
	public void rea_greens(View view) {
		Intent intent = new Intent(BuyVegetablesMainActivity.this,
				BuyVegetablesViewPagerActivity.class);
		if (vegetables_list != null && fruit_list != null && fish_list != null
				&& meet_list != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("vegetables_list",
					(Serializable) vegetables_list);
			bundle.putSerializable("fruit_list", (Serializable) fruit_list);
			bundle.putSerializable("fish_list", (Serializable) fish_list);
			bundle.putSerializable("meet_list", (Serializable) meet_list);
			bundle.putSerializable("map", (Serializable) map);
			bundle.putString("allPrice", allPrice);
			bundle.putString("allNumber", allNumber);
			bundle.putString("time", parameter_door_open);
			bundle.putString("address", service_address_value.getText()
					.toString());
			bundle.putString("location", location);
			bundle.putString("serviceMoney", "(包含" + serviceMoney + "元服务费)");
			String value = littlegoods.getText().toString();
			if (!TextUtils.isEmpty(value)) {
				bundle.putString("littlegoods", value);
			}
			intent.putExtras(bundle);
			startActivityForResult(intent, 101);
		}
	}
	
	
	
	/**
	 * 查询抵用券
	 */
	public void searchCash_coupon() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyCardBagResolution resolution = new MyCardBagResolution();
				final CashCouponState cash_state = resolution.search_cash_coupons(BuyVegetablesMainActivity.this,CMD.SEARCH_CASH_PRICE,cash_params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (cash_state == null) {
							return;
						}
						if (cash_state.getState() == 1 && cash_state.getResp()!=null) {
							use_cash_value.setText("抵用券￥"+DataForMat.twoDecimalPlaces(cash_state.getResp().getCount()));
							return;
						}
					}
				});
			}
		}).start();
	}

}
