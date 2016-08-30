package com.jizhi.hududu.uclient.adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hcs.hududu.uclient.utils.SDCardUtils;
import com.jizhi.hududu.uclient.bean.Photos;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.PicSizeUtils;
import com.neusoft.huduoduoapp.R;

/**
 * 服客评价GridView
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class EvaluateGridAdapter extends BaseAdapter {
	private Activity context;
	private List<Photos> list;
	private LayoutInflater inflater;


	public EvaluateGridAdapter(Activity context,List<Photos> list) {
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
		Photos photo = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.evaluate_grid_item,null);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(photo.isAdd()){
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					takePhoto();
				}
			});
		}
		holder.pic.setImageBitmap(photo.getBitmap());
		return convertView;
	}
	
	
	/**
	 * 点击拍照
	 */
	private void takePhoto() {
		if(SDCardUtils.isSDCardEnable()){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				// 指定存放拍摄照片的位置
				File imagePath = PicSizeUtils.createImageFile();
				String filePath = imagePath.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagePath));
				takePictureIntent.putExtra("filePath",filePath);
				context.startActivityForResult(takePictureIntent,CMD.CAPTURE_PICTURE);
			} catch (IOException e) {
				CommonMethod.makeNoticeShort(context, "调用系统摄像头失败拉!");
				e.printStackTrace();
			}
			return;
		}
		CommonMethod.makeNoticeShort(context, "当前内存卡不可用!");
	}

	class ViewHolder {
		ImageView pic;
	}



}