package com.lxj.xpopup.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.animator.TranslateAlphaAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.util.Utils;
import com.lxj.xpopup.widget.ClickConsumeView;

import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;
import static com.lxj.xpopup.enums.PopupAnimation.ScrollAlphaFromLeftTop;
import static com.lxj.xpopup.enums.PopupAnimation.TranslateFromBottom;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends FrameLayout implements PopupInterface {
    protected PopupInfo popupInfo;

    protected PopupAnimator popupContentAnimator;
    protected PopupAnimator shadowBgAnimator;

    public BasePopupView(@NonNull Context context) {
        super(context);

        // 1.添加背景View，用来拦截所有内容之外的点击
        ClickConsumeView bgView = new ClickConsumeView(context);
        addView(bgView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        shadowBgAnimator = new ShadowBgAnimator(bgView);

        // 2. 添加Popup窗体内容View
        View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();
//        params.topMargin = Utils.getStatusBarHeight();
//        params.bottomMargin = Utils.getNavBarHeight();
//        contentView.setLayoutParams(params);
        // 事先隐藏，等测量完毕恢复。避免View影子跳动现象
        contentView.setAlpha(0);
        addView(contentView);
    }

    public BasePopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPopupInfo(PopupInfo popupInfo) {
        this.popupInfo = popupInfo;
    }

    /**
     * 执行初始化
     * @param afterAnimationStarted
     */
    public void init(final Runnable afterAnimationStarted){
        //1. 初始化Popup
        initPopupContent();

        post(new Runnable() {
            @Override
            public void run() {
                getPopupContentView().setAlpha(1f);

                //2. 收集动画执行器
                // 优先使用自定义的动画器
                if (popupInfo.customAnimator!=null) {
                    popupContentAnimator = popupInfo.customAnimator;
                    popupContentAnimator.targetView = getPopupContentView();
                } else {
                    // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                    popupContentAnimator = genAnimatorByPopupType();
                    if (popupContentAnimator == null) {
                        // 使用默认的animator
                        popupContentAnimator = getPopupAnimator();
                    }
                }
                if(popupContentAnimator==null){
                    throw new IllegalArgumentException("No PopupAnimator impl!");
                }

                //3. 初始化动画执行器
                shadowBgAnimator.initAnimator();
                popupContentAnimator.initAnimator();

                //4. 执行动画
                doShowAnimation();

                // call xpopup init.
                postDelayed(afterAnimationStarted, 20);
            }
        });

    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     */
    protected PopupAnimator genAnimatorByPopupType() {
        if (popupInfo==null || popupInfo.popupAnimation == null) return null;
        switch (popupInfo.popupAnimation) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), popupInfo.popupAnimation);
        }
        return null;
    }

    protected abstract int getPopupLayoutId();

    /**
     * 获取PopupAnimator，每种类型的PopupView可以选择返回一个动画器，
     * 父类默认实现是根据popupType字段返回一个默认最佳的动画器
     *
     * @return
     */
    protected PopupAnimator getPopupAnimator() {
        if(popupInfo==null)return null;
        switch (popupInfo.popupType) {
            case Center:
                return new ScaleAlphaAnimator(getPopupContentView(), ScaleAlphaFromCenter);
            case Bottom:
                return new TranslateAnimator(getPopupContentView(), TranslateFromBottom);
            case AttachView:
                return new ScrollScaleAnimator(getPopupContentView(), ScrollAlphaFromLeftTop);
        }
        return null;
    }

    // 执行初始化Popup的content
    protected void initPopupContent() {
        // 限制显示的Rect不包括导航栏
//        if(Utils.isNavBarVisible((Activity) getContext())){
//            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//            params.bottomMargin = Utils.getNavBarHeight();
//            getPopupContentView().setLayoutParams(params);
//        }
    }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    @Override
    public void doShowAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateShow();
        }
        popupContentAnimator.animateShow();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    @Override
    public void doDismissAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateDismiss();
        }
        popupContentAnimator.animateDismiss();
    }

    /**
     * 获取背景View
     *
     * @return
     */
    @Override
    public View getBackgroundView() {
        return getChildAt(0);
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。而且我们对PopupView执行的动画，也是对它执行的动画
     *
     * @return
     */
    @Override
    public View getPopupContentView() {
        return getChildAt(1);
    }

    @Override
    public int getAnimationDuration() {
        return popupContentAnimator.animateDuration;
    }

    /**
     * 消失
     */
    public void dismiss(){
        if(proxy!=null)proxy.dismiss();
    }

    private DismissProxy proxy;
    public void setDismissProxy(DismissProxy proxy){
        this.proxy = proxy;
    }
    public interface DismissProxy{
        void dismiss();
    }
}
