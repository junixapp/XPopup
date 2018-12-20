package com.lxj.xpopup.animator;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;

import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 平移动画
 * Create by dance, at 2018/12/9
 */
public class TranslateAlphaAnimator extends PopupAnimator {
    //动画起始坐标
    private float startTranslationX, startTranslationY;
    private float defTranslationX, defTranslationY;
    public TranslateAlphaAnimator(View target, PopupAnimation popupAnimation) {
        super(target, popupAnimation);
    }

    @Override
    public void initAnimator() {
        defTranslationX = targetView.getTranslationX();
        defTranslationY = targetView.getTranslationY();

        targetView.setAlpha(0);
        // 设置移动坐标
        applyTranslation();
        startTranslationX = targetView.getTranslationX();
        startTranslationY = targetView.getTranslationY();
    }

    private void applyTranslation() {
        int halfWidthOffset = Utils.getWindowWidth(targetView.getContext())/2 - targetView.getMeasuredWidth()/2;
        int halfHeightOffset = Utils.getWindowHeight(targetView.getContext())/2 - targetView.getMeasuredHeight()/2;
        switch (popupAnimation){
            case TranslateAlphaFromLeft:
                targetView.setTranslationX(-(targetView.getMeasuredWidth()/* + halfWidthOffset*/));
                break;
            case TranslateAlphaFromTop:
                targetView.setTranslationY(-(targetView.getMeasuredHeight() /*+ halfHeightOffset*/));
                break;
            case TranslateAlphaFromRight:
                targetView.setTranslationX(targetView.getMeasuredWidth() /*+ halfWidthOffset*/);
                break;
            case TranslateAlphaFromBottom:
                targetView.setTranslationY(targetView.getMeasuredHeight() /*+ halfHeightOffset*/);
                break;
        }
    }

    @Override
    public void animateShow() {
        targetView.animate().translationX(defTranslationX).translationY(defTranslationY).alpha(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animateDuration).start();
    }

    @Override
    public void animateDismiss() {
        targetView.animate().translationX(startTranslationX).translationY(startTranslationY).alpha(0f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animateDuration).start();
    }
}
