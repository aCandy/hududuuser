package com.jizhi.hududu.uclient.main;

import java.util.List;

import uk.co.senab.photoview.PViewPager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.DishesZoomDetailAdapter;
import com.jizhi.hududu.uclient.bean.Dishes;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 厨师拿手菜缩放图片
 * @author Xuj
 * @date 2015年8月27日 12:38:16
 */
public class DishesZoomActivity extends Activity{
	
	private List<Dishes> list_dishes;
	
	private List<String> list_string;
	
	private int initPosition = 0;// 初始索引
	
	@ViewInject(R.id.tv_title)
	private TextView title;
	
	@ViewInject(R.id.frashowimgs_pager)
	private PViewPager viewPagerImgs;// 使用修复了android系统缩放Bug的ViewPager
	
	@ViewInject(R.id.showimgs_txt_title)
	private TextView txtTitle;// 显示标题
	
	@ViewInject(R.id.menu_title)
	private TextView menu_title;// 显示菜品名称
	
	
	private DishesZoomDetailAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dishes_zoom);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		initView();

	}
	
	public void finishAct(View view) {
		finish();
	}
	
	public void initView(){
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		Bundle bundle = intent.getExtras();
		if(type.equals("1")){
			list_dishes = (List<Dishes>)bundle.getSerializable("list");
			title.setText(R.string.menu_detail);
			menu_title.setText(list_dishes.get(initPosition).getDishname());
		}else if(type.equals("2")){
			list_string = (List<String>)intent.getStringArrayListExtra("list");
			title.setText(R.string.evaluation_picutes);
			menu_title.setVisibility(View.GONE);
		}else{
			finish();
			return;
		}
		initPosition = bundle.getInt("initPosition");
		adapter = new DishesZoomDetailAdapter(list_dishes,list_string,ImageLoader.getInstance(),UtilImageLoader.getImageOptionsFood());
		viewPagerImgs.setAdapter(adapter);
		viewPagerImgs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
				if(list_dishes!=null){
					txtTitle.setText(String.format(getString(R.string.format_showimgs_txt_title),position+1,list_dishes.size()));
					menu_title.setText(list_dishes.get(position).getDishname());
				}else if(list_string !=null){
					txtTitle.setText(String.format(getString(R.string.format_showimgs_txt_title),position+1,list_string.size()));
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		viewPagerImgs.setCurrentItem(initPosition);
		if(list_dishes!=null){
			txtTitle.setText(String.format(getString(R.string.format_showimgs_txt_title),initPosition + 1,list_dishes.size()));
		}else if(list_string!=null){
			txtTitle.setText(String.format(getString(R.string.format_showimgs_txt_title),initPosition + 1,list_string.size()));
		}
	}
}
