package com.lxj.xpopup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PopupInfo;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.impl.AttachListPopupView;
import com.lxj.xpopup.impl.BottomListPopupView;
import com.lxj.xpopup.impl.CenterListPopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.util.KeyboardUtils;

import java.lang.ref.WeakReference;

/**
 * PopupView的控制类，控制生命周期：显示，隐藏，添加，删除。
 */
public class XPopup implements BasePopupView.DismissProxy {
    private static XPopup instance = null;
    private static WeakReference<Context> contextRef;
    private PopupInfo popupInfo = null;
    private Handler handler = new Handler();
    private ViewGroup activityView = null;
    private PopupStatus popupStatus = PopupStatus.Dismiss;
    private BasePopupView popupView;
    private XPopupCallback xPopupCallback;
    private int primaryColor = Color.parseColor("#121212");

    private XPopup() {
    }

    public static XPopup get(Context ctx) {
        if (instance == null) {
            instance = new XPopup();
        }
        contextRef = new WeakReference<>(ctx);
        return instance;
    }

    /**
     * 显示，本质是就将View添加到decorView上，并执行动画
     */
    public void show() {
        if (popupStatus != PopupStatus.Dismiss) return;

        if (contextRef.get() == null) {
            throw new IllegalArgumentException("context can not be null!");
        }
        if (!(contextRef.get() instanceof Activity)) {
            throw new IllegalArgumentException("context must be an instance of Activity");
        }
        Activity activity = (Activity) contextRef.get();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activityView = (ViewGroup) activity.getWindow().getDecorView();

        //1. set popupView
        popupView.setPopupInfo(popupInfo);
        popupView.setDismissProxy(this);

        activityView.addView(popupView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        popupStatus = PopupStatus.Showing;

        //2. 执行初始化
        popupView.init(new Runnable() {
            @Override
            public void run() {
                popupStatus = PopupStatus.Show;
                if (xPopupCallback != null)
                    xPopupCallback.onShow();
            }
        });

        KeyboardUtils.registerSoftInputChangedListener(activity, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) { // 说明对话框隐藏
                    popupView.getPopupContentView().animate().translationY(0)
                            .setDuration(300).start();
                }
            }
        });
    }


    /**
     * 消失
     */
    public void dismiss() {
        if (popupStatus != PopupStatus.Show) return;
        //1. 执行结束动画
        popupStatus = PopupStatus.Dismissing;
        popupView.doDismissAnimation();

        //2. 将PopupView从window中移除
        handler.removeCallbacks(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupStatus = PopupStatus.Dismiss;
                popupInfo = null;
                contextRef.clear();
                contextRef = null;
                if (xPopupCallback != null)
                    xPopupCallback.onDismiss();
                if (activityView != null) {
                    activityView.removeView(popupView);
                    activityView = null;
                }
            }
        }, popupView.getAnimationDuration());
    }

    /**
     * 设置主色调
     *
     * @param color
     */
    public void setPrimaryColor(int color) {
        this.primaryColor = color;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    /**
     * 设置显示和隐藏的回调
     *
     * @param callback
     * @return
     */
    public XPopup setPopupCallback(XPopupCallback callback) {
        this.xPopupCallback = callback;
        return this;
    }

    private XPopup position(PopupType popupType) {
        checkPopupInfo();
        popupInfo.popupType = popupType;
        return this;
    }

    /**
     * 设置某个内置的动画类型
     *
     * @param animation
     * @return
     */
    public XPopup popupAnimation(PopupAnimation animation) {
        checkPopupInfo();
        popupInfo.popupAnimation = animation;
        return this;
    }

    /**
     * 设置自定义的动画器
     *
     * @param animator
     * @return
     */
    public XPopup customAnimator(PopupAnimator animator) {
        checkPopupInfo();
        popupInfo.customAnimator = animator;
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
        popupInfo.touchPoint = null;
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

    /**
     * 收集某个View的按下坐标，用于Attach类型的弹窗显示。如果调用这个方法，弹窗就有了
     * 参考点，无需再调用atView方法了
     *
     * @param view
     * @return
     */
    public XPopup watch(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN &&
                        popupStatus == PopupStatus.Dismiss) {
                    checkPopupInfo();
                    popupInfo.touchPoint = new PointF(event.getRawX(), event.getRawY());
                }
                return false;
            }
        });
        return this;
    }

    /************** 便捷方法 ************/

    /**
     * 显示确认和取消对话框
     *
     * @param title           对话框标题
     * @param content         对话框内容
     * @param confirmListener 点击确认的监听器
     * @param cancelListener  点击取消的监听器
     * @return
     */
    public XPopup asConfirm(String title, String content, OnConfirmListener confirmListener, OnCancelListener cancelListener) {
        if (popupStatus != PopupStatus.Dismiss) return this;
        position(PopupType.Center);

        ConfirmPopupView popupView = new ConfirmPopupView(contextRef.get());
        popupView.setTitleContent(title, content);
        popupView.setListener(confirmListener, cancelListener);
        this.popupView = popupView;
        return this;
    }

    public XPopup asConfirm(String title, String content, OnConfirmListener confirmListener) {
        return asConfirm(title, content, confirmListener, null);
    }

    /**
     * 显示带有输入框，确认和取消对话框
     *
     * @param title           对话框标题
     * @param content         对话框内容
     * @param confirmListener 点击确认的监听器
     * @param cancelListener  点击取消的监听器
     * @return
     */
    public XPopup asInputConfirm(String title, String content, OnInputConfirmListener confirmListener, OnCancelListener cancelListener) {
        if (popupStatus != PopupStatus.Dismiss) return this;
        position(PopupType.Center);

        InputConfirmPopupView popupView = new InputConfirmPopupView(contextRef.get());
        popupView.setTitleContent(title, content);
        popupView.setListener(confirmListener, cancelListener);
        this.popupView = popupView;
        return this;
    }

    public XPopup asInputConfirm(String title, String content, OnInputConfirmListener confirmListener) {
        return asInputConfirm(title, content, confirmListener, null);
    }

    /**
     * 显示在中间的列表Popup
     *
     * @param title          标题，可以不传，不传则不显示
     * @param data           显示的文本数据
     * @param iconIds        图标的id数组，可以没有
     * @param selectListener 选中条目的监听器
     * @return
     */
    public XPopup asCenterList(String title, String[] data, int[] iconIds, int checkedPosition, OnSelectListener selectListener) {
        if (popupStatus != PopupStatus.Dismiss) return this;
        position(PopupType.Center);
        this.popupView = new CenterListPopupView(contextRef.get())
                .setStringData(title, data, iconIds)
                .setCheckedPosition(checkedPosition)
                .setOnSelectListener(selectListener);
        return this;
    }

    public XPopup asCenterList(String title, String[] data, OnSelectListener selectListener) {
        return asCenterList(title, data, null, -1, selectListener);
    }

    public XPopup asCenterList(String title, String[] data, int[] iconIds, OnSelectListener selectListener) {
        return asCenterList(title, data, iconIds, -1, selectListener);
    }


    /**
     * 显示在中间加载的弹窗
     *
     * @return
     */
    public XPopup asLoading() {
        if (popupStatus != PopupStatus.Dismiss) return this;
        position(PopupType.Center);
        this.popupView = new LoadingPopupView(contextRef.get());
        return this;
    }


    /**
     * 显示在底部的列表Popup
     *
     * @param title          标题，可以不传，不传则不显示
     * @param data           显示的文本数据
     * @param iconIds        图标的id数组，可以没有
     * @param selectListener 选中条目的监听器
     * @return
     */
    public XPopup asBottomList(String title, String[] data, int[] iconIds, int checkedPosition, OnSelectListener selectListener) {
        if (popupStatus != PopupStatus.Dismiss) return this;
        position(PopupType.Bottom);
        this.popupView = new BottomListPopupView(contextRef.get())
                .setStringData(title, data, iconIds)
                .setCheckedPosition(checkedPosition)
                .setOnSelectListener(selectListener);
        return this;
    }

    public XPopup asBottomList(String title, String[] data, OnSelectListener selectListener) {
        return asBottomList(title, data, null, -1, selectListener);
    }

    public XPopup asBottomList(String title, String[] data, int[] iconIds, OnSelectListener selectListener) {
        return asBottomList(title, data, iconIds, -1, selectListener);
    }


    /**
     * 显示依附于某View的列表，必须调用atView()方法，指定依附的View
     *
     * @param data           显示的文本数据
     * @param iconIds        图标的id数组，可以为null
     * @param offsetX        x方向偏移量
     * @param offsetY        y方向偏移量
     * @param selectListener 选中条目的监听器
     * @return
     */
    public XPopup asAttachList(String[] data, int[] iconIds, int offsetX, int offsetY, OnSelectListener selectListener) {
        if (popupStatus != PopupStatus.Dismiss) return this;

        position(PopupType.AttachView);

        this.popupView = new AttachListPopupView(contextRef.get())
                .setStringData(data, iconIds)
                .setOffsetXAndY(offsetX, offsetY)
                .setOnSelectListener(selectListener);
        return this;
    }

    public XPopup asAttachList(String[] data, int[] iconIds, OnSelectListener selectListener) {
        return asAttachList(data, iconIds, 0, 0, selectListener);
    }


    /**
     * 自定义弹窗
     **/
    public XPopup asCustom(BasePopupView popupView) {
        if (popupStatus != PopupStatus.Dismiss) return this;
        if (popupView instanceof CenterPopupView) {
            position(PopupType.Center);
        } else if (popupView instanceof BottomPopupView) {
            position(PopupType.Bottom);
        } else if (popupView instanceof AttachPopupView) {
            position(PopupType.AttachView);
        } else {
            checkPopupInfo();
        }
        this.popupView = popupView;
        return this;
    }
}
