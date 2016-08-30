package com.hcs.hududu.uclient.utils;
import android.graphics.Bitmap;

import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

//package com.hcs.hududu.uclient.utils;
//
//import android.graphics.Bitmap;
//
//import com.jizhi.hukeaide.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
//
public class UtilImageLoader {
	//
	 /*public DisplayImageOptions getFoodOptions() {
	 DisplayImageOptions options = new DisplayImageOptions.Builder()
	 .showImageForEmptyUri(R.drawable.icon_food_fail)
	 .showImageOnFail(R.drawable.icon_food_fail)
	 .cacheOnDisc(true).cacheInMemory(true)
	 .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
	 .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
	  .imageScaleType(ImageScaleType.EXACTLY) // default
	 .build();
	 return options;*/
	// }

	public static DisplayImageOptions RoundedOptionsRound(int angle) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(angle)).build();
		return options;
	}
	
	public static DisplayImageOptions getImageOptions(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	/**
	 * 加载菜品 默认图片
	 * @return
	 */
	public static DisplayImageOptions getImageOptionsFood(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.food_default)
		.showImageForEmptyUri(R.drawable.food_default)
		.showImageOnFail(R.drawable.food_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	
	/**
	 * 加载厨师默认图片
	 * @return
	 */
	public static DisplayImageOptions getImageOptionsChef(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.chefs_default)
		.showImageForEmptyUri(R.drawable.chefs_default)
		.showImageOnFail(R.drawable.chefs_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	
	/**
	 * 加载登录头像默认图片
	 * @return
	 */
	public static DisplayImageOptions getImageOptionsLoginHead(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.user_head)
		.showImageForEmptyUri(R.drawable.user_head)
		.showImageOnFail(R.drawable.user_head)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
}
