package com.jizhi.hududu.uclient.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jizhi.hududu.uclient.bean.MyCardBag;
import com.neusoft.huduoduoapp.R;

/**
 * 我的卡包
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class MyCardBagAdapter extends BaseAdapter {
	private Context context;
	private List<MyCardBag> list;
	private LayoutInflater inflater;

	

	public MyCardBagAdapter(Context context,List<MyCardBag> list) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    ViewHolder holder;
	    MyCardBag card = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_card_bag_item,null);
			holder.listView = (ListView) convertView.findViewById(R.id.listView);
			holder.term_of_validity = (TextView) convertView.findViewById(R.id.term_of_validity);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.unit = (TextView) convertView.findViewById(R.id.unit);
			holder.image_card_type = (ImageView) convertView.findViewById(R.id.image_card_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String modeType = card.getModetype();
		String[] descs = card.getDesc();
		if(modeType.equals("cash")){//现金券
			holder.image_card_type.setImageResource(R.drawable.qie_big);
			holder.type.setText("(抵用券)");
			holder.term_of_validity.setTextColor(context.getResources().getColor(R.color.card_date_yello));
			holder.unit.setTextColor(context.getResources().getColor(R.color.card_money_yello));
			holder.price.setTextColor(context.getResources().getColor(R.color.card_money_yello));
		}else if(modeType.equals("coupon")){ //通用优惠券
			holder.image_card_type.setImageResource(R.drawable.qie_small);
			holder.type.setText("(优惠券)");
			holder.term_of_validity.setTextColor(context.getResources().getColor(R.color.card_date_green));
			holder.unit.setTextColor(context.getResources().getColor(R.color.card_money_green));
			holder.price.setTextColor(context.getResources().getColor(R.color.card_money_green));
		}
		if(descs!=null && descs.length>0){
			holder.listView.setAdapter(new MyCardBagMeansAdapter(context,descs));
		}else{
//			holder.listView.setAdapter(new MyCardBagMeansAdapter(context,new String[]{"仅限呼多多所有服务使用不找零不能兑换现金","除了帮我买菜服务外其他业务都可以使用","全新的个人“用户中心”，推荐有奖，优惠不停"}));
			holder.listView.setVisibility(View.VISIBLE);
		}
		holder.price.setText(card.getCount());
		holder.term_of_validity.setText(String.format(context.getString(R.string.term_of_validity),changeDate(card.getEndtime())));
		return convertView;
	}

	class ViewHolder {
		ListView listView; 
		TextView term_of_validity;
		TextView price;
		TextView type;
		TextView unit;
		ImageView image_card_type;
	}
	
	public String changeDate(String time){
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		return simple.format(new java.util.Date(Long.valueOf(time + "000")));
	}


}