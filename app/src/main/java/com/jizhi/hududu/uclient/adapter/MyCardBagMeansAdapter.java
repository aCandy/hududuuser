package com.jizhi.hududu.uclient.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.huduoduoapp.R;

/**
 * 我的卡包 说明
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class MyCardBagMeansAdapter extends BaseAdapter {
	private Context context;
	private String[] list;
	private LayoutInflater inflater;

	

	public MyCardBagMeansAdapter(Context context,String[] list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    ViewHolder holder;
	    String means = list[position];
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_card_bag_item_means,null);
			holder.introduce = (TextView) convertView.findViewById(R.id.introduce);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.introduce.setText(means);
		return convertView;
	}

	class ViewHolder {
		TextView introduce;
	}
	

}