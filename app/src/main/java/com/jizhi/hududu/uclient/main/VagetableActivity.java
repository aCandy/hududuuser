package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.hcs.hududu.uclient.utils.Utils;
import com.jizhi.hududu.uclient.adapter.VegetableInfodapter;
import com.jizhi.hududu.uclient.bean.VegetableInfo;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;

/**
 * 生鲜报价详细信息
 * 
 * @author huChangSheng
 * @time 2015-8-24 下午4:53:52
 * @version 1.0
 * 
 */
public class VagetableActivity extends Activity {
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_count)
	private TextView tv_count;
	@ViewInject(R.id.tv_price)
	private TextView tv_price;
	@ViewInject(R.id.listview)
	private ListView listview;
	private VagetableActivity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vagetable);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu_client);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		tv_title.setText(getString(R.string.payorder));
		mActivity = VagetableActivity.this;
		String quote = getIntent().getStringExtra("quote");
		List<VegetableInfo> list = new ArrayList<VegetableInfo>();
		String[] veg = quote.split(";");

		for (int i = 0; i < veg.length; i++) {
			String[] vegInfo = veg[i].split(",");
			VegetableInfo info = new VegetableInfo();
			info.setNum(vegInfo[0]);
			info.setName(vegInfo[1]);
			info.setCount(Double.parseDouble(vegInfo[2]));
			info.setPrice(Double.parseDouble(vegInfo[3]));
			info.setPic(vegInfo[4]);
			info.setAllPrice(info.getCount() * info.getPrice());
			list.add(info);
		}
		if (list.size() <= 0) {
			CommonMethod.makeNoticeShort(mActivity, "读取信息失败");
			return;
		}
		listview.setAdapter(new VegetableInfodapter(mActivity, list));
		double price = 0;
		double count = 0;
		for (int i = 0; i < list.size(); i++) {
			price += list.get(i).getAllPrice();
			count += list.get(i).getCount();
		}
		tv_price.setText("金       额:" + "￥" + (int) count);
		tv_count.setText("订单总计:生鲜数量" + Utils.m2(price) + "种");
	}

	@OnClick(R.id.rb_back)
	public void finishAct(View view) {
		finish();
	}
}
