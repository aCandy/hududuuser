package com.jizhi.hududu.uclient.main;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.CaiGridAdapter;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.OrderState;
import com.jizhi.hududu.uclient.json.OrderResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.widget.CloseDialog;
import com.jizhi.hududu.uclient.widget.CloseDialog.CallBackClose;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 订单详情
 * @author Xuj
 * @date 2015年8月14日 09:24:53
 */
@SuppressLint("NewApi")
public class MyOrderDetailActivity extends Activity implements OnClickListener,CallBackClose {

	@ViewInject(R.id.service_address)
	private TextView service_address; // 服务地址

	@ViewInject(R.id.project_name)
	private TextView project_name; // 项目名称

	@ViewInject(R.id.allPrice)
	private TextView allPrice; // 总价

	@ViewInject(R.id.c_name)
	private TextView c_name; // 服客姓名

	@ViewInject(R.id.real_pay)
	private TextView real_pay; // 实际付款

	@ViewInject(R.id.receive_time)
	private TextView receive_time; // 完成时间

	@ViewInject(R.id.order_state)
	private RelativeLayout order_state; // 订单状态

	@ViewInject(R.id.relativeLayout_telephone)
	private RelativeLayout relativeLayout_telephone; // 拨打电话
	
	@ViewInject(R.id.relativeLayout_cancel)
	private RelativeLayout relativeLayout_cancel; // 取消订单
	
	

	@ViewInject(R.id.tv_title)
	private TextView title;

	@ViewInject(R.id.unit)
	private TextView unit;

	@ViewInject(R.id.working_hours_unit)
	private TextView working_hours_unit;

	private Order order;

	@ViewInject(R.id.image)
	private ImageView image;

	@ViewInject(R.id.time)
	private TextView time;

	@ViewInject(R.id.gridView)
	private GridView gridView;

	
	private CustomProgress dialog;
	
	
	private OrderResolution resolution = new OrderResolution();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.order_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		initData();

	}

	private void initData() {
		title.setText(getString(R.string.order_detail));
		order = (Order) getIntent().getExtras().getSerializable("order");
		if (order == null) {
			CommonMethod.makeNoticeShort(MyOrderDetailActivity.this, "读取订单失败!");
			finish();
			return;
		}
		service_address.setText(order.getAddress());
		project_name.setText(order.getProject_name());
		c_name.setText(order.getLname());
		unit.setText(order.getUnit());
		real_pay.setText("￥"+ DataForMat.twoDecimalPlaces(Double.parseDouble(order.getRealpay())));
		allPrice.setText("￥"+ order.getWage());
		working_hours_unit.setText(order.getWorking_hours() + ""+ order.getWorking_hours_unit());
		if (!order.getStatus().equals("9")) {
			order_state.setVisibility(View.GONE);
		}

		if (order.getStatus().equals("7")) {
			relativeLayout_cancel.setVisibility(View.VISIBLE);
			receive_time.setText(timeStamp2Date(order.getServer_time() + ""));
			time.setText("服务时间");
		} else {
			relativeLayout_telephone.setVisibility(View.GONE);
			receive_time.setText(timeStamp2Date(order.getReceive_time() + ""));
		}
		relativeLayout_telephone.setOnClickListener(this);
		relativeLayout_cancel.setOnClickListener(this);
		String wtype = order.getWtype();
		// 设置图片
		if (wtype.equals(CMD.HW)) {

			if (order.getDesc().equals("1")) {
				image.setImageResource(R.drawable.zhuanyebaojie);
			} else if (order.getDesc().equals("2")) {
				image.setImageResource(R.drawable.jiandandasao);
			} else if (order.getDesc().equals("3")) {
				image.setImageResource(R.drawable.fanhouxifan);
			}
		} else if (wtype.equals(CMD.WH)) {
			image.setImageResource(R.drawable.shouxiyifu);
		} else if (wtype.equals(CMD.FH)) {
			image.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			gridView.setAdapter(new CaiGridAdapter(this, order.getCaiPic()));
			working_hours_unit.setText(DataForMat.twoDecimalPlaces(order.getWeight())
					+ order.getWorking_hours_unit());
		} else if (wtype.equals(CMD.HD)) {
			working_hours_unit.setText(order.getDishcount()+ order.getWorking_hours_unit());
			ImageLoader.getInstance().displayImage(
					CMD.NETURL + "uploads/" + order.getPic(), image,
					UtilImageLoader.getImageOptionsChef());
		} else {
			ImageLoader.getInstance().displayImage(
					CMD.NETURL + "uploads/" + order.getPic(), image,
					UtilImageLoader.getImageOptionsChef());
		}
	}

	public String timeStamp2Date(String seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new java.util.Date(Long.valueOf(seconds + "000")));
	}

	public void finishAct(View view) {
		finish();
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.relativeLayout_telephone:
			if (order.getLid() == null && order.getLid().equals("")) {
				CommonMethod.makeNoticeShort(MyOrderDetailActivity.this,"当前服客电话号码出错!");
				return;
			}
			// 用intent启动拨打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ order.getLid()));
			startActivity(intent);
			break;
		case R.id.relativeLayout_cancel:
			CloseDialog dialog = new CloseDialog(MyOrderDetailActivity.this,this, "确定取消订单吗？");
			dialog.show();
			break;
		default:
			break;
		}
		

	}
	
	
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid",order.getCid())); //用户id
		params.add(new BasicNameValuePair("oid",order.getOid())); 
		params.add(new BasicNameValuePair("wtype",order.getWtype())); 
		return params;
	}

	
	/**
	 * 取消订单
	 */
	public void cancelOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final OrderState state = resolution.cancelOrder(MyOrderDetailActivity.this,CMD.CANCEL_ORDER, params());
				MyOrderDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(MyOrderDetailActivity.this,"数据解析失败!");
						}else{
							if(state.getState() == 1){
								CommonMethod.makeNoticeShort(MyOrderDetailActivity.this,"取消订单成功!");
								Intent intent = new Intent();
								intent.putExtra("oid",order.getOid());
								setResult(CMD.CANCEL,intent);
								finish();
							}else if(state.getState() == 0){
								CommonMethod.makeNoticeShort(MyOrderDetailActivity.this,state.getError_meesage());
							}
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void callClose(boolean chooser) {
		if(chooser){
			cancelOrder();
		}
	}
}
