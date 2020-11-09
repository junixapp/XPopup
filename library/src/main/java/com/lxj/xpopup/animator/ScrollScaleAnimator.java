package com.lxj.xpopup.animator;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 像系统的PopupMenu那样的动画
 * Create by lxj, at 2018/12/12
 */
public class ScrollScaleAnimator extends PopupAnimator{

    private IntEvaluator intEvaluator = new IntEvaluator();
    private int startScrollX, startScrollY;
    private float startAlpha = 0f;
    private float startScale = 0f;

    public boolean isOnlyScaleX = false;
    public ScrollScaleAnimator(View target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(startAlpha);
        targetView.setScaleX(startScale);
        if(!isOnlyScaleX){
            targetView.setScaleY(startScale);
        }

        targetView.post(new Runnable() {
            @Override
            public void run() {
                // 设置参考点
                applyPivot();
                targetView.scrollTo(startScrollX, startScrollY);
            }
        });
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
        targetView.post(new Runnable() {
            @Override
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();
                        targetView.setAlpha(fraction);
                        targetView.scrollTo(intEvaluator.evaluate(fraction, startScrollX, 0),
                                intEvaluator.evaluate(fraction, startScrollY, 0));
                        targetView.setScaleX(fraction);
                        if(!isOnlyScaleX)targetView.setScaleY(fraction);
                    }
                });
                animator.setDuration(XPopup.getAnimationDuration()).setInterpolator(new FastOutSlowInInterpolator());
                animator.start();
            }
        });

    }

    @Override
    public void animateDismiss() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                targetView.setAlpha(1-fraction);
                targetView.scrollTo(intEvaluator.evaluate(fraction, 0, startScrollX),
                        intEvaluator.evaluate(fraction, 0, startScrollY));
                targetView.setScaleX(1-fraction);
                if(!isOnlyScaleX)targetView.setScaleY(1-fraction);
            }
        });
        animator.setDuration(XPopup.getAnimationDuration())
                .setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

}
