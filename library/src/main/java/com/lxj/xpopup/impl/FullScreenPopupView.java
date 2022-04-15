package com.lxj.xpopup.impl;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 宽高撑满的全屏弹窗
 * Create by lxj, at 2019/2/1
 */
public class FullScreenPopupView extends BasePopupView {
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    protected View contentView;
    protected FrameLayout fullPopupContainer;
    public FullScreenPopupView(@NonNull Context context) {
        super(context);
        fullPopupContainer = findViewById(R.id.fullPopupContainer);
    }
    @Override
    protected int getInnerLayoutId() {
        return R.layout._xpopup_fullscreen_popup_view;
    }

    protected void addInnerContent(){
        contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), fullPopupContainer, false);
        fullPopupContainer.addView(contentView);
    }
    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if(fullPopupContainer.getChildCount()==0) addInnerContent();
        getPopupContentView().setTranslationX(popupInfo.offsetX);
        getPopupContentView().setTranslationY(popupInfo.offsetY);
    }

    private Paint paint = new Paint();
    protected Rect shadowRect;
    int currColor = Color.TRANSPARENT;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (popupInfo!=null && popupInfo.hasStatusBarShadow) {
            paint.setColor(currColor);
            shadowRect = new Rect(0, 0, getMeasuredWidth(), XPopupUtils.getStatusBarHeight());
            canvas.drawRect(shadowRect, paint);
        }
    }

    @Override
    protected void doShowAnimation() {
        super.doShowAnimation();
        doStatusBarColorTransform(true);
    }

    @Override
    protected void doDismissAnimation() {
        super.doDismissAnimation();
        doStatusBarColorTransform(false);
    }

    private void doStatusBarColorTransform(boolean isShow){
        if (popupInfo!=null && popupInfo.hasStatusBarShadow) {
            //状态栏渐变动画
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator,
                    isShow ? Color.TRANSPARENT : getStatusBarBgColor(),
                    isShow ? getStatusBarBgColor() : Color.TRANSPARENT);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currColor = (Integer) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animator.setDuration(getAnimationDuration()).start();
        }
    }

    private TranslateAnimator translateAnimator;
    @Override
    protected PopupAnimator getPopupAnimator() {
        if(translateAnimator==null){
            translateAnimator = new TranslateAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.TranslateFromBottom);
        }
        return translateAnimator;
    }

    @Override
    protected void onDetachedFromWindow() {
        if(popupInfo!=null && translateAnimator!=null){
            getPopupContentView().setTranslationX(translateAnimator.startTranslationX);
            getPopupContentView().setTranslationY(translateAnimator.startTranslationY);
            translateAnimator.hasInit = true;
        }
        super.onDetachedFromWindow();
    }

}
