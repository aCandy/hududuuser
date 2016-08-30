package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jizhi.hududu.uclient.bean.Dishes;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 拿手菜缩放 adapter
 * @author xuj
 * @date 2015年8月27日 13:06:01
 */
public class DishesZoomDetailAdapter extends PagerAdapter {

	private List<Dishes> list_dishes;
	private List<String> list_string;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public DishesZoomDetailAdapter(List<Dishes> list_dishes,List<String> list_string,ImageLoader imageLoader,
			DisplayImageOptions options) {
		this.list_dishes = list_dishes;
		this.list_string = list_string;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public int getCount() {
		if(list_dishes!=null){
			return list_dishes.size();
		}else if(list_string!=null){
			return list_string.size();
		}
		return 0;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		LayoutInflater li = (LayoutInflater) container.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.dishes_zoom_detail, container, false);
		final PhotoView photoView = (PhotoView) view.findViewById(R.id.picture_test);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
		String loadUrl = null;
		if(list_dishes!=null){
			loadUrl = CMD.NETURL + "uploads/"+ list_dishes.get(position).getDishpic();
		}else if(list_string!=null){
			loadUrl =CMD.PICPATH+"appraise_img/"+list_string.get(position);
		}
		imageLoader.displayImage(loadUrl, photoView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progressBar.setProgress(0);
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progressBar.setVisibility(View.GONE);
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						progressBar.setProgress(Math.round(100.0f * current
								/ total));
					}
				});
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
