package com.lxj.xpopup.util.navbar;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import java.lang.reflect.Method;

/**
 * copy from https://github.com/gyf-dev/ImmersionBar.
 * 手机系统判断
 *
 * @author geyifeng
 * @date 2017/4/18
 */
public class OSUtils {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String KEY_DISPLAY = "ro.build.display.id";

    /**
     * 判断是否为miui
     * Is miui boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }
    /**
     * 判断是否为emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    /**
     * 得到emui的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_NAME, "") : "";
    }

    /**
     * 判断是否为emui3.1版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        if ("EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为emui3.0版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_0() {
        String property = getEMUIVersion();
        if (property.contains("EmotionUI_3.0")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为emui3.x版本
     * Is emui 3 x boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_x() {
        return OSUtils.isEMUI3_0() || OSUtils.isEMUI3_1();
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            @SuppressLint("PrivateApi") Class<?> clz = Class.forName("android.os.SystemProperties");
            Method method = clz.getMethod("get", String.class, String.class);
            return (String) method.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
