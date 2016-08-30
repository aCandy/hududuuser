package com.jizhi.hududu.uclient.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimUtil {
	public static void AnimTarget(Object btn_target) {
		Animation translateIn = new TranslateAnimation(0, 0, -15, 0);
		translateIn.setDuration(300);
		translateIn.setFillAfter(false);
		((View) btn_target).startAnimation(translateIn);
	}
	
	public static void AnimfkInfo(Object btn_target) {
		Animation translateIn = new TranslateAnimation(0, 0, -300, 0);
		translateIn.setDuration(500);
		translateIn.setFillAfter(false);
		((View) btn_target).startAnimation(translateIn);
	}
}
