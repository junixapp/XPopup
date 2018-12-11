package com.lxj.xpopup;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopup.impl.AttachPopupView;
import com.lxj.xpopup.impl.BasePopupView;
import com.lxj.xpopup.impl.BottomPopupView;
import com.lxj.xpopup.impl.CenterPopupView;

/**
 * PopupView的控制类，控制生命周期：显示，隐藏，添加，删除。
 */
public class XPopup implements LifecycleObserver {
    private static XPopup instance = null;
    private Context context;
    private PopupInfo popupInfo = null;
    private PopupInterface popupInterface;
    private Handler handler = new Handler();
    private ViewGroup activityView = null;
    private PopupStatus popupStatus = PopupStatus.Dismiss;
    private XPopup(Context context) {
        this.context = context;
    }

    public static XPopup get(Context context) {
        if (instance == null) {
            instance = new XPopup(context);
        }
        return instance;
    }

    /**
     * 显示，本质是就将View添加到Window上，并执行动画
     */
    public void show() {
        if(popupStatus!=PopupStatus.Dismiss)return;
        if (context == null) {
            throw new IllegalArgumentException("context can not be null!");
        }
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("context must be an instance of Activity");
        }
        Activity activity = (Activity) context;
        activityView = (ViewGroup) activity.getWindow().getDecorView();

        //1. 根据PopupInfo生成PopupView
        popupInterface = genPopupImpl();
        if (popupInterface.getPopupView() == null) {
            throw new RuntimeException("PopupInterface getPopupView() method can not return null!");
        }
        Log.e("tag", "activityView child: "+ activityView.getChildCount());
        activityView.addView(popupInterface.getPopupView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        activityView.bringChildToFront(popupInterface.getPopupView());

        // 监听KeyEvent
        popupInterface.getPopupView().setFocusableInTouchMode(true);
        popupInterface.getPopupView().requestFocus();
        popupInterface.getPopupView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });

        // 监听点击
        popupInterface.getBackgroundView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //2. 执行开始动画
        popupInterface.getPopupView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                popupInterface.getPopupView().getViewTreeObserver().removeOnPreDrawListener(this);
                popupStatus = PopupStatus.Showing;

                popupInterface.doShowAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupStatus = PopupStatus.Show;
                    }
                }, popupInterface.getAnimationDuration()+10);
                return true;
            }
        });


    }

    /**
     * 根据PopupInfo生成对应
     *
     * @return
     */
    private PopupInterface genPopupImpl() {
        checkPopupInfo();
        Log.d("XPopup", popupInfo.toString());
        BasePopupView popupView = null;
        switch (popupInfo.popupType) {
            case Center:
                popupView = new CenterPopupView(context);
                break;
            case Bottom:
                popupView = new BottomPopupView(context);
                break;
            case AttachView:
                popupView = new AttachPopupView(context);
                break;
        }
        popupView.setPopupInfo(popupInfo);
        return popupView;
    }

    /**
     * 消失
     */
    public void dismiss() {
        if(popupStatus!=PopupStatus.Show)return;
        //1. 执行结束动画
        popupStatus = PopupStatus.Dismissing;
        popupInterface.doDismissAnimation();

        //2. 将PopupView从window中移除
        handler.removeCallbacks(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(activityView!=null){
                    activityView.removeView(popupInterface.getPopupView());
                    activityView = null;
                    popupInfo = null;
                    popupStatus = PopupStatus.Dismiss;
                }
            }
        }, popupInterface.getAnimationDuration() + 10);
    }

    public XPopup position(PopupType popupType) {
        checkPopupInfo();
        popupInfo.popupType = popupType;
        return this;
    }

    public XPopup popupAnimation(PopupAnimation animation){
        checkPopupInfo();
        popupInfo.popupAnimation = animation;
        return this;
    }

    public XPopup dismissOnBackPressed(boolean isDismissOnBackPressed) {
        checkPopupInfo();
        popupInfo.isDismissOnBackPressed = isDismissOnBackPressed;
        return this;
    }

    public XPopup dismissOnTouchOutside(boolean isDismissOnTouchOutside) {
        checkPopupInfo();
        popupInfo.isDismissOnTouchOutside = isDismissOnTouchOutside;
        return this;
    }

    public XPopup atView(View view) {
        checkPopupInfo();
        popupInfo.setAtView(view);
        return this;
    }

    public XPopup hasShadowBg(boolean hasShadowBg) {
        checkPopupInfo();
        popupInfo.hasShadowBg = hasShadowBg;
        return this;
    }

    private void checkPopupInfo() {
        if (popupInfo == null) {
            popupInfo = new PopupInfo();
        }
    }
}
