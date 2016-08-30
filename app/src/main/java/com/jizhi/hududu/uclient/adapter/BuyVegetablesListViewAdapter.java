package com.jizhi.hududu.uclient.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.bean.BuyVegetables;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 菜篮子ListView Adapter
 * @author Xuj
 * @date 2015年8月21日 16:04:43
 */
public class BuyVegetablesListViewAdapter extends BaseAdapter{
	
	private Context context;
	private Map<String,BuyVegetables> map;
	private List<BuyVegetables> list;
	private LayoutInflater inflater;
	
	private CallBackPrice call;

	public BuyVegetablesListViewAdapter(Context context,Map<String,BuyVegetables> map,List<BuyVegetables> list,CallBackPrice call) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.map = map;
		this.list = list;
		this.call = call;
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
	public View getView(final int position,View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final BuyVegetables bean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.buy_listview_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.remove = (FrameLayout) convertView.findViewById(R.id.remove);
			holder.add = (FrameLayout) convertView.findViewById(R.id.add);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(bean.getName());
		holder.price.setText(bean.getAllPrice()+"");
		holder.number.setText(bean.getAllNumber()+"");
		holder.remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				bean.setAllNumber(bean.getAllNumber()-1);
				if(bean.getAllNumber()==0){
					map.remove(bean.getFhnum());
					list.remove(position);
					notifyDataSetChanged();
				}else{
					holder.number.setText(bean.getAllNumber()+"");
					String allPrice = DataForMat.twoDecimalPlaces(bean.getAllPrice()-bean.getFee());
					bean.setAllPrice(Double.parseDouble(allPrice));
					holder.price.setText(allPrice);
				}
				call.callPrice(bean,"remove");
			}
		});
		holder.add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				bean.setAllNumber(bean.getAllNumber()+1);
				holder.number.setText(bean.getAllNumber()+"");
				String allPrice = DataForMat.twoDecimalPlaces(bean.getAllPrice()+bean.getFee());
				bean.setAllPrice(Double.parseDouble(allPrice));
				holder.price.setText(allPrice);
				call.callPrice(bean,"add");
			}
		});
		//加载图片
		ImageLoader.getInstance().displayImage(CMD.NETURL+"uploads/fresh_img/"+bean.getPic(), holder.image,UtilImageLoader.getImageOptions());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView number;
		TextView price;
		FrameLayout remove;
		FrameLayout add;
		ImageView image;
	}
	
	
	public interface CallBackPrice{
		public void callPrice(BuyVegetables bean,String type);
	}
	
	

}