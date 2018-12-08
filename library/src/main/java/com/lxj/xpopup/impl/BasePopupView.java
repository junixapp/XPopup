package com.lxj.xpopup.impl;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.PopupInfo;
import com.lxj.xpopup.PopupInterface;
import com.lxj.xpopup.widget.ClickConsumeView;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends FrameLayout implements PopupInterface {
    protected PopupInfo popupInfo;
    protected ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int endBgColor = Color.parseColor("#66000000");
    public BasePopupView(@NonNull Context context) {
        this(context, null);
        setBackgroundColor(Color.TRANSPARENT);

        // 1.添加背景View，用来拦截所有内容之外的点击
        ClickConsumeView view = new ClickConsumeView(context);
        addView( view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


        // 2. 添加Popup窗体内容View
        addView(LayoutInflater.from(context).inflate(getPopupLayoutId() , this,false));
    }

    public BasePopupView( @NonNull Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public BasePopupView( @NonNull Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPopupInfo(PopupInfo popupInfo){
        this.popupInfo = popupInfo;


        initPopup();
    }

    protected abstract int getPopupLayoutId();
    protected abstract void initPopup();

    /**
     * 父类默认执行shadow bg animation
     */
    @Override
    public void startAnimation() {
        if(popupInfo.hasShadowBg){
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, Color.TRANSPARENT, endBgColor);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setBackgroundColor((Integer) animation.getAnimatedValue());
                }
            });
            animator.setDuration(getAnimationDuration()).start();
        }
    }

    @Override
    public void endAnimation() {
        if(popupInfo.hasShadowBg){
            ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, endBgColor, Color.TRANSPARENT);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setBackgroundColor((Integer) animation.getAnimatedValue());
                }
            });
            animator.setDuration(getAnimationDuration()).start();
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public View getBackgroundView() {
        return getChildAt(0);
    }

    @Override
    public int getAnimationDuration() {
        return 500;
    }
}
