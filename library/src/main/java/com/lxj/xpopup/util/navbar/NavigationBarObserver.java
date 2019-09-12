package com.lxj.xpopup.util.navbar;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import java.util.ArrayList;

/**
 * copy from https://github.com/gyf-dev/ImmersionBar.
 * 导航栏显示隐藏处理，目前只支持emui和miui带有导航栏的手机
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
public final class NavigationBarObserver extends ContentObserver {
    /**
     * MIUI导航栏显示隐藏标识位
     */
    static final String IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW = "force_fsg_nav_bar";
    /**
     * EMUI导航栏显示隐藏标识位
     */
    static final String IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW = "navigationbar_is_min";

    private ArrayList<OnNavigationBarListener> mListeners;
    private Context context;
    private Boolean mIsRegister = false;

    public static NavigationBarObserver getInstance() {
        return NavigationBarObserverInstance.INSTANCE;
    }

    private NavigationBarObserver() {
        super(new Handler(Looper.getMainLooper()));
    }

    public void register(Context context) {
        this.context = context.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context != null
                && context.getContentResolver() != null && !mIsRegister) {
            Uri uri = null;
            if (OSUtils.isMIUI()) {
                uri = Settings.Global.getUriFor(IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW);
            } else if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    uri = Settings.System.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW);
                } else {
                    uri = Settings.Global.getUriFor(IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW);
                }
            }
            if (uri != null) {
                context.getContentResolver().registerContentObserver(uri, true, this);
                mIsRegister = true;
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context != null && context.getContentResolver() != null
                && mListeners != null && !mListeners.isEmpty()) {
            int show = 0;
            if (OSUtils.isMIUI()) {
                show = Settings.Global.getInt(context.getContentResolver(), IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW, 0);
            } else if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    show = Settings.System.getInt(context.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0);
                } else {
                    show = Settings.Global.getInt(context.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0);
                }
            }
            for (OnNavigationBarListener onNavigationBarListener : mListeners) {
                onNavigationBarListener.onNavigationBarChange(show != 1);
            }
        }
    }

    public void addOnNavigationBarListener(OnNavigationBarListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeOnNavigationBarListener(OnNavigationBarListener listener) {
        if(mIsRegister){
            context.getContentResolver().unregisterContentObserver(this);
            mIsRegister = false;
        }
        this.context = null;
        if (listener == null || mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    private static class NavigationBarObserverInstance {
        private static final NavigationBarObserver INSTANCE = new NavigationBarObserver();
    }

}
