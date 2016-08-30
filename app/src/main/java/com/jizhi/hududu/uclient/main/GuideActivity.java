package com.jizhi.hududu.uclient.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.UtilConn;
import com.neusoft.huduoduoapp.R;
/**
 * 引导页面
 * */
public class GuideActivity extends Activity implements OnPageChangeListener {
	//装载引导页面的ViewPager
	private ViewPager viewPager;
	//装载引导图片的ImageView的集合
	private List<ImageView> list;

	private int currentPager = 0;
	//几个引导页面的复选框CheckBox
	private CheckBox guide_1;
	private CheckBox guide_2;
	private CheckBox guide_3;
	private CheckBox guide_4;
	private CheckBox guide_5;
	//是否第一次点击，用于判断引导页的加载，boolean默认值为false
	private boolean isClick;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置窗体全屏（连状态栏一起隐藏）
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//加载布局
		setContentView(R.layout.welcome);
		//执行初始化操作
		initData();
		//实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		//实例化CheckBox复选框
		guide_1 = (CheckBox) findViewById(R.id.guide_1);
		guide_2 = (CheckBox) findViewById(R.id.guide_2);
		guide_3 = (CheckBox) findViewById(R.id.guide_3);
		guide_4 = (CheckBox) findViewById(R.id.guide_4);
		guide_5 = (CheckBox) findViewById(R.id.guide_5);
		//设置适配器
		viewPager.setAdapter(new ViewPagerAdapter());
		//设置监听器
		viewPager.setOnPageChangeListener(this);
		//创建子线程并执行，用于上传渠道号（此功能已作废）
		/*new Thread(new Runnable() {
			public void run() {
				String url = CMD.insertinfo;
				UtilConn.getContent(GuideActivity.this, url, upInfo());

			}
		}).start();*/
	}

	/*public List<NameValuePair> upInfo() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			Object msg = info.metaData.get("channelid");
			params.add(new BasicNameValuePair("res", msg + ""));
			params.add(new BasicNameValuePair("mode", android.os.Build.MODEL
					+ ""));
			params.add(new BasicNameValuePair("usertype", "yonghu"));
			params.add(new BasicNameValuePair("mobiletype", "android"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}*/

	public void initData() {
		//创建集合对象
		list = new ArrayList<ImageView>();

		//创建5个引导页所需的ImageView并加载相应图片资源
		ImageView image1 = new ImageView(this);
		image1.setScaleType(ScaleType.CENTER_CROP);
		image1.setImageResource(R.drawable.guide_1);

		ImageView image2 = new ImageView(this);
		image1.setScaleType(ScaleType.CENTER_CROP);
		image2.setImageResource(R.drawable.guide_2);

		ImageView image3 = new ImageView(this);
		image1.setScaleType(ScaleType.CENTER_CROP);
		image3.setImageResource(R.drawable.guide_3);

		ImageView image4 = new ImageView(this);
		image1.setScaleType(ScaleType.CENTER_CROP);
		image4.setImageResource(R.drawable.guide_4);

		ImageView image5 = new ImageView(this);
		image1.setScaleType(ScaleType.CENTER_CROP);
		image5.setImageResource(R.drawable.guide_5);
		//第五张图片资源需要设定点击事件，用于Activity跳转
		image5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//判断是否第一次点击
				if(isClick){
					return;
				}
				isClick = true;
				//跳转到主界面
				Intent intent = new Intent(GuideActivity.this,MainActivity.class);
				/**利用SharedPreferences封装的SPUtils工具类存储boolean数据
				 * 用于下次进入APP时进行读取，该值作为判断是否进入引导页的依据
				 */
				SPUtils.put(GuideActivity.this, "is_first", true,Constance.HUDUDUUSER);
				//开启跳转
				startActivity(intent);
				//跳转后，销毁当前Activity
				finish();
			}
		});

		//集合装载图片对象
		list.add(image1);
		list.add(image2);
		list.add(image3);
		list.add(image4);
		list.add(image5);
	}
	//ViewPager适配器
	class ViewPagerAdapter extends PagerAdapter {

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}



		// 获得当前界面数
		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}




		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

	}


	/**
	 * OnPageChangeListener(ViewPager滑动)监听
	 */
	//页面状态发生改变时的回调。(停止变为滑动，滑动变为停止。这就叫做状态改变。)
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	//ViewPager滑动过程回调
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	//某个页面被选中时的回调
	@Override
	public void onPageSelected(int position) {

		switch (currentPager) {
		case 0:
			guide_1.setChecked(false);
			break;
		case 1:
			guide_2.setChecked(false);
			break;
		case 2:
			guide_3.setChecked(false);
			break;
		case 3:
			guide_4.setChecked(false);
			break;
		case 4:
			guide_5.setChecked(false);
			break;
		default:
			break;
		}
		//CheckBox.setChecked(boolean)意味是否设置为选中状态
		switch (position) {
		case 0:
			guide_1.setChecked(true);
			break;
		case 1:
			guide_2.setChecked(true);
			break;
		case 2:
			guide_3.setChecked(true);
			break;
		case 3:
			guide_4.setChecked(true);
			break;
		case 4:
			guide_5.setChecked(true);
			break;
		default:
			break;
		}
		currentPager = position;
	}

}
