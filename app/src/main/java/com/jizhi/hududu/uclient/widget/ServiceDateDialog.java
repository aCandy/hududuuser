package com.jizhi.hududu.uclient.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.AbstractWheelTextAdapter;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.DateUtil;
import com.neusoft.huduoduoapp.R;

/**
 * 上门服务时间Dialog
 * @date 2015年8月5日 15:45:12
 * @author Xuj
 */
public class ServiceDateDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView day_wheel;
	private WheelView hour_wheel;
	private WheelView minute_wheel;
	
	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;

	private AddressTextAdapter dayAdapter;
	private AddressTextAdapter hourAdapter;
	private AddressTextAdapter minuteAdapter;

	
	private ArrayList<String> day_list = new ArrayList<String>();
	private ArrayList<String> hour_list = new ArrayList<String>();
	private ArrayList<String> minute_list = new ArrayList<String>();
	
	
	private String day = "今天";
	private String hour = "8点";
	private String minute = "00分";
	
	private int nowHour = 0;
	private int nowMinute = 0;
	
	private String state;
	
	private ServiceDateInterface listener;

	private int maxsize = 24;
	private int minsize = 14;
	
	private boolean isaaaaa = false;

	public ServiceDateDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_date_dialog);

		day_wheel = (WheelView) findViewById(R.id.day);
		hour_wheel = (WheelView) findViewById(R.id.hour);
		minute_wheel = (WheelView) findViewById(R.id.minute);
		
		lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeAddress.setOnClickListener(this);
		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initData();
		
		if(state.equals("小于7点")){
			dayAdapter = new AddressTextAdapter(context,day_list,0,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(0);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,0,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(0);
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,0,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(0);
		}
		
		else if(state.equals("小于7.30")){
			dayAdapter = new AddressTextAdapter(context,day_list,0,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(0);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,0,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(0);
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,0,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(0);
			
		}else if(state.equals("大于7.30")){
			dayAdapter = new AddressTextAdapter(context,day_list,0,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(0);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,0,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(0);
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,0,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(0);
			
		}else if(state.equals("服务时间")){
			dayAdapter = new AddressTextAdapter(context,day_list,0,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(0);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,nowHour,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(nowHour);
			
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,nowMinute,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(nowMinute);
			
		}else if(state.equals("超过22点")){
			Log.e("超过22点","");
			
			dayAdapter = new AddressTextAdapter(context,day_list,1,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(1);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,0,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(0);
			
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,0,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(0);
			
		}
		else if(state.equals("睡觉时间")){
			dayAdapter = new AddressTextAdapter(context,day_list,1,maxsize, minsize);
			day_wheel.setVisibleItems(5);
			day_wheel.setViewAdapter(dayAdapter);
			day_wheel.setCurrentItem(1);
			
			hourAdapter = new AddressTextAdapter(context,hour_list,0,maxsize, minsize);
			hour_wheel.setVisibleItems(5);
			hour_wheel.setViewAdapter(hourAdapter);
			hour_wheel.setCurrentItem(0);
			
			minuteAdapter = new AddressTextAdapter(context,minute_list,0,maxsize, minsize);
			minute_wheel.setVisibleItems(5);
			minute_wheel.setViewAdapter(minuteAdapter);
			minute_wheel.setCurrentItem(0);
		}
		
		
		day_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
				day = currentText;
				setTextviewSize(currentText,dayAdapter);
			}
		});

		day_wheel.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, dayAdapter);
			}
		});

		hour_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
				hour = currentText;
				setTextviewSize(currentText, hourAdapter);
			}
		});
		
		hour_wheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, hourAdapter);
			}
		});
		
		
		minute_wheel.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
				minute = currentText;
				setTextviewSize(currentText, minuteAdapter);
			}
		});
		
		minute_wheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, minuteAdapter);
			}
		});
	}
	
	
	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface ServiceDateInterface {
		public void onClick(String chooseDate);
	}
	
	
	public void setListener(ServiceDateInterface listener) {
		this.listener = listener;
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
		if (v == btnSure) {
			if (listener != null) {
				int chooseHour = Integer.parseInt(hour.replace("点",""));
				int chooseMinute = Integer.parseInt(minute.replace("分",""));
				Calendar calendar = Calendar.getInstance();// 日历对象
				Date nowDate = new Date();// 当前时间
				calendar.setTime(nowDate);// 设置当前日期
				calendar.set(calendar.HOUR_OF_DAY,chooseHour);
				calendar.set(calendar.MINUTE,chooseMinute);
				if (day.equals("明天")) {
					calendar.add(Calendar.DAY_OF_MONTH, 1);// 天数加一，为-1的话是天数减1
				} else if (day.equals("后天")) {
					calendar.add(Calendar.DAY_OF_MONTH, 2);// 天数加二，为-2的话是天数减2
				}
				int finalYear = calendar.get(Calendar.YEAR);
				String finalMonth = DateUtil.gtOrGtTen(calendar.get(Calendar.MONTH) + 1);
				String finalDay = DateUtil.gtOrGtTen(calendar.get(Calendar.DAY_OF_MONTH));
				String finalHour = DateUtil.gtOrGtTen(calendar.get(Calendar.HOUR_OF_DAY));
				String finalMinute = DateUtil.gtOrGtTen(calendar.get(Calendar.MINUTE));
				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					Date chooseDate = simple.parse(finalYear+"-"+finalMonth+"-"+finalDay+" "+finalHour+":"+finalMinute);
					long  between = chooseDate.getTime() - nowDate.getTime();
					if(between > (1 * 60 * 29 * 1000)){
						listener.onClick(finalYear+"-"+finalMonth+"-"+finalDay+" "+finalHour+":"+finalMinute);
						dismiss();
					}
					else{
						CommonMethod.makeNoticeShort(context,"请选择半小时后的时间");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else if (v == btnCancel) {
			dismiss();
		} else if (v == lyChangeAddressChild) {
			return;
		} else {
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
		day_list.add("今天");
		day_list.add("明天");
		day_list.add("后天");
		
		
		hour_list.add("8点");
		hour_list.add("9点");
		hour_list.add("10点");
		hour_list.add("11点");
		hour_list.add("12点");
		hour_list.add("13点");
		hour_list.add("14点");
		hour_list.add("15点");
		hour_list.add("16点");
		hour_list.add("17点");
		hour_list.add("18点");
		hour_list.add("19点");
		hour_list.add("20点");
		hour_list.add("21点");
		
		minute_list.add("00分");
		minute_list.add("05分");
		minute_list.add("10分");
		minute_list.add("15分");
		minute_list.add("20分");
		minute_list.add("25分");
		minute_list.add("30分");
		minute_list.add("35分");
		minute_list.add("40分");
		minute_list.add("45分");
		minute_list.add("50分");
		minute_list.add("55分");

		computingHour();

		
	}
	

	
	/**
	 * 返回日期索引
	 * @param province
	 * @return
	 */
	public int getDay(String province) {
		int size = day_list.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(day_list.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		day = "今天";
		return provinceIndex;
	}
	
	/**
	 * 返回日期索引
	 * @param province
	 * @return
	 */
	public int getHour(String province) {
		int size = hour_list.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(hour_list.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		hour = "8点";
		day = "明天";
		return 0;
	}
	
	/**
	 * 返回日期索引
	 * @param province
	 * @return
	 */
	public int getMinute(String province) {
		int size = minute_list.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(minute_list.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		minute = "0分";
		return provinceIndex;
	}
	
	/**
	 * 将分钟四舍五入
	 * @param currentMinutes
	 */
	public void computingMinute(String currentMinute){
		int count;
		int count1 = 0;
		if(currentMinute.length()==2){
			count = Integer.parseInt(String.valueOf(currentMinute.charAt(0)));
			count1 = Integer.parseInt(String.valueOf(currentMinute.charAt(1)));
			if(count1==0){
				count1 = 0;
			}else if(count1>0 && count1<=5){
				count1 = 5;
			}else if(count1>5 && count1<=9){
				count1 = 0;
				if(count==5){
					isaaaaa = true;
					count = 0;
					nowHour = nowHour+1;
					if(nowHour == 22){
						state = "超过22点";
						day = "明天";
					}
				}else{
					count = ++count;
				}
			}
			nowMinute = getMinute(count+""+count1+"分");
		}else{
			count = Integer.parseInt(String.valueOf(currentMinute.charAt(0)));
			if(count==0){
				nowMinute = 0;
			}else if(count>0 && count<=5){
				nowMinute = 1;
			}else if(count>5 && count<=9){
				nowMinute = 2;
			}
		}
		minute = minute_list.get(nowMinute);
	}
	
	/**
	 * 将小时四舍五入
	 * @param currentMinutes
	 */
	public void computingHour(){
		Calendar calendar = Calendar.getInstance();
		int serviceMinute = calendar.get(Calendar.MINUTE)+30;
		nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		if(serviceMinute>=60){
			nowHour = nowHour+1;
		}
		if(nowHour>=0 && nowHour<=6){
			state = "小于7点";
	
		}if(nowHour == 7){
			if(serviceMinute>=30){
				state = "大于7.30";
				calendar.add(calendar.MINUTE,30);
				computingMinute(calendar.get(Calendar.MINUTE)+"");
			}else{
				state = "小于7.30";
			}
		}
		
		else if(nowHour>=8 && nowHour<=21){
			calendar.add(calendar.MINUTE,30);
			state = "服务时间";
			computingMinute(calendar.get(Calendar.MINUTE)+"");
			hour = nowHour+"点";
			nowHour = getHour(hour);
		}else if(nowHour>=22){
			state = "睡觉时间";
			day = "明天";
		}
		
	}
	
	
	
	
	
	
	
	

}