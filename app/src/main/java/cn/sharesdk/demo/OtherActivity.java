package cn.sharesdk.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.caing.news.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.paozhuanyinyu.social.Social;
import cn.paozhuanyinyu.social.SocialResponse;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        Social.init(OtherActivity.this);
        //微信登录
        Button bt_wechat_login = findViewById(R.id.bt_wechat_login);
        bt_wechat_login.setOnClickListener(this);

        //微信分享文本
        Button bt_wechat_share_text = findViewById(R.id.bt_wechat_share_text);
        bt_wechat_share_text.setOnLongClickListener(this);//分享到聊天界面
        mutiClick(bt_wechat_share_text);//分享到朋友圈和收藏

        //微信分享图片
        Button bt_wechat_share_image = findViewById(R.id.bt_wechat_share_image);
        bt_wechat_share_image.setOnLongClickListener(this);//分享到聊天界面
        mutiClick(bt_wechat_share_image);//分享到朋友圈和收藏

        //微信分享链接
        Button bt_wechat_share_link = findViewById(R.id.bt_wechat_share_link);
        bt_wechat_share_link.setOnLongClickListener(this);//分享到聊天界面
        mutiClick(bt_wechat_share_link);//分享到朋友圈和收藏

        //微信分享视频
        Button bt_wechat_share_video = findViewById(R.id.bt_wechat_share_video);
        bt_wechat_share_video.setOnLongClickListener(this);//分享到聊天界面
        mutiClick(bt_wechat_share_video);//分享到朋友圈和收藏

        //微信分享音乐
        Button bt_wechat_share_music = findViewById(R.id.bt_wechat_share_music);
        bt_wechat_share_music.setOnLongClickListener(this);//分享到聊天界面
        mutiClick(bt_wechat_share_music);//分享到朋友圈和收藏

        //微博登录
        Button bt_weibo_login = findViewById(R.id.bt_weibo_login);
        bt_weibo_login.setOnClickListener(this);

        //微博分享文本
        Button bt_weibo_share_text = findViewById(R.id.bt_weibo_share_text);
        bt_weibo_share_text.setOnClickListener(this);

        //微博分享图片
        Button bt_weibo_share_image = findViewById(R.id.bt_weibo_share_image);
        bt_weibo_share_image.setOnClickListener(this);

        //微博分享链接
        Button bt_weibo_share_muti_image = findViewById(R.id.bt_weibo_share_muti_image);
        bt_weibo_share_muti_image.setOnClickListener(this);

        Button bt_weibo_share_video = findViewById(R.id.bt_weibo_share_video);
        bt_weibo_share_video.setOnClickListener(this);


        //QQ
        Button bt_qq_login = findViewById(R.id.bt_qq_login);
        bt_qq_login.setOnClickListener(this);


        Button bt_qq_share_image = findViewById(R.id.bt_qq_share_image);
        bt_qq_share_image.setOnClickListener(this);


        Button bt_qq_share_image_and_text = findViewById(R.id.bt_qq_share_image_and_text);
        bt_qq_share_image_and_text.setOnLongClickListener(this);
        bt_qq_share_image_and_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_wechat_login:
                Social.with(OtherActivity.this)
                        .setAction(Social.LOGIN_FROM_WECHAT)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"微信登录成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微信登录取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微信登录失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;

            case R.id.bt_weibo_login:
                Social.with(OtherActivity.this)
                        .setAction(Social.LOGIN_FROM_WEIBO)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    if(((Oauth2AccessToken)extraData).isSessionValid()){
                                        Toast.makeText(OtherActivity.this,"微博授权成功",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(OtherActivity.this,"微博授权失败",Toast.LENGTH_LONG).show();
                                    }
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微博授权取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微博授权失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_weibo_share_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_TEXT_TO_WEIBO)
                        .setText("微博分享文本")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"微博分享文本成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微博分享文本取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微博分享文本失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;

            case R.id.bt_weibo_share_image:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_TO_WEIBO)
                        .setText("微博分享图片")
                        .setImagePath("http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"微博分享图片成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微博分享图片取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微博分享图片失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.share_cut)
                break;

            case R.id.bt_weibo_share_muti_image:
                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(Uri.fromFile(saveFile(BitmapFactory.decodeResource(getResources(),R.mipmap.share_cut),getCacheDir().getAbsolutePath(),"share_cut.png")));
                uris.add(Uri.fromFile(saveFile(BitmapFactory.decodeResource(getResources(),R.mipmap.send_img),getCacheDir().getAbsolutePath(),"send_img.png")));
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_MUTI_IMAGE_TO_WEIBO)
                        .setText("微博分享多张图片")
                        .setArrayListImageUri(uris)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"微博分享多张图片成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微博分享多张图片取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微博分享多张图片失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;


            case R.id.bt_weibo_share_video:
                //http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_VIDEO_TO_WEIBO)
                        .setText("微博分享视频文本")
                        .setVideoPath(copyFilesFromAssets(OtherActivity.this,"sample.mp4",getExternalCacheDir().getAbsolutePath() + File.separator+ "sample.mp4").getAbsolutePath())
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"微博分享视频成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"微博分享视频取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"微博分享视频失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case R.id.bt_qq_login:
                Social.with(OtherActivity.this)
                        .setAction(Social.LOGIN_FROM_QQ)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"QQ登录成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"QQ登录取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"QQ登录失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.bt_qq_share_image:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_TO_QQ)
                        .setImagePath(saveFile(BitmapFactory.decodeResource(getResources(), R.mipmap.share_cut), getExternalCacheDir().getAbsolutePath(), "share_cut.png").getAbsolutePath())
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(code==SocialResponse.ACTION_SUCCESS){
                                    Toast.makeText(OtherActivity.this,"QQ分享成功",Toast.LENGTH_SHORT).show();
                                }else if(code==SocialResponse.ACTION_CANCEL){
                                    Toast.makeText(OtherActivity.this,"QQ分享取消",Toast.LENGTH_SHORT).show();
                                }else if(code == SocialResponse.ACTION_FAILED){
                                    Toast.makeText(OtherActivity.this,"QQ分享失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.bt_qq_share_image_and_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_AND_TEXT_TO_QQ)
                        .setText("分享图文到QQ")
                        .setDescription("分享图文到QQ描述")
                        .setClickLink("http://www.baidu.com")
                        .setImagePath("http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"QQ分享图文成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"QQ分享图文取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"QQ分享图文失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }
    public static File copyFilesFromAssets(Context context, String assetsPath, String savePath){
        File file = new File(savePath);
        try {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录

                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName,
                            savePath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }
    public static File saveFile(Bitmap bm, String path, String fileName) {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }
    private void mutiClick(final View view){
        final long[] mHits = new long[2];
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();

                if (mHits[0] >= (SystemClock.uptimeMillis() - 600)) {
                    //双击
                    doubleClick(view);
                }else{
                    oneClick(view);
                }
            }
        });
    }
    /**
     * 分享到聊天界面
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()){
            case R.id.bt_wechat_share_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_TEXT_TO_WECHAT)
                        .setWechatType(Social.WXSceneSession)
                        .setText("微信分享文本")
                        .setDescription("微信分享文本描述")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_image:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_TO_WECHAT)
                        .setWechatType(Social.WXSceneSession)
                        .setImagePath("http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信聊天成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信聊天取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享图片到微信聊天失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_link:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_LINK_TO_WECHAT)
                        .setWechatType(Social.WXSceneSession)
                        .setText("微信分享链接")
                        .setDescription("微信分享链接描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher))
                        .setClickLink("http://www.baidu.com")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信聊天成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信聊天取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享链接到微信聊天失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_video:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_VIDEO_TO_WECHAT)
                        .setWechatType(Social.WXSceneSession)
                        .setText("微信分享视频")
                        .setDescription("微信分享视频描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img))
                        .setVideoPath("http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信聊天成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信聊天取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享视频到微信聊天失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_music:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_MUSIC_TO_WECHAT)
                        .setWechatType(Social.WXSceneSession)
                        .setText("微信分享音乐")
                        .setDescription("微信分享音乐描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img))
                        .setMusicPath("http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信聊天成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信聊天取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信聊天失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_qq_share_image_and_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_AND_TEXT_TO_QZONE)
                        .setImagePath("http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg")
                        .setText("分享图文到QQ空间")
                        .setDescription("分享图文到QQ空间描述")
                        .setClickLink("http://www.baidu.com")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"QQ空间分享图文成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"QQ空间分享图文取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"QQ空间分享图文失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
        return true;
    }


    /**
     * 分享到收藏
     * @param v
     */
    private void doubleClick(final View v){
        //分享到收藏
        switch(v.getId()){
            case R.id.bt_wechat_share_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_TEXT_TO_WECHAT)
                        .setWechatType(Social.WXSceneFavorite)
                        .setText("微信分享文本")
                        .setDescription("微信分享文本描述")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信收藏成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信收藏取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享文字到微信收藏失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_image:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_TO_WECHAT)
                        .setWechatType(Social.WXSceneFavorite)
                        .setImagePath("android.resource://" + getPackageName() + "/" + R.mipmap.send_img)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信收藏成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信收藏取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享图片到微信收藏失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.bt_wechat_share_link:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_LINK_TO_WECHAT)
                        .setWechatType(Social.WXSceneFavorite)
                        .setText("微信分享链接")
                        .setDescription("微信分享链接描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher))
                        .setClickLink("http://www.baidu.com")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信收藏成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信收藏取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享链接到微信收藏失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_video:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_VIDEO_TO_WECHAT)
                        .setWechatType(Social.WXSceneFavorite)
                        .setText("微信分享视频")
                        .setDescription("微信分享视频描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img))
                        .setVideoPath("http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信收藏成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信收藏取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享视频到微信收藏失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_music:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_MUSIC_TO_WECHAT)
                        .setWechatType(Social.WXSceneFavorite)
                        .setText("微信分享音乐")
                        .setDescription("微信分享音乐描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img))
                        .setMusicPath("http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信收藏成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信收藏取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信收藏失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

        }
    }

    /**
     * 分享到朋友圈
     * @param v
     */
    public void oneClick(View v){
        switch(v.getId()){
            case R.id.bt_wechat_share_text:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_TEXT_TO_WECHAT)
                        .setWechatType(Social.WXSceneTimeline)
                        .setText("微信分享文本")
                        .setDescription("微信分享文本描述")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享文字到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享文字到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    break;
            case R.id.bt_wechat_share_image:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_IMAGE_TO_WECHAT)
                        .setWechatType(Social.WXSceneTimeline)
                        .setImagePath("android.resource://" + getPackageName() + "/" + R.mipmap.share_cut)
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享图片到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享图片到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    break;
            case R.id.bt_wechat_share_link:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_LINK_TO_WECHAT)
                        .setWechatType(Social.WXSceneTimeline)
                        .setText("微信分享链接")
                        .setDescription("微信分享链接描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher))
                        .setClickLink("http://www.baidu.com")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享链接到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享链接到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    break;
            case R.id.bt_wechat_share_video:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_VIDEO_TO_WECHAT)
                        .setWechatType(Social.WXSceneTimeline)
                        .setText("微信分享视频")
                        .setDescription("微信分享视频描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.share_cut))
                        .setVideoPath("http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享视频到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享视频到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.bt_wechat_share_music:
                Social.with(OtherActivity.this)
                        .setAction(Social.SHARE_MUSIC_TO_WECHAT)
                        .setWechatType(Social.WXSceneTimeline)
                        .setText("微信分享音乐")
                        .setDescription("微信分享音乐描述")
                        .setThumbDataUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img))
                        .setMusicPath("http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3")
                        .build(new SocialResponse() {
                            @Override
                            public void onResponse(int code, String msg, Object extraData) {
                                if(SocialResponse.ACTION_SUCCESS==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                                }else if(SocialResponse.ACTION_CANCEL==code){
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(OtherActivity.this,"分享音乐到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            }
    }
}
