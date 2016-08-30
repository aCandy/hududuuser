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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.CashCouponState;
import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.DinnerState;
import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.jizhi.hududu.uclient.dao.DBOpenHelper;
import com.jizhi.hududu.uclient.dao.DinnerDao;
import com.jizhi.hududu.uclient.json.DinnerResolution;
import com.jizhi.hududu.uclient.json.MyCardBagResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.util.DateUtil;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.FoodTypeDialog;
import com.jizhi.hududu.uclient.widget.FoodTypeDialog.CallFoodChoose;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog.ServiceDateInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 上门做饭主页面
 * 
 * @author Xuj
 * @date 2015年8月6日 10:20:25
 */
public class DinnerMainActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.service_address_value)
	private TextView service_address_value; // 服务地址
	@ViewInject(R.id.service_time_value)
	private TextView service_time_value; // 送货上门时间
	@ViewInject(R.id.choose_cook_value)
	private TextView choose_cook_value; // 选择厨师的值
	@ViewInject(R.id.choose_food_value)
	private TextView choose_food_value; // 选择几个菜的值

	@ViewInject(R.id.service_content_relative)
	private TextView service_content_relative; // 业务参考

	@ViewInject(R.id.door_service_date_value)
	private TextView door_service_date_value; // 上门服务时间值
	@ViewInject(R.id.use_cash_value)
	private TextView use_cash_value; //抵扣券

	@ViewInject(R.id.service_price)
	private TextView service_price; // 服务单价
	@ViewInject(R.id.service_project)
	private TextView service_project; // 服务项目
	@ViewInject(R.id.service_detail)
	private TextView service_detail; // 服务描述
	@ViewInject(R.id.cost_value)
	private TextView cost_value; // 花费金额
	@ViewInject(R.id.unit)
	private TextView unit; // 单位 /小时
	

	@ViewInject(R.id.submit)
	private TextView submit; // 提交按钮

	@ViewInject(R.id.tv_title)
	private TextView title;

	private Dialog submitDialog;

	private Map<String, Integer> map = new HashMap<String, Integer>();

	private ArrayList<String> list = new ArrayList<String>();

	private String lng; // 经度

	private String lat; // 纬度

	private List<Dinner> dinner_list;

	private List<NearByPoiInfo> poiInfos;

	private int choose_success = 101;

	private Dinner currentDinner = new Dinner();

	private DinnerResolution resolution = new DinnerResolution();

	private String cid_parameter; //电话好吗
	private String lid_parameter; //厨师id
	private String servertime_parameter; //上门服务时间
	private String address_parameter; //服务地址
	private String location_parameter; 
	private String dishcount_parameter; //菜品数量
	private String uniqueid;//优惠券id
	
	private float showMoney = 39;
	

	private DinnerDao dao;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dinner_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		poiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");
		initData();
		submit.setOnClickListener(this);
		dao = new DinnerDao(this);

	}

	public void finishAct(View view) {
		finish();
	}

	@OnClick(R.id.rea_top)
	public void reaTop(View view) {
		Intent intent = new Intent(DinnerMainActivity.this,
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
		title.setText(R.string.gohomecook);
		Intent intent = getIntent();

		poiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");

		service_address_value.setText(intent.getStringExtra("address"));

		address_parameter = intent.getStringExtra("address");
		location_parameter = intent.getStringExtra("location");

		String[] str = intent.getStringExtra("location").split(",");

		lng = str[1];
		lat = str[0];

		map.put("三菜以内  39", 39);
		map.put("四菜  49", 49);
		map.put("五菜  59", 59);
		map.put("六菜  69", 69);
		map.put("七菜  79", 79);
		map.put("八菜  89", 89);

		list.add("三菜以内  39");
		list.add("四菜  49");
		list.add("五菜  59");
		list.add("六菜  69");
		list.add("七菜  79");
		list.add("八菜  89");

		choose_food_value.setText(list.get(0));
		cost_value.setText("￥" + DataForMat.twoDecimalPlaces(39));
		dishcount_parameter = "3";

		door_service_date_value.setText(DateUtil.longToDate(DateUtil.timeTo()));
		servertime_parameter = DateUtil.ChangeData();
	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.door_service_date_relative: // 选择送货上门时间
			ServiceDateDialog dialog = new ServiceDateDialog(
					DinnerMainActivity.this);
			dialog.show();
			dialog.setListener(new ServiceDateInterface() {
				@Override
				public void onClick(String chooseDate) {
					try {
						servertime_parameter = DateUtil
								.stringToTimeStamp(chooseDate);
					} catch (ParseException e) {
						CommonMethod.makeNoticeShort(DinnerMainActivity.this,
								"日期解析出错!");
						e.printStackTrace();
						return;
					}
					door_service_date_value.setText(chooseDate);
				}
			});
			break;
		case R.id.choose_food_relative: // 选择菜品
			FoodTypeDialog dialog1 = new FoodTypeDialog(
					DinnerMainActivity.this, list, map);
			dialog1.show();
			dialog1.setListener(new CallFoodChoose() {
				@Override
				public void onClick(int money, String choose_food) {
					choose_food_value.setText(choose_food);
					cost_value.setText("￥" + DataForMat.twoDecimalPlaces(money));
					switch (money) {
					case 39:
						dishcount_parameter = "3";
						showMoney = 39;
						break;
					case 49:
						dishcount_parameter = "4";
						showMoney = 49;
						break;
					case 59:
						dishcount_parameter = "5";
						showMoney = 59;
						break;
					case 69:
						dishcount_parameter = "6";
						showMoney = 69;
						break;
					case 79:
						dishcount_parameter = "7";
						showMoney = 79;
						break;
					case 89:
						dishcount_parameter = "8";
						showMoney = 89;
						break;
					default:
						break;
					}

				}
			});
			break;
		case R.id.choose_cook_relative: // 选择厨师
			Intent intent = new Intent(DinnerMainActivity.this,
					ChooseChefListViewActivity.class);
			Bundle bundle = new Bundle();
			if (dinner_list != null && dinner_list.size() > 0) {
				bundle.putSerializable("dinner_list",(Serializable) dinner_list);
			}
			bundle.putString("lat", lat);
			bundle.putString("lng", lng);
			intent.putExtras(bundle);
			startActivityForResult(intent, choose_success);
			break;
		case R.id.submit: // 提交按钮
			if (TextUtils.isEmpty(servertime_parameter)) {
				CommonMethod.makeNoticeShort(DinnerMainActivity.this,
						"请选择上门做饭时间!");
				return;
			}
			if (TextUtils.isEmpty(lid_parameter)) {
				CommonMethod.makeNoticeShort(DinnerMainActivity.this, "请选择厨师!");
				return;
			}
			if (TextUtils.isEmpty(dishcount_parameter)) {
				CommonMethod.makeNoticeShort(DinnerMainActivity.this,
						"请选择做菜个数!");
				return;
			}
			submitDinnerMain();
			break;
		case R.id.service_content_relative:
			Intent intent1 = new Intent(this, LoadUrlActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("type", 7);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", (String) SPUtils.get(this,
				"mobile", "", Constance.HUDUDUUSER))); // 登录人id
		params.add(new BasicNameValuePair("lid", lid_parameter)); // 厨师id
		params.add(new BasicNameValuePair("jobdesc", "1")); // 服务类型
		params.add(new BasicNameValuePair("servertime", servertime_parameter)); // 服务时间
		params.add(new BasicNameValuePair("address", address_parameter)); // 选择地址
		params.add(new BasicNameValuePair("location", location_parameter)); // 经纬度
		params.add(new BasicNameValuePair("dishcount", dishcount_parameter)); // 选菜的数量
		params.add(new BasicNameValuePair("option", "cdistribute"));
		if(!TextUtils.isEmpty(uniqueid)){
			params.add(new BasicNameValuePair("uniqueid", uniqueid));
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
		params.add(new BasicNameValuePair("wtype",CMD.HD)); //类型
		return params;
	}

	/**
	 * 提交上门做饭
	 */
	public void submitDinnerMain() {
		if (submitDialog != null) {
			submitDialog.dismiss();
		}
		submitDialog = CustomProgress.show(DinnerMainActivity.this,
				getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final DinnerState state = resolution.resolution(
						DinnerMainActivity.this, CMD.SUBMIT_DINNER, params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(
									DinnerMainActivity.this, "数据解析出错!");
						}
						if (state.getState() == 1) {
							CommonMethod.makeNoticeShort(
									DinnerMainActivity.this, "提交成功!");
							Intent intent = new Intent();
							intent.putExtra("oid", state.getOid());
							setResult(Constance.RESULTCODE_HOMEMAKE, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
							finish();
						} else {
							CommonMethod.makeNoticeShort(
									DinnerMainActivity.this,
									"提交失败!:" + state.getCode());
						}
						submitDialog.dismiss();
					}
				});
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (submitDialog != null) {
			submitDialog.dismiss();
		}
	}

	@OnClick(R.id.rea_greens)
	public void rea_greens(View view) {
		startActivity(new Intent(DinnerMainActivity.this,
				BuyVegetablesViewPagerActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == choose_success) {
			Bundle bundle = data.getExtras();
			dinner_list = (List<Dinner>) bundle.getSerializable("dinner_list");
			lid_parameter = bundle.getString("lid");
			choose_cook_value.setText(bundle.getString("choose_name"));
			choose_cook_value.setTextColor(getResources().getColor(
					R.color.money));
		} else if (requestCode == Constance.REQUESTCODE_NEAYBYADDE
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
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("关闭数据成功", "close");
		dao.delete();
		DBOpenHelper.getInstance(this).closeDB();
	}
	
	/**
	 * 查询抵用券
	 */
	public void searchCash_coupon() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MyCardBagResolution resolution = new MyCardBagResolution();
				final CashCouponState cash_state = resolution.search_cash_coupons(DinnerMainActivity.this,CMD.SEARCH_CASH_PRICE,cash_params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (cash_state == null) {
							return;
						}
						if (cash_state.getState() == 1 && cash_state.getResp()!=null) {
							uniqueid = cash_state.getResp().getUniqueid();
							use_cash_value.setText("抵用券￥"+DataForMat.twoDecimalPlaces(cash_state.getResp().getCount())); //设置抵用券价格
							cost_value.setText("￥" + DataForMat.twoDecimalPlaces(showMoney-cash_state.getResp().getCount())); //重新计算抵扣后的花费金额
							return;
						}
					}
				});
			}
		}).start();
	}
}
