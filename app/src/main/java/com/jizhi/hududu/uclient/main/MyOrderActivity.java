package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.MyFragmentPagerAdapter;
import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.fragment.OrderFragments;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.widget.ShowOrderDialog;
import com.jizhi.hududu.uclient.widget.ShowOrderDialog.OnSureClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.umeng.socialize.utils.Log;

/**
 * 我的订单
 * @author Xuj
 * @date 2015年8月14日 09:24:53
 */
@SuppressLint("NewApi")
public class MyOrderActivity extends FragmentActivity implements OnClickListener{

	@ViewInject(R.id.all_linearLayout)
	private LinearLayout all_linearLayout; //全部
	@ViewInject(R.id.dont_identify_linearLayout)
	private LinearLayout dont_identify_linearLayout; //未确认
	@ViewInject(R.id.done_linearLayout)
	private LinearLayout done_linearLayout; //已完成
	@ViewInject(R.id.evaluated_linearLayout)
	private LinearLayout evaluated_linearLayout; //待评价
	
	
	@ViewInject(R.id.all)
	private TextView all; //全部
	@ViewInject(R.id.dont_identify)
	private TextView dont_identify; //未确认
	@ViewInject(R.id.evaluated)
	private TextView evaluated; //待评价
	@ViewInject(R.id.done)
	private TextView done; //已完成

	
	@ViewInject(R.id.relativeLayout1)
	private RelativeLayout relativeLayout1;
	
	@ViewInject(R.id.iv_bottom_line)
	private ImageView iv_bottom_line;
	
	
	@ViewInject(R.id.viewPager)
	private ViewPager pager;
	
	private Resources resources;
	
	private int currentIndex;
	
	private int chooseIndex;
	
	private List<String> list;
	
	private List<String> list_value;
	
	
	private int position_one; //偏移量1
	private int position_two; //偏移量2
	private int position_three; //偏移量3
	
	
	
	private MyFragmentPagerAdapter adapter;
	
	
	
	
	
	private OnSureClickListener listener = new OnSureClickListener() {
		@Override
		public void getText(int chooseIndex) {
			if(MyOrderActivity.this.chooseIndex!=chooseIndex){
				MyOrderActivity.this.chooseIndex = chooseIndex;
				String value = list_value.get(chooseIndex);
				for(int i = 0 ;i<adapter.getCount();i++){
					OrderFragments fragment = (OrderFragments)adapter.getItem(i);
					if(fragment!=null){
						if(fragment.getList()!=null){
							fragment.setHead_view(MyOrderActivity.this.getLayoutInflater().inflate(R.layout.order_head, null));
							fragment.getRefreshListView().getRefreshableView().addHeaderView(fragment.getHead_view());
							fragment.getList().clear();
							fragment.getAdapter().notifyDataSetChanged();
						}
							fragment.setWtype(value);
						if(i == pager.getCurrentItem()){
							fragment.flushData();
						}else{
							fragment.setFirst(true);
						}
					  }
					}
			}
		}
	};
	
	
	
	
	private ArrayList<Fragment> fragmentsList;// 中间fragment集合
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_order);
		initData();
		initView();
		

	}
	
	private void initData(){
		list = new ArrayList<String>();
		list.add("全部");
		list.add("帮我买菜");
		list.add("家庭保洁");
		list.add("吃饭了");
		list.add("手洗衣服");
//		list.add("急事速办");
		
		list_value = new ArrayList<String>();
		list_value.add("0");
		list_value.add("FH");
		list_value.add("HW");
		list_value.add("HD");
		list_value.add("WH");
//		list_value.add("JS");
		
		//模块类型不传（显示全部）    (FH-->买菜         HW-->保洁      HD-->吃饭      WH-->洗衣     JS-->急事)
		//模块类型不传（显示全部）    (FH-->买菜         HW-->保洁      HD-->吃饭      WH-->洗衣     JS-->急事)
		//模块类型不传（显示全部）    (FH-->买菜         HW-->保洁      HD-->吃饭      WH-->洗衣     JS-->急事)
		
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
		
	}
	
	public void finishAct(View view) {
		finish();
	}

	public void initView() {
		ViewUtils.inject(this); //Xutil必须调用的一句话
		resources = getResources();
		fragmentsList = new ArrayList<Fragment>();
		
		Fragment fragment1 = new OrderFragments(list_value.get(0),null); //全部
		Fragment fragment2 = new OrderFragments(list_value.get(0),"7"); //未确认
		Fragment fragment3 = new OrderFragments(list_value.get(0),"8"); //待评价
		Fragment fragment4 = new OrderFragments(list_value.get(0),"9"); //已完成
		fragmentsList.add(fragment1);
		fragmentsList.add(fragment2);
		fragmentsList.add(fragment3);
		fragmentsList.add(fragment4);
		adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentsList);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		pager.setOffscreenPageLimit(3);  
		all_linearLayout.setOnClickListener(this);
		dont_identify_linearLayout.setOnClickListener(this);
		done_linearLayout.setOnClickListener(this);
		evaluated_linearLayout.setOnClickListener(this);
		relativeLayout1.setOnClickListener(this);
		
	}

	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			Animation animation = null;
			switch (index) {
			case 0:
				if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				all.setTextColor(resources.getColor(R.color.green1));
				break;
			case 1:
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two,position_one, 0, 0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three, position_one, 0, 0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				dont_identify.setTextColor(resources.getColor(R.color.green1));
				break;
			case 2:
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one, position_two, 0, 0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three, position_two, 0, 0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				done.setTextColor(resources.getColor(R.color.green1));
				break;
			case 3:
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_three, 0, 0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one, position_three, 0, 0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two, position_three, 0, 0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				} 
				evaluated.setTextColor(resources.getColor(R.color.green1));
				break;
			}
			currentIndex = index;
			if(animation!=null){
	            animation.setFillAfter(true);
	            animation.setDuration(300);
	            iv_bottom_line.startAnimation(animation);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void onClick(View view) {
		Animation animation = null;
		switch (view.getId()) {
		case R.id.relativeLayout1:
			ShowOrderDialog diaglog = new ShowOrderDialog(this,chooseIndex,listener,list);
			diaglog.setCanceledOnTouchOutside(true);
			diaglog.setCancelable(true);
			diaglog.show();
			break;
		case R.id.all_linearLayout:
			if(currentIndex!=0){
				if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one,0,0,0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two,0,0,0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three,0,0,0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				currentIndex = 0;
				pager.setCurrentItem(currentIndex);
				all.setTextColor(resources.getColor(R.color.green1));
			}
			break;
		case R.id.dont_identify_linearLayout:
			if(currentIndex!=1){
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0,position_one,0,0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two,position_one,0,0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three,position_one,0,0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				dont_identify.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 1;
				pager.setCurrentItem(currentIndex);
			}
			break;
		case R.id.done_linearLayout:
			if(currentIndex!=2){
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0,position_two,0,0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one,position_two,0,0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 3){
					animation = new TranslateAnimation(position_three,position_two,0,0);
					evaluated.setTextColor(resources.getColor(R.color.black_color_text));
				}
				done.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 2;
				pager.setCurrentItem(currentIndex);
			}
			break;
		case R.id.evaluated_linearLayout:
			if(currentIndex!=3){
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0,position_three,0,0);
					all.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one,position_three,0,0);
					dont_identify.setTextColor(resources.getColor(R.color.black_color_text));
				} else if(currentIndex == 2){
					animation = new TranslateAnimation(position_two,position_three,0,0);
					done.setTextColor(resources.getColor(R.color.black_color_text));
				}
				evaluated.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 3;
				pager.setCurrentItem(currentIndex);
			}
			break;
		default:
			break;
		}
		if(animation!=null){
            animation.setFillAfter(true);
            animation.setDuration(300);
            iv_bottom_line.startAnimation(animation);
		}
	
	}
//	946546
	
	interface CallBackSelectData{
		void selectData();
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CMD.SUCCESS) {
			String oid = data.getStringExtra("oid");
			for(int i = 0;i<fragmentsList.size();i++){
				if(i == 0 || i==2){
					OrderFragments fragment = (OrderFragments)fragmentsList.get(i);
					List<Order> tempList = fragment.getList();
					for(int j = 0;j<tempList.size();j++){
						if(tempList.get(j).getOid().equals(oid)){
							tempList.get(j).setStatus("9");
							fragment.getAdapter().notifyDataSetChanged();
						}
					}
				}
			}
		}else if(resultCode == CMD.CANCEL){
			String oid = data.getStringExtra("oid");
			Log.e("fanhui   OId",oid);
			for(int i = 0;i<fragmentsList.size();i++){
				if(i == 0 || i==1){
					OrderFragments fragment = (OrderFragments)fragmentsList.get(i);
					List<Order> tempList = fragment.getList();
					for(int j = 0;j<tempList.size();j++){
						if(tempList.get(j).getOid().equals(oid)){
							tempList.remove(j);
							fragment.getAdapter().notifyDataSetChanged();
						}
					}
				}
			}
		}
	}

}
