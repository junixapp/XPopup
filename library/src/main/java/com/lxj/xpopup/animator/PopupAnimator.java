package com.lxj.xpopup.animator;

import android.view.View;

import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public abstract class PopupAnimator {
    protected View targetView;
    protected int animateDuration;
    protected PopupAnimation popupAnimation;
    public PopupAnimator(View target, int duration){
        this(target, duration, null);
    }

    public PopupAnimator(View target, int duration, PopupAnimation popupAnimation){
        this.targetView = target;
        this.animateDuration = duration;
        this.popupAnimation = popupAnimation;
    }

    public abstract void initAnimator();
    public abstract void animateShow();
    public abstract void animateDismiss();
}
