package com.jizhi.hududu.uclient.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.AbstractWheelTextAdapter;
import com.neusoft.huduoduoapp.R;

/**
 * 选择炒菜个数
 * @date 2015年8月5日 15:45:27
 * @author Xuj
 */
public class FoodTypeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private WheelView service_time_wheel;
	
	private TextView title;

	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private FoodTypeTextAdapter foodAdapter;

	private Context context;
	
	private Map<String,Integer> map;
	
	private ArrayList<String> list;
	
	private int money = 39;
	
	private String choose_food = "三菜以内  39";
	
	private CallFoodChoose listener;

	private int maxsize = 24;
	private int minsize = 14;

	public FoodTypeDialog(Context context,ArrayList<String> list,Map<String,Integer> map) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.list = list;
		this.map = map;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_time_dialog);
		service_time_wheel = (WheelView) findViewById(R.id.service_time1);
		title = (TextView) findViewById(R.id.service_time);
		title.setText("请选择菜品");

		lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeAddress.setOnClickListener(this);
		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		
		foodAdapter = new FoodTypeTextAdapter(context,list,0,maxsize, minsize);
		service_time_wheel.setVisibleItems(5);
		service_time_wheel.setViewAdapter(foodAdapter);
		service_time_wheel.setCurrentItem(0);

		service_time_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) foodAdapter.getItemText(wheel.getCurrentItem());
				money = map.get(currentText);
				choose_food = currentText;
				setTextviewSize(currentText,foodAdapter);
			}
		});

		service_time_wheel.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) foodAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, foodAdapter);
			}
		});

	}

	public void setListener(CallFoodChoose listener) {
		this.listener = listener;
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface ServiceTimeInteface {
		public void onClick(String service_time);
	}

	private class FoodTypeTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected FoodTypeTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem,maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,FoodTypeTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textView = (TextView) arrayList.get(i);
			currentText = textView.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textView.setTextSize(24);
			} else {
				textView.setTextSize(14);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (listener != null) {
				listener.onClick(money,choose_food);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * @author Administrator
	 */
	public interface CallFoodChoose {
		public void onClick(int money,String choose_food);
	}

}