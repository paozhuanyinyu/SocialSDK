package cn.sharesdk.demo;

import android.content.Context;
import android.content.Intent;
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
import cn.paozhuanyinyu.social.SocialManager;
import cn.paozhuanyinyu.social.SocialResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocialManager.getInstance().init(MainActivity.this,Constants.WECHAT_APPID,Constants.QQ_APPID,Constants.APP_KEY_WEIBO,Constants.REDIRECT_URL_WEIBO,Constants.SCOPE_WEIBO);
//        SocialManager.getInstance().init(MainActivity.this);
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




        Button bt_go_to_other = findViewById(R.id.go_to_other);
        bt_go_to_other.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_wechat_login:
                SocialManager.getInstance().wechatLogin(MainActivity.this, new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"微信登录成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微信登录取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微信登录失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.bt_weibo_login:

                SocialManager.getInstance().weiboLogin(MainActivity.this, new SocialResponse() {

                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            if(((Oauth2AccessToken)extraData).isSessionValid()){
                                Toast.makeText(MainActivity.this,"微博授权成功",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this,"微博授权失败",Toast.LENGTH_LONG).show();
                            }
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微博授权取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微博授权失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_weibo_share_text:
                SocialManager.getInstance().shareTextToWeibo(MainActivity.this, "微博分享文本", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"微博分享文本成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微博分享文本取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微博分享文本失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
                //Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.share_cut)
            case R.id.bt_weibo_share_image:
                SocialManager.getInstance().shareImageToWeibo(MainActivity.this, "http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg","微博分享图片", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"微博分享图片成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微博分享图片取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微博分享图片失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.bt_weibo_share_muti_image:
                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(Uri.fromFile(saveFile(BitmapFactory.decodeResource(getResources(),R.mipmap.share_cut),getCacheDir().getAbsolutePath(),"share_cut.png")));
                uris.add(Uri.fromFile(saveFile(BitmapFactory.decodeResource(getResources(),R.mipmap.send_img),getCacheDir().getAbsolutePath(),"send_img.png")));
                SocialManager.getInstance().shareMutiImageToWeibo(MainActivity.this, uris, "微博分享多张图片", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"微博分享多张图片成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微博分享多张图片取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微博分享多张图片失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;


            case R.id.bt_weibo_share_video:
                //http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4
                SocialManager.getInstance().shareVideoToWeibo(MainActivity.this, copyFilesFromAssets(MainActivity.this,"sample.mp4",getExternalCacheDir().getAbsolutePath() +File.separator+ "sample.mp4").getAbsolutePath(), "微博分享视频文本", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"微博分享视频成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"微博分享视频取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"微博分享视频失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.bt_qq_login:
                SocialManager.getInstance().loginQQ(MainActivity.this, new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"QQ登录成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"QQ登录取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"QQ登录失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_qq_share_image:
                SocialManager.getInstance().shareImageToQQ(MainActivity.this, saveFile(BitmapFactory.decodeResource(getResources(), R.mipmap.share_cut), getExternalCacheDir().getAbsolutePath(), "share_cut.png").getAbsolutePath(), new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(code==SocialResponse.ACTION_SUCCESS){
                            Toast.makeText(MainActivity.this,"QQ分享成功",Toast.LENGTH_SHORT).show();
                        }else if(code==SocialResponse.ACTION_CANCEL){
                            Toast.makeText(MainActivity.this,"QQ分享取消",Toast.LENGTH_SHORT).show();
                        }else if(code == SocialResponse.ACTION_FAILED){
                            Toast.makeText(MainActivity.this,"QQ分享失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_qq_share_image_and_text:
                SocialManager.getInstance().shareImageAndTextToQQ(MainActivity.this,"分享图文到QQ", "分享图文到QQ描述", "http://www.baidu.com", "http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"QQ分享图文成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"QQ分享图文取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"QQ分享图文失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.go_to_other:
                Intent intent = new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
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
                SocialManager.getInstance().shareTextToWechat(MainActivity.this, Social.WXSceneSession, "微信分享文本", "微信分享文本描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享文字到微信聊天成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享文字到微信聊天取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享文字到微信聊天失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_image:
                SocialManager.getInstance().shareImageToWechat(MainActivity.this, Social.WXSceneSession, "android.resource://" + getPackageName() + "/" + R.mipmap.share_cut, new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享图片到微信聊天成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享图片到微信聊天取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享图片到微信聊天失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_link:
                SocialManager.getInstance().shareLinkToWechat(MainActivity.this, Social.WXSceneSession, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher), "http://www.baidu.com", "微信分享链接", "微信分享链接描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享链接到微信聊天成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享链接到微信聊天取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享链接到微信聊天失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_video:
                SocialManager.getInstance().shareVideoToWechat(MainActivity.this, Social.WXSceneSession, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img), "http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4", "微信分享视频", "微信分享视频描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信聊天成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信聊天取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享视频到微信聊天失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_music:
                SocialManager.getInstance().shareMusicToWechat(MainActivity.this, Social.WXSceneSession, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img), "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3", "微信分享音乐", "微信分享音乐描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信聊天成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信聊天取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享音乐到微信聊天失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_qq_share_image_and_text:
                ArrayList<String> list = new ArrayList<String>();
                list.add("http://7xsknm.com2.z0.glb.clouddn.com/widget/image/help_1.jpg");//目前只支持分享单张图片，如果分享多张图片，默认取第一张图片显示
                SocialManager.getInstance().shareImageAndTextToQzone(MainActivity.this, "分享图文到QQ空间", "分享图文到QQ空间描述", "http://www.baidu.com", list, new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"QQ空间分享图文成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"QQ空间分享图文取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"QQ空间分享图文失败",Toast.LENGTH_SHORT).show();
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
                SocialManager.getInstance().shareTextToWechat(MainActivity.this, Social.WXSceneFavorite, "微信分享文本", "微信分享文本描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享文字到微信收藏成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享文字到微信收藏取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享文字到微信收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_image:
                SocialManager.getInstance().shareImageToWechat(MainActivity.this, Social.WXSceneFavorite, "android.resource://" + getPackageName() + "/" + R.mipmap.send_img, new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享图片到微信收藏成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享图片到微信收藏取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享图片到微信收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_link:
                SocialManager.getInstance().shareLinkToWechat(MainActivity.this, Social.WXSceneFavorite, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher), "http://www.baidu.com", "微信分享链接", "微信分享链接描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享链接到微信收藏成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享链接到微信收藏取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享链接到微信收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_video:
                SocialManager.getInstance().shareVideoToWechat(MainActivity.this, Social.WXSceneFavorite, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img), "http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4", "微信分享视频", "微信分享视频描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信收藏成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信收藏取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享视频到微信收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_music:
                SocialManager.getInstance().shareMusicToWechat(MainActivity.this, Social.WXSceneFavorite, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img), "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3", "微信分享音乐", "微信分享音乐描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信收藏成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信收藏取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享音乐到微信收藏失败",Toast.LENGTH_SHORT).show();
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
                    SocialManager.getInstance().shareTextToWechat(MainActivity.this, Social.WXSceneTimeline, "微信分享文本", new SocialResponse() {
                        @Override
                        public void onResponse(int code, String msg, Object extraData) {
                            if(SocialResponse.ACTION_SUCCESS==code){
                                Toast.makeText(MainActivity.this,"分享文字到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                            }else if(SocialResponse.ACTION_CANCEL==code){
                                Toast.makeText(MainActivity.this,"分享文字到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this,"分享文字到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            case R.id.bt_wechat_share_image:
                    SocialManager.getInstance().shareImageToWechat(MainActivity.this, Social.WXSceneTimeline, "android.resource://" + getPackageName() + "/" + R.mipmap.share_cut, new SocialResponse() {
                        @Override
                        public void onResponse(int code, String msg, Object extraData) {
                            if(SocialResponse.ACTION_SUCCESS==code){
                                Toast.makeText(MainActivity.this,"分享图片到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                            }else if(SocialResponse.ACTION_CANCEL==code){
                                Toast.makeText(MainActivity.this,"分享图片到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this,"分享图片到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            case R.id.bt_wechat_share_link:
                    SocialManager.getInstance().shareLinkToWechat(MainActivity.this, Social.WXSceneTimeline, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher), "http://www.baidu.com", "微信分享链接", "微信分享链接描述", new SocialResponse() {
                        @Override
                        public void onResponse(int code, String msg, Object extraData) {
                            if(SocialResponse.ACTION_SUCCESS==code){
                                Toast.makeText(MainActivity.this,"分享链接到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                            }else if(SocialResponse.ACTION_CANCEL==code){
                                Toast.makeText(MainActivity.this,"分享链接到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this,"分享链接到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            case R.id.bt_wechat_share_video:
                SocialManager.getInstance().shareVideoToWechat(MainActivity.this, Social.WXSceneTimeline, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.share_cut), "http://7xsknm.com2.z0.glb.clouddn.com/video/sample.mp41adeab952a9d973d42531fcd179400c2.mp4", "微信分享视频", "微信分享视频描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享视频到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享视频到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.bt_wechat_share_music:
                SocialManager.getInstance().shareMusicToWechat(MainActivity.this, Social.WXSceneTimeline, Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.send_img), "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3", "微信分享音乐", "微信分享音乐描述", new SocialResponse() {
                    @Override
                    public void onResponse(int code, String msg, Object extraData) {
                        if(SocialResponse.ACTION_SUCCESS==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信朋友圈成功",Toast.LENGTH_SHORT).show();
                        }else if(SocialResponse.ACTION_CANCEL==code){
                            Toast.makeText(MainActivity.this,"分享音乐到微信朋友圈取消",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"分享音乐到微信朋友圈失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            }
    }
}
