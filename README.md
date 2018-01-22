# SocialSDK
集成微信，微博，QQ登录和分享功能，支持链式调用

# 1.首先在你工程的gradle.properties配置
```
QQ_APPID="100371282"
QQ_APPID_TENCENT="tencent100371282"
WECHAT_APPID="wx4868b35061f87885"
WEIBO_APPKEY="568898243"
WEIBO_REDIRECT_URL="http://www.sharesdk.cn"
WEIBO_SCOPE="email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"

```
然后新建package:你的应用包名+wxapi;在此包下新建WXEntryActivity，使它继承SocialActivity
# 2.导入library: social

# 3.代码初始化
```
Social.init(context);

```

# 4.调用登录或者分享API,支持链式调用
```
//登录
Social.with(context)//传入上下文
       .setAction(Social.LOGIN_FROM_WECHAT)//指定动作
       .build(new SocialResponse() {
              @Override
              public void onResponse(int code, String msg, Object extraData) {
                     if(code==SocialResponse.ACTION_SUCCESS){//成功回调
                         Toast.makeText(OtherActivity.this,"微信登录成功",Toast.LENGTH_SHORT).show();
                     }else if(code==SocialResponse.ACTION_CANCEL){//取消回调
                         Toast.makeText(OtherActivity.this,"微信登录取消",Toast.LENGTH_SHORT).show();
                     }else if(code == SocialResponse.ACTION_FAILED){//失败回调
                         Toast.makeText(OtherActivity.this,"微信登录失败",Toast.LENGTH_SHORT).show();
                     }
              }
       });
       
//分享
Social.with(context)//传入上下文
      .setAction(Social.SHARE_TEXT_TO_WECHAT)//指定动作
      .setWechatType(Social.WXSceneSession)//微信分享需要指定是分享到朋友圈还是聊天界面还是收藏，其他平台不需要设置
      .setText("微信分享文本")//分享文本
      .setDescription("微信分享文本描述")//分享描述
      .build(new SocialResponse() {
           @Override
           public void onResponse(int code, String msg, Object extraData) {
                if(SocialResponse.ACTION_SUCCESS==code){//成功回调
                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天成功",Toast.LENGTH_SHORT).show();
                }else if(SocialResponse.ACTION_CANCEL==code){//取消回调
                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天取消",Toast.LENGTH_SHORT).show();
                }else{//失败回调
                    Toast.makeText(OtherActivity.this,"分享文字到微信聊天失败",Toast.LENGTH_SHORT).show();
                }
          }
      });

详细的调用方法参考demo
```