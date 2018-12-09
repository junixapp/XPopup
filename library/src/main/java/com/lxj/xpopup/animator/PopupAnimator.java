package com.lxj.xpopup.animator;

import android.view.View;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public abstract class PopupAnimator {
    protected View targetView;
    protected int animateDuration;
    public PopupAnimator(View target, int duration){
        this.targetView = target;
        this.animateDuration = duration;
    }

    public abstract void initAnimator();
    public abstract void animateShow();
    public abstract void animateDismiss();
}
