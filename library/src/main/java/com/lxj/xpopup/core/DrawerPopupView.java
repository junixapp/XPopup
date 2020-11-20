package com.lxj.xpopup.core;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.PopupDrawerLayout;

/**
 * Description: 带Drawer的弹窗
 * Create by dance, at 2018/12/20
 */
public abstract class DrawerPopupView extends BasePopupView {
    protected PopupDrawerLayout drawerLayout;
    protected FrameLayout drawerContentContainer;
    float mFraction = 0f;
    public DrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerContentContainer = findViewById(R.id.drawerContentContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), drawerContentContainer, false);
        drawerContentContainer.addView(contentView);
    }

    @Override
    public View getPopupImplView() {
        return drawerContentContainer.getChildAt(0);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_drawer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        drawerLayout.enableShadow = popupInfo.hasShadowBg;
        drawerLayout.isDismissOnTouchOutside = popupInfo.isDismissOnTouchOutside;
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                beforeDismiss();
                if(popupInfo.xPopupCallback!=null) popupInfo.xPopupCallback.beforeDismiss(DrawerPopupView.this);
                doAfterDismiss();
            }
            @Override
            public void onOpen() {
                DrawerPopupView.super.doAfterShow();
            }
            @Override
            public void onDrag(int x, float fraction, boolean isToLeft) {
                drawerLayout.isDrawStatusBarShadow = popupInfo.hasStatusBarShadow;
                if(popupInfo.xPopupCallback!=null) popupInfo.xPopupCallback.onDrag(DrawerPopupView.this,
                        x, fraction,isToLeft);
                mFraction = fraction;
                postInvalidate();
            }
        });
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        drawerLayout.setDrawerPosition(popupInfo.popupPosition == null ? PopupPosition.Left : popupInfo.popupPosition);
        drawerLayout.enableDrag = popupInfo.enableDrag;
    }

    Paint paint = new Paint();
    Rect shadowRect;
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int currColor = Color.TRANSPARENT;
    int defaultColor = Color.TRANSPARENT;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (popupInfo.hasStatusBarShadow) {
            if (shadowRect == null) {
                shadowRect = new Rect(0, 0, getMeasuredWidth(), XPopupUtils.getStatusBarHeight());
            }
            paint.setColor((Integer) argbEvaluator.evaluate(mFraction, defaultColor, XPopup.statusBarShadowColor));
            canvas.drawRect(shadowRect, paint);
        }
    }
    public void doStatusBarColorTransform(boolean isShow){
        if (popupInfo.hasStatusBarShadow) {
            //状态栏渐变动画
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator,
                    isShow ? Color.TRANSPARENT : XPopup.statusBarShadowColor,
                    isShow ? XPopup.statusBarShadowColor : Color.TRANSPARENT);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currColor = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.setDuration(XPopup.getAnimationDuration()).start();
        }
    }
    @Override
    protected void doAfterShow() { }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
        doStatusBarColorTransform(true);
    }

    @Override
    public void doDismissAnimation() {
    }

    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        clearFocus();
        doStatusBarColorTransform(false);
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
    }
    @Override
    protected PopupAnimator getPopupAnimator() {
        return null;
    }
    @Override
    protected View getTargetSizeView() {
        return getPopupImplView();
    }
}
