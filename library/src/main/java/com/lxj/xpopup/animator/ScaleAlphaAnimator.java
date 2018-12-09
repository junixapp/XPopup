package com.lxj.xpopup.animator;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class ScaleAlphaAnimator extends PopupAnimator {
    private PopupAnimation popupAnimation;
    public ScaleAlphaAnimator(View target, int duration, PopupAnimation popupAnimation) {
        super(target, duration);
        this.popupAnimation = popupAnimation;
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
                targetView.setPivotX(.5f);
                targetView.setPivotY(.5f);
                break;
            case ScaleAlphaFromLeftTop:
                targetView.setPivotX(0f);
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromRightTop:
                targetView.setPivotX(1f);
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(1f);
                break;
            case ScaleAlphaFromRightBottom:
                targetView.setPivotX(1f);
                targetView.setPivotY(1f);
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
        targetView.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(animateDuration).start();
    }



}
