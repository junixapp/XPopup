package com.lxj.xpopup.animator;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.View;

import com.lxj.xpopup.XPopup;

/**
 * Description: 背景Shadow动画器，负责执行半透明的渐入渐出动画
 * Create by dance, at 2018/12/9
 */
public class ShadowBgAnimator extends PopupAnimator {

    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    public int startColor = Color.TRANSPARENT;
    public boolean isZeroDuration = false;
    public ShadowBgAnimator(View target) {
        super(target);
    }
    public ShadowBgAnimator() {}
    @Override
    public void initAnimator() {
        targetView.setBackgroundColor(startColor);
    }

    @Override
    public void animateShow() {
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, startColor, XPopup.getShadowBgColor());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration?0:XPopup.getAnimationDuration()).start();
    }

    @Override
    public void animateDismiss() {
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, XPopup.getShadowBgColor(), startColor);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration?0:XPopup.getAnimationDuration()).start();
    }

    public int calculateBgColor(float fraction){
        return (int) argbEvaluator.evaluate(fraction, startColor, XPopup.getShadowBgColor());
    }

}
