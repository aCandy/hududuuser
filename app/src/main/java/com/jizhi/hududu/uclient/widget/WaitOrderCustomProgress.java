package com.jizhi.hududu.uclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.TimesUtils;
import com.jizhi.hududu.uclient.bean.OrderhMFkInfo;
import com.neusoft.huduoduoapp.R;

public class WaitOrderCustomProgress extends Dialog {
	public static WaitOrderCustomProgress dialog;
	private static TextView tv_time;
	private static TextView tv_hint;

	public WaitOrderCustomProgress(Context context) {
		super(context);
	}

	public WaitOrderCustomProgress(Context context, int theme) {
		super(context, theme);
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
	public synchronized static WaitOrderCustomProgress show(Context context,
			boolean cancelable,
			android.view.View.OnClickListener onClickListener, String hint) {
		dialog = new WaitOrderCustomProgress(context, R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.layout_order_wait);
		// 设置点击屏幕Dialog不消失
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(cancelable);// 设置为false，按返回键不能退出。默认为true。
		// // 监听返回键处理
		// dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.1f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		tv_time = (TextView) dialog.findViewById(R.id.tv_time);
		tv_hint = (TextView) dialog.findViewById(R.id.tv_hint);
		timer.start();
		if (!hint.equals("")) {
			tv_hint.setVisibility(View.VISIBLE);
			tv_hint.setText(hint);
		} else {
			tv_hint.setVisibility(View.GONE);
		}
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(onClickListener);
		return dialog;
	}

	public synchronized static void dissmiss() {
		if (null != timer) {
			timer.cancel();
		}
		if (null != dialog) {
			dialog.dismiss();
		}
	}

	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.spinner);
		// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) imageView
				.getBackground();
		// 开始动画
		spinner.start();
	}

	private static CountDownTimer timer = new CountDownTimer(180000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			String time = TimesUtils
					.dataFormatMinAndSecond((millisUntilFinished / 1000));
			tv_time.setText(time);
		}

		@Override
		public void onFinish() {

			dissmiss();
		}
	};
}
