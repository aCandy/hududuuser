package com.jizhi.hududu.uclient.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.LeftBean;
import com.jizhi.hududu.uclient.fragment.MainFragment;
import com.jizhi.hududu.uclient.fragment.MenuFragment;
import com.jizhi.hududu.uclient.main.AboutActivity;
import com.jizhi.hududu.uclient.main.FeedBackActivity;
import com.jizhi.hududu.uclient.main.LoadUrlActivity;
import com.jizhi.hududu.uclient.main.MainActivity;
import com.jizhi.hududu.uclient.main.MyCardBagActivity;
import com.jizhi.hududu.uclient.main.MyOrderActivity;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CloseDialog;
import com.jizhi.hududu.uclient.widget.CloseDialog.CallBackClose;
import com.neusoft.huduoduoapp.R;
/**
 * Slidmenu 菜单
 * @author Xuj
 * @date 2015年9月9日 12:03:10
 */
public class LeftAdapter extends BaseAdapter implements CallBackClose{

	private MainActivity context;
	private List<LeftBean> list;
	private LayoutInflater inflater;

	
	public LeftAdapter(MainActivity context, List<LeftBean> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		final LeftViewHolder holder;
		LeftBean bean = list.get(position);
		if (convertView == null) {
			holder = new LeftViewHolder();
			convertView = inflater.inflate(R.layout.left_item, null);
			holder.menu_text = (TextView) convertView.findViewById(R.id.menu_texts);
			holder.menu_image = (ImageView) convertView.findViewById(R.id.menu_images);
			holder.viewline = (View) convertView.findViewById(R.id.viewline);
			convertView.setTag(holder);
		} else {
			holder = (LeftViewHolder) convertView.getTag();
		}
		
		convertView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					holder.viewline.setVisibility(View.VISIBLE);
					v.setBackground(context.getResources().getDrawable(R.color.blue_0_4));
				}else if(event.getAction() == MotionEvent.ACTION_CANCEL){
					holder.viewline.setVisibility(View.GONE);
					v.setBackground(context.getResources().getDrawable(android.R.color.transparent));
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					holder.viewline.setVisibility(View.GONE);
					v.setBackground(context.getResources().getDrawable(android.R.color.transparent));
				}
				return false;
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intentPa(position);
			}
		});
		
		holder.menu_image.setImageDrawable(bean.getMenu_image());
		holder.menu_text.setText(bean.getMenu_text());
		return convertView;
	}

	public class LeftViewHolder {
		TextView menu_text;
		ImageView menu_image;
		View viewline;
	}
	
	public void intentPa(int position) {
		Intent intent = new Intent();
		switch (position) {
		case 0: // 我的订单
			intent.setClass(context, MyOrderActivity.class);
			context.startActivity(intent);
			break;
		case 1: //我的卡包
			intent.setClass(context, MyCardBagActivity.class);
			context.startActivity(intent);
			break;
		case 2: // 推荐有奖
			intent.setClass(context, LoadUrlActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", 3);
			intent.putExtras(bundle);
			context.startActivity(intent);
			break;
		case 3: // 服客招募
			intent.setClass(context, LoadUrlActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("type", 5);
			intent.putExtras(bundle1);
			context.startActivity(intent);
			break;
		case 4: // 关于我们
			intent.setClass(context, AboutActivity.class);
			context.startActivity(intent);
			break;
		case 5: // 意见反馈
			intent.setClass(context, FeedBackActivity.class);
			context.startActivity(intent);
			break;
		case 6: // 使用帮助
			intent.setClass(context, LoadUrlActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putInt("type", 4);
			intent.putExtras(bundle2);
			context.startActivity(intent);
			break;
		case 7: // 退出账户
			CloseDialog dialog = new CloseDialog(context,this,"确定退出登录账户吗?");
			dialog.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void callClose(boolean chooser) {
		if (chooser) {
			SPUtils.clear(context, Constance.HUDUDUUSER);
			CommonMethod.makeNoticeShort(context, "退出成功!");
			context.toggle();
			((MainFragment) context.getMain_fragment()).getImg_head().setImageResource(R.drawable.user_head);
			((MenuFragment) context.getMenu_fragment()).getHead().setImageResource(R.drawable.user_head);
		} 
	}
}