package com.jizhi.hududu.uclient.application;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * application
 * 
 * @author huChangSheng
 * @time 2015-7-28 下午3:22:43
 * @version 1.0
 * 
 */
public class UclientApplication extends Application {
	public static UclientApplication instance;
	
	//public static ImageLoader imageLoader;

	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(UclientApplication.this);
		context = getApplicationContext();
		instance = this;

		File cacheDir = StorageUtils.getOwnCacheDirectory(
				context, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		//context = getApplicationContext();
	}
	
	/*public static ImageLoader getImageLoader() {
		return imageLoader;
	}*/
	
}
