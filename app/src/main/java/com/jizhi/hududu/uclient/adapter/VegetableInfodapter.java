package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.Utils;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.VegetableInfo;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.neusoft.huduoduoapp.R;

public class VegetableInfodapter extends BaseAdapter {

	private Context context;
	private List<VegetableInfo> list;
	private LayoutInflater inflater;

	public VegetableInfodapter(Context context, List<VegetableInfo> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		VegetableInfo bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.vegettables_info_item, null);
			holder.pic = (ImageView) convertView
					.findViewById(R.id.pic);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			holder.tv_allPcice = (TextView) convertView
					.findViewById(R.id.tv_allPcice);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(bean.getName());
		holder.tv_price.setText("￥:"+bean.getPrice() + "");
		holder.tv_count.setText(bean.getCount()+"");
		holder.tv_allPcice.setText("￥:"+Utils.m2(bean.getAllPrice())+"");
		return convertView;
	}

	class ViewHolder {
		ImageView pic;
		TextView tv_name;
		TextView tv_price;
		TextView tv_count;
		TextView tv_allPcice;
	}
}