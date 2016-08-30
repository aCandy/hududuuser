package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.jizhi.hududu.uclient.adapter.ChooseChefAdapter;
import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.DinnerState;
import com.jizhi.hududu.uclient.dao.DBOpenHelper;
import com.jizhi.hududu.uclient.dao.DinnerDao;
import com.jizhi.hududu.uclient.json.EatFoodResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.ShowOrderDialog;
import com.jizhi.hududu.uclient.widget.ShowOrderDialog.OnSureClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;

/**
 * 选择厨师
 * @author Xuj
 * @date 2015年8月6日 10:20:25
 */
public class ChooseChefListViewActivity extends Activity implements OnClickListener{

	@ViewInject(R.id.listview)
	private ListView listview; 
	
	@ViewInject(R.id.screening)
	private TextView screening;  //筛选
	
	private EatFoodResolution resolution = new EatFoodResolution();
	
	private ChooseChefAdapter adapter;
	
	private String lng; //经度
	
	private String lat; //纬度
	
	private List<Dinner> list;
	
	private List<String> choose_list;
	
	private int currentIndex = 100; //当前筛选的值
	
	private DinnerDao dao;
	
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_chef_listview);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		initData();
		initView();
		if(list.size()==0){
			searchEatFood();
		}
	}
	


	public void finishAct(View view) {
		finish();
	}

	
	public void initData(){
		Bundle bundle = getIntent().getExtras();
		lat = bundle.getString("lat");
		lng = bundle.getString("lng");
		List<Dinner> tempList = (List<Dinner>)bundle.getSerializable("dinner_list");
		if(tempList!=null && tempList.size()>0){
			list = tempList;
		}else{
			list = new ArrayList<Dinner>();
		}
		choose_list = new ArrayList<String>();
		choose_list.add("离我最近");
		choose_list.add("评分最高");
		
	}
	
	public void initView(){
		dao = new DinnerDao(this);
		screening.setOnClickListener(this);
		adapter = new ChooseChefAdapter(this,list,dao);
		listview.setAdapter(adapter);
		
	}
	
	
	
	
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lng", lng)); //经度
		params.add(new BasicNameValuePair("lat", lat)); //纬度
		params.add(new BasicNameValuePair("loadpic","yes")); //是否加载拿手菜图片
		params.add(new BasicNameValuePair(CMD.NEED_PARAMETER_TEXT,"nearfk"));
		return params;
	}

	
	//	final DinnerState state = resolution.resolution(ChooseChefListViewActivity.this,
	//CMD.SEARCH_EAT_FOOD, params(),Double.parseDouble("104.067112"),Double.parseDouble("30.553093"));
	
	/**
	 * 查询厨师
	 */
	public void searchEatFood() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(ChooseChefListViewActivity.this,getString(R.string.searing), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final DinnerState state = resolution.resolution(ChooseChefListViewActivity.this,
						CMD.SEARCH_EAT_FOOD, params(),Double.parseDouble(lng),Double.parseDouble(lat));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(ChooseChefListViewActivity.this, "数据解析出错!");
						}else{
							if (state.getState() == 1) {
								if(state.getResp()!=null && state.getResp().size()>0){
									list.addAll(state.getResp());
									adapter.notifyDataSetChanged();
									dao.save(list);
								}else{
									CommonMethod.makeNoticeShort(ChooseChefListViewActivity.this, "附近暂未搜索到厨师!");
								}
							}
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
		
	}

	@Override
	public void onClick(View view) {
		if(list==null || list.size()==0){
			return;
		}
		ShowOrderDialog dialog = new ShowOrderDialog(this,currentIndex,listener,choose_list);
		dialog.show();
	}
	
	
	private OnSureClickListener listener = new OnSureClickListener() {
		@Override
		public void getText(int chooseIndex) {
			if(chooseIndex == currentIndex){
				return;
			}
			list.clear();
			List<Dinner> tempList = null;
			if(chooseIndex==0){
				 tempList = dao.byFarOrder();
			}else if(chooseIndex==1){
				 tempList = dao.byAvgrateOrder();
			}
			if(tempList!=null && tempList.size()>0){
				list.addAll(tempList);
			}
			adapter.notifyDataSetChanged();
			currentIndex = chooseIndex;
		}
	};
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null) {
			dialog.dismiss();
		}
	}



	
	
	
	
	

}
