package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.CustomEvaluationAdapter;
import com.jizhi.hududu.uclient.bean.CustomDetail;
import com.jizhi.hududu.uclient.bean.Photos;
import com.jizhi.hududu.uclient.bean.ServerDetail;
import com.jizhi.hududu.uclient.bean.ServerDetailState;
import com.jizhi.hududu.uclient.json.ServerDataResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

/**
 * 服客详细资料
 * @author Xuj
 * @date 2015年8月18日 16:45:45
 */
public class FKDetailActivity extends Activity {

	@ViewInject(R.id.head)
	private ImageView head; // 头像

	@ViewInject(R.id.name)
	private TextView name; // 姓名

	@ViewInject(R.id.id)
	private TextView id; // 身份证

	@ViewInject(R.id.distance)
	private TextView distance; // 距离

	@ViewInject(R.id.service_number)
	private TextView service_number; // 服务次数

	@ViewInject(R.id.refreshListView)
	private PullToRefreshListView refreshListView; // 顾客点评

	@ViewInject(R.id.tv_title)
	private TextView tv_title; // 标题
	
	@ViewInject(R.id.ratingbar)
	private RatingBar ratingbar;
	
	private Dialog dialog;
	
	private int pager = 1;
	
	private List<CustomDetail> list = new ArrayList<CustomDetail>();
	
	private ServerDetailState state = new ServerDetailState();
	
	private CustomEvaluationAdapter adapter;
	
	private ServerDataResolution resolution = new ServerDataResolution();
	
	private View load; //ListView底部刷新视图
	
	private boolean isDownData = true;//是否还有上拉数据
	
	private String far;
	
	private String wtype;

	private String lid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fk_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		tv_title.setText(getString(R.string.service_data_detail));
		initData();
		initView();
		search(true);
		// 添加上拉刷新事件读取旧数据
		refreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if(list.size()>=10){
					if(isDownData){
						load.setVisibility(View.VISIBLE);
						search(false);
					}
				}
			}
		});
	}
	
	public void finishAct(View view) {
		finish();
	}
	
	
	public void initData(){
		Intent intent = getIntent();
		wtype = intent.getStringExtra("wtype");
		far = intent.getStringExtra("far");
		lid = intent.getStringExtra("lid");
		load = getLayoutInflater().inflate(R.layout.load_data, null); // 加载对话框
		load.setVisibility(View.GONE);
		refreshListView.getRefreshableView().addFooterView(load);//添加底部显示加载布局
	}
	
	public void initView(){
		adapter = new CustomEvaluationAdapter(this, list);
		refreshListView.setAdapter(adapter);
		distance.setText(far);
	}
	
	
	public List<NameValuePair> params() {
		//"18583961321"
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lid",lid)); //服客id
		params.add(new BasicNameValuePair("wtype",wtype)); //
		params.add(new BasicNameValuePair("pageno",pager+"")); //分页信息
		return params;
	}
	
	/**
	 * 查询服客详情资料
	 */
	public void search(final boolean isFirst) {
		if(isFirst){
			if (dialog != null) {
				dialog.dismiss();
			}
			dialog = CustomProgress.show(FKDetailActivity.this,getString(R.string.seaching_fk), true, null);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				state = resolution.search(FKDetailActivity.this,CMD.SEARCH_SERVER_DETAIL, params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(FKDetailActivity.this,"查询失败!:");
							return;
						}
						if (state.getState() == 1) {
							if(state.getResp()!=null){
								if(state.getResp().getCustomer()!=null && state.getResp().getCustomer().size()>0){
									list.addAll(state.getResp().getCustomer());
									handler.sendEmptyMessage(1);
									pager++;
								}
								if(state.getResp().getCustomer()!=null && state.getResp().getCustomer().size()==0){
									isDownData = false;
								}
								if(isFirst){
									name.setText(state.getResp().getName());
									id.setText(state.getResp().getIcno());
									service_number.setText(state.getResp().getRaisecount()+" 次");
									if(state.getResp().getAvgrate()!=null && !"".equals(state.getResp().getAvgrate())){
										ratingbar.setRating((int)(Float.parseFloat(state.getResp().getAvgrate())));
									}else{
										ratingbar.setRating(5);
									}
									ImageLoader.getInstance().displayImage(CMD.PICPATH+state.getResp().getPic(),head,UtilImageLoader.getImageOptionsLoginHead());
								}
							}else{
								isDownData = false;
							}

						} else if(state.getState() == 0) {
							isDownData = false;
						}
						if(dialog!=null){
							dialog.dismiss();
						}
						load.setVisibility(View.GONE);
					}
				});
			}
		}).start();
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:
				break;
			}
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	};

}
