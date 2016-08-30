package com.jizhi.hududu.uclient.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.main.BuyVegetablesViewPagerActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

/**
 * 帮我买菜 GridView Adapter
 * 
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class BuyVegetablesGridAdapter extends BaseAdapter {
	private BuyVegetablesViewPagerActivity context;
	private List<BuyVegetables> list;
	private LayoutInflater inflater;
	private CallBackFee call;

	private RelativeLayout relativeLayout;

	private ImageView buyImg;// 这是在界面上跑的小图片
	
	private float dpi;

	public BuyVegetablesGridAdapter(BuyVegetablesViewPagerActivity context,
			List<BuyVegetables> list, RelativeLayout relativeLayout,
			float dpi,CallBackFee call) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.call = call;
		this.relativeLayout = relativeLayout;
		this.dpi = dpi;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.buy_vegettables_grid_item,
					null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic111);
			holder.fee = (TextView) convertView.findViewById(R.id.fee);
			holder.choose = (Button) convertView.findViewById(R.id.choose);
			holder.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.relativeLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final View tempView = convertView;
		final BuyVegetables bean = list.get(position);
		holder.relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
				holder.pic.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
				buyImg = new ImageView(context);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
				ImageLoader.getInstance().displayImage(
						CMD.NETURL + "uploads/fresh_img/" + bean.getPic(),
						buyImg, UtilImageLoader.getImageOptions());
				setAnim(buyImg, start_location, tempView);// 开始执行动画
				bean.setAllNumber(bean.getAllNumber() + 1);
				bean.setAllPrice(Double.parseDouble(DataForMat.twoDecimalPlaces(bean.getAllPrice() + bean.getFee())));
				holder.choose.setText("已选" + bean.getAllNumber());
				holder.choose.setVisibility(View.VISIBLE);
				call.callFee(bean);
			}
		});
		if (bean.getAllNumber() > 0) {
			holder.choose.setText("已选" + bean.getAllNumber());
			holder.choose.setVisibility(View.VISIBLE);
		} else {
			holder.choose.setVisibility(View.INVISIBLE);
		}
		holder.name.setText(bean.getName());
		holder.fee.setText("指导价格:￥" + bean.getFee() + "/斤");
		// 加载图片
		ImageLoader.getInstance().displayImage(
				CMD.NETURL + "uploads/fresh_img/" + bean.getPic(), holder.pic,
				UtilImageLoader.getImageOptions());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		ImageView pic;
		TextView fee;
		Button choose;
		RelativeLayout relativeLayout;
	}

	public interface CallBackFee {
		public void callFee(BuyVegetables bean);
	}

	@SuppressLint("NewApi")
	private void setAnim(final View v, int[] start_location, View imageView) {
		final ViewGroup anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(v, start_location);
		int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		relativeLayout.getLocationInWindow(end_location);// shopCart是那个购物车
		// 计算位移
		int endX = (int)(end_location[0] - start_location[0] + (dpi*10));
		int endY = (int)(end_location[1] - start_location[1] + (dpi*5));// 动画位移的y坐标

		final ScaleAnimation animation = new ScaleAnimation(1.2f, 0.2f, 1.2f,
				0.2f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, endY);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setFillAfter(true);

//		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
//				0, endY);
//		translateAnimationY.setInterpolator(new AccelerateInterpolator());
//		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(animation);
//		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(900);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				anim_mask_layout.removeView(v);
				anim_mask_layout.setVisibility(View.GONE);
			}
		});

	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(final View view, int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

}