package cn.paozhuanyinyu.social;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/7.
 */

public class SocialActivity extends Activity implements WbShareCallback,IUiListener,IWXAPIEventHandler {
    static final String TAG = "SocialActivity";
    static final int SHARE_TEXT_TO_WEIBO_ACTION = 0;
    static final int SHARE_IMAGE_TO_WEIBO_ACTION = 1;
    static final int SHARE_MUTI_IMAGE_TO_WEIBO_ACTION = 2;
    static final int SHARE_VIDEO_TO_WEIBO_ACTION = 3;

    static final int LOGIN_FROM_WEIBO_ACTION = 4;
    static final int LOGIN_FROM_QQ_ACTION = 5;
    static final int LOGIN_FROM_WECHAT_ACTION = 6;

    static final int SHARE_IMAGE_AND_TEXT_TO_QQ_ACTION = 7;
    static final int SHARE_IMAGE_AND_TEXT_TO_QZONE_ACTION = 8;
    static final int SHARE_IMAGE_TO_QQ_ACTION = 9;
    static final int SHARE_MUSIC_TO_QQ_ACTION = 10;

    static final int PLATFORM_QQ = 101;
    static final int PLATFORM_WEIBO = 102;
    static final int PLATFORM_WECHAT = 103;


    private static SocialResponse socialResponse;
    private WbShareHandler wbShareHandler;
    private SsoHandler ssoHandler;
    private Tencent mTencent;
    protected IWXAPI iwxapi;
    private String wechatAppId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getIntExtra("platform",-1)==PLATFORM_WEIBO){
            wbShareHandler = new WbShareHandler(this);
            wbShareHandler.registerApp();
        }else if(getIntent().getIntExtra("platform",-1)==PLATFORM_QQ){
            mTencent = Tencent.createInstance(SocialManager.getInstance().getQqAppId(),getApplicationContext());
        }else{
            wechatAppId = SocialManager.getInstance().getWechatAppId();
            this.iwxapi = WXAPIFactory.createWXAPI(this,wechatAppId,true);
            this.iwxapi.registerApp(wechatAppId);
            iwxapi.handleIntent(getIntent(),this);
        }
        int action = getIntent().getIntExtra("action",-1);
        switch(action){
            case SHARE_TEXT_TO_WEIBO_ACTION:
                shareTextToWeibo(getIntent().getStringExtra("text"));
                break;
            case SHARE_IMAGE_TO_WEIBO_ACTION:
                Uri uri = getIntent().getData();
                Glide.with(SocialActivity.this)
                        .asBitmap()
                        .load(uri)
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL){

                            @Override
                            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                shareImageToWeibo(getIntent().getStringExtra("text"),bitmap);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                Log.e(TAG,"onLoadFailed");
                            }
                        });

                break;
            case SHARE_MUTI_IMAGE_TO_WEIBO_ACTION:
                ArrayList<Uri> uris = getIntent().getParcelableArrayListExtra("uris");
                shareMutiImageToWeibo(getIntent().getStringExtra("text"),uris);
                break;
            case SHARE_VIDEO_TO_WEIBO_ACTION:
                shareVideoToWeibo(getIntent().getStringExtra("text"),getIntent().getData());
                break;
            case LOGIN_FROM_WEIBO_ACTION:
                loginWeibo();
                break;
            case LOGIN_FROM_QQ_ACTION:
                loginQQ();
                break;
            case SHARE_IMAGE_AND_TEXT_TO_QQ_ACTION:
                shareImageAndTextToQQ(getIntent().getBundleExtra("qq_bundle"));
                break;
            case SHARE_IMAGE_TO_QQ_ACTION:
                shareImageToQQ(getIntent().getBundleExtra("qq_bundle"));
                break;
            case SHARE_IMAGE_AND_TEXT_TO_QZONE_ACTION:
                shareImageAndTextToQzone(getIntent().getBundleExtra("qq_bundle"));
                break;
            case SHARE_MUSIC_TO_QQ_ACTION:
                shareMusicToQQ(getIntent().getBundleExtra("qq_bundle"));
                break;

            default:
                break;
        }

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(wbShareHandler!=null){
            wbShareHandler.doResultIntent(intent,this);
        }
        if(this.iwxapi!=null){
            setIntent(intent);
            this.iwxapi.handleIntent(intent,this);
        }
    }
    static void setSocialResponse(SocialResponse response){
        socialResponse = response;
    }

    private void shareTextToWeibo(String text){
        TextObject textObject = new TextObject();
        textObject.text = text;
        textObject.actionUrl = "http://www.baidu.com";
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = textObject;
        wbShareHandler.shareMessage(weiboMultiMessage,false);
    }

    private void shareImageToWeibo(String text, Bitmap bitmap){
        TextObject textObject = new TextObject();
        textObject.text = text;
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = textObject;
        weiboMultiMessage.imageObject = imageObject;
        wbShareHandler.shareMessage(weiboMultiMessage,false);
    }
    private void shareMutiImageToWeibo(String text, ArrayList<Uri> uris){
        Log.d(TAG,"isSupportMutiImage: " + WbSdk.supportMultiImage(this));
        TextObject textObject = new TextObject();
        textObject.text = text;
        MultiImageObject imageObject = new MultiImageObject();
        imageObject.setImageList(uris);
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = textObject;
        weiboMultiMessage.imageObject = new ImageObject();
        weiboMultiMessage.multiImageObject = imageObject;
        wbShareHandler.shareMessage(weiboMultiMessage,false);
    }
    private void shareVideoToWeibo(String text,Uri uri){
        TextObject textObject = new TextObject();
        textObject.text = text;
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = uri;
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = textObject;
        weiboMultiMessage.videoSourceObject = videoSourceObject;
        wbShareHandler.shareMessage(weiboMultiMessage,false);
    }

    private void loginWeibo(){
        ssoHandler = new SsoHandler(this);
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                socialResponse.onResponse(SocialResponse.ACTION_SUCCESS,"weibo login success",oauth2AccessToken);
            }

            @Override
            public void cancel() {
                socialResponse.onResponse(SocialResponse.ACTION_CANCEL,"weibo login cancel",null);
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                socialResponse.onResponse(SocialResponse.ACTION_FAILED,"weibo login failed",wbConnectErrorMessage);
            }
        });
    }
    private void loginQQ() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", this);
        }else{
            socialResponse.onResponse(SocialResponse.ACTION_FAILED,"session invalid",null);
        }
    }
    private void shareImageAndTextToQQ(Bundle params){
        mTencent.shareToQQ(this, params, this);
    }
    private void shareImageToQQ(Bundle params){
        mTencent.shareToQQ(this, params, this);
    }
    private void shareMusicToQQ(Bundle params){
        mTencent.shareToQQ(this, params, this);
    }
    private void shareImageAndTextToQzone(Bundle params){
        mTencent.shareToQzone(this, params, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ssoHandler!=null){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
            ssoHandler = null;
            finish();
        }
        if(mTencent!=null){
            Tencent.onActivityResultData(requestCode,resultCode,data,this);
        }
    }
    //微博回调
    @Override
    public void onWbShareSuccess() {
        socialResponse.onResponse(SocialResponse.ACTION_SUCCESS,"weibo share success",null);
        finish();
    }

    @Override
    public void onWbShareCancel() {
        socialResponse.onResponse(SocialResponse.ACTION_CANCEL,"weibo share cancel",null);
        finish();
    }

    @Override
    public void onWbShareFail() {
        socialResponse.onResponse(SocialResponse.ACTION_FAILED,"weibo share failed",null);
        finish();
    }

    //QQ回调
    @Override
    public void onComplete(Object object) {
        socialResponse.onResponse(SocialResponse.ACTION_SUCCESS,"success",object);
        finish();
    }

    @Override
    public void onError(UiError uiError) {
        socialResponse.onResponse(SocialResponse.ACTION_FAILED,"failed",null);
        finish();
    }

    @Override
    public void onCancel() {
        socialResponse.onResponse(SocialResponse.ACTION_CANCEL,"cancel",null);
        finish();
    }


    //微信回调
    @Override
    public  void onReq(BaseReq baseReq) {
        Log.d(TAG,"openId: "+ baseReq.openId +
                ";transaction: " + baseReq.transaction);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG,"errCode: "+ baseResp.errCode +
                ";errStr: " + baseResp.errStr +
                ";transaction: " + baseResp.transaction + ";type: " + baseResp.getType());
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                if(baseResp instanceof SendAuth.Resp){//baseResp.getType()==1
                    Log.d(TAG,"code: " + ((SendAuth.Resp)baseResp).code);
                    socialResponse.onResponse(SocialResponse.ACTION_SUCCESS,"success",baseResp);
                }else{
                    socialResponse.onResponse(SocialResponse.ACTION_SUCCESS,"success",null);
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if(baseResp instanceof SendAuth.Resp){//baseResp.getType()==1
                    socialResponse.onResponse(SocialResponse.ACTION_FAILED,"failed",null);
                }else{
                    socialResponse.onResponse(SocialResponse.ACTION_FAILED,"failed",null);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if(baseResp instanceof SendAuth.Resp){//baseResp.getType()==1
                    socialResponse.onResponse(SocialResponse.ACTION_CANCEL,"cancel",null);
                }else{
                    socialResponse.onResponse(SocialResponse.ACTION_CANCEL,"cancel",null);
                }
                break;
            default:
        }
        finish();
    }
}
