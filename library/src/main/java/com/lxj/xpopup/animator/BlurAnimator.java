package com.lxj.xpopup.animator;

import android.animation.FloatEvaluator;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 背景模糊动画器
 * Create by dance, at 2018/12/9
 */
public class BlurAnimator extends PopupAnimator {

    private FloatEvaluator evaluate = new FloatEvaluator();
    public BlurAnimator(View target) {
        super(target);
    }
    public Bitmap decorBitmap;
    public boolean hasShadowBg = false;

    public BlurAnimator() {}
    @Override
    public void initAnimator() {
        Bitmap blurBmp = XPopupUtils.renderScriptBlur(targetView.getContext(), decorBitmap,  25, true);
        BitmapDrawable drawable = new BitmapDrawable(targetView.getResources(), blurBmp);
        if(hasShadowBg) drawable.setColorFilter(XPopup.getShadowBgColor(), PorterDuff.Mode.SRC_OVER);
        targetView.setBackground(drawable);
    }

    @Override
    public void animateShow() {
        //有性能问题
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
