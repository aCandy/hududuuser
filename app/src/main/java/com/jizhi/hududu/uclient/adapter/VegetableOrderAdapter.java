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

import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.neusoft.huduoduoapp.R;

public class VegetableOrderAdapter extends BaseAdapter {

	private Context context;
	private List<VegetableOrderhFkInfo> list;
	private LayoutInflater inflater;
	private Handler handler;

	public VegetableOrderAdapter(Context context,
			List<VegetableOrderhFkInfo> list, Handler handler) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.handler = handler;
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
		final VegetableOrderhFkInfo bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_fukeinfo_vagetables,
					null);
			holder.tv_fkname = (TextView) convertView
					.findViewById(R.id.tv_fkname);
			holder.tv_fkage = (TextView) convertView
					.findViewById(R.id.tv_fkage);
			holder.tv_fknum = (TextView) convertView
					.findViewById(R.id.tv_fknum);
			holder.tv_fkordercount = (TextView) convertView
					.findViewById(R.id.tv_fkordercount);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.btn_toPay = (Button) convertView
					.findViewById(R.id.btn_toPay);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			holder.detail = (Button) convertView
					.findViewById(R.id.detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_fkname.setText(bean.getName());
		holder.tv_fkage.setText(bean.getAge() + "岁");
		holder.tv_fknum.setText(bean.getIcno());
		holder.tv_fkordercount.setText(bean.getOrdertotal() + "");
		holder.tv_price.setText(bean.getDiscount() + "");
		holder.tv_distance.setText("距离：" + bean.getDistance() + "米");
		holder.btn_toPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.obj = bean;
				message.what = 0X01;
				handler.sendMessage(message);

			}
		});
		holder.detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.obj = bean.getQuote();
				message.what = 0X02;
				handler.sendMessage(message);

			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_fkname;
		TextView tv_fkage;
		TextView tv_fknum;
		TextView tv_fkordercount;
		TextView tv_price;
		TextView tv_distance;
		Button btn_toPay;
		Button detail;
	}
}