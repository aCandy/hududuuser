package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.neusoft.huduoduoapp.R;

public class NearByInfoAdapter extends BaseAdapter {

	private Context context;
	private List<NearByPoiInfo> poiInfos;
	private LayoutInflater inflater;
	private  boolean isLoc;

	public NearByInfoAdapter(Context context, List<NearByPoiInfo> poiInfos,boolean isLoc) {
		this.context = context;
		this.poiInfos = poiInfos;
		this.isLoc=isLoc;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return poiInfos == null ? 0 : poiInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return poiInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		NearByPoiInfo poiInfo = poiInfos.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_nearby, null);

			holder.tv_nearbyName = (TextView) convertView
					.findViewById(R.id.tv_nearbyName);
			holder.tv_nearbyAddr = (TextView) convertView
					.findViewById(R.id.tv_nearbyAddr);
			holder.img_icon = (ImageView) convertView
					.findViewById(R.id.img_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_nearbyName.setText(poiInfo.getName());
		holder.tv_nearbyAddr.setText(poiInfo.getAddress());
		if (position == 0 &&isLoc) {
			holder.img_icon.setVisibility(View.VISIBLE);
		} else {
			holder.img_icon.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {

		TextView tv_nearbyName;
		TextView tv_nearbyAddr;
		ImageView img_icon;
	}
}