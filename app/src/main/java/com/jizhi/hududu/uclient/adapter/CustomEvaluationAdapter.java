package com.jizhi.hududu.uclient.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jizhi.hududu.uclient.adapter.CustomEvaluationGallerAdapter.OnItemClickLitener1;
import com.jizhi.hududu.uclient.bean.CustomDetail;
import com.jizhi.hududu.uclient.main.DishesZoomActivity;
import com.jizhi.hududu.uclient.widget.MyRecyclerView;
import com.neusoft.huduoduoapp.R;

/**
 * 用户评价Adapter
 * @author Xuj
 * @date 2015年9月9日 11:59:25
 */
public class CustomEvaluationAdapter extends BaseAdapter {
	
	
	private List<CustomDetail> list;
	private Context con;
	private LayoutInflater inflater;

	public CustomEvaluationAdapter(Context con,List<CustomDetail> list) {
		this.con = con;
		this.list = list;
		inflater = LayoutInflater.from(con);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final CustomDetail bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.custom_evaluation_item, null);
			holder.custom_phone = (TextView) convertView.findViewById(R.id.custom_phone);
			holder.custom_evaluation_date = (TextView) convertView.findViewById(R.id.custom_evaluation_date);
			holder.custom_evaluation_content = (TextView) convertView.findViewById(R.id.custom_evaluation_content);
			holder.ratingbar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			holder.evaluations_pictures = (MyRecyclerView) convertView.findViewById(R.id.evaluations_pictures);
			LinearLayoutManager linearLayoutManager = new LinearLayoutManager(con);
			linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			holder.evaluations_pictures.setLayoutManager(linearLayoutManager);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(bean.getAppraisepic()!=null && bean.getAppraisepic().size()>0){
			CustomEvaluationGallerAdapter adapter = new CustomEvaluationGallerAdapter(con,bean.getAppraisepic());
			holder.evaluations_pictures.setAdapter(adapter);
			holder.evaluations_pictures.setVisibility(View.VISIBLE);
			adapter.setOnItemClickLitener(new OnItemClickLitener1() {
				@Override
				public void onItemClick(View view, int position) {
					Intent intent = new Intent(con,DishesZoomActivity.class);
					intent.putStringArrayListExtra("list",(ArrayList<String>)bean.getAppraisepic());
					intent.putExtra("initPosition",position);
					intent.putExtra("type","2");
					con.startActivity(intent);
				}
			});
		}else{
			holder.evaluations_pictures.setVisibility(View.GONE);
		}
		holder.custom_phone.setText(bean.getName());
		holder.custom_evaluation_date.setText(changeDate(bean.getAppraise_time()));
		if(bean.getLappraise()!=null && !"".equals(bean.getLappraise())){
			holder.custom_evaluation_content.setText(bean.getLappraise());
		}else{
			holder.custom_evaluation_content.setText("好评");
		}
		if(bean.getLrate()!=null && !"".equals(bean.getLrate())){
			holder.ratingbar.setRating((int)(Float.parseFloat(bean.getLrate())));
		}else{
			holder.ratingbar.setRating(5);
		}
		return convertView;
	}
	
	public String changeDate(String time){
		SimpleDateFormat simple = new SimpleDateFormat("MM月dd日 HH:mm");
		return simple.format(new java.util.Date(Long.valueOf(time + "000")));
	}

	class ViewHolder {
		TextView custom_phone; //用户电话
		TextView custom_evaluation_date; //评论时间
		TextView custom_evaluation_content; //评论内容
		RatingBar ratingbar; //评级
		MyRecyclerView evaluations_pictures;
	}
}
