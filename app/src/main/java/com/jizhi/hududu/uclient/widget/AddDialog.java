package com.jizhi.hududu.uclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neusoft.huduoduoapp.R;


/**
 * 添加优惠券
 * @date 2015年9月15日 11:36:48
 * @author Xuj
 */
public class AddDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private Button sure;
	private Button cancel;
	private TextView showDialog;
	private CallBackClose listener;
	private String text;
	
	
	public AddDialog(Context context,CallBackClose listener,String text) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.listener = listener;
		this.text = text;
	}





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		sure = (Button)findViewById(R.id.sure);
		cancel = (Button)findViewById(R.id.cancel);
		showDialog = (TextView) findViewById(R.id.showDialog);
		showDialog.setText(text);
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sure:
			listener.callClose(true);
			dismiss();
			break;
		case R.id.cancel:
			listener.callClose(false);
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	
	public interface CallBackClose{
		public void callClose(boolean chooser);
	}
	
	
	
	

}