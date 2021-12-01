package com.lxj.xpopup.core;

import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.util.ArrayList;

/**
 * Description: Popup的属性封装
 * Create by dance, at 2018/12/8
 */
public class PopupInfo {
    public PopupType popupType = null; //窗体的类型
    public Boolean isDismissOnBackPressed = true;  //按返回键是否消失
    public Boolean isDismissOnTouchOutside = true; //点击外部消失
    public Boolean autoDismiss = true; //操作完毕后是否自动关闭
    public Boolean hasShadowBg = true; // 是否有半透明的背景
    public Boolean hasBlurBg = false; // 是否有高斯模糊背景
    public View atView = null; // 依附于那个View
    // 动画执行器，如果不指定，则会根据窗体类型popupType字段生成默认合适的动画执行器
    public PopupAnimation popupAnimation = null;
    public PopupAnimator customAnimator = null;
    public PointF touchPoint = null; // 触摸的点
    public int maxWidth; // 最大宽度
    public int maxHeight; // 最大高度
    public int popupWidth, popupHeight; // 指定弹窗的宽高，受max的宽高限制
    public float borderRadius = 15; // 圆角
    public Boolean autoOpenSoftInput = false;//是否自动打开输入法
    public XPopupCallback xPopupCallback;

    public Boolean isMoveUpToKeyboard = true; //是否移动到软键盘上面，默认弹窗会移到软键盘上面
    public PopupPosition popupPosition = null; //弹窗出现在目标的什么位置
    public Boolean hasStatusBarShadow = false; //是否显示状态栏阴影
    public Boolean hasStatusBar = true; //是否显示状态栏
    public Boolean hasNavigationBar = true; //是否显示导航栏
    public int navigationBarColor = 0; //是否显示导航栏
    public int isLightNavigationBar = 0; //是否是亮色导航栏，>0为true，<0为false
    public int isLightStatusBar = 0; //是否是亮色状态栏，>0为true，<0为false
    public int offsetX, offsetY;//x，y方向的偏移量
    public Boolean enableDrag = true;//是否启用拖拽
    public boolean isCenterHorizontal = false;//是否水平居中
    public boolean isRequestFocus = true; //弹窗是否强制抢占焦点
    public boolean autoFocusEditText = true; //是否让输入框自动获取焦点
    public boolean isClickThrough = false;//是否点击透传，默认弹背景点击是拦截的
    public boolean isTouchThrough = false;//是否触摸透传，默认是不支持的
    public boolean isDarkTheme = false; //是否是暗色调主题
    public boolean enableShowWhenAppBackground = false; //是否允许应用在后台的时候也能弹出弹窗
    public boolean isThreeDrag = false; //是否开启三阶拖拽
    public boolean isDestroyOnDismiss = false; //是否关闭后进行资源释放
    public boolean positionByWindowCenter = false; //是否已屏幕中心进行定位，默认根据Material范式进行定位
    public boolean isViewMode = false; //是否是View实现，默认是Dialog实现
    public boolean keepScreenOn = false; //是否保持屏幕常亮
    public int shadowBgColor = 0; //阴影背景的颜色
    public int animationDuration = -1; //动画的时长
    public int statusBarBgColor = 0; //状态栏阴影颜色，对Drawer弹窗和全屏弹窗有效
    public ArrayList<Rect> notDismissWhenTouchInArea; //当触摸在这个区域时，不消失
    public Lifecycle hostLifecycle; //自定义的宿主生命周期

    public Rect getAtViewRect(){
        int[] locations = new int[2];
        atView.getLocationInWindow(locations);
        return new Rect(locations[0], locations[1], locations[0] + atView.getMeasuredWidth(),
                locations[1] + atView.getMeasuredHeight());
    }
}
