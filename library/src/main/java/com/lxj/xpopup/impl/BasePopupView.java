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
    private int endBgColor = Color.parseColor("#99000000");
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
     * 显示时的背景动画，由透明到半透明灰色过渡动画
     */
    public void startShadowBgAnimation(){
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

    /**
     * 隐藏时的背景动画，由半透明灰色到透明过渡动画
     */
    public void endShadowBgAnimation(){
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

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    @Override
    public void doShowAnimation() {
        startShadowBgAnimation();
        doShowPopupContentAnimation();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    @Override
    public void doDismissAnimation() {
        endShadowBgAnimation();
        doDismissPopupContentAnimation();
    }
    /**
     * 执行PopupView的Content显示动画
     */
    protected abstract void doShowPopupContentAnimation();

    /**
     * 执行PopupView的Content隐藏动画
     */
    protected abstract void doDismissPopupContentAnimation();

    /**
     * 整个PopupView容器，由背景View和内容View组成
     * @return
     */
    @Override
    public View getPopupView() {
        return this;
    }

    /**
     * 获取背景View
     * @return
     */
    @Override
    public View getBackgroundView() {
        return getChildAt(0);
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。而且我们对PopupView执行的动画，也是对它执行的动画
     * @return
     */
    @Override
    public View getPopupContentView() {
        return getChildAt(1);
    }

    @Override
    public int getAnimationDuration() {
        return 1000;
    }
}
