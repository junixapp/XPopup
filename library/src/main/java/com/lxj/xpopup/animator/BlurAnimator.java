package com.lxj.xpopup.animator;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 背景Shadow动画器，负责执行半透明的渐入渐出动画
 * Create by dance, at 2018/12/9
 */
public class BlurAnimator extends PopupAnimator {

    private FloatEvaluator evaluate = new FloatEvaluator();
    public BlurAnimator(View target) {
        super(target);
    }
    public Bitmap decorBitmap;

    public BlurAnimator() {}
    @Override
    public void initAnimator() {
        Bitmap blurBmp = XPopupUtils.renderScriptBlur(targetView.getContext(), decorBitmap,  25,false);
        targetView.setBackground(new BitmapDrawable(targetView.getResources(), blurBmp));
    }

    @Override
    public void animateShow() {
//        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float fraction = animation.getAnimatedFraction();
//                Bitmap blurBmp = ImageUtils.renderScriptBlur(decorBitmap, evaluate.evaluate(0f, 25f, fraction), false);
//                targetView.setBackground(new BitmapDrawable(targetView.getResources(), blurBmp));
//            }
//        });
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(XPopup.getAnimationDuration()).start();
    }

    @Override
    public void animateDismiss() {
//        ValueAnimator animator = ValueAnimator.ofFloat(1,0);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float fraction = animation.getAnimatedFraction();
//                Bitmap blurBmp = ImageUtils.renderScriptBlur(decorBitmap, evaluate.evaluate(0f, 25f, fraction), false);
//                targetView.setBackground(new BitmapDrawable(targetView.getResources(), blurBmp));
//            }
//        });
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(XPopup.getAnimationDuration()).start();
    }


}
