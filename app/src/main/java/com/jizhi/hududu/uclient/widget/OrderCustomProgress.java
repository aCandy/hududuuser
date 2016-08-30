package com.jizhi.hududu.uclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.jizhi.hududu.uclient.main.FKDetailActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 自定义dialog*/
public class OrderCustomProgress extends Dialog {
	public static OrderCustomProgress dialog;

	
	
	public OrderCustomProgress(Context context) {
		super(context);
	}

	public OrderCustomProgress(Context context, int theme) {
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
	public synchronized static OrderCustomProgress show(final Context context,
			boolean cancelable,final VegetableOrderhFkInfo fkInfo,
			final String wtype,android.view.View.OnClickListener onClickListener) {
		dialog = new OrderCustomProgress(context, R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.layout_fukeinfos);
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

		CircleImageView img_fkhead = (CircleImageView) dialog.findViewById(R.id.img_fkhead);
		
		((TextView) dialog.findViewById(R.id.tv_fkname)).setText(fkInfo
				.getName());
		((TextView) dialog.findViewById(R.id.tv_fkage)).setText(fkInfo.getAge()
				+ "岁");
		((TextView) dialog.findViewById(R.id.tv_fknum)).setText(fkInfo
				.getIcno());
		((TextView) dialog.findViewById(R.id.tv_fkordercount)).setText(fkInfo
				.getOrdertotal() + "");
		((TextView)dialog.findViewById(R.id.tv_fkfar)).setText(fkInfo.getDistance()+"米");
		ImageLoader.getInstance().displayImage(CMD.NETURL+"uploads/"+fkInfo.getPic(),img_fkhead,UtilImageLoader.getImageOptionsChef());
		RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.ratingbar);
		int rat = (int) fkInfo.getAvgrate();
		if (rat >= 5 || rat <= 0) {
			rat = 5;
		}
		ratingbar.setRating(rat);
		Button findvButton = (Button) dialog.findViewById(R.id.btn_toPay);
		findvButton.setOnClickListener(onClickListener);
		img_fkhead.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,FKDetailActivity.class);
				intent.putExtra("wtype",wtype);
				intent.putExtra("far",fkInfo.getDistance()+" 米");
				intent.putExtra("lid",fkInfo.getLid());
				context.startActivity(intent);
			}
		});
		return dialog;
	}

	public synchronized static void dissmiss() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}

}
