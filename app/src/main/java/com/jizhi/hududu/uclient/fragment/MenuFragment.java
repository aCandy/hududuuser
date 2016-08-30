package com.jizhi.hududu.uclient.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcs.hududu.uclient.utils.SDCardUtils;
import com.hcs.hududu.uclient.utils.SPUtils;
import com.hcs.hududu.uclient.utils.StrUtil;
import com.hcs.hududu.uclient.utils.Toasts;
import com.hcs.hududu.uclient.utils.UtilImageLoader;
import com.jizhi.hududu.uclient.adapter.LeftAdapter;
import com.jizhi.hududu.uclient.bean.LeftBean;
import com.jizhi.hududu.uclient.main.MainActivity;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.jizhi.hududu.uclient.util.PicSizeUtils;
import com.jizhi.hududu.uclient.util.UploadPic;
import com.jizhi.hududu.uclient.util.UtilFile;
import com.jizhi.hududu.uclient.widget.CircleImageView;
import com.jizhi.hududu.uclient.widget.CustomProgress;
import com.neusoft.huduoduoapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuFragment extends Fragment implements
		android.view.View.OnClickListener {

	private LeftAdapter adapter;
	private ListView listView;
	private CustomProgress dialog;

	private TextView mobile;

	private List<LeftBean> list;

	private CircleImageView head;

	private MainActivity activity;

	private PopupWindow pop = null;

	private LinearLayout ll_popup;

	private View mainView;

	private Resources resources;
	private String filePath; // 文件路径
	private String message;
	private String pic;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.leftmenu, null);
		initData();
		listView = (ListView) mainView.findViewById(R.id.listView);
		mobile = (TextView) mainView.findViewById(R.id.mobile);
		head = (CircleImageView) mainView.findViewById(R.id.head);
		head.setOnClickListener(this);
		adapter = new LeftAdapter(activity, list);
		listView.setAdapter(adapter);
		mobile.setText((String) SPUtils.get(activity, "hide_mobile", "请登录!",
				Constance.HUDUDUUSER));
		ImageLoader.getInstance().displayImage(
				CMD.PICPATH
						+ ((String) SPUtils.get(activity, "pic", "",
								Constance.HUDUDUUSER)), head,
				UtilImageLoader.getImageOptionsLoginHead());
		pop = new PopupWindow(activity);

		View popo_view = activity.getLayoutInflater().inflate(
				R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) popo_view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(popo_view);

		RelativeLayout parent = (RelativeLayout) popo_view
				.findViewById(R.id.parent);
		Button bt1 = (Button) popo_view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) popo_view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) popo_view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				takePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				// // 选择用图库图片
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, Constance.REQUESTCODE_CHOOSEIMG);
				if (null != pop) {
					pop.dismiss();
				}
			}
		});
		bt3.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		return mainView;
	}

	/**
	 * 提交头像
	 */
	public void submitHead() {
		if (dialog != null) {
			dialog.dismiss();
		}
		dialog = CustomProgress.show(activity, getString(R.string.sumiting),
				true, null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String json = UploadPic.uploadFile(
						new File(filePath),
						CMD.SUBMIT_HEAD
								+ "?cid="
								+ ((String) SPUtils.get(activity, "mobile", "",
										Constance.HUDUDUUSER)), activity);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int state = jsonObject.getInt("state");
					if (state == 1) {
						pic = jsonObject.getJSONObject("resp").getString("pic");
						if (pic != null && !"".equals(pic)) {
							handler.sendEmptyMessage(2);
							return;
						}
					} else {
						message = "服务器异常!";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
	}

	public void initData() {

		resources = activity.getResources();
		list = new ArrayList<LeftBean>();

		LeftBean myorder = new LeftBean(getString(R.string.my_order),
				resources.getDrawable(R.drawable.m1));
		LeftBean my_card_bag = new LeftBean(getString(R.string.my_card_bag),
				resources.getDrawable(R.drawable.m2));
		LeftBean recommended_award = new LeftBean(
				getString(R.string.recommended_award),
				resources.getDrawable(R.drawable.m3));
		LeftBean server_recruitment = new LeftBean(
				getString(R.string.server_recruitment),
				resources.getDrawable(R.drawable.m5));
		LeftBean about_us = new LeftBean(getString(R.string.about_us),
				resources.getDrawable(R.drawable.m6));
		LeftBean feedback = new LeftBean(getString(R.string.feedback),
				resources.getDrawable(R.drawable.m7));
		LeftBean use_help = new LeftBean(getString(R.string.use_help),
				resources.getDrawable(R.drawable.m8));
		LeftBean close_username = new LeftBean(
				getString(R.string.close_username),
				resources.getDrawable(R.drawable.m9));
		list.add(myorder);
		list.add(my_card_bag);
		list.add(recommended_award);
		list.add(server_recruitment);
		list.add(about_us);
		list.add(feedback);
		list.add(use_help);
		list.add(close_username);

	}

	public CircleImageView getHead() {
		return head;
	}

	public void setHead(CircleImageView head) {
		this.head = head;
	}

	@Override
	public void onClick(View arg0) {
		if (StrUtil.isFastDoubleClick()) {
			return;
		}
		ll_popup.startAnimation(AnimationUtils.loadAnimation(activity,
				R.anim.activity_translate_in));
		pop.showAtLocation(activity.findViewById(R.id.content_frame),
				Gravity.BOTTOM, 0, 0);
	}

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
				CommonMethod.makeNoticeShort(activity, "调用系统摄像头失败拉!");
				e.printStackTrace();
			}
			return;
		}
		CommonMethod.makeNoticeShort(activity, "当前内存卡不可用!");
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
			// String _filePath_file.replace(File.separatorChar +
			// file.getName(), "");
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CMD.CAPTURE_PICTURE
				&& resultCode == activity.RESULT_OK) {// 拍照
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
					submitHead();
				} catch (Exception e) {
					CommonMethod.makeNoticeShort(activity, "生成照片时出错拉!");
					// 删除已经创建的拍摄文件
					PicSizeUtils.deleteTempFile(filePath);
					PicSizeUtils.deleteTempFile(f.getAbsolutePath());
				}
				return;
			}
			//裁剪回调
		} else if (requestCode == Constance.REQUESTCODE_CROPIDCARD
				&& (resultCode == 0 || resultCode == -1)) { 
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				if (null == bitmap) {
					CommonMethod.makeNoticeShort(activity, "获取图片失败!");
					return;
				}
				Uri url;
				try {
					url = Uri.parse(saveBitmapToFile(bitmap));
					filePath = url.toString();
					pop.dismiss();
					ll_popup.clearAnimation();
					bitmap.recycle();
					submitHead();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				CommonMethod.makeNoticeShort(activity, "获取图片失败!");
			}
			//选择相册回调
		} else if (requestCode == Constance.REQUESTCODE_CHOOSEIMG
				&& (resultCode == 0 || resultCode == -1)) {
			if (data != null) {
				Uri uri = data.getData();
				 Intent intent = getCropImageIntent(uri);
				startActivityForResult(intent, Constance.REQUESTCODE_CROPIDCARD);
			} else {
				Toasts.showShort(getActivity(), "图片未找到...");
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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				CommonMethod.makeNoticeLong(activity, message);
				pop.dismiss();
				ll_popup.clearAnimation();
				break;
			case 2:
				ImageLoader.getInstance().displayImage(CMD.PICPATH + pic, head,
						UtilImageLoader.getImageOptionsLoginHead());
				ImageLoader.getInstance().displayImage(CMD.PICPATH + pic,
						activity.getMain_fragment().getImg_head(),
						UtilImageLoader.getImageOptionsLoginHead());
				SPUtils.put(activity, "pic", pic, Constance.HUDUDUUSER);
				pop.dismiss();
				ll_popup.clearAnimation();
				break;
			}
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	};

	public TextView getMobile() {
		return mobile;
	}

	public void setMobileText(String value) {

		mobile.setText(value);
	}
}
