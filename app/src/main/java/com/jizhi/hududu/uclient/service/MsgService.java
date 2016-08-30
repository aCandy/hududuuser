package com.jizhi.hududu.uclient.service;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.hcs.hududu.uclient.utils.SPUtils;
import com.jizhi.hududu.uclient.bean.OrderhMFkState;
import com.jizhi.hududu.uclient.bean.VegOrderhFkState;
import com.jizhi.hududu.uclient.dao.NearFkStateListener;
import com.jizhi.hududu.uclient.json.ParseHttpData;
import com.jizhi.hududu.uclient.net.CMD;
import com.jizhi.hududu.uclient.util.Constance;

//import com.jizhi.hududu.uclient.dao.NearFkStateListener;
/**
 * 抢单Service*/

public class MsgService extends Service {
	public VegOrderhFkState fkState;
	private Timer timerCancel;
	public NearFkStateListener fkStateListener;
	private String oid;
	private String wType;

	/**
	 * 返回一个Binder对象
	 */
	@Override
	public IBinder onBind(Intent intent) {
		oid = intent.getStringExtra("oid");
		wType = intent.getStringExtra("wType");
		new Work().start();
		return new MsgBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		super.unbindService(conn);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public MsgService getService() {
			return MsgService.this;
		}
	}

	class Work extends Thread {

		@Override
		public void run() {
			String body = "option=canswerd&oid=" + oid;
			String str2 = jniFunction.getorderanswerd(wType, body);
//			if(wType.equals(CMD.SUBMIT_VEGETABLES_CLEANING)){
				fkState = ParseHttpData.getVegOrderfkInfo(str2);
//			}
//			System.out.println(oid + ",,," + str2);
			if (null != fkStateListener) {
				if (null != fkState && fkState.getState() == 1) {
					if (null == fkState.getResp().getInfo()) {
						return;
					}
					fkStateListener.fkInfo(fkState);
				}
			}
		}
	}

	/**
	 * 注册回调接口的方法，供外部调用
	 * 
	 * @param onProgressListener
	 */
	public void NearFkStateListener(NearFkStateListener fkStateListener) {
		this.fkStateListener = fkStateListener;
	}

	/**
	 * 增加get()方法，供Activity调用
	 * 
	 */
	public VegOrderhFkState getNearFkInfo() {
		return fkState;
	}
}
