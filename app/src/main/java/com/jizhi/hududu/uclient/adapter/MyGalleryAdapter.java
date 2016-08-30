package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.Dishes;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.ViewHolder>
{

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
	}
	
	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	private LayoutInflater mInflater;
	private List<Dishes> list;

	public MyGalleryAdapter(Context context, List<Dishes> list)
	{
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		public ViewHolder(View arg0)
		{
			super(arg0);
		}

		ImageView pic;
		TextView name;
	}

	@Override
	public int getItemCount()
	{
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		View view = mInflater.inflate(R.layout.dishes_gr,viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.pic = (ImageView) view.findViewById(R.id.pic);
		viewHolder.name = (TextView) view.findViewById(R.id.name);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Dishes bean = list.get(position);
		ImageLoader.getInstance().displayImage(CMD.NETURL+"uploads/"+bean.getDishpic(), holder.pic,UtilImageLoader.getImageOptionsFood());
		holder.name.setText(bean.getDishname()+"");
		if (mOnItemClickLitener != null)
		{
			holder.pic.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mOnItemClickLitener.onItemClick(null,0);
				}
			});

		}
	}

	public List<Dishes> getList() {
		return list;
	}

	public void setList(List<Dishes> list) {
		this.list = list;
	}
}
