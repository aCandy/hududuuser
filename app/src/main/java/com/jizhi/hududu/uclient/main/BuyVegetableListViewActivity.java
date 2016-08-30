package com.jizhi.hududu.uclient.main;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.adapter.BuyVegetablesListViewAdapter;
import com.jizhi.hududu.uclient.adapter.BuyVegetablesListViewAdapter.CallBackPrice;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.bean.BuyVegetablesState;
import com.jizhi.hududu.uclient.json.BuyVegetablesResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;

/**
 * 菜篮子ListView 列表
 * @author Xuj
 * @date 2015年8月21日 15:54:01
 */
@SuppressLint("NewApi")
public class BuyVegetableListViewActivity extends FragmentActivity implements OnClickListener{

	private Map<String,BuyVegetables> map;
	
	private BuyVegetablesListViewAdapter adapter;
	
	@ViewInject(R.id.clickBottom)
	private Button clickBottom;
	
	@ViewInject(R.id.allPrice)
	private TextView allPrice;
	
	@ViewInject(R.id.allNumber)
	private TextView allNumber;
	
	@ViewInject(R.id.include_money)
	private TextView include_money;
	
	
	@ViewInject(R.id.listView)
	private ListView listView;
	
	private List<BuyVegetables> list;
	
	private boolean isChangeData = false;
	
	private Dialog dialog;
	
	private String parameter_door_open;
	private String location; //经纬度
	private String address;  //地址
	private String littlegoods; //顺手带
	
	
	private StringBuffer buffer;
	private BuyVegetablesResolution resolution = new BuyVegetablesResolution();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.buy_vegetable_listview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.buy_vegetables_detail);
		ViewUtils.inject(this); //Xutil必须调用的一句话
		initData();
		initView();
		
	}

	public void finishAct(View view) {
		if(isChangeData){
			Intent intent = new Intent();
			intent.putExtra("allNumber", allNumber.getText().toString());
			intent.putExtra("allPrice", allPrice.getText().toString());
			Bundle bundle = new Bundle();
			bundle.putSerializable("map",(Serializable)map);
			intent.putExtras(bundle);
			setResult(1,intent);
		}
		finish();
	}
	
	
	public void initData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		map = (Map<String,BuyVegetables>)bundle.getSerializable("map");
		allPrice.setText(bundle.getString("allPrice"));
		include_money.setText(bundle.getString("serviceMoney"));
		allNumber.setText(bundle.getString("allNumber"));
		parameter_door_open = bundle.getString("time");
		location = bundle.getString("location");
		address = bundle.getString("address");
		littlegoods = bundle.getString("littlegoods");
	}
	
	public void initView(){
		if(map!=null && map.size()>0){
			list = new ArrayList<BuyVegetables>();
			Set<String> keys = map.keySet();
			for(String key:keys){
				list.add(map.get(key));
			}
			adapter = new BuyVegetablesListViewAdapter(this,map,list,call);
			listView.setAdapter(adapter);
		}else{
			CommonMethod.makeNoticeShort(BuyVegetableListViewActivity.this,"请选择商品!");
			finish();
		}
		clickBottom.setOnClickListener(this);
	}
	
	
	public CallBackPrice call = new CallBackPrice() {
		
		@Override
		public void callPrice(BuyVegetables bean,String type) {
			int number = Integer.parseInt(allNumber.getText().toString());
			if(type.equals("remove")){
				number = number-1;
				if(number==0){
					setResult(BuyVegetablesMainActivity.RETURN_CANCEL);
					finish();
				}else{
					allNumber.setText(number+"");
				}
				Double d = Double.parseDouble(allPrice.getText().toString());
				allPrice.setText(DataForMat.twoDecimalPlaces(d-bean.getFee()));
			}else if(type.equals("add")){
			    number = number+1;
				Double d = Double.parseDouble(allPrice.getText().toString());
				allPrice.setText(DataForMat.twoDecimalPlaces(d+bean.getFee()));
				allNumber.setText(number+"");
			}
			isChangeData = true;
		}
	};


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.clickBottom:
			if(parameter_door_open!=null && !parameter_door_open.equals("")){
				buffer = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					buffer.append(list.get(i).getFhnum() + ",");
					buffer.append(list.get(i).getName() + ",");
					buffer.append(list.get(i).getAllNumber() + ",");
					buffer.append(list.get(i).getFee() + ",");
					buffer.append(list.get(i).getPic() + ";");
				}
				submitBuyVegetables();
			}else{
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("allNumber", allNumber.getText().toString());
				bundle.putString("allPrice",allPrice.getText().toString());
				bundle.putSerializable("all_list",(Serializable)list);
				bundle.putSerializable("map",(Serializable)map);
				intent.putExtras(bundle);
				setResult(BuyVegetablesMainActivity.RETURN_SUCCESS,intent);
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	
	
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid",(String)SPUtils.get(this,"mobile","",Constance.HUDUDUUSER))); // 顾客ID
		params.add(new BasicNameValuePair("servertime",parameter_door_open)); // 上门服务的时间
		params.add(new BasicNameValuePair("address",address)); // 服务地址
		params.add(new BasicNameValuePair("location",location)); // 服务地点坐标
		params.add(new BasicNameValuePair("freshdesc",buffer.toString()));
		params.add(new BasicNameValuePair("totalprice",allPrice.getText().toString()));
		params.add(new BasicNameValuePair("option","cdistribute"));
		if(littlegoods!=null && !"".equals(littlegoods)){
			params.add(new BasicNameValuePair("littlegoods",littlegoods));
		}
		return params;
	}
	
	/**
	 * 提交帮我买菜订单
	 */
	public void submitBuyVegetables() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(BuyVegetableListViewActivity.this,getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final BuyVegetablesState state = resolution.submit(BuyVegetableListViewActivity.this,CMD.SUBMIT_VEGETABLES_CLEANING, params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(BuyVegetableListViewActivity.this,"提交失败!:" + state.getCode());
							return;
						}
						if (state.getState() == 1) {
							CommonMethod.makeNoticeShort(BuyVegetableListViewActivity.this, "提交成功!");
							Intent intent = new Intent();
							intent.putExtra("oid", state.getOid());
							setResult(Constance.RESULTCODE_HOMEMAKE, intent); 
							finish();
						} else {
							CommonMethod.makeNoticeShort(BuyVegetableListViewActivity.this,"提交失败!:" + state.getCode());
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
	}
	
	
	
	
	
	

	
	
	
	

}
