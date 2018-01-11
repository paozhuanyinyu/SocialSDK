package cn.sharesdk.demo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import cn.paozhuanyinyu.social.SocialActivity;

/**
 * Created by Administrator on 2017/12/4.
 */

public class WXEntryActivity extends SocialActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        super.onReq(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        super.onResp(baseResp);
    }
}
