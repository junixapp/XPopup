package com.lxj.xpopup;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.enums.PopupType;


public class XPopup implements LifecycleObserver {
    private static XPopup instance = null;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Context context;

    private XPopup(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static XPopup get(Context context) {
        if (instance == null) {
            instance = new XPopup(context);
        }
        return instance;
    }

    public void show() {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null!");
        }
        if (!(context instanceof FragmentActivity)) {
            throw new IllegalArgumentException("context must be an instance of FragmentActivity");
        }
        FragmentActivity activity = (FragmentActivity) context;
        activity.getLifecycle().addObserver(this);
        layoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION
        );
        windowManager.addView(popupContainer, layoutParams);
    }

    /**
     * 显示
     */
    public void dismiss() {

    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        windowManager.removeViewImmediate(popupContainer);
    }


    /** 属性 **/
    private PopupType popupPosition = PopupType.Center; //窗体显示的位置
    private Boolean isDismissOnBackPressed = true;  //按返回键是否消失
    private Boolean isDismissOnTouchOutside = true; //点击外部消失
    private View atView = null; // 依附于那个View显示

    public XPopup position(PopupType popupPosition) {
        this.popupPosition = popupPosition;
        return this;
    }
    public XPopup dismissOnBackPressed(boolean isDismissOnBackPressed) {
        this.isDismissOnBackPressed = isDismissOnBackPressed;
        return this;
    }
    public XPopup dismissOnTouchOutside(boolean isDismissOnTouchOutside) {
        this.isDismissOnTouchOutside = isDismissOnTouchOutside;
        return this;
    }
    public XPopup atView(){

    }
}
