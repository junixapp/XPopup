package com.lxj.xpopup.animator;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 缩放透明
 * Create by dance, at 2018/12/9
 */
public class ScaleAlphaAnimator extends PopupAnimator {
    public ScaleAlphaAnimator(View target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(0f);
        targetView.setScaleY(0f);
        targetView.setAlpha(0);

        // 设置动画参考点
        applyPivot();
    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot(){
        switch (popupAnimation){
            case ScaleAlphaFromCenter:
                targetView.setPivotX(targetView.getMeasuredWidth()/2);
                targetView.setPivotY(targetView.getMeasuredHeight()/2);
                break;
            case ScaleAlphaFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleAlphaFromRightTop:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
            case ScaleAlphaFromRightBottom:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(animateDuration)
                .setInterpolator(new OvershootInterpolator(1f))
                .start();
    }

    @Override
    public void animateDismiss() {
        targetView.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(animateDuration)
                .setInterpolator(new FastOutSlowInInterpolator()).start();
    }

}
