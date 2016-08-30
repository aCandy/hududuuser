package com.jizhi.hududu.uclient.main;

import net.sourceforge.simcpux.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jizhi.hududu.uclient.bean.Share;
import com.neusoft.huduoduoapp.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
/**
 * 分享界面
 *
 */
public class ShareAppActivity extends Activity implements OnClickListener{

	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	
	
	private Share bean;
	
	private TextView tv_title;
	
	public void finishAct(View view) {
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sharetofriend);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.bar_hududu);
	    Button button = (Button)findViewById(R.id.sharetofriends);
	    button.setOnClickListener(this);
	    tv_title = (TextView)findViewById(R.id.tv_title);
	    tv_title.setText(R.string.share);
	    
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bean = (Share)bundle.getSerializable("share");
		
		// 设置分享内容
		mController.setShareContent(bean.getWxshare_desc());
		// 设置分享图片, 参数2为图片的url地址 
		mController.setShareMedia(new UMImage(this,bean.getWxshare_img()));
		
		
        // 配置需要分享的相关平台
        configPlatforms();
   
	}
	
	/**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        // 添加微信、微信朋友圈平台
        addWXPlatform();
        
        UMImage urlImage = new UMImage(this,bean.getWxshare_img());
        
        //添加微信
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(bean.getWxshare_desc());
        weixinContent.setTitle(bean.getWxshare_title());
        weixinContent.setTargetUrl(bean.getWxshare_uri());
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);
        
        
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(bean.getWxshare_desc());
        circleMedia.setTitle(bean.getWxshare_title());
        circleMedia.setTargetUrl(bean.getWxshare_uri());
        circleMedia.setShareMedia(urlImage);
        mController.setShareMedia(circleMedia);
        
        
        
        QZoneShareContent qzone = new QZoneShareContent();  
        qzone.setTargetUrl(bean.getWxshare_uri());  
        qzone.setTitle(bean.getWxshare_title());
        qzone.setShareContent(bean.getWxshare_desc());
        qzone.setShareImage(urlImage);
        mController.setShareMedia(qzone);
        
        
        QQShareContent qqShareContent = new QQShareContent();  
        qqShareContent.setTargetUrl(bean.getWxshare_uri());// 设置点击消息的跳转URL  
        qqShareContent.setTitle(bean.getWxshare_title());// 设置分享标题  
        qqShareContent.setShareContent(bean.getWxshare_desc());// 设置分享内容
        qqShareContent.setShareImage(urlImage);//设置图片
        
        
        mController.setShareMedia(qqShareContent);  
    }
    
    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        String appId = "1104773063";
        String appKey = "6vskvM7L0kt9Wn0Q";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,appId, appKey);
        qqSsoHandler.addToSocialSDK();
        
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

        
        
    }
    

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "wx8ecda1951fc8abbf";
//        String appSecret = "0213f1e5986999c9df0c14f892743703";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, Constants.APP_ID, Constants.AppSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this,Constants.APP_ID, Constants.AppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        
    }


	@Override
	public void onClick(View view) {
//        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
              SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
        mController.openShare(this, false);
	}
    
    

	
	
}
