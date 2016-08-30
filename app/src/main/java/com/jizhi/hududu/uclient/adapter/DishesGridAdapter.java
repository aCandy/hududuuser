package com.jizhi.hududu.uclient.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.Dishes;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 厨师拿手菜Adapter
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class DishesGridAdapter extends BaseAdapter {
	private Context context;
	private List<Dishes> list;
	private LayoutInflater inflater;

	public DishesGridAdapter(Context context,List<Dishes> list) {
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
	public View getView(final int position,View convertView, ViewGroup parent) {
		final ViewHolder holder;
		Dishes bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.dishes_grid_item, null);
			holder.food_name = (TextView) convertView.findViewById(R.id.food_name);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final View tempView = convertView;
		holder.food_name.setText(bean.getDishname());
		//加载图片
		ImageLoader.getInstance().displayImage(CMD.NETURL+"uploads/"+bean.getDishpic(),holder.pic,UtilImageLoader.getImageOptionsFood());
		return convertView;
	}

	class ViewHolder {
		TextView food_name;
		ImageView pic;
	}
	
	

}