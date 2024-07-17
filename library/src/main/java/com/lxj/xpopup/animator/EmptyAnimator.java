package com.lxj.xpopup.animator;

import android.view.View;

/**
 * Description: 没有动画效果的动画器
 * Create by dance, at 2019/6/6
 */
public class EmptyAnimator extends PopupAnimator {
    public EmptyAnimator(View target, int animationDuration){
        super(target, animationDuration);
    }
    @Override
    public void initAnimator() {
        targetView.setAlpha(0);
    }

    @Override
    public void animateShow() {
        targetView.animate().alpha(1f).setDuration(animationDuration).withLayer()
                .start();
    }

    @Override
    public void animateDismiss() {
        if(animating)return;
        observerAnimator(targetView.animate().alpha(0f).setDuration(animationDuration).withLayer())
                .start();
    }
}
