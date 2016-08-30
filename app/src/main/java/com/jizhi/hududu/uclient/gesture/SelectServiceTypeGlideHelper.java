package com.jizhi.hududu.uclient.gesture;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jizhi.hududu.uclient.dao.SelectServiceTypeGlideListenerDao;

public class SelectServiceTypeGlideHelper implements OnTouchListener {
	private Context context;
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;
	SelectServiceTypeGlideListenerDao dao;

	public SelectServiceTypeGlideHelper(final Context context,
			SelectServiceTypeGlideListenerDao dao) {
		this.context = context;
		this.dao = dao;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 当手指按下的时候
			x1 = event.getX();
			y1 = event.getY();
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			
		}else if (event.getAction() == MotionEvent.ACTION_UP) {
			// 当手指离开的时候
			x2 = event.getX();
			y2 = event.getY();
			if (y1 - y2 > 50) {
				// up
			} else if (y2 - y1 > 50) {
				// down
			} else if (x1 - x2 > 50) {
				// left
				dao.OnGlideLeft();
			} else if (x2 - x1 > 50) {
				// right
				dao.OnGlideRight();
			}
		}
		return false;
	}
}
