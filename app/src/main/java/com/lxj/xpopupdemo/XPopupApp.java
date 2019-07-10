package com.lxj.xpopupdemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
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
        Bugly.init(getApplicationContext(), "e494d36dcc", false);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }
}
