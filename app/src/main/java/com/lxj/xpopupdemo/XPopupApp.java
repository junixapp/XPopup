package com.lxj.xpopupdemo;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import com.blankj.utilcode.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Description:
 * Create by dance, at 2019/1/1
 */
public class XPopupApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        UMConfigure.init(this, "60e2b78726a57f101846a2c2", "UMENG_CHANNEL", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        ToastUtils.getDefaultMaker().setGravity(Gravity.CENTER, 0 , 0);
        ToastUtils.getDefaultMaker().setBgResource(R.drawable.bg_toast);
        ToastUtils.getDefaultMaker().setTextColor(Color.WHITE);
    }
}
