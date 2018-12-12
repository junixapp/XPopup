package com.lxj.xpopup.animator;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewTreeObserver;

import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 平移动画，不带渐变
 * Create by dance, at 2018/12/9
 */
public class TranslateAnimator extends PopupAnimator {
    //动画起始坐标
    private float startTranslationX, startTranslationY;
    public TranslateAnimator(View target, int duration, PopupAnimation popupAnimation) {
        super(target, duration, popupAnimation);
    }

    @Override
    public void initAnimator() {
        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 设置移动坐标
                applyTranslation();
                startTranslationX = targetView.getTranslationX();
                startTranslationY = targetView.getTranslationY();
            }
        });
    }

    private void applyTranslation() {
        int halfWidthOffset = Utils.getWindowWidth(targetView.getContext())/2 - targetView.getMeasuredWidth()/2;
        int halfHeightOffset = Utils.getWindowHeight(targetView.getContext())/2 - targetView.getMeasuredHeight()/2;
        switch (popupAnimation){
            case TranslateFromLeft:
                targetView.setTranslationX(-(targetView.getMeasuredWidth()/* + halfWidthOffset*/));
                break;
            case TranslateFromTop:
                targetView.setTranslationY(-(targetView.getMeasuredHeight() /*+ halfHeightOffset*/));
                break;
            case TranslateFromRight:
                targetView.setTranslationX(targetView.getMeasuredWidth() /*+ halfWidthOffset*/);
                break;
            case TranslateFromBottom:
                targetView.setTranslationY(targetView.getMeasuredHeight() /*+ halfHeightOffset*/);
                break;
        }
    }

    @Override
    public void animateShow() {
        targetView.animate().translationX(0).translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animateDuration).start();
    }

    @Override
    public void animateDismiss() {
        targetView.animate().translationX(startTranslationX).translationY(startTranslationY)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animateDuration).start();
    }
}
