package com.jizhi.hududu.uclient.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.hcs.hududu.uclient.utils.DownLoadingApkUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jizhi.hududu.uclient.bean.VersionBean;
import com.jizhi.hududu.uclient.fragment.MainFragment;
import com.jizhi.hududu.uclient.fragment.MenuFragment;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.jizhi.hududu.uclient.util.Constance;
import com.neusoft.huduoduoapp.R;

/**
 * 呼多多主 地图下单相关
 * 
 * @author huChangSheng
 * @time 2015-7-27 下午6:11:02
 * @version 1.0
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends SlidingFragmentActivity {

	//主界面Fragment
	private Fragment main_fragment;
	//菜单(侧滑)界面Fragment
	private Fragment menu_fragment;
	//@TargetApi该注解用于Android版本兼容
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//加载的布局是个帧布局，由此可见MainActivity的布局是通过Fragment动态替换的
		setContentView(R.layout.layout_main);
		//初始化SidingMenu数据，savedInstanceState的作用保存当前Activity的状态信息
		initSlidingMenu(savedInstanceState);
	}

	private void initSlidingMenu(Bundle savedInstanceState) {
		/**
		 * check if the content frame contains the menu frame
		 * R.layout.menu_frame布局文件仍只是一个帧布局文件
		 */
		setBehindContentView(R.layout.menu_frame);
		getSlidingMenu().setSlidingEnabled(true);
		// 设置触摸屏幕的模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在正文布局中通过手势也可以打开SlidingMenu
		// TOUCHMODE_MARGIN 边缘模式，在正文布局的边缘处通过手势可以找开SlidingMenu
		// TOUCHMODE_NONE 自然是不能通过手势打开SlidingMenu了
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// set the Above View Fragment
		/**
		 * 如果savedInstanceState!=null说明里面是有Activity的状态信息
		 * 就可以通过KEY("mContent")获取(存入的方法定义在下面)
		 */
		if (savedInstanceState != null) {
			main_fragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}
		//创建主界面Fragment
		if (main_fragment == null) {
			main_fragment = new MainFragment();
		}
		//创建菜单界面Fragment
		if(menu_fragment == null){
			menu_fragment = new MenuFragment();
		}
		//动态替换主界面Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, main_fragment).commit();

		/**
		 * set the Behind View Fragment
		 * 动态替换菜单界面Fragment
		 */
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame,menu_fragment).commit();
		/**
		 * customize the SlidingMenu
		 * 获取SlidingMenu
		 */
		SlidingMenu sm = getSlidingMenu();
		//SlidingMenu划出时主页面显示的剩余宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//设置滑动时菜单的是否淡入淡出
		sm.setFadeEnabled(false);
		//设置滑动时拖拽效果
		sm.setBehindScrollScale(0.25f);
		//SlidingMenu滑动时的渐变程度
		sm.setFadeDegree(0.25f);
		/**
		 * 将home.png图片转换为Drawable对象
		 */
		Resources resources = this.getResources();
		Drawable drawable = resources.getDrawable(R.drawable.home);
		//设置SlidingMenu的背景
		sm.setBackground(drawable);
		//设置侧滑栏的显示动画
		sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
		//设置主界面的显示动画
		sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
			}
		});
		
		//DownLoadingApkUtil down = new DownLoadingApkUtil(this);
//		down.checkUpdateInfo();
	}
	
	
	
	
	
	/**
	 * 检查版本更新
	 */
	/*public void checkVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				final VersionBean state = resolution.submit(MainActivity.this, CMD.CHECKED_VERSION,params());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state.getState() == 1) {
							CommonMethod.makeNoticeShort(MainActivity.this, "提交成功!");
							Intent intent = new Intent();
							intent.putExtra("oid", state.getOid());
							setResult(Constance.RESULTCODE_HOMEMAKE, intent);
							finish();
						} else {
							CommonMethod.makeNoticeShort(MainActivity.this,"提交失败!:" + state.getCode());
						}
						dialog.dismiss();
					}
				});
			}
		}).start();
	}*/
	
	
	
	
	
	
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//存储Activity的状态信息
		getSupportFragmentManager().putFragment(outState, "mContent", main_fragment);
	}

	//菜单Fragment未知调用
	public MenuFragment getMenu_fragment() {
		return (MenuFragment)menu_fragment;
	}
	//主界面Fragment未知调用
	public MainFragment getMain_fragment() {
		return (MainFragment)main_fragment;
	}

	private long exitTime = 0;
	//物理按键监听器，此处为返回键监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {

				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.addCategory(Intent.CATEGORY_HOME);
//				startActivity(intent);
//				HuKeApplications.getInstance().exit();
				// 杀死该应用进程
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
