package com.lxj.xpopup.animator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 缩放透明
 * Create by dance, at 2018/12/9
 */
public class ScaleAlphaAnimator extends PopupAnimator {
    public ScaleAlphaAnimator(View target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }
    public static float fromScaleX = 0.7f;
    public static float fromScaleY = 0.7f;
    public static float fromAlpha = 0.1f;
    @Override
    public void initAnimator() {
        targetView.setScaleX(fromScaleX);
        targetView.setScaleY(fromScaleY);
        targetView.setAlpha(fromAlpha);

        // 设置动画参考点
        targetView.post(new Runnable() {
            @Override
            public void run() {
                applyPivot();
            }
        });
    }

    /**
     * 根据不同的PopupAnimation来设定对应的pivot
     */
    private void applyPivot() {
        switch (popupAnimation) {
            case ScaleAlphaFromCenter:
                targetView.setPivotX(targetView.getMeasuredWidth() / 2);
                targetView.setPivotY(targetView.getMeasuredHeight() / 2);
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
                .setDuration(XPopup.getAnimationDuration())
                .setInterpolator(new OvershootInterpolator(1f))
                .start();
    }

    @Override
    public void animateDismiss() {
        targetView.animate().scaleX(fromScaleX).scaleY(fromScaleY).alpha(0f).setDuration(XPopup.getAnimationDuration())
                .setInterpolator(new FastOutSlowInInterpolator()).start();
    }

}
