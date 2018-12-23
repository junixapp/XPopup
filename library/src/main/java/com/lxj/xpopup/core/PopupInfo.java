package com.lxj.xpopup.core;

import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupType;

/**
 * Description: PopupView的属性封装
 * Create by dance, at 2018/12/8
 */
public class PopupInfo {
    public PopupType popupType = null; //窗体的类型
    public Boolean isDismissOnBackPressed = true;  //按返回键是否消失
    public Boolean isDismissOnTouchOutside = true; //点击外部消失
    public Boolean hasShadowBg = true; // 是否有半透明的背景
    private View atView = null; // 依附于那个View显示
    // 动画执行器，如果不指定，则会根据窗体类型popupType字段生成默认合适的动画执行器
    public PopupAnimation popupAnimation = null;
    public PopupAnimator customAnimator = null;
    public PointF touchPoint = null; // 触摸的点

    public View getAtView() {
        return atView;
    }
    public void setAtView(View atView) {
        this.atView = atView;
        this.popupType = PopupType.AttachView;
    }

    @Override
    public String toString() {
        return "PopupInfo{" +
                "popupType=" + popupType +
                ", isDismissOnBackPressed=" + isDismissOnBackPressed +
                ", isDismissOnTouchOutside=" + isDismissOnTouchOutside +
                ", hasShadowBg=" + hasShadowBg +
                ", atView=" + atView +
                ", popupAnimation=" + popupAnimation +
                ", customAnimator=" + customAnimator +
                ", touchPoint=" + touchPoint +
                '}';
    }
}
