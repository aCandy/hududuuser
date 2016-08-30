package com.jizhi.hududu.uclient.main;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.DishesGridAdapter;
import com.jizhi.hududu.uclient.bean.Dishes;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;

/**
 * 拿手菜GridView 显示列表
 * @author Xuj
 * @date 2015年8月27日 12:54:13
 */
public class DishesGridActivity extends Activity implements OnItemClickListener{

	@ViewInject(R.id.gridView)
	private GridView gridView;
	
	@ViewInject(R.id.tv_title)
	private TextView title;
	
	private DishesGridAdapter adapter;
	
	private List<Dishes> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.dishes_grid);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		initView();
	}
	
	public void finishAct(View view) {
		finish();
	}
	
	public void initView(){
		ViewUtils.inject(this); // Xutil必须调用的一句话
		title.setText(R.string.cherf_menu);
		Bundle bundle = getIntent().getExtras();
		List<Dishes> tempList = (List<Dishes>)bundle.getSerializable("list");
		if(tempList==null && tempList.size()==0){
			CommonMethod.makeNoticeShort(DishesGridActivity.this, "拿手菜加载失败!");
			finish();
			return;
		}
		list = tempList;
		adapter = new DishesGridAdapter(this, list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		Intent intent = new Intent(this,DishesZoomActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("list",(Serializable)list);
		bundle.putInt("initPosition",position);
		intent.putExtra("type","1");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	
	
}
