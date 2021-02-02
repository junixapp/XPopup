package com.lxj.xpopupdemo;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;

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
        ToastUtils.getDefaultMaker().setGravity(Gravity.CENTER, 0,0);
//        CrashReport.initCrashReport(getApplicationContext(), "e494d36dcc", false);
    }
}
