package com.jizhi.hududu.uclient.widget;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.VegetableOrderAdapter;
import com.jizhi.hududu.uclient.bean.OrderhMFkInfo;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.neusoft.huduoduoapp.R;

public class VegetablesOrderCustomProgress extends Dialog {
	public static VegetablesOrderCustomProgress dialog;

	public VegetablesOrderCustomProgress(Context context) {
		super(context);
	}

	public VegetablesOrderCustomProgress(Context context, int theme) {
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
	public synchronized static VegetablesOrderCustomProgress show(
			Context context, boolean cancelable,  List<VegetableOrderhFkInfo> fkInfo,
			android.view.View.OnClickListener onClickListener,Handler handler) {
		dialog = new VegetablesOrderCustomProgress(context,
				R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.listview);
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
		ListView listview = (ListView) dialog.findViewById(R.id.listview);
		listview.setAdapter(new VegetableOrderAdapter(context, fkInfo,handler));
		return dialog;
	}

	public synchronized static void dissmiss() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}

}
