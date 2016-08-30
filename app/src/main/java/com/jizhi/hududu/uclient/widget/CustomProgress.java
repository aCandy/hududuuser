package com.jizhi.hududu.uclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.huduoduoapp.R;
/**
 * 订单确认dialog*/

public class CustomProgress extends Dialog {
	public static CustomProgress dialog;
	public static TextView txt;

	public CustomProgress(Context context) {
		super(context);
	}

	public CustomProgress(Context context, int theme) {
		super(context, theme);
	}

	
	
	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
		// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
		// 开始动画
		spinner.start();
	}

	/**
	 * 给Dialog设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(CharSequence message) {
		if (message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);
			TextView txt = (TextView) findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}

	/**
	 * 弹出自定义ProgressDialog
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            提示
	 * @param cancelable
	 *            是否按返回键取消
	 * @param cancelListener
	 *            按下返回键监听
	 * @return
	 */
	public synchronized static CustomProgress show(Context context, CharSequence message,
			boolean cancelable, OnCancelListener cancelListener) {
		dialog = new CustomProgress(context,R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_custom);
		if (message == null || message.length() == 0) {
			dialog.findViewById(R.id.message).setVisibility(View.GONE);
		} else {
			txt = (TextView) dialog.findViewById(R.id.message);
			txt.setText(message);
		}
		// 设置点击屏幕Dialog不消失
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(cancelable);//设置为false，按返回键不能退出。默认为true。
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.1f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}

	public synchronized static void dissmiss() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}

	public static void setMessage(String message) {
		if (null != txt) {
			txt.setText(message);
		}
	}
}