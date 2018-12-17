package com.lxj.xpopup;

import android.graphics.Color;

/**
 * Description: 相关配置信息
 * Create by dance, at 2018/12/17
 */
public class XPopupConfig {
    public static int primaryColor = Color.parseColor("#008577");
    public static void init(int primaryColor){
        XPopupConfig.primaryColor = primaryColor;
    }
}
