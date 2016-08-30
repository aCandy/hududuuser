package com.jizhi.hududu.uclient.widget;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.AbstractWheelTextAdapter;
import com.neusoft.huduoduoapp.R;

/**
 * 洗衣时长Dialog
 * @date 2015年8月5日 15:45:27
 * @author Xuj
 */
public class ServiceHansWasingDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView service_time_wheel;
	
	private TextView btnSure;
	private TextView btnCancel;

	private AddressTextAdapter serviceTimeAdapter;
	
	private ArrayList<String> list = new ArrayList<String>();
	private Context context;
	
	private String service_time = "0.5";
	
	private CallBackWasingInteface listener;

	private int maxsize = 24;
	private int minsize = 14;

	public ServiceHansWasingDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_time_dialog);

		service_time_wheel = (WheelView) findViewById(R.id.service_time1);
		
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initData();
		
		serviceTimeAdapter = new AddressTextAdapter(context,list,getServiceTimeIndex(list.get(0)), maxsize, minsize);
		service_time_wheel.setVisibleItems(5);
		service_time_wheel.setViewAdapter(serviceTimeAdapter);
		service_time_wheel.setCurrentItem(getServiceTimeIndex(list.get(0)));

		service_time_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) serviceTimeAdapter.getItemText(wheel.getCurrentItem());
				service_time = currentText;
				setTextviewSize(currentText,serviceTimeAdapter);
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
				String currentText = (String) serviceTimeAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, serviceTimeAdapter);
			}
		});

	}
	
	
	
	
	public void setListener(CallBackWasingInteface listener) {
		this.listener = listener;
	}




	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface CallBackWasingInteface {
		public void onClick(String service_time);
	}
	
	
	

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
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
	public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
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
				listener.onClick(service_time);
				dismiss();
			}
		} else if (v == btnCancel) {
			dismiss();
		} 
		
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnChoose {
		public void onClick(String day,String hour,String minute);
	}


	
	public void initData(){
		list.add("0.5");
		list.add("1.0");
		list.add("1.5");
		list.add("2.0");
		list.add("2.5");
		list.add("3.0");
	}
	
	/**
	 * 返回服务时长索引
	 * @param province
	 * @return
	 */
	public int getServiceTimeIndex(String province) {
		int size = list.size();
		int provinceIndex = 0;
		for (int i = 0; i < size; i++) {
			if (province.equals(list.get(i))) {
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		service_time = "1.0";
		return 0;
	}

}