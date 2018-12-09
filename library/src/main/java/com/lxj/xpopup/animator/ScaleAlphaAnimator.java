package com.lxj.xpopup.animator;

import android.view.View;

import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class ScaleAlphaAnimator extends PopupAnimator {
    private PopupAnimation popupAnimation;
    public ScaleAlphaAnimator(View target, int duration, PopupAnimation popupAnimation) {
        super(target, duration);
        this.popupAnimation = popupAnimation;
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(0f);
        targetView.setScaleY(0f);
    }

    @Override
    public void animateShow() {
        targetView.animate().scaleX(1f).scaleY(1f).setDuration(animateDuration).start();

    }

    @Override
    public void animateDismiss() {
        targetView.animate().scaleX(0f).scaleY(0f).setDuration(animateDuration).start();

    }
}
