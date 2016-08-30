package com.jizhi.hududu.uclient.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.adapter.MyOrderAdapter;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.OrderState;
import com.jizhi.hududu.uclient.json.OrderResolution;
import com.jizhi.hududu.uclient.main.MyOrderActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.neusoft.huduoduoapp.R;

/**
 * 我的订单Fragment
 * @author Xuj
 * @date 2015年8月13日 16:07:36
 */
@SuppressLint("ValidFragment")
public class OrderFragments extends Fragment {
	
	
	
	private MyOrderActivity mainac;
	
	private PullToRefreshListView refreshListView;// 下拉刷新组件
	
	private MyOrderAdapter adapter;
	
	private List<Order> list;
	
	private OrderResolution resolution = new OrderResolution();
	
	private OrderState state;
	
	private String wtype; //业务参数
	
	private String status; //状态
	
	private int pager = 1;
	
	private View load; //ListView底部刷新视图
	
	private View head_view; //头部
	
	private boolean isFirst = true;
	
	private boolean isDownData = true;//是否还有上拉数据
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
        	if(isFirst){
        		 flushData();
                 isFirst = false;
        	}
        }
        else if (isVisibleToUser == false) { 
            
        }
    }
	
	public OrderFragments(String wtype,String status){
		this.wtype = wtype;
		this.status = status;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainac = (MyOrderActivity) activity;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.my_order_listview, container,false);
		initView(v);
		return v;
	}
	
	public void flushData(){
		new Handler().postDelayed(new Runnable() { //自动刷新数据
			@Override
			public void run() {
				refreshListView.setRefreshing();
			}
		}, 600);
	}
	
	
	
	
	public void initView(View v){
		list = new ArrayList<Order>();
		refreshListView = (PullToRefreshListView) v.findViewById(R.id.refreshListView);
		if(adapter==null)
			adapter =  new MyOrderAdapter(mainac,list,resolution);
		refreshListView.setAdapter(adapter);
		head_view = mainac.getLayoutInflater().inflate(R.layout.order_head, null); //头部对话框
		refreshListView.getRefreshableView().addHeaderView(head_view);
		load = mainac.getLayoutInflater().inflate(R.layout.load_data, null); // 加载对话框
		load.setVisibility(View.GONE);
		refreshListView.getRefreshableView().addFooterView(load);//添加底部显示加载布局
		// 添加下拉刷新事件读取最新数据
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(mainac, System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				searchUpOrder();
			}
		});
		// 添加上拉刷新事件读取旧数据
		refreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if(list.size()>=10){
					if(isDownData){
						load.setVisibility(View.VISIBLE);
						searchDownOrder();
					}
				}
			}
		});
	}
	
	
	/**
	 * 查询最新订单
	 */
	public void searchUpOrder() {
		final int oldPager = pager;
		pager = 1;
		new Thread(new Runnable() {
			@Override
			public void run() {
				state = resolution.resolution(mainac,CMD.SEARCH_MY_ORDER, params());
				mainac.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(mainac,"数据解析失败!");
							pager = oldPager;
						}else{
							if (state.getState() == 1) {
								if(state.getResp()!=null && state.getResp().size()>0){
									list.clear();
									list.addAll(0,state.getResp());
									adapter.notifyDataSetChanged();
									pager++;
									isDownData = true;
									if(head_view!=null){
										refreshListView.getRefreshableView().removeHeaderView(head_view);
									}
								}
							} else {
								isDownData = false;
							}
						}
						refreshListView.onRefreshComplete();// 关闭下拉刷新提示框
					}
				});
			}
		}).start();
	}
	
	
	
	
	/**
	 * 查询缓存订单
	 */
	public void searchDownOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				state = resolution.resolution(mainac,CMD.SEARCH_MY_ORDER, params());
				mainac.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(mainac,"数据解析失败!");
						}else{
							if (state.getState() == 1) {
								if(state.getResp()!=null && state.getResp().size()>0){
									list.addAll(state.getResp());
									adapter.notifyDataSetChanged();
									pager++;
								}else if(state.getResp().size()==0){
									isDownData = false;
								}
							}else if(state.getState() == 0){
								isDownData = false;
							}
						}
						load.setVisibility(View.GONE);
					}
				});
			}
		}).start();
	}
	
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid",(String)SPUtils.get(mainac,"mobile","",Constance.HUDUDUUSER))); //用户id
		if(!wtype.equals("0")){
			params.add(new BasicNameValuePair("wtype",wtype)); //模块类型不传（显示全部）    (FH-->买菜         HW-->保洁      HD-->吃饭      WH-->洗衣     JS-->急事)
		}
		if(null!=status){
			params.add(new BasicNameValuePair("status",status)); 
		}
			params.add(new BasicNameValuePair("page",pager+""));
		return params;
	}
	
	
	public List<Order> getList() {
		return list;
	}


	public void setList(List<Order> list) {
		this.list = list;
	}
	
	public MyOrderAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(MyOrderAdapter adapter) {
		this.adapter = adapter;
	}


	public String getWtype() {
		return wtype;
	}


	public void setWtype(String wtype) {
		this.wtype = wtype;
	}


	public PullToRefreshListView getRefreshListView() {
		return refreshListView;
	}


	public void setRefreshListView(PullToRefreshListView refreshListView) {
		this.refreshListView = refreshListView;
	}


	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	

	public View getHead_view() {
		return head_view;
	}

	public void setHead_view(View head_view) {
		this.head_view = head_view;
	}
	
	
	
}
