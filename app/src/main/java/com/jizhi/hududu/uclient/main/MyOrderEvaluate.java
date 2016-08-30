package com.jizhi.hududu.uclient.main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcs.hududu.uclient.utils.SDCardUtils;
import com.hcs.hududu.uclient.utils.StrUtil;
import com.hcs.hududu.uclient.utils.Toasts;
import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.CaiGridAdapter;
import com.jizhi.hududu.uclient.bean.Order;
import com.jizhi.hududu.uclient.bean.Photos;
import com.jizhi.hududu.uclient.bean.Share;
import com.jizhi.hududu.uclient.bean.ShareState;
import com.jizhi.hududu.uclient.json.EvaluateResolution;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.DataForMat;
import com.jizhi.hududu.uclient.util.PicSizeUtils;
import com.jizhi.hududu.uclient.util.SingsHttpUtils;
import com.jizhi.hududu.uclient.util.UtilFile;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.jizhi.hududu.uclient.widget.SelectPicPopupWindow;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

/**
 * 订单评价
 * 
 * @author Xuj
 * @date 2015年8月14日 09:24:53
 */
@SuppressLint("NewApi")
public class MyOrderEvaluate extends Activity implements OnClickListener {

	@ViewInject(R.id.image5)
	private RadioButton radio5; // 非常好

	@ViewInject(R.id.image4)
	private RadioButton radio4; // 满意

	@ViewInject(R.id.image3)
	private RadioButton radio3; // 中评

	@ViewInject(R.id.image2)
	private RadioButton radio2; // 一般

	@ViewInject(R.id.image1)
	private RadioButton radio1; // 差评

	@ViewInject(R.id.linearLayout5)
	private LinearLayout linearLayout5;

	@ViewInject(R.id.linearLayout4)
	private LinearLayout linearLayout4;

	@ViewInject(R.id.linearLayout3)
	private LinearLayout linearLayout3;

	@ViewInject(R.id.linearLayout2)
	private LinearLayout linearLayout2;

	@ViewInject(R.id.layout_main)
	private LinearLayout layout_main;

	@ViewInject(R.id.linearLayout_nimin)
	private LinearLayout linearLayout_nimin;

	@ViewInject(R.id.five_star)
	private ImageView five_star; // 五星
	@ViewInject(R.id.four_star)
	private ImageView four_star; // 四星
	@ViewInject(R.id.three_star)
	private ImageView three_star; // 三星
	@ViewInject(R.id.two_star)
	private ImageView two_star; // 两星
	@ViewInject(R.id.one_star)
	private ImageView one_star; // 一星

	@ViewInject(R.id.text5)
	private TextView text5; // 五星

	@ViewInject(R.id.text4)
	private TextView text4; // 四星

	@ViewInject(R.id.text3)
	private TextView text3; // 三星

	@ViewInject(R.id.text2)
	private TextView text2; // 两星

	@ViewInject(R.id.text1)
	private TextView text1; // 一星

	@ViewInject(R.id.camera)
	private ImageView camera; // 照相机

	@ViewInject(R.id.project_name)
	private TextView project_name; // 项目名称
	@ViewInject(R.id.wage)
	private TextView wage; // 总价
	@ViewInject(R.id.cName)
	private TextView cName; // 服客姓名

	@ViewInject(R.id.tv_title)
	private TextView title;

	@ViewInject(R.id.evaluation_change)
	private TextView evaluation_change;

	@ViewInject(R.id.feed)
	private EditText feed;

	@ViewInject(R.id.anonymous)
	private CheckBox anonymous;

	@ViewInject(R.id.unit)
	private TextView unit;

	@ViewInject(R.id.unit_value)
	private TextView unit_value;

	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	private Order order; // 上级菜单传递过来的对象

	private int currentChoose = 5; // 当前选择的星级

	private SelectPicPopupWindow menuWindow;

	private int max = 5; // 最多只能上传5张图片

	@ViewInject(R.id.gridView)
	private GridView grid;

	@ViewInject(R.id.vege_gridview)
	private GridView gridView;

	@ViewInject(R.id.submit)
	private Button submit;

	private GridAdapter adapter;

	private List<Photos> photos_list;

	private String filePath; // 文件路径

	private String fileName; // 文件名
	
	private String idCamerapath;

	@ViewInject(R.id.image)
	private ImageView image;

	private PopupWindow pop = null;

	private LinearLayout ll_popup;

	private String message = "";

	private Share share;

	private CustomProgress dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.evaluation_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bar_hududu);
		ViewUtils.inject(this); // Xutil必须调用的一句话
		initData();
		initView();
	}

	private void initView() {
		feed.clearFocus();
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				submitEvaluation();
			}
		});
		anonymous.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean checked) {
				if (checked) {
					order.setAnonymous("s");
				} else {
					order.setAnonymous("h");
				}
			}
		});
		linearLayout_nimin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (order.getAnonymous().equals("h")) {
					order.setAnonymous("s");
					anonymous.setChecked(false);
				} else {
					order.setAnonymous("h");
					anonymous.setChecked(true);
				}
			}
		});

		pop = new PopupWindow(MyOrderEvaluate.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				takePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 选择用图库图片
				// // 选择用图库图片
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, Constance.REQUESTCODE_CHOOSEIMG);
				if (null != pop) {
					pop.dismiss();
				}
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (null != pop) {
					pop.dismiss();
				}
				ll_popup.clearAnimation();
			}
		});

		photos_list = new ArrayList<Photos>();
		photos_list.add(new Photos(true, BitmapFactory.decodeResource(
				this.getResources(), R.drawable.icon_addpic_focused)));
		adapter = new GridAdapter(this);

		grid.setAdapter(adapter);

		grid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position + 1 == photos_list.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							MyOrderEvaluate.this, R.anim.activity_translate_in));
					pop.showAtLocation(findViewById(R.id.layout_main),
							Gravity.BOTTOM, 0, 0);
				} else {
					// Intent intent = new
					// Intent(MainActivity.this,GalleryActivity.class);
					// intent.putExtra("position", "1");
					// intent.putExtra("ID", arg2);
					// startActivity(intent);
				}
			}
		});

		camera.setOnClickListener(headClickListener);
	}

	private void initData() {
		tv_title.setText(getString(R.string.payorder));
		Bundle bundle = getIntent().getExtras();
		order = (Order) bundle.getSerializable("order");
		project_name.setText(order.getProject_name());
		wage.setText("￥" + order.getWage());
		cName.setText(order.getLname());
		unit.setText(order.getUnit());
		unit_value.setText(order.getWorking_hours()
				+ order.getWorking_hours_unit());
		String wtype = order.getWtype();
		// 设置图片
		if (wtype.equals(CMD.HW)) {
			if (order.getDesc().equals("1")) {
				image.setImageResource(R.drawable.zhuanyebaojie);
			} else if (order.getDesc().equals("2")) {
				image.setImageResource(R.drawable.jiandandasao);
			} else if (order.getDesc().equals("3")) {
				image.setImageResource(R.drawable.fanhouxifan);
			}
		} else if (wtype.equals(CMD.WH)) {
			image.setImageResource(R.drawable.shouxiyifu);
		} else if (wtype.equals(CMD.HD)) {
			unit_value.setText(order.getDishcount()
					+ order.getWorking_hours_unit());
			ImageLoader.getInstance().displayImage(
					CMD.NETURL + "uploads/" + order.getPic(), image,
					UtilImageLoader.getImageOptionsChef());
		} else if (wtype.equals(CMD.FH)) {
			image.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			gridView.setAdapter(new CaiGridAdapter(this, order.getCaiPic()));
			unit_value.setText(DataForMat.twoDecimalPlaces(order.getWeight())
					+ order.getWorking_hours_unit());
		} else {
			ImageLoader.getInstance().displayImage(
					CMD.NETURL + "uploads/" + order.getPic(), image,
					UtilImageLoader.getImageOptionsChef());
		}

	}

	public void finishAct(View view) {
		finish();
		int i = 0;
		for (Photos p : photos_list) {
			i++;
			p.getBitmap().recycle();
			Log.e("正在释放图片", i + "");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finishAct(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 提交评价
	 */
	public void submitEvaluation() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(this, getString(R.string.sumiting), true,
				null);
		RequestParams params = new RequestParams();
		params.addBodyParameter("cid", order.getCid());// 服客ID
		params.addBodyParameter("oid", order.getOid());// 订单id
		params.addBodyParameter("wtype", order.getWtype());// 服务类型
		if (!TextUtils.isEmpty(feed.getText().toString())) {
			params.addBodyParameter("lappraise", feed.getText().toString());// 评论内容
		} else {
			params.addBodyParameter("lappraise", "好评!");// 评论内容
		}
		params.addBodyParameter("lrate", currentChoose + "");// 评论分数
		params.addBodyParameter("noname", order.getAnonymous());// 是否匿名 h:匿名//
																// s:不匿名
		for (int i = 0; i < photos_list.size(); i++) {
			if (photos_list.get(i).getUrl() != null
					&& !"".equals(photos_list.get(i).getUrl())) {
				params.addBodyParameter("appraisepic" + i, new File(photos_list
						.get(i).getUrl()));
			}
		}
		HttpUtils http = SingsHttpUtils.getHttp();
		http.configTimeout(30000);
		http.send(HttpRequest.HttpMethod.POST, CMD.SUBMIT_EVALUATION, params,
				new RequestCallBack<String>() {
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						EvaluateResolution json = new EvaluateResolution();
						ShareState state = json.resolution(responseInfo.result);
						if (state.getState() == 1) {
							share = state.getResp();
							message = "评价成功!";
							handler.sendEmptyMessage(2);
						} else {
							message = "服务器异常!";
							handler.sendEmptyMessage(1);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						message = "网络连接异常!";
						handler.sendEmptyMessage(1);
					}
				});
	}

	@Override
	public void onClick(View view) {
		if (currentChoose == 5) {
			radio5.setChecked(false);
			text5.setTextColor(getResources().getColor(R.color.evaluation_11));
			one_star.setImageResource(R.drawable.star_normal);
			two_star.setImageResource(R.drawable.star_normal);
			three_star.setImageResource(R.drawable.star_normal);
			four_star.setImageResource(R.drawable.star_normal);
			five_star.setImageResource(R.drawable.star_normal);
		} else if (currentChoose == 4) {
			radio4.setChecked(false);
			text4.setTextColor(getResources().getColor(R.color.evaluation_11));
			one_star.setImageResource(R.drawable.star_normal);
			two_star.setImageResource(R.drawable.star_normal);
			three_star.setImageResource(R.drawable.star_normal);
			four_star.setImageResource(R.drawable.star_normal);
		} else if (currentChoose == 3) {
			radio3.setChecked(false);
			text3.setTextColor(getResources().getColor(R.color.evaluation_11));
			one_star.setImageResource(R.drawable.star_normal);
			two_star.setImageResource(R.drawable.star_normal);
			three_star.setImageResource(R.drawable.star_normal);
		} else if (currentChoose == 2) {
			radio2.setChecked(false);
			text2.setTextColor(getResources().getColor(R.color.evaluation_11));
			one_star.setImageResource(R.drawable.star_normal);
			two_star.setImageResource(R.drawable.star_normal);
		} else if (currentChoose == 1) {
			radio1.setChecked(false);
			text1.setTextColor(getResources().getColor(R.color.evaluation_11));
			one_star.setImageResource(R.drawable.star_normal);
		}
		switch (view.getId()) {
		case R.id.image5:
			currentChoose = 5;
			text5.setTextColor(getResources().getColor(R.color.evaluation_tttt));
			evaluation_change.setText("非常好");
			one_star.setImageResource(R.drawable.star_press);
			two_star.setImageResource(R.drawable.star_press);
			three_star.setImageResource(R.drawable.star_press);
			four_star.setImageResource(R.drawable.star_press);
			five_star.setImageResource(R.drawable.star_press);
			radio5.setChecked(true);
			break;
		case R.id.image4:
			text4.setTextColor(getResources().getColor(R.color.evaluation_tttt));
			evaluation_change.setText("满意");
			one_star.setImageResource(R.drawable.star_press);
			two_star.setImageResource(R.drawable.star_press);
			three_star.setImageResource(R.drawable.star_press);
			four_star.setImageResource(R.drawable.star_press);
			five_star.setImageResource(R.drawable.star_normal);
			currentChoose = 4;
			radio4.setChecked(true);
			break;
		case R.id.image3:
			text3.setTextColor(getResources().getColor(R.color.evaluation_tttt));
			evaluation_change.setText("中评");
			one_star.setImageResource(R.drawable.star_press);
			two_star.setImageResource(R.drawable.star_press);
			three_star.setImageResource(R.drawable.star_press);
			four_star.setImageResource(R.drawable.star_normal);
			five_star.setImageResource(R.drawable.star_normal);
			currentChoose = 3;
			radio3.setChecked(true);
			break;
		case R.id.image2:
			text2.setTextColor(getResources().getColor(R.color.evaluation_tttt));
			evaluation_change.setText("一般");
			one_star.setImageResource(R.drawable.star_press);
			two_star.setImageResource(R.drawable.star_press);
			three_star.setImageResource(R.drawable.star_normal);
			four_star.setImageResource(R.drawable.star_normal);
			five_star.setImageResource(R.drawable.star_normal);
			currentChoose = 2;
			radio2.setChecked(true);
			break;
		case R.id.image1:
			text1.setTextColor(getResources().getColor(R.color.evaluation_tttt));
			evaluation_change.setText("差评");
			one_star.setImageResource(R.drawable.star_press);
			two_star.setImageResource(R.drawable.star_normal);
			three_star.setImageResource(R.drawable.star_normal);
			four_star.setImageResource(R.drawable.star_normal);
			five_star.setImageResource(R.drawable.star_normal);
			currentChoose = 1;
			radio1.setChecked(true);
			break;
		case R.id.one_star:

			break;
		case R.id.two_star:
			break;
		case R.id.three_star:
			break;
		case R.id.four_star:
			break;
		case R.id.five_star:
			break;
		case R.id.project_name:
			break;
		default:
			break;
		}
	}

	OnClickListener headClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.e("size", photos_list.size() + "");
			if (photos_list.size() == max) {
				CommonMethod.makeNoticeShort(MyOrderEvaluate.this,
						"亲~只能上传4张图片哦！");
				return;
			}
			if (StrUtil.isFastDoubleClick()) {
				return;
			}
			ll_popup.startAnimation(AnimationUtils.loadAnimation(
					MyOrderEvaluate.this, R.anim.activity_translate_in));
			pop.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM,
					0, 0);
		}
	};

	/**
	 * 点击拍照
	 */
	private void takePhoto() {
		if (SDCardUtils.isSDCardEnable()) {
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				// 指定存放拍摄照片的位置
				File imagePath = PicSizeUtils.createImageFile();
				filePath = imagePath.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(imagePath));
				startActivityForResult(takePictureIntent, CMD.CAPTURE_PICTURE);
			} catch (IOException e) {
				CommonMethod.makeNoticeShort(MyOrderEvaluate.this,
						"调用系统摄像头失败拉!");
				e.printStackTrace();
			}
			return;
		}
		CommonMethod.makeNoticeShort(MyOrderEvaluate.this, "当前内存卡不可用!");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CMD.CAPTURE_PICTURE && resultCode == RESULT_OK) {
			if (filePath != null && !"".equals(filePath)) {
				File f = new File(filePath);
				try {
					Bitmap bm = PicSizeUtils.getSmallBitmap(filePath);
					File compressFile = new File(PicSizeUtils.getAlbumDir(),
							"small_" + f.getName());
					FileOutputStream fos = new FileOutputStream(compressFile);
					bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
					filePath = compressFile.getAbsolutePath();
					PicSizeUtils.deleteTempFile(f.getAbsolutePath());
					Photos photo = new Photos(
							BitmapFactory.decodeFile(filePath), filePath);
					photos_list.add(0, photo);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					CommonMethod.makeNoticeShort(MyOrderEvaluate.this,
							"生成照片时出错拉!");
					// 删除已经创建的拍摄文件
					PicSizeUtils.deleteTempFile(filePath);
					PicSizeUtils.deleteTempFile(f.getAbsolutePath());
				}
				return;
			}
		} else if (requestCode == Constance.REQUESTCODE_CROPIDCARD
				&& (resultCode == 0 || resultCode == -1)) {
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				if (null == bitmap) {
					CommonMethod.makeNoticeShort(MyOrderEvaluate.this,
							"获取图片失败!");
					return;
				}
				Uri url;
				try {
					url = Uri.parse(saveBitmapToFile(bitmap));
					// 选择的是拍照 裁剪身份证
					Photos photo = new Photos(BitmapFactory.decodeFile(url
							.toString()), url.toString());
					photos_list.add(0, photo);
					adapter.notifyDataSetChanged();
					pop.dismiss();
					ll_popup.clearAnimation();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == Constance.REQUESTCODE_CHOOSEIMG
				&& (resultCode == 0 || resultCode == -1)) {
			if (data != null) {
				Uri uri = data.getData();
				Intent intent = getCropImageIntent(uri);
				startActivityForResult(intent, Constance.REQUESTCODE_CROPIDCARD);
			} else {
				Toasts.showShort(MyOrderEvaluate.this, "图片未找到...");
			}

		}

	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	private static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);// 黑边
		intent.putExtra("scaleUpIfNeeded", true);// 黑边
		intent.putExtra("return-data", true);
		return intent;
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return photos_list.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.evaluate_grid_item,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.pic);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == max - 1) {
				holder.image.setVisibility(View.GONE);
			} else {
				holder.image.setImageBitmap(photos_list.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				CommonMethod.makeNoticeLong(MyOrderEvaluate.this, message);
				break;
			case 2:
				Intent intent = new Intent(MyOrderEvaluate.this,
						ShareAppActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("share", share);
				intent.putExtras(bundle);
				startActivity(intent);
				CommonMethod.makeNoticeLong(MyOrderEvaluate.this, message);
				Intent returnIntent = new Intent();
				returnIntent.putExtra("oid", order.getOid());
				setResult(CMD.SUCCESS, returnIntent);
				finish();
				int i = 0;
				for (Photos p : photos_list) {
					i++;
					p.getBitmap().recycle();
					Log.e("正在释放图片", i + "");
				}
				break;
			}
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	};

	// 请求Gallery程序
	private void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, Constance.REQUESTCODE_CROPIDCARD);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "获取照片错误", Toast.LENGTH_LONG).show();
		}
	}

	// 封装请求Gallery的intent
	private static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 10);
		intent.putExtra("aspectY", 10);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 * 
	 * @param bitmap
	 * @param file
	 * @return error message if the saving is failed. null if the saving is
	 *         successful.
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static String saveBitmapToFile(Bitmap bitmap) throws IOException {
		BufferedOutputStream os = null;
		String _file = UtilFile.createHeadFileImg()
				+ "/"
				+ new DateFormat().format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA)) + ".png";
		try {
			File file = new File(_file);
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.fillInStackTrace();
				}
			}
		}
		return _file;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		int i = 0;
		// for (Photos p : photos_list) {
		// i++;
		// p.getBitmap().recycle();
		// Log.e("正在释放图片",i+"");
		// }
	}

}
