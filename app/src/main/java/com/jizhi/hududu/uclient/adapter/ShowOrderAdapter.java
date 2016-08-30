package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.huduoduoapp.R;
/**
 * 显示订单顶部Adapter
 * @author Xuj
 * @date 2015年8月26日 13:05:05
 */
public class ShowOrderAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	private LayoutInflater inflater;
	private int currentIndex;

	public ShowOrderAdapter(Context context, List<String> list, int currentIndex) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.currentIndex = currentIndex;
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.show_order_item, null);
			holder.wtype = (TextView) convertView.findViewById(R.id.wtype);
			holder.gou = (ImageView) convertView.findViewById(R.id.gou);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == currentIndex) {
			holder.wtype.setTextColor(context.getResources().getColor(R.color.show_order));
			holder.gou.setVisibility(View.VISIBLE);
		} else {
			holder.gou.setVisibility(View.GONE);
			holder.wtype.setTextColor(context.getResources().getColor(R.color.gray_5));
		}
		holder.wtype.setText(list.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView wtype;
		ImageView gou;
	}
}