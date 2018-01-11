package cn.paozhuanyinyu.social;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/26.
 */

public class Social {
    public static void init(Context context,String wechatAppId,String qqAppId,String weiboAppKey,String weiboRedirectUrl,String weiboScope){
        SocialManager.getInstance().init(context,wechatAppId,qqAppId,weiboAppKey,weiboRedirectUrl,weiboScope);
    }
    public static void init(Context context){
        SocialManager.getInstance().init(context);
    }
    public static Builder with(Context context){
        return new Builder(context);
    }
    private static Builder builder;
    public static class  Builder {
        Context mContext;
        @WechatType int mWechatType;
        @Action int mAction;
        String mText;
        String mDescription;
        String mImagePath;
        Uri mThumbDataUri;
        String mVideoPath;
        String mMusicPath;
        String mClickLink;
        ArrayList<Uri> mImageList;
        private Builder() {

        }
        Builder(Context context) {
            mContext = context;
        }

        /**
         * 仅微信分享的时候使用，用来区别分析到聊天界面，朋友圈还是收藏
         * @param wechatType 分享类型
         * @return
         */
        public Builder setWechatType(@WechatType int wechatType){
            this.mWechatType = wechatType;
            return this;
        }

        /**
         * 设置动作类型
         * @param action
         * @return
         */
        public Builder setAction(@Action int action){
            this.mAction = action;
            return this;
        }

        /**
         * 设置文本
         * @param text
         * @return
         */
        public Builder setText(String text){
            this.mText = text;
            return this;
        }

        /**
         * 设置文本描述(分享文本到微信聊天界面和微信收藏必须要传)
         * @param description
         * @return
         */
        public Builder setDescription(String description){
            this.mDescription = description;
            return this;
        }

        /**
         * 设置图片路径（分享到QQ时必须是图片的本地路径，必须是QQ能访问的路径，不能放在私有目录，不支持web url形式的图片）
         * @param imagePath
         * @return
         */
        public Builder setImagePath(String imagePath){
            this.mImagePath = imagePath;
            return this;
        }

        /**
         * 仅微博分享多张图片使用
         * @param listImageUri；必须是设备本地路径，res下的都不行
         * @return
         */
        public Builder setArrayListImageUri(ArrayList<Uri> listImageUri){
            this.mImageList = listImageUri;
            return this;
        }

        /**
         * 设置缩略图Uri
         * @param thumbDataUri
         * @return
         */
        public Builder setThumbDataUri(Uri thumbDataUri){
            this.mThumbDataUri = thumbDataUri;
            return this;
        }

        /**
         * 微博分享视频必须是本地路径，微信可以是视频链接
         * @param videoPath 视频地址
         * @return
         */
        public Builder setVideoPath(String videoPath){
            this.mVideoPath = videoPath;
            return this;
        }

        /**
         * 设置要分享的音乐链接地址
         * @param musicPath
         * @return
         */
        public Builder setMusicPath(String musicPath){
            this.mMusicPath = musicPath;
            return this;
        }

        /**
         * 设置点击跳转的url
         * @param clickLink
         * @return
         */
        public Builder setClickLink(String clickLink){
            this.mClickLink = clickLink;
            return this;
        }
        public void build(SocialResponse socialResponse){
            switch(mAction){
                case LOGIN_FROM_WECHAT:
                    SocialManager.getInstance().wechatLogin(mContext,socialResponse);
                    break;
                case LOGIN_FROM_WEIBO:
                    SocialManager.getInstance().weiboLogin(mContext,socialResponse);
                    break;
                case LOGIN_FROM_QQ:
                    SocialManager.getInstance().loginQQ(mContext,socialResponse);
                    break;
                case SHARE_TEXT_TO_WECHAT:
                    SocialManager.getInstance().shareTextToWechat(mContext,mWechatType, mText,mDescription,socialResponse);
                    break;
                case SHARE_IMAGE_TO_WECHAT:
                    SocialManager.getInstance().shareImageToWechat(mContext,mWechatType,mImagePath,socialResponse);
                    break;
                case SHARE_LINK_TO_WECHAT:
                    SocialManager.getInstance().shareLinkToWechat(mContext,mWechatType,mThumbDataUri,mClickLink,mText,mDescription,socialResponse);
                    break;
                case SHARE_VIDEO_TO_WECHAT:
                    SocialManager.getInstance().shareVideoToWechat(mContext,mWechatType,mThumbDataUri,mVideoPath, mText,mDescription,socialResponse);
                    break;
                case SHARE_MUSIC_TO_WECHAT:
                    SocialManager.getInstance().shareMusicToWechat(mContext,mWechatType,mThumbDataUri,mMusicPath, mText,mDescription,socialResponse);
                    break;
                case SHARE_TEXT_TO_WEIBO:
                    SocialManager.getInstance().shareTextToWeibo(mContext, mText,socialResponse);
                    break;
                case SHARE_IMAGE_TO_WEIBO:
                    SocialManager.getInstance().shareImageToWeibo(mContext,mImagePath, mText,socialResponse);
                    break;
                case SHARE_MUTI_IMAGE_TO_WEIBO:
                    SocialManager.getInstance().shareMutiImageToWeibo(mContext,mImageList, mText,socialResponse);
                    break;
                case SHARE_VIDEO_TO_WEIBO:
                    SocialManager.getInstance().shareVideoToWeibo(mContext,mVideoPath, mText,socialResponse);
                    break;
                case SHARE_IMAGE_AND_TEXT_TO_QQ:
                    SocialManager.getInstance().shareImageAndTextToQQ(mContext, mText,mDescription,mClickLink,mImagePath,socialResponse);
                    break;
                case SHARE_IMAGE_TO_QQ:
                    SocialManager.getInstance().shareImageToQQ(mContext,mImagePath,socialResponse);
                    break;
                case SHARE_MUSIC_TO_QQ:
                    SocialManager.getInstance().shareMusicToQQ(mContext, mText,mDescription,mClickLink,mImagePath,mMusicPath,socialResponse);
                    break;
                case SHARE_IMAGE_AND_TEXT_TO_QZONE:
                    SocialManager.getInstance().shareImageAndTextToQzone(mContext, mText,mDescription,mClickLink,mImagePath,socialResponse);
                    break;
            }
        }
    }
    public static final int WXSceneSession = 1;
    public static final int WXSceneTimeline = 2;
    public static final int WXSceneFavorite = 3;
    @IntDef({WXSceneSession,WXSceneTimeline, WXSceneFavorite})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WechatType{}

    public static final int LOGIN_FROM_WECHAT = 11;
    public static final int LOGIN_FROM_WEIBO = 12;
    public static final int LOGIN_FROM_QQ = 13;

    public static final int SHARE_TEXT_TO_WECHAT = 14;
    public static final int SHARE_IMAGE_TO_WECHAT = 15;
    public static final int SHARE_LINK_TO_WECHAT = 16;
    public static final int SHARE_VIDEO_TO_WECHAT = 17;
    public static final int SHARE_MUSIC_TO_WECHAT = 18;

    public static final int SHARE_TEXT_TO_WEIBO = 19;
    public static final int SHARE_IMAGE_TO_WEIBO = 20;
    public static final int SHARE_MUTI_IMAGE_TO_WEIBO = 21;
    public static final int SHARE_VIDEO_TO_WEIBO = 22;

    public static final int SHARE_IMAGE_AND_TEXT_TO_QQ = 23;
    public static final int SHARE_IMAGE_TO_QQ = 24;
    public static final int SHARE_MUSIC_TO_QQ = 25;
    public static final int SHARE_IMAGE_AND_TEXT_TO_QZONE = 26;
    @IntDef({LOGIN_FROM_WECHAT, LOGIN_FROM_WEIBO, LOGIN_FROM_QQ,

            SHARE_TEXT_TO_WECHAT, SHARE_IMAGE_TO_WECHAT, SHARE_LINK_TO_WECHAT, SHARE_VIDEO_TO_WECHAT, SHARE_MUSIC_TO_WECHAT,

            SHARE_TEXT_TO_WEIBO,SHARE_IMAGE_TO_WEIBO,SHARE_MUTI_IMAGE_TO_WEIBO,SHARE_VIDEO_TO_WEIBO,

            SHARE_IMAGE_AND_TEXT_TO_QQ,SHARE_IMAGE_TO_QQ,SHARE_MUSIC_TO_QQ,SHARE_IMAGE_AND_TEXT_TO_QZONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Action{
    }
}
