package com.jizhi.hududu.uclient.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.MyGalleryAdapter.OnItemClickLitener;
import com.jizhi.hududu.uclient.bean.Dinner;
import com.jizhi.hududu.uclient.bean.Dishes;
import com.jizhi.hududu.uclient.dao.DinnerDao;
import com.jizhi.hududu.uclient.main.DishesGridActivity;
import com.jizhi.hududu.uclient.main.FKDetailActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.widget.ChooseChefDialog;
import com.jizhi.hududu.uclient.widget.ChooseChefDialog.CallBackChooseChef;
import com.jizhi.hududu.uclient.widget.MyRecyclerView;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;
/**
 * 选择厨师Adapter
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class ChooseChefAdapter extends BaseAdapter {
	private Activity context;
	private List<Dinner> list;
	private LayoutInflater inflater;
	private DinnerDao dao;

	public ChooseChefAdapter(Activity context,List<Dinner> list,DinnerDao dao) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.dao = dao;
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
		final ViewHolder holder;
		final Dinner bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.choose_chef_listview_item, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic); //服客头像
			holder.name = (TextView) convertView.findViewById(R.id.name);//服客姓名
			holder.avgrate = (TextView) convertView.findViewById(R.id.avgrate); //评分
			holder.location = (TextView) convertView.findViewById(R.id.location);//离我距离
			holder.ratingbar = (RatingBar)convertView.findViewById(R.id.ratingbar); //星星评分
			holder.relativeLayout3 = (RelativeLayout) convertView.findViewById(R.id.linearLayout3); //选择厨师按钮
			holder.choose_cook_text = (TextView) convertView.findViewById(R.id.choose_cook_text); //选择厨师 中间文本
			holder.ordertotal = (TextView) convertView.findViewById(R.id.ordertotal); //完成服务次数
			holder.bestcooking = (TextView) convertView.findViewById(R.id.bestcooking); //拿手菜
			holder.recy = (MyRecyclerView) convertView.findViewById(R.id.recycler_view_test_rv);
			LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
			linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			holder.recy.setLayoutManager(linearLayoutManager);
			MyGalleryAdapter adapter = new MyGalleryAdapter(context,bean.getBestcooking());
			holder.recy.setAdapter(adapter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(bean.getName());
		holder.avgrate.setText(bean.getAvgrate()+"分");
		holder.ratingbar.setRating(bean.getAvgrate());
		holder.ordertotal.setText(bean.getOrdertotal()+"");
		holder.bestcooking.setText(bean.getNames());
		holder.location.setText(bean.getFar()+"");
		
		if(holder.recy.getAdapter()!=null){
			((MyGalleryAdapter)holder.recy.getAdapter()).setList(bean.getBestcooking());
			holder.recy.getAdapter().notifyDataSetChanged();
			((MyGalleryAdapter)holder.recy.getAdapter()).setOnItemClickLitener(new OnItemClickLitener() {
				@Override
				public void onItemClick(View view, int position) {
					List<Dishes> tempList = new ArrayList<Dishes>();
					for(Dishes food:bean.getBestcooking()){
						tempList.add(food);
					}
					Intent intent = new Intent(context,DishesGridActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("list",(Serializable)tempList);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
			});
		}
		if(bean.isChoose()){
			holder.relativeLayout3.setBackground(context.getResources().getDrawable(R.drawable.cook_button_press));
			holder.choose_cook_text.setTextColor(context.getResources().getColor(R.color.white));
			holder.choose_cook_text.setText("已选择该厨师");
		}else{
			holder.relativeLayout3.setBackground(context.getResources().getDrawable(R.drawable.cook_button_normal));
			holder.choose_cook_text.setTextColor(context.getResources().getColor(R.color.eat5));
		}
		
		holder.relativeLayout3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//ChooseChefDialog类为“选择厨师”dialog
				ChooseChefDialog dialog = new ChooseChefDialog(context,bean.getName(),new CallBackChooseChef(){
					@Override
					public void callChoose(boolean chooser) {
						if(chooser){
								for(Dinner d:list){
									d.setChoose(false);
								}
								dao.updateStatus(bean.getLid(),false);
								dao.updateStatus(bean.getLid(),true);
								bean.setChoose(chooser);
								Intent intent = new Intent();
								Bundle bundle = new Bundle();
								bundle.putSerializable("dinner_list",(Serializable) list);
								bundle.putString("choose_name",bean.getName());
								bundle.putString("lid",bean.getLid());
								intent.putExtras(bundle);
								context.setResult(101,intent);
								context.finish();
						}
					}
				});
				dialog.show();
			}
		});
		holder.pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,FKDetailActivity.class);
				intent.putExtra("wtype",CMD.HD);
				intent.putExtra("far",bean.getFar()+" 米");
				intent.putExtra("lid",bean.getLid());
				context.startActivity(intent);
			}
		});
		//加载图片
		ImageLoader.getInstance().displayImage(CMD.NETURL+"uploads/"+bean.getPic(), holder.pic,UtilImageLoader.getImageOptionsChef());
		return convertView;
	}

	class ViewHolder {
		TextView name; 
		TextView location; 
		TextView avgrate; 
		TextView bestcooking; 
		TextView choose_cook_text; 
		RatingBar ratingbar;
		ImageView pic;
		RelativeLayout relativeLayout3;
		TextView ordertotal; 
		MyRecyclerView  recy;
	}
	
	
	

}