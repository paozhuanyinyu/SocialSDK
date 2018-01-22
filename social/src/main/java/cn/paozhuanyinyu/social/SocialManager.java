package cn.paozhuanyinyu.social;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import paozhuanyinyu.com.social.BuildConfig;

/**
 * Created by Administrator on 2017/12/4.
 */

public class SocialManager {
    static final String TAG = "SocialManager";
    private static final int THUMB_SIZE = 150;
    static final Object TRIGGER = new Object();
    private static SocialManager sInstance;
    private IWXAPI iwxapi;
    String wechatAppId;
    String qqAppId;
    String weiboAppKey;
    String weiboRedirectUrl;
    String weiboScope;
    private SocialManager() {
    }
    public static SocialManager getInstance() {
        if(sInstance == null) {
            synchronized(TRIGGER) {
                if(sInstance == null) {
                    sInstance = new SocialManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化方法，代码配置社交平台参数
     * @param context 上下文
     * @param wechatAppId 微信appid
     * @param qqAppId QQ appid
     * @param weiboAppKey 微博 appkey
     * @param weiboRedirectUrl 微博回调地址
     * @param weiboScope 微博 scope
     */
//    public void init(Context context,String wechatAppId,String qqAppId,String weiboAppKey,String weiboRedirectUrl,String weiboScope){
//        this.wechatAppId = wechatAppId;
//        this.qqAppId = qqAppId;
//        this.weiboAppKey = weiboAppKey;
//        this.weiboRedirectUrl = weiboRedirectUrl;
//        this.weiboScope = weiboScope;
//        initWeibo(context);
//    }

    /**
     * 初始化方法，local.prpperties配置社交平台参数
     * @param context
     */
    public void init(Context context){
        initWeibo(context);
    }

    String getWechatAppId() {
        if(TextUtils.isEmpty(wechatAppId)){
            wechatAppId = BuildConfig.WECHAT_APPID;
        }
        if(TextUtils.isEmpty(wechatAppId)){
            throw new IllegalArgumentException("wechat appid was empty: " + wechatAppId);
        }
        return wechatAppId;
    }

    String getQqAppId() {
        if(TextUtils.isEmpty(qqAppId)){
            qqAppId = BuildConfig.QQ_APPID;
        }
        if(TextUtils.isEmpty(qqAppId)){
            throw new IllegalArgumentException("qq appid was empty: " + qqAppId);
        }
        return qqAppId;
    }

    String getWeiboAppKey() {
        if(TextUtils.isEmpty(weiboAppKey)){
            weiboAppKey = BuildConfig.WEIBO_APPKEY;
        }
        if(TextUtils.isEmpty(weiboAppKey)){
            throw new IllegalArgumentException("weibo appkey was empty: " + weiboAppKey);
        }
        return weiboAppKey;
    }

    String getWeiboRedirectUrl() {
        if(TextUtils.isEmpty(weiboRedirectUrl)){
            weiboRedirectUrl = BuildConfig.WEIBO_REDIRECT_URL;
        }
        if(TextUtils.isEmpty(weiboRedirectUrl)){
            throw new IllegalArgumentException("weibo redirect url was empty: " + weiboRedirectUrl);
        }
        return weiboRedirectUrl;
    }

    String getWeiboScope() {
        if(TextUtils.isEmpty(weiboScope)){
            weiboScope = BuildConfig.WEIBO_SCOPE;
        }
        if(TextUtils.isEmpty(weiboScope)){
            throw new IllegalArgumentException("weibo scope was empty: " + weiboScope);
        }
        return weiboScope;
    }


    //微信
    void initWechat(Context context, String wechatAppId){
        iwxapi = WXAPIFactory.createWXAPI(context,wechatAppId,true);
        iwxapi.registerApp(wechatAppId);
    }

    /**
     * 微信登录
     * @param context 上下文
     * @param response 回调
     */
    public void wechatLogin(Context context,SocialResponse response){
        checkIwxapi(context, getWechatAppId());
        Log.d(TAG,"isWXAppInstalled: " + iwxapi.isWXAppInstalled() +
                ";isWXAppSupportAPI: "+ iwxapi.isWXAppSupportAPI() +
                ";wXAppSupportAPI: "+ iwxapi.getWXAppSupportAPI());
        SocialActivity.setSocialResponse(response);
        if(iwxapi.isWXAppInstalled()){
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = Util.getRandom(context);
            iwxapi.sendReq(req);
        }else{
            Toast.makeText(context, "未安装微信客户端", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 是否安装过微信
     * @param context 上下文
     * @return true yes,  false no
     */
    public boolean isWXAppInstalled(Context context){
        checkIwxapi(context, getWechatAppId());
        return iwxapi.isWXAppInstalled();
    }
    private void checkIwxapi(Context context, String wehatAppId) {
        if(iwxapi==null){
            initWechat(context,wehatAppId);
        }
    }
    private void checkDescription(@Social.WechatType int wechatType, String description) {
        if(Social.WXSceneTimeline!=wechatType && TextUtils.isEmpty(description)){
            throw new IllegalArgumentException("description cannot be empty when you share text to WXSceneSession or WXSceneFavorite!");
        }
    }
    /**
     * 分享文本到微信
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param shareText 分享文本
     * @param response 回调
     */
    public void shareTextToWechat(Context context, @Social.WechatType int wechatType, String shareText, SocialResponse response){
       shareTextToWechat(context,wechatType,shareText,"",response);
    }

    /**
     * 分享文本到微信
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param shareText 分享文本
     * @param description 文本描述，分享到朋友圈可以不传,聊天界面和收藏必须传
     * @param response 回调
     */
    public void shareTextToWechat(Context context, @Social.WechatType int wechatType, String shareText, String description, SocialResponse response){
        checkIwxapi(context,getWechatAppId());
        checkDescription(wechatType, description);
        SocialActivity.setSocialResponse(response);

        WXTextObject wxTextObject = new WXTextObject();
        wxTextObject.text = shareText;

        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        wxMediaMessage.mediaObject = wxTextObject;
        wxMediaMessage.description = description;

        sendReq(wechatType, wxMediaMessage);
    }

    private void sendReq(@Social.WechatType int wechatType, WXMediaMessage wxMediaMessage) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wxMediaMessage;
        if(Social.WXSceneSession==wechatType){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }else if(Social.WXSceneTimeline==wechatType){
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }else if(Social.WXSceneFavorite==wechatType){
            req.scene = SendMessageToWX.Req.WXSceneFavorite;
        }else{
            throw new IllegalArgumentException("you have not set WechatType.");
        }
        iwxapi.sendReq(req);
    }

    /**
     * 分享图片到微信
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param imagePath 图片路径;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param response 回调
     */
    public void shareImageToWechat(Context context, final @Social.WechatType int wechatType, String imagePath, SocialResponse response){
        checkIwxapi(context,getWechatAppId());
        SocialActivity.setSocialResponse(response);
        Glide.with(context)
                .asBitmap()
                .load(imagePath)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL){

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        //imageData大小限制为10MB,imagePath是指图片本地路径，大小为10K,内容大小限制为10M
                        WXImageObject wxImageObject = new WXImageObject(bitmap);

                        WXMediaMessage wxMediaMessage = new WXMediaMessage();
                        wxMediaMessage.mediaObject = wxImageObject;

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                        wxMediaMessage.thumbData = Util.bmpToByteArray(thumbBmp, false);//缩略图大小限制为32K

                        sendReq(wechatType, wxMediaMessage);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e(TAG,"onLoadFailed");
                    }
                });

    }

    /**
     * 分享到链接到微信
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 链接缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param url 链接地址
     * @param title 链接标题
     * @param response 链接描述，可缺省
     */
    public void shareLinkToWechat(Context context, @Social.WechatType int wechatType, Uri uri, String url, String title, SocialResponse response){
        shareLinkToWechat(context,wechatType,uri,url,title,"",response);
    }
    /**
     * 分享网页链接到微信
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 链接缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param url 链接
     * @param title 链接标题
     * @param description 链接描述，可缺省
     * @param response 链接描述，可缺省
     */
    public void shareLinkToWechat(Context context, final @Social.WechatType int wechatType, final Uri uri, final String url, final String title, final String description, SocialResponse response){
        checkIwxapi(context,getWechatAppId());
        SocialActivity.setSocialResponse(response);
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL){

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        WXWebpageObject wxWebpageObject = new WXWebpageObject();
                        wxWebpageObject.webpageUrl = url;
                        WXMediaMessage wxMediaMessage = new WXMediaMessage();
                        wxMediaMessage.mediaObject = wxWebpageObject;

                        wxMediaMessage.title = title;
                        wxMediaMessage.description = description;
                        wxMediaMessage.thumbData = Util.bmpToByteArray(bitmap, false);//缩略图大小限制为32K
                        sendReq(wechatType,wxMediaMessage);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e(TAG,"onLoadFailed");
                    }
                });

    }

    /**
     * 分享视频到微信,无描述
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 视频缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param videoUrl 视频链接地址
     * @param title 分享视频的标题，必传，但是可是是空字符串(不建议这样，体验不好)
     * @param response 回调
     */
    public void shareVideoToWechat(Context context,@Social.WechatType int wechatType, Uri uri, final String videoUrl, final String title, SocialResponse response){
        shareVideoToWechat(context,wechatType,uri,videoUrl,title,"",response);
    }
    /**
     * 分享视频到微信，有描述
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 视频缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param videoUrl 视频链接地址
     * @param title 分享视频的标题，必传，但是可是是空字符串(不建议这样，体验不好)
     * @param description 分享视频的描述，可缺省
     * @param response 回调
     */
    public void shareVideoToWechat(Context context, final @Social.WechatType int wechatType, final Uri uri, final String videoUrl, final String title, final String description, SocialResponse response){
        checkIwxapi(context,getWechatAppId());
        SocialActivity.setSocialResponse(response);
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL){

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        WXVideoObject videoObject = new WXVideoObject();
                        videoObject.videoUrl = videoUrl;
                        WXMediaMessage wxMediaMessage = new WXMediaMessage(videoObject);
                        wxMediaMessage.title = title;
                        wxMediaMessage.description = description;
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                        wxMediaMessage.thumbData = Util.bmpToByteArray(thumbBmp, false);//缩略图大小限制为32K
                        sendReq(wechatType,wxMediaMessage);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e(TAG,"onLoadFailed");
                    }
                });


    }

    /**
     * 分享音乐到微信,无描述
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 音乐缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param musicUrl 音乐链接
     * @param title 分享音乐的标题，必传，但是可是是空字符串(不建议这样，体验不好)
     * @param response 回调
     */
    public void shareMusicToWechat(Context context, @Social.WechatType int wechatType, Uri uri,  String musicUrl, String title, SocialResponse response){
        shareMusicToWechat(context,wechatType,uri,musicUrl,title,"",response);
    }
    /**
     * 分享音乐到微信，有描述
     * @param context 上下文
     * @param wechatType 微信分享类型：朋友圈，聊天界面，收藏
     * @param uri 音乐缩略图uri;imageData大小限制为10MB; imagePath是指图片本地路径，大小为10K,内容大小限制为10M;thumbData是指缩略图，大小限制为32K
     * @param musicUrl 音乐链接
     * @param title 分享音乐的标题，必传，但是可是是空字符串(不建议这样，体验不好)
     * @param description 分享音乐的描述，可不传
     * @param response 回调
     */
    public void shareMusicToWechat(Context context, final @Social.WechatType int wechatType, Uri uri, final String musicUrl, final String title, final String description, SocialResponse response){
        checkIwxapi(context,getWechatAppId());
        SocialActivity.setSocialResponse(response);
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL){

                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        WXMusicObject musicObject = new WXMusicObject();
                        musicObject.musicUrl = musicUrl;
                        WXMediaMessage wxMediaMessage = new WXMediaMessage(musicObject);
                        wxMediaMessage.title = title;
                        wxMediaMessage.description = description;
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                        wxMediaMessage.thumbData = Util.bmpToByteArray(thumbBmp, false);//缩略图大小限制为32K
                        sendReq(wechatType,wxMediaMessage);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e(TAG,"onLoadFailed");
                    }
                });

    }



    //微博
    /**
     * 是否安装过微博
     * @param context 上下文
     * @return true yes,false no
     */
    public boolean isWbInstall(Context context){
        return WbSdk.isWbInstall(context);
    }
    void initWeibo(Context context){
        WbSdk.install(context,new AuthInfo(context,getWeiboAppKey(),getWeiboRedirectUrl(),getWeiboScope()));
    }
    /**
     * 微博登录
     * @param context 上下文
     * @param socialResponse 回调
     */
    public void weiboLogin(final Context context,SocialResponse socialResponse){
        WbSdk.checkInit();
        SocialActivity.setSocialResponse(socialResponse);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_WEIBO);
        intent.putExtra("action",SocialActivity.LOGIN_FROM_WEIBO_ACTION);
        context.startActivity(intent);
    }

    /**
     * 微博分享文本
     * @param context 上下文
     * @param text 文本
     * @param socialResponse 回调
     */
    public void shareTextToWeibo(Context context,String text,SocialResponse socialResponse){
        WbSdk.checkInit();
        SocialActivity.setSocialResponse(socialResponse);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_WEIBO);
        intent.putExtra("action",SocialActivity.SHARE_TEXT_TO_WEIBO_ACTION);
        intent.putExtra("text",text);
        context.startActivity(intent);
    }

    /**
     * 微博分享单张图片
     * @param context 上下文
     * @param imagePath 图片路径 图片大小限制2M,缩略图限制32K
     * @param text 文本
     * @param socialResponse 回调
     */
    public void shareImageToWeibo(Context context, String imagePath, String text, SocialResponse socialResponse){
        WbSdk.checkInit();
        SocialActivity.setSocialResponse(socialResponse);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.setData(Uri.parse(imagePath));
        intent.putExtra("platform",SocialActivity.PLATFORM_WEIBO);
        intent.putExtra("action",SocialActivity.SHARE_IMAGE_TO_WEIBO_ACTION);
        intent.putExtra("text",text);
        context.startActivity(intent);
    }

    /**
     * 分享多图到微博
     * @param context 上下文
     * @param uriList 图片Uri集合，必须是本地路径，res下的都不行
     * @param text  文本
     * @param socialResponse 回调
     */
    public void shareMutiImageToWeibo(Context context, ArrayList<Uri> uriList, String text, SocialResponse socialResponse){
        WbSdk.checkInit();
        Log.d(TAG,"是否支持多图分享： " + WbSdk.supportMultiImage(context));
        SocialActivity.setSocialResponse(socialResponse);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.putParcelableArrayListExtra("uris",uriList);
        intent.putExtra("platform",SocialActivity.PLATFORM_WEIBO);
        intent.putExtra("action",SocialActivity.SHARE_MUTI_IMAGE_TO_WEIBO_ACTION);
        intent.putExtra("text",text);
        context.startActivity(intent);
    }

    /**
     * 分享视频到微博
     * @param context 上下文
     * @param videoPath 视频链地址,必须是本地的路径，网络路径不支持
     * @param text 文本
     * @param socialResponse 回调
     */
    public void shareVideoToWeibo(Context context, String videoPath, String text, SocialResponse socialResponse){
        WbSdk.checkInit();
        SocialActivity.setSocialResponse(socialResponse);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.setData(Uri.fromFile(new File(videoPath)));
        intent.putExtra("platform",SocialActivity.PLATFORM_WEIBO);
        intent.putExtra("action",SocialActivity.SHARE_VIDEO_TO_WEIBO_ACTION);
        intent.putExtra("text",text);
        context.startActivity(intent);
    }


    //QQ

    /**
     * 是否安装了QQ客户端
     * @param context 上下文
     * @return true yes,  false no
     */
    public boolean isQQInstalled(Context context){
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.getInstalledPackages(0);
        if(list != null) {
            for(int i = 0; i < list.size(); ++i) {
                String packageName = ((PackageInfo)list.get(i)).packageName;
                if(packageName.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * QQ登录
     * @param context 上下文
     * @param response 回调
     */
    public void loginQQ(Context context,SocialResponse response){
        SocialActivity.setSocialResponse(response);
        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_QQ);
        intent.putExtra("action",SocialActivity.LOGIN_FROM_QQ_ACTION);
        context.startActivity(intent);
    }

    /**
     * 分享图文到QQ聊天界面; 此功能是灵活的，也可以实现像微信那样分享视频，分享链接
     * @param context 上下文
     * @param title 文本标题
     * @param description 文本描述
     * @param link 链接
     * @param imageUrl 图片链接地址
     * @param response 回调
     */
    public void shareImageAndTextToQQ(Context context,String title,String description,String link,String imageUrl,SocialResponse response){
        SocialActivity.setSocialResponse(response);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  link);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imageUrl);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_QQ);
        intent.putExtra("action",SocialActivity.SHARE_IMAGE_AND_TEXT_TO_QQ_ACTION);
        intent.putExtra("qq_bundle",params);
        context.startActivity(intent);
    }

    /**
     * 分享图片到QQ聊天界面
     * @param context 上下文
     * @param imagePath 图片的本地路径，必须是QQ能访问的路径，不能放在私有目录，不支持web url形式的图片
     * @param response 回调
     */
    public void shareImageToQQ(Context context,String imagePath,SocialResponse response){
        SocialActivity.setSocialResponse(response);

        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,imagePath);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_QQ);
        intent.putExtra("action",SocialActivity.SHARE_IMAGE_TO_QQ_ACTION);
        intent.putExtra("qq_bundle",params);
        context.startActivity(intent);
    }

    /**
     * 分享音乐到QQ聊天界面
     * @param context 上下文
     * @param title 标题
     * @param description 描述
     * @param link 链接
     * @param imageUrl 图片链接
     * @param musicUrl 音乐链接
     * @param response 回调
     */
    public void shareMusicToQQ(Context context,String title,String description,String link,String imageUrl,String musicUrl,SocialResponse response){
        SocialActivity.setSocialResponse(response);

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  link);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, musicUrl);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);

        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_QQ);
        intent.putExtra("action",SocialActivity.SHARE_MUSIC_TO_QQ_ACTION);
        intent.putExtra("qq_bundle",params);
        context.startActivity(intent);
    }

    /**
     * 分享图文到QQ空间；此功能是灵活的，也可以实现像微信那样分享视频，分享链接
     * @param context 上下文
     * @param title 标题
     * @param description 描述
     * @param link 链接
     * @param imagePath 图片链接地址；目前只支持分享单张图片，如果分享多张图片，默认取第一张图片显示
     * @param response 回调
     */
    public void shareImageAndTextToQzone(Context context, String title, String description, String link, String imagePath,SocialResponse response){
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(imagePath);
        shareImageAndTextToQzone(context,title,description,link,arrayList,response);
    }
    /**
     * 分享图文到QQ空间；此功能是灵活的，也可以实现像微信那样分享视频，分享链接
     * @param context 上下文
     * @param title 标题
     * @param description 描述
     * @param link 链接
     * @param images 图片链接地址集合；目前只支持分享单张图片，如果分享多张图片，默认取第一张图片显示
     * @param response 回调
     */
    public void shareImageAndTextToQzone(Context context, String title, String description, String link, ArrayList<String> images,SocialResponse response){
        SocialActivity.setSocialResponse(response);

        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  description);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, link);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,images);

        Intent intent = new Intent(context,SocialActivity.class);
        intent.putExtra("platform",SocialActivity.PLATFORM_QQ);
        intent.putExtra("action",SocialActivity.SHARE_IMAGE_AND_TEXT_TO_QZONE_ACTION);
        intent.putExtra("qq_bundle",params);
        context.startActivity(intent);
    }
}
