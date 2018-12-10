package com.lxj.xpopup.animator;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class ScaleAnimator extends PopupAnimator {
    public ScaleAnimator(View target, int duration, PopupAnimation popupAnimation) {
        super(target, duration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(0f);
        targetView.setScaleY(0f);
        targetView.setAlpha(0);

        // 确保能获取到View最新的宽高
        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 设置动画参考点
                applyPivot();
            }
        });

    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot(){
        switch (popupAnimation){
            case ScaleFromCenter:
                targetView.setPivotX(targetView.getMeasuredWidth()/2);
                targetView.setPivotY(targetView.getMeasuredHeight()/2);
                break;
            case ScaleFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleFromRightTop:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
            case ScaleFromRightBottom:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(animateDuration)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .start();

    }

    @Override
    public void animateDismiss() {
        targetView.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(animateDuration)
                .setInterpolator(new FastOutSlowInInterpolator()).start();
    }



}
