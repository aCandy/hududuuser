package com.jizhi.hududu.uclient.widget;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jizhi.hududu.uclient.adapter.ShowOrderAdapter;
import com.neusoft.huduoduoapp.R;

/**
 * 选择订单Dialog
 * 
 * @date 2015年8月5日 15:45:12
 * @author Xuj
 */
public class ShowOrderDialog extends Dialog implements OnItemClickListener {

	private ListView listView;

	private int currentIndex;

	private Context context;

	private ShowOrderAdapter adapter;

	private OnSureClickListener listener;

	private List<String> list;
	
	private LinearLayout ly_myinfo_changeaddress;

	public ShowOrderDialog(Context context, int currentIndex,
			OnSureClickListener listener, List<String> list) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.currentIndex = currentIndex;
		this.listener = listener;
		this.list = list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_order);
		listView = (ListView) findViewById(R.id.listView);
		ly_myinfo_changeaddress = (LinearLayout) findViewById(R.id.ly_myinfo_changeaddress);
		adapter = new ShowOrderAdapter(context, list, currentIndex);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!(event.getX() >= -10 && event.getY() >= -10)
					|| event.getX() >= ly_myinfo_changeaddress.getWidth() + 10
					|| event.getY() >= ly_myinfo_changeaddress.getHeight() + 20) {// 如果点击位置在当前View外部则销毁当前视图,其中10与20为微调距离
				dismiss();
			}
		}
		return true;
	}

	public interface OnSureClickListener {
		void getText(int chooseIndex);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		if (listener != null) {
			listener.getText(position);
			dismiss();
		}
	}

}