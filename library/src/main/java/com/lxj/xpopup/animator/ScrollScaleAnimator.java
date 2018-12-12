package com.lxj.xpopup.animator;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewTreeObserver;

import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 像系统的PopupMenu那样的动画
 * Create by lxj, at 2018/12/12
 */
public class ScrollScaleAnimator extends PopupAnimator{

    FloatEvaluator floatEvaluator = new FloatEvaluator();
    IntEvaluator intEvaluator = new IntEvaluator();
    int startScrollX, startScrollY;
    float startAlpha = .2f;
    float startScale = 0f;
    public ScrollScaleAnimator(View target, int duration, PopupAnimation popupAnimation) {
        super(target, duration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(startAlpha);
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);
        targetView.setPivotX(0f);
        targetView.setPivotY(0f);

        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startScrollX = (int) (targetView.getMeasuredWidth());
                startScrollY = (int) (targetView.getMeasuredHeight());
                targetView.scrollTo( startScrollX, startScrollY);
            }
        });

    }

    @Override
    public void animateShow() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                targetView.setAlpha(floatEvaluator.evaluate(fraction, startAlpha, 1f));
                targetView.scrollTo(intEvaluator.evaluate(fraction, startScrollX, 0),
                        intEvaluator.evaluate(fraction, startScrollY, 0));
                float scale = floatEvaluator.evaluate(fraction, startScale, 1f);
                targetView.setScaleX(scale);
                targetView.setScaleY(scale);
            }
        });
        animator.setDuration(animateDuration).setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    @Override
    public void animateDismiss() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                targetView.setAlpha(floatEvaluator.evaluate(fraction, 1f, startAlpha));
                targetView.scrollTo(intEvaluator.evaluate(fraction, 0, startScrollX),
                        intEvaluator.evaluate(fraction, 0, startScrollY));
                float scale = floatEvaluator.evaluate(fraction, 1f, startScale);
                targetView.setScaleX(scale);
                targetView.setScaleY(scale);
            }
        });
        animator.setDuration(animateDuration)
                .setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    private void doAnim(){

    }
}
