package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.hcs.hududu.uclient.utils.KeyBoardUtils;
import com.jizhi.hududu.uclient.adapter.NearByInfoAdapter;
import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.jizhi.hududu.uclient.util.Constance;
import com.neusoft.huduoduoapp.R;

/**
 * 附近地址
 * 
 * @author huChangSheng
 * @time 2015-7-30 上午10:11:04
 * @version 1.0
 * 
 */
public class NearbyAddrActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
	private NearbyAddrActivity mActivity;
	private ListView lv_nearby;
	private List<NearByPoiInfo> byPoiInfos, oldByPoiInfos;
	private EditText ed_addr;
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private int load_Index = 0;
	private List<PoiInfo> poiInfos;

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

	}

	private NearByInfoAdapter adapter;
	private String city;
	private LinearLayout rea_seach;
	private boolean PoiInfo;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_nearby);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_select_addr);
		byPoiInfos = (List<NearByPoiInfo>) getIntent().getSerializableExtra(
				"poiInfo");
		mActivity = NearbyAddrActivity.this;

		initView();
		city = "成都";
		if (null != byPoiInfos) {
			oldByPoiInfos = byPoiInfos;
			adapter = new NearByInfoAdapter(mActivity, byPoiInfos, true);
			lv_nearby.setAdapter(adapter);
		}
		lv_nearby.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NearByPoiInfo info;
				if (ed_addr.length() <= 0) {
					info = oldByPoiInfos.get(arg2);
				} else {
					info = byPoiInfos.get(arg2);
				}
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", info);
				intent.putExtras(bundle);
				setResult(Constance.RESULTCODE_NEAYBYADDE, intent);
				mActivity.finish();
			}
		});

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		ed_addr.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					if (null != oldByPoiInfos) {
						adapter = new NearByInfoAdapter(mActivity,
								oldByPoiInfos, false);
						lv_nearby.setAdapter(adapter);
					}
					return;
				}
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				lv_nearby.setVisibility(View.INVISIBLE);
				serachAddr();
			}
		});
	}

	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.spinner);
		// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) imageView
				.getBackground();
		// 开始动画
		spinner.start();
	}

	private void initView() {
		lv_nearby = (ListView) findViewById(R.id.lv_nearby);
		ed_addr = (EditText) findViewById(R.id.ed_addr);
		KeyBoardUtils.closeKeybord(ed_addr, mActivity);
		KeyBoardUtils.cursorMoveToLase(ed_addr);
		rea_seach = (LinearLayout) findViewById(R.id.rea_seach);
		RadioButton rb_back=(RadioButton) findViewById(R.id.rb_back);
		rb_back.setText(getString(R.string.back));
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
	}

	public void finishAct(View view) {
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void serach_addr(View view) {
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("成都市")
				.keyword(ed_addr.getText().toString()).pageNum(load_Index)
				.pageCapacity(30));

	}

	public void serachAddr() {
		// CustomProgress.show(mActivity, "搜索中…", true, null);
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
				.keyword(ed_addr.getText().toString()).pageNum(load_Index)
				.pageCapacity(30));

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult arg0) {

	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(mActivity, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		poiInfos = result.getAllPoi();
		if (null != poiInfos) {
			byPoiInfos = new ArrayList<NearByPoiInfo>();
			for (int i = 0; i < poiInfos.size(); i++) {
				String address = poiInfos.get(i).address;
				String name = poiInfos.get(i).name;
				if (null != poiInfos.get(i).location) {
					double longitude = poiInfos.get(i).location.longitude;
					double latitude = poiInfos.get(i).location.latitude;
					NearByPoiInfo byPoiInfo = new NearByPoiInfo(address, name,
							longitude, latitude);
					byPoiInfos.add(byPoiInfo);
				}

			}
			adapter = new NearByInfoAdapter(mActivity, byPoiInfos, false);
			lv_nearby.setAdapter(adapter);
		}
		lv_nearby.setVisibility(View.VISIBLE);
	}

	public void goToNextPage(View v) {
		load_Index++;
		serach_addr(null);
	}

	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);

			return false;
		}
		return false;
	}
}
