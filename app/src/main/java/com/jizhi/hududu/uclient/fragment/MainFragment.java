package com.jizhi.hududu.uclient.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.hcs.hududu.uclient.utils.TimesUtils;
import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.hcs.hududu.uclient.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jizhi.hududu.receiver.SDKReceiver;
import com.jizhi.hududu.uclient.bean.GetCodeBean;
import com.jizhi.hududu.uclient.bean.HouseHoldCleaningState;
import com.jizhi.hududu.uclient.bean.NearByPoiInfo;
import com.jizhi.hududu.uclient.bean.NearFk;
import com.jizhi.hududu.uclient.bean.NearFkState;
import com.jizhi.hududu.uclient.bean.VegOrderhFkState;
import com.jizhi.hududu.uclient.bean.VegetableOrderhFkInfo;
import com.jizhi.hududu.uclient.dao.NearFkStateListener;
import com.jizhi.hududu.uclient.dao.SelectServiceTypeGlideListenerDao;
import com.jizhi.hududu.uclient.gesture.SelectServiceTypeGlideHelper;
import com.jizhi.hududu.uclient.json.HouseHoldCleningResolution;
import com.jizhi.hududu.uclient.json.ParseHttpData;
import com.jizhi.hududu.uclient.main.BuyVegetablesMainActivity;
import com.jizhi.hududu.uclient.main.DinnerMainActivity;
import com.jizhi.hududu.uclient.main.HandWasingMainActivity;
import com.jizhi.hududu.uclient.main.HouseholdCleaningMainActivity;
import com.jizhi.hududu.uclient.main.LoadUrlActivity;
import com.jizhi.hududu.uclient.main.LoginActivity;
import com.jizhi.hududu.uclient.main.MainActivity;
import com.jizhi.hududu.uclient.main.NearbyAddrActivity;
import com.jizhi.hududu.uclient.main.PayForActivity;
import com.jizhi.hududu.uclient.main.VagetableActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.service.MsgService;
import com.jizhi.hududu.uclient.util.AnimUtil;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.widget.CircleImageView;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.OrderCustomProgress;
import com.jizhi.hududu.uclient.widget.VegetablesOrderCustomProgress;
import com.jizhi.hududu.uclient.widget.WaitOrderCustomProgress;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 主界面Fragment
 * 继承Fragment
 * 实现OnGetGeoCoderResultListener（地理编码/反地理编码结果）
 * SelectServiceTypeGlideListenerDao（未知接口）
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements
		OnGetGeoCoderResultListener, SelectServiceTypeGlideListenerDao {
	//标题栏“返回”（“首页”）按钮
	@ViewInject(R.id.rb_back)
	private RadioButton rb_back;
	//带“首页”字样的标题栏布局
	@ViewInject(R.id.rea_back)
	private RelativeLayout rea_back;
	//尚不明确的RadioButton
	@ViewInject(R.id.rbRight2)
	private RadioButton rbRight2;
	// 底部五个栏目布局
	@ViewInject(R.id.layout_bottom)
	private LinearLayout layout_bottom;
	// 带用户头像的标题栏布局
	@ViewInject(R.id.main_bar)
	private RelativeLayout main_bar;
	// “点击上门”按钮
	@ViewInject(R.id.imgShowTopView)
	private ImageView imgShowTopView;
	//“当前位置”图标
	@ViewInject(R.id.btn_target)
	private ImageView btn_target;
	// ContentFragment布局
	@ViewInject(R.id.layout_main)
	private RelativeLayout layout_main;
	// 底部五个栏目布局（滑块进度条）
	@ViewInject(R.id.rea_greens)
	private RelativeLayout rea_greens;
	/*@ViewInject(R.id.rea_homemake)
	private RelativeLayout rea_homemake;
	@ViewInject(R.id.rea_food)
	private RelativeLayout rea_food;
	@ViewInject(R.id.rea_cloth)
	private RelativeLayout rea_cloth;*/
	@ViewInject(R.id.rea_work)
	private RelativeLayout rea_work;
	// 底部五个栏目布局中显示文字（服务项目）
	@ViewInject(R.id.tv_greens)
	private TextView tv_greens;
	/*@ViewInject(R.id.tv_homemake)
	private TextView tv_homemake;
	@ViewInject(R.id.tv_food)
	private TextView tv_food;
	@ViewInject(R.id.tv_cloth)
	private TextView tv_cloth;*/
	@ViewInject(R.id.tv_work)
	private TextView tv_work;
	// 底部五个栏目布局中选中显示图片
	@ViewInject(R.id.img_greens)
	private ImageView img_greens;
	/*@ViewInject(R.id.img_homemake)
	private ImageView img_homemake;
	@ViewInject(R.id.img_food)
	private ImageView img_food;
	@ViewInject(R.id.img_cloth)
	private ImageView img_cloth;*/
	@ViewInject(R.id.img_work)
	private ImageView img_work;
	/** 左下角三个不同栏目（单价条目）
	 * 第一个*/
	@ViewInject(R.id.rea_homemake_complex_hour)
	private RelativeLayout rea_homemake_complex_hour;
	//第二个
	@ViewInject(R.id.rea_homemake_simple_hour)
	private RelativeLayout rea_homemake_simple_hour;
	//第三个
	@ViewInject(R.id.rea_homemake_min)
	private RelativeLayout rea_homemake_min;
	// 地图顶部显示定位位置和具体地址填写的条目
	@ViewInject(R.id.lin_top)
	private LinearLayout lin_top;
	//定位位置的Button
	@ViewInject(R.id.btn_addr)
	private Button btn_addr;
	//填写具体位置的EditText
	@ViewInject(R.id.ed_addr)
	private EditText ed_addr;
	// 左下角所有单价条目的LinearLayout
	@ViewInject(R.id.lin_bottom_left)
	private LinearLayout lin_bottom_left;
	//第一个单价条目上的文字
	@ViewInject(R.id.tv_complex_hour)
	private TextView tv_complex_hour;
	//第二个单价条目上的文字
	@ViewInject(R.id.tv_simple_hour)
	private TextView tv_simple_hour;
	//第三个单价条目上的文字
	@ViewInject(R.id.tv_min)
	private TextView tv_min;
	// slidingmenu及 内控件
	@ViewInject(R.id.head)
	private LinearLayout head;
	private Resources resources;
	private List<ImageView> listImageView;
	private List<TextView> listTextView;
	//此Fragment所依附的Activity
	private MainActivity mActivity;
	//百度地图KEY验证以及网络验证的广播接收器
	private SDKReceiver mReceiver;
	//地图控件
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	//百度地图定位监听
	public MyLocationListenner myListener = new MyLocationListenner();
	//定位模式
	private LocationMode mCurrentMode;
	//定位相关
	LocationClient mLocClient;
	//是否首次定位
	boolean isFirstLoc = true;
	//搜索模块，也可去掉地图模块独立使用
	private GeoCoder mSearch = null;
	private List<NearByPoiInfo> poiInfos;
	//手指操作监听
	private SelectServiceTypeGlideHelper glideHelper;
	// 点击的下标
	private int position;
	// 家政动画执行次数
	private int hmPos = 0;
	private NearFkState nearFkState;
	private List<NearFk> nearFks;
	private ArrayList<Marker> markers;
	private InfoWindow mInfoWindow;
	private ArrayList<BitmapDescriptor> giflist;
	// 用户选择的坐标中心点
	private LatLng latLng;
	// 定位的地图中心点
	private LatLng myLoc;
	private String loc;
	private Intent intent;
	private String userAddr;
	private String p1, p2, p3, oid;
	private View orderView;
	private SlidingMenu menu;
	// 服客抢单信息数据对象
	private VegetableOrderhFkInfo fkInfo;
	private HouseHoldCleningResolution resolution = new HouseHoldCleningResolution(); // 家庭保洁解析
	private View fragView;
	private String wType, wTypes;
	//服客抢单信息数据集合
	private List<VegetableOrderhFkInfo> fkInfos;
	//是否是简单打扫
	private boolean isSimpleClear;
	//是否是饭后洗碗
	private boolean isWashDishes;
	//标题栏用户头像控件
	@ViewInject(R.id.img_head)
	private CircleImageView img_head;

	public CircleImageView getImg_head() {
		return img_head;
	}

	public void setImg_head(CircleImageView img_head) {
		this.img_head = img_head;
	}

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0X01:
				// 生鲜跳转支付页面
				fkInfo = (VegetableOrderhFkInfo) msg.obj;
				topay();
				VegetablesOrderCustomProgress.dissmiss();
				break;
			case 0X02:
				// 生鲜跳转支付页面
				String quote = (String) msg.obj;
				if (null == quote || quote.equals("")) {
					CommonMethod.makeNoticeShort(mActivity, "读取信息失败");
					return;
				}
				intent = new Intent(mActivity, VagetableActivity.class);
				intent.putExtra("quote", quote);
				fragView.getContext().startActivity(intent);
				break;
			}
		}

	};
	/**
	 * Fragment回调：当Fragment与Activity发生关联时调用。
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//将此Fragment所依附的Activity定义为全局变量，用于在本类中多次使用
		this.mActivity = (MainActivity) activity;
	}

	/**
	 * Fragment回调：创建该Fragment的视图
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MapView.setMapCustomEnable(true);
		//加载布局
		fragView = inflater.inflate(R.layout.content_fragment, null);


		//使用Xutils注解功能必须要进行的初始化操作
		ViewUtils.inject(this, fragView);
		//初始化数据
		initView(fragView);
		// 初始化底部按钮
		setImgeView();
		// 初始化地图
		initBaiduMap();
		// 获取菜价信息
		ParseHttpData.getPrice(mActivity);
		//地图单击事件监听
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			//地图内 Poi 单击事件回调函数
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
			//地图单击事件回调
			@Override
			public void onMapClick(LatLng arg0) {
				/**
				 * 将“点击上门”按钮隐藏
				 * 意思就是点击了地图其他区域时（非定位图标上）
				 * 就将“点击上门”按钮隐藏
				 */
				imgShowTopView.setVisibility(View.GONE);
				//执行相关控件的隐藏
				hmAnimInVisible(true);
				if (mBaiduMap!=null )
				/**
				 * 隐藏当前 InfoWindow
				 * InfoWindow:貌似是选择自驾还是公交等导航窗口（正在确认）
				 */
					mBaiduMap.hideInfoWindow();

			}
		});
		/**
		 * 地图 Marker 覆盖物点击事件监听接口
		 * 地图覆盖物：可理解为地图上的定位图标（毕竟大部分情况下只有定位了才能产生覆盖）
		 */
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			//地图 Marker 覆盖物点击事件监听函数
			@Override
			public boolean onMarkerClick(Marker marker) {
				//装载“阿姨详情页”布局
				View view = LayoutInflater.from(mActivity).inflate(
						R.layout.layout_fukeinfo, null);
				//隐藏当前 InfoWindow
				mBaiduMap.hideInfoWindow();
				ImageView img_fkhead = (ImageView) view
						.findViewById(R.id.img_fkhead);
				TextView tv_fkname = (TextView) view
						.findViewById(R.id.tv_fkname);
				TextView tv_fkage = (TextView) view.findViewById(R.id.tv_fkage);
				TextView tv_fknum = (TextView) view.findViewById(R.id.tv_fknum);
				TextView tv_fkordercount = (TextView) view
						.findViewById(R.id.tv_fkordercount);
				RatingBar ratingBar = (RatingBar) view
						.findViewById(R.id.room_ratingbar);
				for (int i = 0; i < markers.size(); i++) {
					if (marker == markers.get(i)) {
						LatLng ll = marker.getPosition();
						mBaiduMap.showInfoWindow(mInfoWindow);
						imgShowTopView.setVisibility(View.GONE);
						layout_bottom.setVisibility(View.VISIBLE);
						tv_fkname.setText(nearFks.get(i).getName());
						tv_fkage.setText(nearFks.get(i).getAge() + "岁");
						tv_fknum.setText(nearFks.get(i).getIcno());
						tv_fkordercount.setText(nearFks.get(i).getOrdertotal()
								+ "");
						ImageLoader.getInstance().displayImage(
								CMD.PICPATH + nearFks.get(i).getPic(),
								img_fkhead,
								new UtilImageLoader().RoundedOptionsRound(75));
						int rat = (int) nearFks.get(i).getAvgrate();
						if (rat >= 5 || rat <= 0) {
							rat = 5;
						}
						ratingBar.setRating(rat);
						mInfoWindow = new InfoWindow(view, ll, -107);
					}
				}

				return true;

			}
		});
		//返回该布局以显示
		return fragView;
	}

	public void showLeftMenu() {
		mActivity.getSlidingMenu().toggle();
	}

	/**
	 * 初始化地图,定位
	 */
	private void initBaiduMap() {
		// 初始化地图
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);



		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化（定位模式）
		mCurrentMode = LocationMode.NORMAL;
		mLocClient = new LocationClient(mActivity);
		//绑定监听器
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型、
		option.setIsNeedAddress(true);
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		// 修改为自定义marker
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.ub__fare_split_badge);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, false, mCurrentMarker));
		completeLis();
	}

	@Override
	public void onStart() {
		super.onStart();
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		mActivity.registerReceiver(mReceiver, iFilter);
	}

	private void initView(View view) {
		//getResources()用于调取系统资源
		resources = getResources();
		//创建服客抢单信息数据集合
		fkInfos = new ArrayList<VegetableOrderhFkInfo>();
		//地图控件实例化
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		//手指监听实例化
		glideHelper = new SelectServiceTypeGlideHelper(mActivity, this);
		/*rea_homemake.setOnTouchListener(glideHelper);
		rea_cloth.setOnTouchListener(glideHelper);
		rea_food.setOnTouchListener(glideHelper);*/
		rea_greens.setOnTouchListener(glideHelper);
		rea_work.setOnTouchListener(glideHelper);
		rb_back.setText(getString(R.string.back));
		rea_back.setVisibility(View.GONE);
		main_bar.setVisibility(View.VISIBLE);
		wType = CMD.SUBMIT_VEGETABLES_CLEANING;
		wTypes = CMD.FH;

		ImageLoader.getInstance().displayImage(
				CMD.PICPATH
						+ ((String) SPUtils.get(mActivity, "pic", "",
								Constance.HUDUDUUSER)), img_head,
				UtilImageLoader.getImageOptionsLoginHead());
	}

	@Override
	public void onPause() {
		if (null != mMapView)
			mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		if (null != mMapView)
			mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (null != mLocClient)
			mLocClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		if (null != mMapView)
			mMapView.onDestroy();
		mMapView = null;
		recycleBitmap();
		try {
			mActivity.unbindService(conn);
		} catch (Exception e) {
		}

		super.onDestroy();
	}

	public void intentActHm(int Job_Desc, String price) {

		String addrs = userAddr;
		String village = ed_addr.getText().toString().trim();
		if (!village.equals("")) {
			addrs = addrs + "," + village;
		}
		if (null == addrs || addrs.equals("") || null == loc || loc.equals("")) {
			CommonMethod.makeNoticeShort(mActivity, "地址错误");
			return;
		}
		if (position == 0) {
			intent = new Intent(mActivity, BuyVegetablesMainActivity.class);
			rbRight2.setVisibility(View.INVISIBLE);
		} else if (position == 1) {
			intent = new Intent(mActivity, HouseholdCleaningMainActivity.class);
			rbRight2.setVisibility(View.VISIBLE);
		} else if (position == 2) {
			intent = new Intent(mActivity, DinnerMainActivity.class);
			rbRight2.setVisibility(View.INVISIBLE);
		} else if (position == 3) {
			intent = new Intent(mActivity, HandWasingMainActivity.class);
			rbRight2.setVisibility(View.INVISIBLE);
		} else {
			intent = new Intent(mActivity, HouseholdCleaningMainActivity.class);
			rbRight2.setVisibility(View.INVISIBLE);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("poiInfo", (Serializable) poiInfos);
		intent.putExtra("jobdesc", Job_Desc);
		intent.putExtra("address", addrs);
		intent.putExtra("location", loc);
		intent.putExtra("price", price);
		intent.putExtras(bundle);
		this.startActivityForResult(intent, Constance.REQUESTCODE_HOMEMAKE);
		if (null != orderView) {
			layout_main.removeView(orderView);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 用户在选择地址
		if (requestCode == Constance.REQUESTCODE_NEAYBYADDE
				&& resultCode == Constance.RESULTCODE_NEAYBYADDE) {
			NearByPoiInfo info = (NearByPoiInfo) data
					.getSerializableExtra("info");
			LatLng ll = new LatLng(info.getLatitude(), info.getLongitude());
			loc = info.getLatitude() + "," + info.getLongitude();
			// 定义地图中心点
			MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(14)
					.build();
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// 改变地图状态
			mBaiduMap.setMapStatus(mMapStatusUpdate);
			imgShowTopView.setVisibility(View.VISIBLE);
			// tv_myaddr.setText(info.getName());
			userAddr = info.getAddress() + "," + info.getName();
			if (lin_top.getVisibility() == 0) {
				btn_addr.setText(info.getName());
			}
			AnimUtil.AnimTarget(btn_target);

		} else if (requestCode == Constance.REQUESTCODE_HOMEMAKE
				&& resultCode == Constance.RESULTCODE_HOMEMAKE) {
			// 此处启动service 查询是否有人抢应订单
			oid = data.getStringExtra("oid");
			Log.e("oid", oid);
			// 绑定Service
			Intent intent = new Intent("com.example.communication.MSG_ACTION");
			intent.putExtra("oid", oid);
			intent.putExtra("wType", wType);
			mActivity.bindService(intent, conn, Context.BIND_AUTO_CREATE);
			showOrderDialog();
			fkInfos.clear();
		} else if (requestCode == Constance.REQUESTCODE_LOGIN
				&& resultCode == Constance.RESULTCODE_LOGIN) {
			String hide_mobile = (String) SPUtils.get(mActivity, "hide_telph",
					"", Constance.HUDUDUUSER);
			((MenuFragment) mActivity.getMenu_fragment())
					.setMobileText(hide_mobile);
			String pic = (String) SPUtils.get(mActivity, "pic", "",
					Constance.HUDUDUUSER);
			if (null != pic && !pic.equals("") && !pic.equals("null")) {
				ImageLoader.getInstance()
						.displayImage(
								CMD.PICPATH + pic,
								((MenuFragment) mActivity.getMenu_fragment())
										.getHead(),
								UtilImageLoader.getImageOptionsLoginHead());
				ImageLoader.getInstance().displayImage(CMD.PICPATH + pic,
						img_head, UtilImageLoader.getImageOptionsLoginHead());
			}
		}

	}

	private CountDownTimer timer = new CountDownTimer(30000, 3000) {

		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			CustomProgress.dissmiss();
		}
	};

	public void setBg(int pos) {
		imgShowTopView.setVisibility(View.VISIBLE);
		for (int i = 0; i < listImageView.size(); i++) {
			if (i == pos) {
				listImageView.get(i).setVisibility(View.VISIBLE);

			} else {
				listImageView.get(i).setVisibility(View.GONE);

			}
		}
		for (int i = 0; i < listTextView.size(); i++) {
			if (i == pos) {
				listTextView.get(i).setTextColor(
						this.getResources().getColor(R.color.grens_light));
				listTextView.get(i).setGravity(Gravity.TOP | Gravity.CENTER);
			} else {
				listTextView.get(i).setTextColor(
						this.getResources().getColor(R.color.black));
				listTextView.get(i).setGravity(Gravity.BOTTOM | Gravity.CENTER);
			}
		}
		hmAnimInVisible(false);
	}

	/**
	 * 显示左边按钮动画
	 */
	private void leftAnimVisible(int pos) {
		// 4 不可见 0 已经显示 顶部动画
		if (lin_top.getVisibility() == 4) {
			Animation anims = AnimationUtils.loadAnimation(mActivity,
					R.anim.top_in);
			lin_top.setVisibility(View.VISIBLE);
			lin_top.startAnimation(anims);
		}
		// 左边动画
		// rea_homemake_min.getVisibility() == 4 &&
		if (position == 0) {
			if (null != leftAnimTimer_in) {
				hmPos = 0;
				leftAnimTimer_in.cancel();
				leftAnimTimer_in.start();
			}
		} else if (pos == 1) {
			hmPos = 0;
			greensAnim();
		} else if (pos == 2) {
			hmPos = 0;
			eatAnim();
		} else if (pos == 3) {
			hmPos = 0;
			washClothAnim();
		}
		imgShowTopView.setVisibility(View.GONE);
		GoneBottom();

	}

	public void GoneBottom() {
		layout_bottom.setVisibility(View.GONE);
		rea_back.setVisibility(View.VISIBLE);
		Log.e("lp", "position = " + position);
		if (position == 1) {
			rbRight2.setVisibility(View.VISIBLE);
		} else {
			rbRight2.setVisibility(View.INVISIBLE);
		}
		main_bar.setVisibility(View.GONE);
	}

	public void VisibleBottom() {
		rea_back.setVisibility(View.GONE);
		main_bar.setVisibility(View.VISIBLE);
		// Animation animTop = AnimationUtils.loadAnimation(mActivity,
		// R.anim.bottom_in);
		AlphaAnimation amin = new AlphaAnimation(0.1f, 1.0f);
		// 渐变时间
		amin.setDuration(500);
		layout_bottom.setVisibility(View.VISIBLE);
		// if (layout_bottom.getVisibility() != 4) {
		layout_bottom.startAnimation(amin);
		amin.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});

		// }
	}

	/**
	 * 隐藏按钮
	 * 
	 * @param isbottom
	 */
	public void hmAnimInVisible(boolean isbottom) {
		//动画管理
		Animation animTop = AnimationUtils.loadAnimation(mActivity,
				R.anim.top_out);
		/**
		 * lin_top是显示定位和填写具体位置的LinearLayout
		 * 判断：如果lin_top当前状态不是不可见（或可见或隐藏）的话
		 * 就将其设置为不可见状态
		 * 同时使用动画特效
		 */
		if (lin_top.getVisibility() !=View.INVISIBLE) {
			lin_top.setVisibility(View.INVISIBLE);
			lin_top.startAnimation(animTop);
		}
		/**
		 * lin_bottom_left是左下角整个单价条目的LinearLayout控件
		 * 判断：如果lin_bottom_left当前状态不是不可见状态和isbottom为true（必须两个条件同时满足）
		 * 就将其设置为不可见状态（lin_bottom_left）
		 * 第三个单价条目设置为不可见状态（rea_homemake_min）
		 * 第二个单价条目设置为不可见状态（rea_homemake_simple_hour）
		 * 第一个单价条目设置为不可见状态（rea_homemake_complex_hour）
		 */
		if (lin_bottom_left.getVisibility() != View.INVISIBLE && isbottom) {
			lin_bottom_left.setVisibility(View.INVISIBLE);
			rea_homemake_min.setVisibility(View.INVISIBLE);
			rea_homemake_simple_hour.setVisibility(View.INVISIBLE);
			rea_homemake_complex_hour.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化底部业务按钮
	 */
	public void setImgeView() {
		listImageView = new ArrayList<ImageView>();
		listImageView.add(img_greens);
		/*listImageView.add(img_homemake);
		listImageView.add(img_food);
		listImageView.add(img_cloth);*/
		listImageView.add(img_work);
		listTextView = new ArrayList<TextView>();
		listTextView.add(tv_greens);
		/*listTextView.add(tv_homemake);
		listTextView.add(tv_food);
		listTextView.add(tv_cloth);*/
		listTextView.add(tv_work);

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.accuracy(0).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				myLoc = ll;
				latLng = ll;
				MyLoc(ll);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 定义地图中心点
	 * 
	 * @param ll
	 */
	private void MyLoc(LatLng ll) {
		MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(14)
				.build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		// ifFlushAddr = true;
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
		// 获取附近服客
		nearFK(ll.longitude + "", ll.latitude + "");
	}

	/**
	 * 获取附近服客
	 * 
	 * @param lng经度
	 * @param lat纬度
	 */
	public void nearFK(final String lng, final String lat) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				nearFkState = ParseHttpData.getNeatFK(mActivity, wType,
						paramsNearFK(lng, lat));
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (nearFkState.getState() == 1) {
							nearFks = nearFkState.getNearFks();
							recycleBitmap();
							if (null != mBaiduMap) {
								mBaiduMap.clear();
							}
							if (null != markers && markers.size() > 0) {
								for (int i = 0; i < markers.size(); i++) {
									markers.get(i).remove();
								}
								markers = null;
							}
							if (null != nearFks && nearFks.size() > 0) {

								giflist = new ArrayList<BitmapDescriptor>();
								markers = new ArrayList<Marker>();
								for (int i = 0; i < nearFks.size(); i++) {
									LatLng llA = new LatLng(nearFks.get(i)
											.getLatitude(), nearFks.get(i)
											.getLongitude());
									// 添加附近服客覆盖物
									BitmapDescriptor bd = BitmapDescriptorFactory
											.fromResource(R.drawable.icon_marka);
									OverlayOptions oo = new MarkerOptions()
											.position(llA).icon(bd).zIndex(5);
									Marker marker = (Marker) mBaiduMap
											.addOverlay(oo);
									markers.add(marker);
									giflist.add(bd);
								}

							} else {
								CommonMethod.makeNoticeShort(mActivity,
										"附近暂时没有服客");
							}
						} else {
							CommonMethod.makeNoticeShort(mActivity, "附近暂时没有服客");
						}
					}
				});
			}
		}).start();
	}

	// 附近服客参数
	public List<NameValuePair> paramsNearFK(String lng, String lat) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lng", lng + ""));
		params.add(new BasicNameValuePair("lat", lat + ""));
		params.add(new BasicNameValuePair("option", "nearfk"));
		return params;
	}

	/**
	 * 地图状态改变监听
	 */
	public void completeLis() {
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus status) {
				// 手势操作地图，设置地图状态等操作导致地图状态开始改变
				imgShowTopView.setVisibility(View.GONE);
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				LatLng ll = mBaiduMap.getMapStatus().target;
				LatLng ptCenter = new LatLng(ll.latitude, ll.longitude);
				if (null != mBaiduMap) {
					mBaiduMap.hideInfoWindow();
				}
				if (null != latLng) {
					double distance = Utils.DistanceOfTwoPoints(
							latLng.latitude, latLng.longitude, ll.latitude,
							ll.longitude);
					if (distance >= 800) {
						nearFK(ll.longitude + "", ll.latitude + "");
					}
				}
				// 可见则显示移动图钉
				if (lin_top.getVisibility() == 0) {
					btn_addr.setText(getString(R.string.move));
				}
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
			}

			@Override
			public void onMapStatusChange(MapStatus status) {
			}
		});
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (null == result || null == result.getLocation()) {
			CommonMethod.makeNoticeShort(mActivity, "地址错误");
			return;
		}
		imgShowTopView.setVisibility(View.VISIBLE);
		AnimUtil.AnimTarget(btn_target);
		List<PoiInfo> poiInfo = result.getPoiList();
		if (null != poiInfo && poiInfo.size() >= 1) {
			btn_addr.setText(poiInfo.get(0).name);
			loc = poiInfo.get(0).location.latitude + ","
					+ poiInfo.get(0).location.longitude;
			userAddr = poiInfo.get(0).address + "," + poiInfo.get(0).name;

			if (null == poiInfos) {
				poiInfos = new ArrayList<NearByPoiInfo>();
			} else {
				poiInfos.clear();
			}

			for (int j = 0; j < poiInfo.size(); j++) {
				PoiInfo p = poiInfo.get(j);
				NearByPoiInfo info = new NearByPoiInfo(p.address, p.name,
						p.location.longitude, p.location.latitude);
				poiInfos.add(info);
			}
		} else {
			loc = result.getLocation().latitude + ","
					+ result.getLocation().longitude;
			btn_addr.setText(result.getAddress());
			userAddr = result.getAddress();

		}
	}

	// 左右滑动回调
	@Override
	public void OnGlideLeft() {
		if (position <= 0) {
			return;
		}
		setBg(position -= 1);
	}

	@Override
	public void OnGlideRight() {
		if (position >= 4) {
			return;
		}
		setBg(position += 1);
	}

	/** 左边按钮动画 **/
	private CountDownTimer leftAnimTimer_in = new CountDownTimer(800, 200) {
		@Override
		public void onTick(long millisUntilFinished) {
			// 执行中
			Animation anim = AnimationUtils.loadAnimation(mActivity,
					R.anim.left_in);

			lin_bottom_left.setVisibility(View.VISIBLE);
			if (hmPos == 0) {
				rea_homemake_min.setVisibility(View.VISIBLE);
				rea_homemake_min.startAnimation(anim);
				p1 = (String) SPUtils.get(mActivity, "dailycleaning", "6",
						Constance.PRICEINFO);
				tv_complex_hour.setText("开荒保洁(" + p1 + "元/㎡)");
			} else if (hmPos == 1) {
				rea_homemake_simple_hour.setVisibility(View.VISIBLE);
				rea_homemake_simple_hour.startAnimation(anim);
				p2 = (String) SPUtils.get(mActivity, "freshhelper", "45",
						Constance.PRICEINFO);
				tv_simple_hour.setText("局部清洁(" + p2 + "元/人/小时)");
			} else if (hmPos == 2) {
				rea_homemake_complex_hour.setVisibility(View.VISIBLE);
				rea_homemake_complex_hour.startAnimation(anim);
				p3 = (String) SPUtils.get(mActivity, "washdishes", "39",
						Constance.PRICEINFO);
				//int price = Integer.parseInt(p3) / 2;
				tv_min.setText("大扫除(" + p3 + "元/人/小时)");
			}

			hmPos += 1;
		}

		@Override
		public void onFinish() {
			hmPos = 0;
		}
	};

	/**
	 * 吃饭了
	 */
	public void eatAnim() {
		Animation anim = AnimationUtils
				.loadAnimation(mActivity, R.anim.left_in);
		lin_bottom_left.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.startAnimation(anim);
		tv_simple_hour.setText("吃饭了");
	}

	/**
	 * 洗衣代码
	 */
	public void washClothAnim() {
		Animation anim = AnimationUtils
				.loadAnimation(mActivity, R.anim.left_in);
		lin_bottom_left.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.startAnimation(anim);
		p2 = (String) SPUtils.get(mActivity, "washclothes", "19",
				Constance.PRICEINFO);
		tv_simple_hour.setText("手洗衣服(" + p2 + "元/小时)");
	}

	/**
	 * 生鲜
	 */
	public void greensAnim() {
		Animation anim = AnimationUtils
				.loadAnimation(mActivity, R.anim.left_in);
		lin_bottom_left.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.setVisibility(View.VISIBLE);
		rea_homemake_simple_hour.startAnimation(anim);
		p2 = (String) SPUtils.get(mActivity, "washclothes", "19",
				Constance.PRICEINFO);
		tv_simple_hour.setText(getString(R.string.main_wash_clothes));
	}

	public void recycleBitmap() {
		if (null != giflist && giflist.size() > 0) {
			for (int i = 0; i < giflist.size(); i++) {
				giflist.get(i).recycle();
			}
			giflist.clear();
			giflist = null;

		}

	}

	/***
	 * 如果有人抢应了订单 bindle回调到此处处理 弹出对话框等操作
	 */
	private MsgService msgService;
	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			msgService = ((MsgService.MsgBinder) service).getService();
			msgService.NearFkStateListener(new NearFkStateListener() {
				@Override
				public void fkInfo(final VegOrderhFkState nearFkState) {

					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (null != nearFkState
									&& nearFkState.getState() == 1) {
								if (null == nearFkState.getResp()
										|| null == nearFkState.getResp()
												.getInfo()) {
									return;
								}

								fkInfo = nearFkState.getResp().getInfo();
								if (null != fkInfo) {
									WaitOrderCustomProgress.dissmiss();
									// 显示抢单成功dialog
									OrderCustomProgress.show(
											fragView.getContext(), true,
											fkInfo, wTypes,
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													// 点击去付款按钮回调,可以跳转到付款页面
													topay();
												}

											});
									CustomProgress.dissmiss();
								} else {
									if (null != timer) {
										timer.cancel();
									}
								}
							} else {
								if (null != timer) {
									timer.cancel();
								}
								CustomProgress.dissmiss();
							}
						}

					});
				}
			});
		}
	};

	/**
	 * 跳转支付页面
	 */
	private void topay() {
		OrderCustomProgress.dissmiss();
		intent = new Intent(mActivity, PayForActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fkInfo", fkInfo);
		intent.putExtras(bundle);
		intent.putExtra("oid", oid);
		intent.putExtra("wtype", wType);
		fragView.getContext().startActivity(intent);
		if (null != timer)
			timer.cancel();
	}

	// ------------点击事件开始-------------
	@OnClick(R.id.rb_back)
	public void rbBack(View view) {
		VisibleBottom();
		imgShowTopView.setVisibility(View.VISIBLE);
		hmAnimInVisible(true);
	}

	// 点击上门按钮
	@OnClick(R.id.imgShowTopView)
	public void imgShowTopView(View view) {
		leftAnimVisible(position);
	}

	// 专业保洁
	@OnClick(R.id.rea_greens)
	public void reaGreens(View view) {
		position = 0;
		setBg(position);
		wType = CMD.SUBMIT_VEGETABLES_CLEANING;
		nearFK(myLoc.longitude + "", myLoc.latitude + "");
		wTypes = CMD.FH;
	}

	/*// 家庭保洁
	@OnClick(R.id.rea_homemake)
	public void reaHomemake(View view) {
		position = 1;
		setBg(position);
		wType = CMD.SUBMIT_HOUSEHOLD_CLEANING;
		nearFK(myLoc.longitude + "", myLoc.latitude + "");
		wTypes = CMD.HW;
	}

	// 吃饭了
	@OnClick(R.id.rea_food)
	public void reaFood(View view) {
		position = 2;
		setBg(position);
		wType = CMD.SUBMIT_DINNER;
		nearFK(myLoc.longitude + "", myLoc.latitude + "");
		wTypes = CMD.HD;
	}

	// 手洗衣服
	@OnClick(R.id.rea_cloth)
	public void reaCloth(View view) {
		position = 3;
		setBg(position);
		wType = CMD.SUBMIT_WASHCLOSE_CLEANING;
		wTypes = CMD.WH;
	}*/

	// 手洗衣服
	@OnClick(R.id.rea_work)
	public void reaWork(View view) {
		position = 1;
		setBg(position);
		wTypes = CMD.JS;
	}

	@OnClick(R.id.btn_target)
	public void btnTarget(View view) {
		imgShowTopView.setVisibility(View.VISIBLE);
	}

	//首页“多多心语”按钮，已作废
	/*@OnClick(R.id.rbRight)
	public void rbRight(View view) {
		//使用Intent进行页面跳转
		Intent intent = new Intent(mActivity, LoadUrlActivity.class);
		Bundle bundle = new Bundle();
		//存入key为type，value为8
		bundle.putInt("type", 8);
		//存入bundle
		intent.putExtras(bundle);
		//界面跳转
		startActivity(intent);
	}*/

	@OnClick(R.id.rbRight2)
	public void rbRight2(View view) {
		Intent intent = new Intent(mActivity, LoadUrlActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("type", 1);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@OnClick(R.id.btn_loc)
	public void btnLoc(View view) {
		MyLoc(myLoc);
	}

	@OnClick(R.id.rea_main_info)
	public void reaMainInfo(View view) {
		String uid = (String) SPUtils.get(mActivity, "uid", "",
				Constance.HUDUDUUSER);
		if (uid != null && uid.equals("") || uid.equals("null")) {
			Intent intent = new Intent(mActivity, LoginActivity.class);
			startActivityForResult(intent, Constance.REQUESTCODE_LOGIN);
		} else {
			showLeftMenu();
		}
	}

	@OnClick(R.id.rea_homemake_complex_hour)
	public void rea_homemake_complex_hour(View view) {
		// 工作描述：日常保洁：1
		String mobile = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);
		if (null == mobile || mobile.equals("")) {
			intent = new Intent(view.getContext(), LoginActivity.class);
			startActivityForResult(intent, Constance.REQUESTCODE_LOGIN);
			return;
		}
		String addrs = btn_addr.getText().toString().trim();
		if (!addrs.equals("") && !addrs.equals(getString(R.string.move))) {
			try {
				mActivity.unbindService(conn);
			} catch (Exception e) {
			}
			intentActHm(1, p1);
		} else {
			CommonMethod.makeNoticeShort(mActivity, "请选择地址");
		}
	}

	@OnClick(R.id.rea_homemake_simple_hour)
	public void rea_homemake_simple_hour(View view) {
		// 工作描述： 简单打扫 ：2
		String mobile = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);
		if (null == mobile || mobile.equals("")) {
			intent = new Intent(view.getContext(), LoginActivity.class);
			startActivityForResult(intent, Constance.REQUESTCODE_LOGIN);
			return;
		}
		String addrs = btn_addr.getText().toString().trim();
		if (!addrs.equals("") && !addrs.equals(getString(R.string.move))) {
			try {
				mActivity.unbindService(conn);
			} catch (Exception e) {
			}
			if (position == 1) {
				addrs = userAddr;
				String village = ed_addr.getText().toString().trim();
				if (!village.equals("")) {
					addrs = addrs + "," + village;
				}
				if (null == addrs || addrs.equals("") || null == loc
						|| loc.equals("")) {
					CommonMethod.makeNoticeShort(mActivity, "地址错误");
					return;
				}
				isSimpleClear = true;
				submitHouseholdClean(2 + "", addrs, p2,
						TimesUtils.getNowTimeAdd30(), "1");
			} else {
				intentActHm(2, p2);
				isSimpleClear = false;
			}

		} else {
			CommonMethod.makeNoticeShort(mActivity, "请选择地址");
		}
	}

	@OnClick(R.id.rea_homemake_min)
	public void rea_homemake_min(View view) {
		// 工作描述： 饭后洗碗：3
		String mobile = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);
		if (null == mobile || mobile.equals("")) {
			intent = new Intent(view.getContext(), LoginActivity.class);
			startActivityForResult(intent, Constance.REQUESTCODE_LOGIN);
			return;
		}
		String addrs = btn_addr.getText().toString().trim();
		if (!addrs.equals("") && !addrs.equals(getString(R.string.move))) {
			try {
				mActivity.unbindService(conn);
			} catch (Exception e) {
			}
			if (position == 1) {
				addrs = userAddr;
				String village = ed_addr.getText().toString().trim();
				if (!village.equals("")) {
					addrs = addrs + "," + village;
				}
				if (null == addrs || addrs.equals("") || null == loc
						|| loc.equals("")) {
					CommonMethod.makeNoticeShort(mActivity, "地址错误");
					return;
				}
				CustomProgress.show(mActivity, "", false, null);
				isWashDishes = true;
				submitHouseholdClean(3 + "", addrs, p3,
						TimesUtils.getNowTimeAdd30(), "0.5");
			} else {
				intentActHm(2, p2);
				isWashDishes = false;
			}
		} else {
			CommonMethod.makeNoticeShort(mActivity, "请选择地址");
		}
	}

	@OnClick(R.id.btn_addr)
	public void btnAddr(View view) {
		if (null != poiInfos && poiInfos.size() > 0) {
			Intent intent = new Intent(mActivity, NearbyAddrActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("poiInfo", (Serializable) poiInfos);
			intent.putExtras(bundle);
			this.startActivityForResult(intent,
					Constance.REQUESTCODE_NEAYBYADDE);
			mActivity.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		} else {
			CommonMethod.makeNoticeShort(mActivity, "附近没有可用地址");
		}
	}

	// ------------点击事件结束-------------

	public List<NameValuePair> paramss(String Job_Desc, String addrs,
			String pricem, String ser_time, String hour) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String mobile = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);
		params.add(new BasicNameValuePair("cid", mobile)); // 顾客ID
		params.add(new BasicNameValuePair("jobdesc", Job_Desc + ""));
		params.add(new BasicNameValuePair("servertime", ser_time)); // 上门服务的时间
		params.add(new BasicNameValuePair("address", addrs)); // 服务地址
		params.add(new BasicNameValuePair("location", loc)); // 服务地点坐标
																// "纬度，经度"
		params.add(new BasicNameValuePair("workinghours", Utils.m2(Double
				.parseDouble(hour)))); // 工作时长
		// 以小时为一个单位
		params.add(new BasicNameValuePair(CMD.NEED_PARAMETER_TEXT,
				CMD.NEED_PARAMETER_VALUE));
		return params;
	}

	/**
	 * 提交家庭保洁订单
	 */
	public void submitHouseholdClean(final String Job_Desc, final String addrs,
			final String price, final String ser_time, final String hour) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final HouseHoldCleaningState state = resolution.resolution(
						mActivity, CMD.SUBMIT_HOUSEHOLD_CLEANING,
						paramss(Job_Desc, addrs, price, ser_time, hour));
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state == null) {
							CommonMethod.makeNoticeShort(mActivity, "数据解析出错!");
						}
						CustomProgress.dissmiss();
						if (state.getState() == 1) {
							Intent intent = new Intent(
									"com.example.communication.MSG_ACTION");
							oid = state.getOid();
							intent.putExtra("oid", state.getOid());
							intent.putExtra("wType", wType);
							mActivity.bindService(intent, conn,
									Context.BIND_AUTO_CREATE);
							showOrderDialog();

						} else {
							CommonMethod.makeNoticeShort(mActivity, "提交失败!:"
									+ state.getCode());
						}
					}

				});
			}
		}).start();
	}

	/**
	 * 发布订单成功的动画，,,
	 */
	private void showOrderDialog() {
		String str = "";
		VisibleBottom();
		hmAnimInVisible(true);
		if (isSimpleClear == true) {
			str = "一键即时服务,1小时" + p2 + "元";
			isSimpleClear = false;
		}
		if (isWashDishes == true) {
			int p = Integer.parseInt(p3) / 2;
			str = "一键即时服务,半小时" + p + "元";
			isWashDishes = false;
		}
		// 简单打扫 饭后洗碗发乎成功后直接显示等待对话框
		WaitOrderCustomProgress.show(mActivity, false, new OnClickListener() {

			@Override
			public void onClick(View v) {
				VisibleBottom();
				hmAnimInVisible(true);
				CustomProgress.show(mActivity, "", true, null);
				// 取消订单
				cancelOrder();
			}
		}, str);
	}

	public List<NameValuePair> paramsCancelOrder() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String mobile = (String) SPUtils.get(mActivity, "mobile", "",
				Constance.HUDUDUUSER);
		params.add(new BasicNameValuePair("cid", mobile));
		params.add(new BasicNameValuePair("oid", oid));
		params.add(new BasicNameValuePair("wtype", wTypes));
		return params;
	}

	public void cancelOrder() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final GetCodeBean bean = ParseHttpData.cancelOrder(mActivity,
						CMD.CCANCEL, paramsCancelOrder());
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (null == bean) {
							CommonMethod.makeNoticeShort(mActivity, "数据解析出错!");
							return;
						}
						if (bean.getState() != 1) {
							CommonMethod.makeNoticeShort(mActivity, "取消失败");

						}
						WaitOrderCustomProgress.dissmiss();
						CustomProgress.dissmiss();
					}

				});
			}
		}).start();
	}


}
