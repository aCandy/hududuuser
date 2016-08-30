package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.adapter.MyCardBagAdapter;
import com.jizhi.hududu.uclient.bean.CashCouponState;
import com.jizhi.hududu.uclient.bean.MyCardBag;
import com.jizhi.hududu.uclient.bean.MyCardBagState;
import com.jizhi.hududu.uclient.json.MyCardBagResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.umeng.socialize.utils.Log;


/**
 * 我的卡包
 * @author Xuj
 * @date 2015年8月30日 11:18:34
 */
@SuppressLint("NewApi")
public class MyCardBagActivity extends Activity implements OnClickListener{

	@ViewInject(R.id.listView)
	private ListView listView;
	
	@ViewInject(R.id.use_button)
	private Button use_button;
	
	@ViewInject(R.id.add)
	private TextView add;
	
	@ViewInject(R.id.overdue_date)
	private RelativeLayout overdue_date;
	
	private MyCardBagAdapter adapter;
	
	private List<MyCardBag> list;
	
	private Dialog dialog;
	
	private MyCardBagResolution resolution = new MyCardBagResolution();
	
	
	private float dpi;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_card_bag);
		ViewUtils.inject(this); //Xutil必须调用的一句话
		initView();
		search(false);
		
	}

	public void finishAct(View view) {
		finish();
	}
	
	public void initView(){
		list = new ArrayList<MyCardBag>();
		adapter = new MyCardBagAdapter(MyCardBagActivity.this, list);
		listView.setAdapter(adapter);
		add.setOnClickListener(this);
		use_button.setOnClickListener(this);
		overdue_date.setOnClickListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		dpi = dm.density;
		
		overdue_date.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					overdue_date.setAlpha(0.3f);
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					overdue_date.setAlpha(1.0f);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					overdue_date.setAlpha(1.0f);
				}
				return false;
			}

		});
	}
	
	
	
	
	public List<NameValuePair> params(String searchOrAdd,String uniqueid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(searchOrAdd.equals("add")){
			params.add(new BasicNameValuePair("option","addOneCashCoupon")); 
			params.add(new BasicNameValuePair("modetype","cash")); 
			params.add(new BasicNameValuePair("uniqueid",uniqueid)); 
		}else{
			params.add(new BasicNameValuePair("option","showcoupons"));
			params.add(new BasicNameValuePair("status","1"));
		}
		params.add(new BasicNameValuePair("cid",(String) SPUtils.get(this,"mobile", "", Constance.HUDUDUUSER))); //
		return params;
	}
	
	/**
	 * 查询我的优惠券
	 */
	public void search(boolean isAdding) {
		if (dialog != null) {
			dialog.dismiss();
		}
		if(!isAdding){
			dialog = CustomProgress.show(MyCardBagActivity.this,getString(R.string.seaching_my_card_bag), true, null);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				final MyCardBagState state = resolution.resolution(MyCardBagActivity.this,CMD.SEARCH_CASH_COUPON, params("search",null));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state.getState() == 1) {
							if(state.getResp()!=null){
								if(state.getResp()!=null && state.getResp().size()>0){
									if(list.size()>0){
										list.clear();
									}
									list.addAll(state.getResp());
									adapter.notifyDataSetChanged();
								}
							}
						}
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				});
			}
		}).start();
	}
	
	
	
	/**
	 * 添加优惠券
	 */
	public void addCashCoupon(final String uniqueid) {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(MyCardBagActivity.this,getString(R.string.adding), true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final CashCouponState state = resolution.add(MyCardBagActivity.this,CMD.SEARCH_CASH_COUPON,params("add",uniqueid));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state.getState() == 1) {
							search(true);
						}else{
							CommonMethod.makeNoticeLong(MyCardBagActivity.this,state.getErrormsg());
						}
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				});
			}
		}).start();
	}
	


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.add:
			
			final EditText editText = new EditText(this);
			editText.setHint("请输入优惠券");
			editText.setHintTextColor(getResources().getColor(R.color.add));
			editText.setTextSize(5*dpi);
			editText.setBackground(getResources().getDrawable(R.drawable.background_white_line1));
			
			LinearLayout title_layout = new LinearLayout(this);  
			title_layout.setGravity(Gravity.CENTER);
			
			TextView title = new TextView(this);
			title.setText("添加优惠券");
			title.setTextSize(7*dpi);
			title.setTextColor(getResources().getColor(R.color.add));
			
			LinearLayout.LayoutParams title_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			title_params.topMargin = (int)(dpi*15);
			title_params.bottomMargin = (int)(dpi*15);
			title_layout.addView(title,title_params);
			editText.setLayoutParams(title_params);
			
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			lp1.leftMargin = (int)(dpi*10);
			lp1.rightMargin = (int)(dpi*10);
			lp1.topMargin = (int)(dpi*15);
			LinearLayout message_layout = new LinearLayout(this);  
			message_layout.addView(editText,lp1);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MyCardBagActivity.this,android.R.style.Theme_Holo_Light_Dialog));
			AlertDialog alertDialog =
			        builder//给builder set各种属性值
			            .setTitle(getString(R.string.add_coupon))
			            .setCustomTitle(title_layout)
			            .setView(message_layout)
			            .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {//确定按钮
			                @Override
			                public void onClick(DialogInterface dialog, int which) {
			                	if(!TextUtils.isEmpty(editText.getText().toString())){
			                		addCashCoupon(editText.getText().toString());
			                		dialog.dismiss();
			                	}else{
			                		
			                	}
			                }
			            })
			            .setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {//取消按钮
			                @Override
			                public void onClick(DialogInterface dialog, int which) {
			                    dialog.dismiss();
			                }
			            })
			            .create();//创建AlertDialog对象
			alertDialog.show();
			break;
		case R.id.use_button:
			Intent intent = new Intent(MyCardBagActivity.this,LoadUrlActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type",9);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.overdue_date:
			Intent intent1 = new Intent(MyCardBagActivity.this,OverdueMyCardBagActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putInt("type",9);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
	
	
	

	
	
	
	

}
