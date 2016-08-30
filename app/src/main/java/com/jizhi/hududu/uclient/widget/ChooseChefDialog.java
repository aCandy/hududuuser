package com.jizhi.hududu.uclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neusoft.huduoduoapp.R;


/**
 * 选择厨师Dialogs
 * @date 2015年8月5日 15:45:27
 * @author Xuj
 */
public class ChooseChefDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private Button sure;
	private Button cancel;
	private CallBackChooseChef listener;
	private String name;
	private TextView text;
	
	public ChooseChefDialog(Context context,String name,CallBackChooseChef listener) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.name = name;
		this.listener = listener;
	}





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_chef_dialog);
		sure = (Button)findViewById(R.id.sure);
		cancel = (Button)findViewById(R.id.cancel);
		text = (TextView) findViewById(R.id.text);
		text.setText(Html.fromHtml("<font color='#535353'>亲~你确定要选择</font><font color='#FF9900'>"+name+"</font><font color='#535353'>上门做饭吗？</font>"));
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sure:
			listener.callChoose(true);
			dismiss();
			break;
		case R.id.cancel:
			listener.callChoose(false);
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	
	public interface CallBackChooseChef{
		public void callChoose(boolean chooser);
	}
	
	
	
	

}