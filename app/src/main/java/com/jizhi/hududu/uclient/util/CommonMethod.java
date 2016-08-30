package com.jizhi.hududu.uclient.util;


import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.huduoduoapp.R;

public class CommonMethod {
	private static int timPos = 3;

	/**
	 * toast
	 * 
	 * @param context
	 * @param err
	 */
	public static void makeNoticeLong(Context context, String err) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.toast_custom_layout, null);
		TextView title = (TextView) layout.findViewById(R.id.toast_custom_tv);
		title.setText(err);
		Toast t = new Toast(context);
		t.setGravity(Gravity.BOTTOM, 0, 110);
		t.setView(layout);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}

	/**
	 * toast
	 * 
	 * @param context
	 * @param err
	 */
	public static void makeNoticeShort(Context context, String err) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.toast_custom_layout, null);
		TextView title = (TextView) layout.findViewById(R.id.toast_custom_tv);
		title.setText(err);
		Toast t = new Toast(context);
		t.setGravity(Gravity.BOTTOM, 0, 110);
		t.setView(layout);
		t.setDuration(Toast.LENGTH_SHORT);
		timer.start();
		if (timPos == 3) {
			t.show();
		}
	}

	private static CountDownTimer timer = new CountDownTimer(3000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			timPos -= 1;
		}

		@Override
		public void onFinish() {
			timPos = 3;
		}
	};
}
