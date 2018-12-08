package com.lxj.xpopup.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class Utils {
    public static int getWindowWidth(Context context){
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }
    public static int getWindowHeight(Context context){
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
