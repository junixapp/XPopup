package com.lxj.xpopup.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 弹窗动画执行器
 * Create by dance, at 2018/12/9
 */
public abstract class PopupAnimator {
    protected boolean animating = false;
    public View targetView;
    public int animationDuration = 0;
    public PopupAnimation popupAnimation; // 内置的动画
    public PopupAnimator(){}
    public PopupAnimator(View target, int animationDuration){
        this(target, animationDuration, null);
    }

    public PopupAnimator(View target, int animationDuration, PopupAnimation popupAnimation){
        this.targetView = target;
        this.animationDuration = animationDuration;
        this.popupAnimation = popupAnimation;
    }

    public abstract void initAnimator();
    public abstract void animateShow();
    public abstract void animateDismiss();
    public int getDuration(){
        return animationDuration;
    }

    protected ValueAnimator observerAnimator(ValueAnimator animator){
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animating = true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
            }
        });
        return animator;
    }
    protected ViewPropertyAnimator observerAnimator(ViewPropertyAnimator animator){
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animating = true;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
            }
        });
        return animator;
    }
}
