package com.jizhi.hududu.uclient.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.OrderState;
import com.jizhi.hududu.uclient.json.OrderResolution;
import com.jizhi.hududu.uclient.main.MyOrderDetailActivity;
import com.jizhi.hududu.uclient.main.MyOrderEvaluate;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的订单Adapter
 * @author Xuj
 * @date 2015年8月26日 13:04:29
 */
@SuppressLint("NewApi")
public class MyOrderAdapter extends BaseAdapter {

	private Activity context;
	private List<Order> list;
	private LayoutInflater inflater;
	private OrderResolution resolution;
	private CustomProgress dialog;
	

	public MyOrderAdapter(Activity context, List<Order> list,OrderResolution resolution) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.resolution = resolution;
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
		final Order bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_order_item, null);
			holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
			holder.product_image = (ImageView) convertView.findViewById(R.id.product_image);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.unit = (TextView) convertView.findViewById(R.id.unit);
			holder.unit_number = (TextView) convertView.findViewById(R.id.unit_number);
			holder.reviews = (TextView) convertView.findViewById(R.id.reviews);
			holder.go = (Button) convertView.findViewById(R.id.go);
			holder.grid = (GridView) convertView.findViewById(R.id.gridView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.grid.setVisibility(View.GONE);
		holder.product_image.setVisibility(View.VISIBLE);
		String wtype = bean.getWtype();
		String status = bean.getStatus();
		// 设置付款状态
		if (status.equals("7")) {
			holder.reviews.setText("待确认");
			holder.reviews.setTextColor(context.getResources().getColor(R.color.or));
			holder.go.setText("确认完成");
			holder.go.setBackground(context.getResources().getDrawable(R.drawable.yuan_shape3));
			holder.go.setVisibility(View.VISIBLE);
		} else if (status.equals("8")) {
			holder.reviews.setText("待评价");
			holder.reviews.setTextColor(context.getResources().getColor(R.color.or));
			holder.go.setText("去评价");
			holder.go.setBackground(context.getResources().getDrawable(R.drawable.yuan_shape1));
			holder.go.setVisibility(View.VISIBLE);
		} else if (status.equals("9")) {
			holder.reviews.setText("已完成");
			holder.reviews.setTextColor(context.getResources().getColor(R.color.color_bar));
			holder.go.setVisibility(View.GONE);
		}
		holder.go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(bean.getStatus().equals("7")){
					submitStatus7(bean,holder.reviews,holder.go);
				}else if(bean.getStatus().equals("8")){
					Intent intent = new Intent(context,MyOrderEvaluate.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("order",bean);
					intent.putExtras(bundle);
					context.startActivityForResult(intent,5);
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context,MyOrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("order",bean);
				intent.putExtras(bundle);
				context.startActivityForResult(intent,5);
			}
		});
		holder.product_name.setText(bean.getProject_name());
		holder.unit.setText(bean.getUnit());
		if(wtype.equals(CMD.FH)){
			holder.unit_number.setText(DataForMat.twoDecimalPlaces(bean.getWeight()) + bean.getWorking_hours_unit());
		}else{
			holder.unit_number.setText(bean.getWorking_hours() + bean.getWorking_hours_unit());
		}
		// 设置图片
		if (wtype.equals(CMD.HW)) {
			if (bean.getDesc().equals("1")) {
				holder.product_image.setImageResource(R.drawable.zhuanyebaojie);
			} else if (bean.getDesc().equals("2")) {
				holder.product_image.setImageResource(R.drawable.jiandandasao);
			} else if (bean.getDesc().equals("3")) {
				holder.product_image.setImageResource(R.drawable.fanhouxifan);
			}
		}else if(wtype.equals(CMD.WH)){
			holder.product_image.setImageResource(R.drawable.shouxiyifu);
		}else if(wtype.equals(CMD.FH)){
			holder.grid.setVisibility(View.VISIBLE);
			holder.grid.setAdapter(new CaiGridAdapter(context,bean.getCaiPic()));
			holder.product_image.setVisibility(View.GONE);
		}else if(wtype.equals(CMD.HD)){
			holder.unit_number.setText(bean.getDishcount()+bean.getWorking_hours_unit());
			ImageLoader.getInstance().displayImage(CMD.NETURL + "uploads/" + bean.getPic(),holder.product_image,UtilImageLoader.getImageOptionsChef());
		}
		else{
			ImageLoader.getInstance().displayImage(CMD.NETURL + "uploads/" + bean.getPic(),holder.product_image,UtilImageLoader.getImageOptionsChef());
		}
		holder.price.setText("￥"+bean.getWage());
		return convertView;
	}

	class ViewHolder {
		TextView product_name;
		ImageView product_image;
		TextView price;
		TextView unit;
		TextView unit_number;
		TextView reviews;
		Button go;
		GridView grid;
	}
	
	
	/**
	 * 提交确认订单
	 */
	public void submitStatus7(final Order bean,final TextView reviews,final Button go) {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(context,context.getString(R.string.sumiting), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final OrderState temp = resolution.submitStatus7(context,CMD.SUBMIT_STATUE_7, params(bean));
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (temp!=null && temp.getState() == 1) {
							reviews.setText("已确认");
							go.setText("去评价");
							go.setBackground(context.getResources().getDrawable(R.drawable.yuan_shape1));
							bean.setStatus("8");
							bean.setReceive_time(temp.getReceive_time());
							Intent intent = new Intent(context,MyOrderEvaluate.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("order",bean);
							intent.putExtras(bundle);
							context.startActivityForResult(intent,CMD.SUCCESS);
						} else {
							CommonMethod.makeNoticeShort(context, "确认失败!");
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
	}
	
	
	public List<NameValuePair> params(Order bean) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid",bean.getCid())); 
		params.add(new BasicNameValuePair("wtype",bean.getWtype())); 
		params.add(new BasicNameValuePair("oid",bean.getOid()));
		return params;
	}
	
	
}