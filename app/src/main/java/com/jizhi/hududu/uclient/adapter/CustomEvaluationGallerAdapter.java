package com.jizhi.hududu.uclient.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.MyGalleryAdapter.OnItemClickLitener;
import com.jizhi.hududu.uclient.main.DishesZoomActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomEvaluationGallerAdapter extends RecyclerView.Adapter<CustomEvaluationGallerAdapter.ViewHolder>
{

	public interface OnItemClickLitener1
	{
		void onItemClick(View view, int position);
	}
	
	private OnItemClickLitener1 mOnItemClickLitener;
	private Context context;

	public void setOnItemClickLitener(OnItemClickLitener1 mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	private LayoutInflater mInflater;
	private List<String> list;

	public CustomEvaluationGallerAdapter(Context context, List<String> list)
	{
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		public ViewHolder(View arg0)
		{
			super(arg0);
		}

		ImageView pic;
	}

	@Override
	public int getItemCount()
	{
		return list.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		View view = mInflater.inflate(R.layout.custom_evaluation_grid_item,viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.pic = (ImageView) view.findViewById(R.id.pic);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder,final int position) {
		ImageLoader.getInstance().displayImage(CMD.PICPATH+"appraise_img/"+list.get(position),holder.pic,UtilImageLoader.getImageOptionsFood());
		if (mOnItemClickLitener != null)
		{
			holder.pic.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mOnItemClickLitener.onItemClick(holder.pic,position);
				}
			});

		}
	}



}
