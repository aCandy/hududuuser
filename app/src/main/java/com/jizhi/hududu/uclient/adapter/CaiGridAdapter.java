package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.main.BuyVegetablesViewPagerActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 帮我买菜 GridView Adapter
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class CaiGridAdapter extends BaseAdapter {
	private List<String> list;
	private LayoutInflater inflater;
	private Context context;

	public CaiGridAdapter(Context context,List<String> list) {
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.cai,null);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 加载图片
		ImageLoader.getInstance().displayImage(CMD.NETURL + "uploads/fresh_img/"+list.get(position), holder.pic,UtilImageLoader.getImageOptionsFood());
		return convertView;
	}

	class ViewHolder {
		ImageView pic;
	}



}