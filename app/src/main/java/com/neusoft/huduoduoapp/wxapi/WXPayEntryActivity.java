package com.neusoft.huduoduoapp.wxapi;

import net.sourceforge.simcpux.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jizhi.hududu.uclient.main.MyOrderActivity;
import com.jizhi.hududu.uclient.main.PayForActivity;
import com.jizhi.hududu.uclient.util.CommonMethod;
import com.neusoft.huduoduoapp.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付结果回调页面
 * 
 * @author huChangSheng
 * @time 2015-8-18 下午3:11:35
 * @version 1.0
 * 
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		String a=resp.errStr;
		// 0成功 -1失败 -2用户取消
		if (resp.errCode == 0) {
			CommonMethod.makeNoticeShort(getApplicationContext(),
					getString(R.string.pay_success));
			Intent intent = new Intent(WXPayEntryActivity.this,
					MyOrderActivity.class);
			if (null != PayForActivity.payActivity) {
				PayForActivity.payActivity.finish();
			}

			startActivity(intent);
		} else if (resp.errCode == -2) {
			CommonMethod.makeNoticeLong(getApplicationContext(),
					getString(R.string.pay_cancel));
		} else {
			CommonMethod.makeNoticeLong(getApplicationContext(),
					getString(R.string.pay_fail)+"");

		}
		WXPayEntryActivity.this.finish();
	}
}