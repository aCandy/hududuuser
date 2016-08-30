package com.jizhi.hududu.uclient.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.MyFragmentPagerAdapter;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.fragment.BuyVegetablesFragment;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;


/**
 * 帮我买菜ViewPager
 * @author Xuj
 * @date 2015年8月21日 15:54:01
 */
@SuppressLint("NewApi")
public class BuyVegetablesViewPagerActivity extends FragmentActivity implements
		OnClickListener {
	private Map<String,BuyVegetables> map;
	
	@ViewInject(R.id.vegetables)
	private TextView vegetables; // 蔬菜
	@ViewInject(R.id.fruit)
	private TextView fruit;// 水果
	@ViewInject(R.id.fish)
	private TextView fish; // 水产
	@ViewInject(R.id.meet)
	private TextView meet; // 肉
	@ViewInject(R.id.number)
	private TextView number; //所选数量
	
	
	@ViewInject(R.id.include_money)
	private TextView include_money; //包含服务费
	

	@ViewInject(R.id.price)
	private TextView price;

	@ViewInject(R.id.vegetables_linearLayout)
	private LinearLayout vegetables_linearLayout;
	@ViewInject(R.id.fruit_linearLayout)
	private LinearLayout fruit_linearLayout;
	@ViewInject(R.id.fish_linearLayout)
	private LinearLayout fish_linearLayout;
	@ViewInject(R.id.meet_linearLayout)
	private LinearLayout meet_linearLayout;
	
	@ViewInject(R.id.relativeLayoutBottom)
	private RelativeLayout relativeLayoutBottom; //所选数量
	

	@ViewInject(R.id.pager)
	private ViewPager pager; //ViewPager

	@ViewInject(R.id.circle)
	private ImageView circle; //滚动图标

	private Resources resources;

	private int currIndex = 0; // 当前所选中的ViewPager

	private int position_one; //偏移量1
	private int position_two; //偏移量2
	private int position_three; //偏移量3

	private int currentIndex;
	
	private String parameter_door_open;//上门服务时间
	private String location; //经纬度
	private String address;  //地址
	private String littlegoods; //顺手带
	private String serviceMoney; 
	

	private ArrayList<Fragment> fragmentsList;// 中间fragment集合
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.buy_vegetables_viewpager);
		initView();
		initData();
	}

	public void finishAct(View view) {
		finish();
	}
	
	
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		lp.leftMargin = ((int)vegetables.getX()+vegetables.getWidth()/3);
		lp.bottomMargin = 10;
		lp.gravity = Gravity.BOTTOM;
		circle.setLayoutParams(lp);

	}

	public void initView() {
		ViewUtils.inject(this); //Xutil必须调用的一句话
		resources = getResources();
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.buy_vegetables);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		map = (Map<String,BuyVegetables>)bundle.getSerializable("map");
		fragmentsList = new ArrayList<Fragment>();
		Fragment fragment1 = new BuyVegetablesFragment("1",map,(List<BuyVegetables>)bundle.getSerializable("vegetables_list"));
		Fragment fragment2 = new BuyVegetablesFragment("2",map,(List<BuyVegetables>)bundle.getSerializable("fruit_list"));
		Fragment fragment3 = new BuyVegetablesFragment("3",map,(List<BuyVegetables>)bundle.getSerializable("fish_list"));
		Fragment fragment4 = new BuyVegetablesFragment("4",map,(List<BuyVegetables>)bundle.getSerializable("meet_list"));
		price.setText(bundle.getString("allPrice"));
		number.setText(bundle.getString("allNumber"));
		parameter_door_open = bundle.getString("time");
		location = bundle.getString("location");
		address = bundle.getString("address");
		littlegoods = bundle.getString("littlegoods");
		
		serviceMoney = bundle.getString("serviceMoney");
		include_money.setText(serviceMoney);
		
		fragmentsList.add(fragment1);
		fragmentsList.add(fragment2);
		fragmentsList.add(fragment3);
		fragmentsList.add(fragment4);
		pager.setAdapter(new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentsList));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		pager.setOffscreenPageLimit(3);  
		
		
		vegetables_linearLayout.setOnClickListener(this);
		fruit_linearLayout.setOnClickListener(this);
		fish_linearLayout.setOnClickListener(this);
		meet_linearLayout.setOnClickListener(this);
		relativeLayoutBottom.setOnClickListener(this);
	}

	public void initData() {
		
		
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		position_one = (int) (screenW / 4.0);
		position_two = position_one * 2;
		position_three = position_one * 3;
	}

	public Drawable getDrawble(int id) {
		return getResources().getDrawable(id);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				vegetables.setTextColor(resources.getColor(R.color.green));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					vegetables.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_one, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_one, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				fruit.setTextColor(resources.getColor(R.color.green));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					vegetables.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_two, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_two, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				fish.setTextColor(resources.getColor(R.color.green));
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, position_three, 0, 0);
					vegetables.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_three, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_three, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				meet.setTextColor(resources.getColor(R.color.green));
				break;
			}
			currIndex = arg0;
			if (animation != null) {
				animation.setFillAfter(true);
				animation.setDuration(300);
				circle.startAnimation(animation);
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
		case R.id.vegetables_linearLayout:
			if (currentIndex != 0) {
				if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 3) {
					animation = new TranslateAnimation(position_three, 0, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				currentIndex = 0;
				pager.setCurrentItem(currentIndex);
				vegetables.setTextColor(resources.getColor(R.color.green1));
			}
			break;
		case R.id.fruit_linearLayout:
			if (currentIndex != 1) {
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_one, 0, 0);
					vegetables.setTextColor(resources.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_one, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_one, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				fruit.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 1;
				pager.setCurrentItem(currentIndex);
			}
			break;
		case R.id.fish_linearLayout:
			if (currentIndex != 2) {
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_two, 0, 0);
					vegetables.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_two, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 3) {
					animation = new TranslateAnimation(position_three,
							position_two, 0, 0);
					meet.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				fish.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 2;
				pager.setCurrentItem(currentIndex);
			}
			break;
		case R.id.meet_linearLayout:
			if (currentIndex != 3) {
				if (currentIndex == 0) {
					animation = new TranslateAnimation(0, position_three, 0, 0);
					vegetables.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 1) {
					animation = new TranslateAnimation(position_one,
							position_three, 0, 0);
					fruit.setTextColor(resources
							.getColor(R.color.black_color_text));
				} else if (currentIndex == 2) {
					animation = new TranslateAnimation(position_two,
							position_three, 0, 0);
					fish.setTextColor(resources
							.getColor(R.color.black_color_text));
				}
				meet.setTextColor(resources.getColor(R.color.green1));
				currentIndex = 3;
				pager.setCurrentItem(currentIndex);
			}
			break;
		case R.id.relativeLayoutBottom:
			if(map.size()<=0){
				CommonMethod.makeNoticeShort(BuyVegetablesViewPagerActivity.this,"至少选择一个商品!");
			}else{
				Intent intent = new Intent(BuyVegetablesViewPagerActivity.this,BuyVegetableListViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("map",(Serializable)map);
				bundle.putString("allPrice",price.getText().toString());
				bundle.putString("allNumber",number.getText().toString());
				bundle.putString("time",parameter_door_open);
				bundle.putString("location",location);
				bundle.putString("address",address);
				bundle.putString("littlegoods",littlegoods);
				bundle.putString("serviceMoney",serviceMoney);
				intent.putExtras(bundle);
				startActivityForResult(intent,1);
			}
			break;
		default:
			break;
		}
		if (animation != null) {
			animation.setFillAfter(true);
			animation.setDuration(300);
			circle.startAnimation(animation);
		}
	}
	
	
	 @Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	      // TODO Auto-generated method stub  
	      super.onActivityResult(requestCode, resultCode, data);  
	      //requestCode标示请求的标示   resultCode表示有数据  
	      if (resultCode == 1) {  
	    	  Map<String,BuyVegetables> tempMap = (Map<String,BuyVegetables>)data.getExtras().getSerializable("map");
	    	  if(tempMap!=null){
	    		  map.clear();
	    		  map.putAll(tempMap);
		    	  price.setText(data.getStringExtra("allPrice"));
		    	  number.setText(data.getStringExtra("allNumber"));
		    	  for(Fragment f:fragmentsList){
		    		  BuyVegetablesFragment fragment = (BuyVegetablesFragment)f;
				      for(BuyVegetables tempBean: fragment.getList()){
			    			  if(!map.containsKey(tempBean.getFhnum()) && tempBean.getAllNumber()>0){
			    				  tempBean.setAllNumber(0);
			    				  tempBean.setAllPrice(0);
			    			  }else if(map.containsKey(tempBean.getFhnum())){
			    				  tempBean.setAllNumber(map.get(tempBean.getFhnum()).getAllNumber());
			    			  }
		    		  }
		    		  fragment.getAdapter().notifyDataSetChanged();
		    	  }
	    	  }
	      }else if(resultCode == BuyVegetablesMainActivity.RETURN_SUCCESS){
	    	  Intent intent = new Intent();
	    	  Bundle currentBundle = new Bundle();
	    	  Bundle bundle = data.getExtras();
	    	  Map<String,BuyVegetables> tempMap = (Map<String,BuyVegetables>)bundle.getSerializable("map");
	    	  for(Fragment f:fragmentsList){
	    		  BuyVegetablesFragment fragment = (BuyVegetablesFragment)f;
	    		  for(BuyVegetables bean:fragment.getList()){
	    				 if(tempMap.containsKey(bean.getFhnum())){
	    					  bean.setAllNumber(tempMap.get(bean.getFhnum()).getAllNumber());
	    					  bean.setAllPrice(tempMap.get(bean.getFhnum()).getAllPrice());
	    				 }else{
	    					 bean.setAllNumber(0);
	    					 bean.setAllPrice(0);
	    				 }
	    		  }
	    		  if(fragment.getFhtype().equals("1")){ //蔬菜
	    			  currentBundle.putSerializable("vegetables_list", (Serializable)fragment.getList());
	    		  }else if(fragment.getFhtype().equals("2")){ //水果
	    			  currentBundle.putSerializable("fruit_list", (Serializable)fragment.getList());
	    		  }else if(fragment.getFhtype().equals("3")){ //水产
	    			  currentBundle.putSerializable("fish_list", (Serializable)fragment.getList());
	    		  }else if(fragment.getFhtype().equals("4")){ //肉类
	    			  currentBundle.putSerializable("meet_list", (Serializable)fragment.getList());
	    		  }
	    	  }
	    	 currentBundle.putString("allNumber",bundle.getString("allNumber"));
	    	 currentBundle.putString("allPrice",bundle.getString("allPrice"));
	    	 currentBundle.putSerializable("all_list",bundle.getSerializable("all_list"));
	    	 currentBundle.putSerializable("map",bundle.getSerializable("map"));
	    	 intent.putExtras(currentBundle);
	    	 setResult(BuyVegetablesMainActivity.RETURN_SUCCESS,intent);
	    	 finish();
	      }else if(resultCode == BuyVegetablesMainActivity.RETURN_CANCEL){
	    	  map.clear();
	    	  price.setText("0.00");
	    	  number.setText("0");
	    	  for(Fragment f:fragmentsList){
	    		  BuyVegetablesFragment fragment = (BuyVegetablesFragment)f;
			      for(BuyVegetables tempBean: fragment.getList()){
		    			tempBean.setAllNumber(0);
		    			tempBean.setAllPrice(0);
	    		  }
	    		  fragment.getAdapter().notifyDataSetChanged();
	    	  }
	      }else if(resultCode == Constance.RESULTCODE_HOMEMAKE){
	    	  Intent intent = new Intent();
	    	  intent.putExtra("oid", data.getStringExtra("oid"));
	    	  setResult(Constance.RESULTCODE_HOMEMAKE, intent); 
	    	  finish();
	      }
	}  
	
	
	
	
	
	
	
	

	public TextView getPrice() {
		return price;
	}

	public void setPrice(TextView price) {
		this.price = price;
	}

	public TextView getNumber() {
		return number;
	}

	public void setNumber(TextView number) {
		this.number = number;
	}

	public RelativeLayout getRelativeLayout() {
		return relativeLayoutBottom;
	}

	public void setRelativeLayout(RelativeLayout relativeLayout) {
		this.relativeLayoutBottom = relativeLayout;
	}
	
	
	
	

}
