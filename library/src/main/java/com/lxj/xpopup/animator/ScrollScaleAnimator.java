package com.lxj.xpopup.animator;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
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
    public ScrollScaleAnimator(View target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(startAlpha);
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);

        // 设置参考点
        applyPivot();

        targetView.scrollTo(startScrollX, startScrollY);
    }

    private void applyPivot(){
        switch (popupAnimation){
            case ScrollAlphaFromLeft:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getMeasuredHeight()/2);

                startScrollX = targetView.getMeasuredWidth();
                startScrollY = 0;
                break;
            case ScrollAlphaFromLeftTop:
                targetView.setPivotX(0f);
                targetView.setPivotY(0f);
                startScrollX =  targetView.getMeasuredWidth();
                startScrollY =  targetView.getMeasuredHeight();
                break;
            case ScrollAlphaFromTop:
                targetView.setPivotX(targetView.getMeasuredWidth()/2);
                targetView.setPivotY(0f);

                startScrollY =  targetView.getMeasuredHeight();
                break;
            case ScrollAlphaFromRightTop:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(0f);
                startScrollX =  -targetView.getMeasuredWidth();
                startScrollY =  targetView.getMeasuredHeight();
                break;
            case ScrollAlphaFromRight:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight()/2);

                startScrollX =  -targetView.getMeasuredWidth();
                break;
            case ScrollAlphaFromRightBottom:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight());

                startScrollX =  -targetView.getMeasuredWidth();
                startScrollY =  -targetView.getMeasuredHeight();
                break;
            case ScrollAlphaFromBottom:
                targetView.setPivotX(targetView.getMeasuredWidth()/2);
                targetView.setPivotY(targetView.getMeasuredHeight());

                startScrollY =  -targetView.getMeasuredHeight();
                break;
            case ScrollAlphaFromLeftBottom:
                targetView.setPivotX(0);
                targetView.setPivotY(targetView.getMeasuredHeight());

                startScrollX =  targetView.getMeasuredWidth();
                startScrollY =  -targetView.getMeasuredHeight();
                break;
        }
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
