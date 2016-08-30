package com.jizhi.hududu.uclient.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.adapter.BuyVegetablesGridAdapter;
import com.jizhi.hududu.uclient.adapter.BuyVegetablesGridAdapter.CallBackFee;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.bean.BuyVegetablesState;
import com.jizhi.hududu.uclient.json.BuyVegetablesResolution;
import com.jizhi.hududu.uclient.main.BuyVegetablesViewPagerActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.neusoft.huduoduoapp.R;

/**
 * 帮我买菜Fragment (GridView)
 * @author Xuj
 * @date 2015年8月21日 16:03:17
 */
@SuppressLint("ValidFragment")
public class BuyVegetablesFragment extends Fragment {
	
	
	private BuyVegetablesViewPagerActivity activity;
	
	private List<BuyVegetables> list;
	
	private GridView gridView;
	
	private BuyVegetablesGridAdapter adapter;
	
	private String fhtype;
	
	private BuyVegetablesResolution json = new BuyVegetablesResolution();
	
	private Map<String,BuyVegetables> map;
	
	private LinearLayout load;
	
	
	private boolean isLoading = false;
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
        	if(!isLoading){
        		searchVegetables();
        	}
        }
        else if (isVisibleToUser == false) { 
            
        }
    }
	
	
	public BuyVegetablesFragment(String fhtype,Map<String,BuyVegetables> map,List<BuyVegetables> list){
		this.fhtype = fhtype;
		this.map = map;
		if(list!=null){
			this.list = list;
		}else{
			this.list = new ArrayList<BuyVegetables>();
		}
	}
	
	
	
	
	public void setMap(Map<String, BuyVegetables> map) {
		this.map = map;
	}




	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (BuyVegetablesViewPagerActivity) activity;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.buy_vegettable_grid, container,false);
		gridView = (GridView) v.findViewById(R.id.gridView);
		load = (LinearLayout) v.findViewById(R.id.load);
		DisplayMetrics dm = new DisplayMetrics();  
	    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);  
	    float dpi = dm.density;
		if(adapter==null)
			adapter = new BuyVegetablesGridAdapter(activity,list,activity.getRelativeLayout(),dpi,new CallBackFee() {
				@Override
				public void callFee(BuyVegetables bean) {
					double price = Double.parseDouble(activity.getPrice().getText().toString()); 
					int number = Integer.parseInt(activity.getNumber().getText().toString());
					activity.getPrice().setText(DataForMat.twoDecimalPlaces(price+bean.getFee()));
					activity.getNumber().setText((number+1)+"");
					if(!map.containsKey(bean.getFhnum())){
						map.put(bean.getFhnum(),bean);
					}else{
						BuyVegetables tempBean = map.get(bean.getFhnum());
						tempBean.setAllNumber(bean.getAllNumber());
						tempBean.setAllPrice(Double.parseDouble(DataForMat.twoDecimalPlaces(bean.getAllPrice())));
					}
				}
			});
		gridView.setAdapter(adapter);
		return v;
	}
	
	
	public List<NameValuePair> params() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cid", (String) SPUtils.get(activity,"mobile", "", Constance.HUDUDUUSER))); // 登录人id
		params.add(new BasicNameValuePair("fhtype",fhtype));
		params.add(new BasicNameValuePair("option","freshimage")); // 上门服务的时间
		return params;
	}
	
	
	
	/**
	 * 查询帮我买菜
	 */
	public void searchVegetables() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final BuyVegetablesState state = json.resolution(activity,CMD.SEARCH_BUY_VEGETABLES,params(),fhtype);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(activity, "数据解析出错!");
						}else{
							if (state.getState() == 1) {
								if(state.getResp()!=null && state.getResp().size()>0){
									list.addAll(state.getResp());
									adapter.notifyDataSetChanged();
									isLoading = true;
								}else{
									CommonMethod.makeNoticeShort(activity, "查询失败");
								}
							} else {
								CommonMethod.makeNoticeShort(activity, "查询失败");
							}
						}
						load.setVisibility(View.GONE);
					}
				});
			}
		}).start();
	}


	public BuyVegetablesGridAdapter getAdapter() {
		return adapter;
	}


	public void setAdapter(BuyVegetablesGridAdapter adapter) {
		this.adapter = adapter;
	}

	public List<BuyVegetables> getList() {
		return list;
	}




	public void setList(List<BuyVegetables> list) {
		this.list = list;
	}




	public String getFhtype() {
		return fhtype;
	}




	public void setFhtype(String fhtype) {
		this.fhtype = fhtype;
	}

}
