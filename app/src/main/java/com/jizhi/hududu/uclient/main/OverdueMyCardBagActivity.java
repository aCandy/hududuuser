package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.OverdueMyCardBagAdapter;
import com.jizhi.hududu.uclient.bean.MyCardBag;
import com.jizhi.hududu.uclient.bean.MyCardBagState;
import com.jizhi.hududu.uclient.json.MyCardBagResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;


/**
 * 过期优惠券
 * @author Xuj
 * @date 2015年8月30日 11:18:34
 */
public class OverdueMyCardBagActivity extends Activity{

	@ViewInject(R.id.listView)
	private ListView listView;
	
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	private OverdueMyCardBagAdapter adapter;
	
	private List<MyCardBag> list;
	
	private Dialog dialog;
	
	private MyCardBagResolution resolution = new MyCardBagResolution();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.overdue_my_card_bag);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); //Xutil必须调用的一句话
		initView();
		search();
		
	}

	public void finishAct(View view) {
		finish();
	}
	
	public void initView(){
		tv_title.setText(R.string.over_coupon);
		list = new ArrayList<MyCardBag>();
		adapter = new OverdueMyCardBagAdapter(OverdueMyCardBagActivity.this, list);
		listView.setAdapter(adapter);
		
	}
	
	
	
	
	public List<NameValuePair> params(String searchOrAdd,String uniqueid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("option","showcoupons"));
		params.add(new BasicNameValuePair("cid","18502860652")); //
		params.add(new BasicNameValuePair("status","0")); //
		return params;
	}
	
	/**
	 * 查询我的优惠券
	 */
	public void search() {
		if (dialog != null) {
				dialog.dismiss();
		}
		dialog = CustomProgress.show(OverdueMyCardBagActivity.this,getString(R.string.seaching_my_card_bag), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final MyCardBagState state = resolution.resolution(OverdueMyCardBagActivity.this,CMD.SEARCH_CASH_COUPON, params("search",null));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(OverdueMyCardBagActivity.this,"查询失败!:");
							return;
						}
						if (state.getState() == 1) {
							if(state.getResp()!=null){
								if(state.getResp()!=null && state.getResp().size()>0){
									list.addAll(state.getResp());
									adapter.notifyDataSetChanged();
								}
							}
						}
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				});
			}
		}).start();
	}
	
}
