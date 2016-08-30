package com.jizhi.hududu.uclient.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * 未知服务*/
public class jzService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Timer timer = new Timer();
		timer.schedule(new Work(), 0, 3000);
	}

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

}

class Work extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String body = "option=canswerd&oid=55c9ea618d20745c478b568c";
		String url = "http://123.56.40.5/Jzhw/common";
		String str2 = jniFunction.getorderanswerd(url, body);
		Log.i("hududu", str2);
	}

}
