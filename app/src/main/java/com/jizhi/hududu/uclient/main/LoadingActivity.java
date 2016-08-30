package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.application.UclientApplication;
import com.jizhi.hududu.uclient.json.ParseHttpData;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.UtilConn;
import com.neusoft.huduoduoapp.R;

public class LoadingActivity extends Activity {
	private ApplicationInfo info;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading);
		ImageView loading = (ImageView) findViewById(R.id.loading);
		/** 设置透明度渐变动画 */
		final AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
		animation.setDuration(2500);// 设置动画持续时间
		try {
			info = getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			Object msg = info.metaData.get("channelid");
			// huawei_10华为
			if (msg.toString().equals("huawei_11")) {
					loading.setImageResource(R.drawable.huawei);
				// wandoujia_04豌豆荚
			} else if (msg.toString().equals("wandoujia_04")) {
					loading.setImageResource(R.drawable.wandoujia);
			} else {
				    loading.setImageResource(R.drawable.loading);
			}
		} catch (Exception e) {
			loading.setImageResource(R.drawable.loading);
			e.printStackTrace();
		}
		/** 常用方法 */
		// animation.setRepeatCount(int repeatCount);//设置重复次数
		// animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态
		// animation.setStartOffset(long startOffset);//执行前的等待时间
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				boolean is_first = (Boolean) SPUtils.get(LoadingActivity.this,"is_first", false, Constance.HUDUDUUSER);
				if (!is_first) {
					// 渐变动画结束后，执行此方法，跳转到主界面
					Intent intent = new Intent
						(LoadingActivity.this,GuideActivity.class);
					startActivity(intent);
					finish();
				} else {
					// 渐变动画结束后，执行此方法，跳转到主界面
					Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});
		loading.setAnimation(animation);
		animation.start();

	}

}
