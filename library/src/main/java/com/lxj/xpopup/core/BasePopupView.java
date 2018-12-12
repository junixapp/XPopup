package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.animator.TranslateAlphaAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
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
        this(context, null);

        // 1.添加背景View，用来拦截所有内容之外的点击
        ClickConsumeView bgView = new ClickConsumeView(context);
        addView(bgView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        shadowBgAnimator = new ShadowBgAnimator(bgView, getAnimationDuration());

        // 2. 添加Popup窗体内容View
        View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) contentView.getLayoutParams();
//        params.topMargin = Utils.getStatusBarHeight();
//        params.bottomMargin = Utils.getNavBarHeight();
//        contentView.setLayoutParams(params);
        addView(contentView);
    }

    public BasePopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
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
        getPopupContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getPopupContentView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //1. 初始化Popup
                initPopupContent();

                //2. 收集动画执行器
                // 如果是想使用自定义的动画，则需要实现 getPopupAnimator()
                if (useCustomAnimator()) {
                    popupContentAnimator = getPopupAnimator();
                } else {
                    // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                    popupContentAnimator = genPopupContentAnimator();
                    if (popupContentAnimator == null) {
                        // 使用默认的animator
                        popupContentAnimator = getPopupAnimator();
                    }
                }

                //3. 初始化动画执行器
                shadowBgAnimator.initAnimator();
                popupContentAnimator.initAnimator();

                //4. 执行动画
                doShowAnimation();
                postDelayed(afterAnimationStarted, getAnimationDuration()+20);
            }
        });
    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的动画执行器
     */
    protected PopupAnimator genPopupContentAnimator() {
        if (popupInfo.popupAnimation == null) return null;
        switch (popupInfo.popupAnimation) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

        }
        return null;
    }

    protected abstract int getPopupLayoutId();

    /**
     * 是否使用自定义的动画器，如果返回true，则必须实现getPopupAnimator()
     *
     * @return
     */
    protected boolean useCustomAnimator() {
        return false;
    }

    /**
     * 获取自己的PopupAnimator，每种类型的PopupView可以选择返回一个动画器，
     * 也可以配合 useCustomAnimator()，来实现自定义动画器。
     * 父类默认实现是根据popupType字段返回一个默认合适的动画器
     *
     * @return
     */
    protected PopupAnimator getPopupAnimator() {
        switch (popupInfo.popupType) {
            case Center:
                return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), ScaleAlphaFromCenter);
            case Bottom:
                return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), TranslateFromBottom);
            case AttachView:
                return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), ScrollAlphaFromLeftTop);
        }
        return null;
    }

    // 执行初始化Popup的content
    protected void initPopupContent() {}

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
     * 整个PopupView容器，由背景View和内容View组成
     *
     * @return
     */
    @Override
    public View getPopupView() {
        return this;
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
        return 400;
    }
}
