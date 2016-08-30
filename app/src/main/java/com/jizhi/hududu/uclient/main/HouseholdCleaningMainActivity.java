package com.jizhi.hududu.uclient.main;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.CashCouponState;
import com.jizhi.hududu.uclient.bean.HouseHoldCleaningState;
import com.jizhi.hududu.uclient.bean.HouseholdCleaningBean;
import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.jizhi.hududu.uclient.json.HouseHoldCleningResolution;
import com.jizhi.hududu.uclient.json.MyCardBagResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.AnimUtil;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.util.DateUtil;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog;
import com.jizhi.hududu.uclient.widget.ServiceDateDialog.ServiceDateInterface;
import com.jizhi.hududu.uclient.widget.ServiceTimeDialog;
import com.jizhi.hududu.uclient.widget.ServiceTimeDialog.ServiceTimeInteface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 专业保洁 主界面
 * @author Xuj
 * @date 2015年8月6日 10:20:25
 */
public class HouseholdCleaningMainActivity extends Activity implements
		OnClickListener {

	@ViewInject(R.id.door_service_date_relative)
	private RelativeLayout door_service_date_relative; // 上门服务RelativeLayout
	@ViewInject(R.id.service_time_relative)
	private RelativeLayout service_time_relative; // 服务时长 RelativeLayout
	@ViewInject(R.id.service_content_relative)
	private RelativeLayout service_content_relative; // 服务内容及耗时参考
	@ViewInject(R.id.cost_relative)
	private RelativeLayout cost_relative; // 花费金额

	@ViewInject(R.id.service_address_value)
	private TextView service_address_value; // 服务地址
	@ViewInject(R.id.service_time_value)
	private TextView service_time_value; // 服务时长值
	@ViewInject(R.id.door_service_date_value)
	private TextView door_service_date_value; // 上门服务时间值
	@ViewInject(R.id.use_cash_value)
	private TextView use_cash_value; //抵扣券
	@ViewInject(R.id.submit)
	private TextView submit; // 提交按钮
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

	@ViewInject(R.id.tv_title)
	private TextView title;

	private Dialog dialog;

	private HouseHoldCleningResolution resolution = new HouseHoldCleningResolution(); // 家庭保洁解析

	private HouseholdCleaningBean currentHouse = new HouseholdCleaningBean();
	private List<NearByPoiInfo> poiInfos;
	
	
	private String uniqueid; //优惠券
	
	private float count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.household_cleaning_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		initData();

	}

	public void finishAct(View view) {
		finish();
	}

	@OnClick(R.id.rea_top)
	public void reaTop(View view) {
		Intent intent = new Intent(HouseholdCleaningMainActivity.this,
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
		title.setText(R.string.main_homemake);
		Intent intent = getIntent();
		currentHouse.setJob_desc(intent.getIntExtra("jobdesc", 0));
		currentHouse.setAddress(intent.getStringExtra("address"));
		currentHouse.setLocation(intent.getStringExtra("location"));
		currentHouse.setPrice(Float.parseFloat(intent.getStringExtra("price")));
		poiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");
		// 1：专业保洁 2：简单打扫 3：饭后洗碗
		switch (currentHouse.getJob_desc()) {
		case 1:
			service_project.setText(getString(R.string.cleaning));
			service_detail.setText(getString(R.string.cleaning_desc));
			break;
		case 2:
			service_project.setText(getString(R.string.simple_cleaning));
			service_detail.setText(getString(R.string.simple_desc));
			break;
		case 3:
			service_project.setText(getString(R.string.dishes));
			service_detail.setText(getString(R.string.dishes_desc));
			break;
		default:
			break;
		}
		service_address_value.setText(currentHouse.getAddress());
		service_price.setText("￥"+DataForMat.twoDecimalPlaces(currentHouse.getPrice()));
		submit.setOnClickListener(this);
		
		currentHouse.setWorking_hours("2.0");
		currentHouse.setFinalPrice(currentHouse.getPrice() * 2);
		cost_value.setText("￥" + DataForMat.twoDecimalPlaces(currentHouse.getFinalPrice()));
		service_time_value.setText("2.0小时");
		
		door_service_date_value.setText(DateUtil.longToDate(DateUtil.timeTo()));
		currentHouse.setServer_time(DateUtil.ChangeData());
		searchCash_coupon();
		
	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.door_service_date_relative:
			ServiceDateDialog dialog = new ServiceDateDialog(
					HouseholdCleaningMainActivity.this);
			dialog.show();
			dialog.setListener(new ServiceDateInterface() {
				@Override
				public void onClick(String chooseDate) {
					try {
						currentHouse.setServer_time(DateUtil.stringToTimeStamp(chooseDate));
					} catch (ParseException e) {
						CommonMethod.makeNoticeShort(HouseholdCleaningMainActivity.this, "日期解析出错!");
						e.printStackTrace();
						return;
					}
					door_service_date_value.setText(chooseDate);
				}
			});
			break;
		case R.id.service_time_relative:
			ServiceTimeDialog dialog1 = new ServiceTimeDialog(
					HouseholdCleaningMainActivity.this);
			dialog1.show();
			dialog1.setListener(new ServiceTimeInteface() {
				@Override
				public void onClick(String service_time) {
					if (null != service_time && !"".equals(service_time)) {
						currentHouse.setWorking_hours(service_time);
						float hour = Float.parseFloat(service_time);
						currentHouse.setFinalPrice(currentHouse.getPrice()* hour);
						if(count!=0){
							currentHouse.setFinalPrice(currentHouse.getFinalPrice()-count);
						}
						cost_value.setText("￥" + currentHouse.getFinalPrice());
						service_time_value.setText(service_time + "小时");
					}
				}
			});
			break;
		case R.id.service_content_relative:
			Intent intent = new Intent(this, LoadUrlActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type",1);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.submit:
			String time = currentHouse.getServer_time();
			if (time == null) {
				CommonMethod.makeNoticeShort(HouseholdCleaningMainActivity.this,
						"请选择上门服务时间!");
				return;
			}
			String hours = currentHouse.getWorking_hours();
			if (hours == null) {
				CommonMethod.makeNoticeShort(HouseholdCleaningMainActivity.this,
						"请选择服务时长!");
				return;
			}
			submitHouseholdClean();
			break;
		default:
			break;
		}
	}

	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid",(String)SPUtils.get(this,"mobile","",Constance.HUDUDUUSER))); // 顾客ID
		params.add(new BasicNameValuePair("jobdesc", currentHouse.getJob_desc() + ""));
		params.add(new BasicNameValuePair("servertime", currentHouse.getServer_time())); // 上门服务的时间
		params.add(new BasicNameValuePair("address", currentHouse.getAddress())); // 服务地址
		params.add(new BasicNameValuePair("location", currentHouse.getLocation())); // 服务地点坐标  "纬度，经度"
		params.add(new BasicNameValuePair("workinghours", currentHouse.getWorking_hours())); // 工作时长 以小时为一个单位
		params.add(new BasicNameValuePair(CMD.NEED_PARAMETER_TEXT,CMD.NEED_PARAMETER_VALUE));
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
		params.add(new BasicNameValuePair("wtype",CMD.HW)); //类型
		return params;
	}

	/**
	 * 提交家庭保洁订单
	 */
	public void submitHouseholdClean() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(HouseholdCleaningMainActivity.this,
				getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final HouseHoldCleaningState state = resolution.resolution(
						HouseholdCleaningMainActivity.this,
						CMD.SUBMIT_HOUSEHOLD_CLEANING, params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(
									HouseholdCleaningMainActivity.this, "数据解析出错!");
						}
						if (state.getState() == 1) {
							CommonMethod.makeNoticeShort(HouseholdCleaningMainActivity.this,"提交成功!");
							Intent intent = new Intent();
							intent.putExtra("oid", state.getOid());
							setResult(Constance.RESULTCODE_HOMEMAKE, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
							finish();
						} else {
							CommonMethod.makeNoticeShort(
									HouseholdCleaningMainActivity.this, "提交失败!:"
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
			currentHouse.setAddress(userAddr);
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
				final CashCouponState cash_state = resolution.search_cash_coupons(HouseholdCleaningMainActivity.this,CMD.SEARCH_CASH_PRICE,cash_params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (cash_state == null) {
							return;
						}
						if (cash_state.getState() == 1 && cash_state.getResp()!=null) {
							count = Float.parseFloat(DataForMat.twoDecimalPlaces(cash_state.getResp().getCount()));
							uniqueid = cash_state.getResp().getUniqueid();
							use_cash_value.setText("抵用券￥"+DataForMat.twoDecimalPlaces(cash_state.getResp().getCount())); //设置抵用券价格
							cost_value.setText("￥" + DataForMat.twoDecimalPlaces(currentHouse.getFinalPrice()-cash_state.getResp().getCount())); //重新计算抵扣后的花费金额
							return;
						}
					}
				});
			}
		}).start();
	}

}
