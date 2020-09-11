package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.PartShadowContainer;

/**
 * Description: 依附于某个View的弹窗，弹窗会出现在目标的上方或下方，如果你想要出现在目标的左边或者右边，请使用HorizontalAttachPopupView。
 * 支持通过popupPosition()方法手动指定想要出现在目标的上边还是下边，但是对Left和Right则不生效。
 * Create by dance, at 2018/12/11
 */
public abstract class AttachPopupView extends BasePopupView {
    protected int defaultOffsetY = 0;
    protected int defaultOffsetX = 0;
    protected PartShadowContainer attachPopupContainer;

    public AttachPopupView(@NonNull Context context) {
        super(context);
        attachPopupContainer = findViewById(R.id.attachPopupContainer);
    }

    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.addView(contentView);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_attach_popup_view;
    }

    public boolean isShowUp;
    boolean isShowLeft;
    protected int bgDrawableMargin = 6;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (attachPopupContainer.getChildCount() == 0) addInnerContent();
        if (popupInfo.getAtView() == null && popupInfo.touchPoint == null)
            throw new IllegalArgumentException("atView() or touchPoint must not be null for AttachPopupView ！");

        defaultOffsetY = popupInfo.offsetY == 0 ? XPopupUtils.dp2px(getContext(), 4) : popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX;

        attachPopupContainer.setTranslationX(popupInfo.offsetX);
        attachPopupContainer.setTranslationY(popupInfo.offsetY);
        applyBg();
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    protected void applyBg() {
        if (!popupInfo.hasShadowBg && !isCreated) {
            //实现shadow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //优先使用implView的背景
                if (getPopupImplView().getBackground() != null) {
                    //复制一份
                    Drawable.ConstantState constantState = getPopupImplView().getBackground().getConstantState();
                    if (constantState != null) {
                        Drawable newDrawable = constantState.newDrawable();
                        attachPopupContainer.setBackground(newDrawable);
                        getPopupImplView().setBackground(null);
                    }
                } else {
                    attachPopupContainer.setBackgroundColor(Color.WHITE);
                }
                attachPopupContainer.setElevation(XPopupUtils.dp2px(getContext(), 20));
            } else {
                //优先使用implView的背景
                if (getPopupImplView().getBackground() == null) {
                    defaultOffsetX -= bgDrawableMargin;
                    defaultOffsetY -= bgDrawableMargin;
                    attachPopupContainer.setBackgroundResource(R.drawable._xpopup_shadow);
                } else {
                    Drawable.ConstantState constantState = getPopupImplView().getBackground().getConstantState();
                    if (constantState != null) {
                        Drawable newDrawable = constantState.newDrawable();
                        attachPopupContainer.setBackground(newDrawable);
                        getPopupImplView().setBackground(null);
                    }
                }
            }
        }
    }

    /**
     * 执行倚靠逻辑
     */
    float translationX = 0, translationY = 0;
    // 弹窗显示的位置不能超越Window高度
    float maxY = XPopupUtils.getWindowHeight(getContext());
    int overflow = 10;

    public void doAttach() {
        overflow = XPopupUtils.dp2px(getContext(), overflow);
        final boolean isRTL = XPopupUtils.isLayoutRtl(this);
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            // 依附于指定点,尽量优先放在下方，当不够的时候在显示在上方
            //假设下方放不下，超出window高度
            boolean isTallerThanWindowHeight = (popupInfo.touchPoint.y + getPopupContentView().getMeasuredHeight()) > maxY;
            if (isTallerThanWindowHeight) {
                isShowUp = popupInfo.touchPoint.y > XPopupUtils.getWindowHeight(getContext()) / 2;
            } else {
                isShowUp = false;
            }
            isShowLeft = popupInfo.touchPoint.x < XPopupUtils.getWindowWidth(getContext()) / 2;

            //限制最大宽高
            ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
            int maxHeight = (int) (isShowUpToTarget() ? (popupInfo.touchPoint.y - XPopupUtils.getStatusBarHeight() - overflow)
                    : (XPopupUtils.getWindowHeight(getContext()) - popupInfo.touchPoint.y - overflow));
            int maxWidth = (int) (isShowLeft ? (XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.x - overflow) : (popupInfo.touchPoint.x - overflow));
            if (getPopupContentView().getMeasuredHeight() > maxHeight) {
                params.height = maxHeight;
            }
            if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                params.width = maxWidth;
            }
            getPopupContentView().setLayoutParams(params);

            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if (isRTL) {
                        translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth() - defaultOffsetX)
                                : -(XPopupUtils.getWindowWidth(getContext()) - popupInfo.touchPoint.x + defaultOffsetX);
                    } else {
                        translationX = isShowLeft ? (popupInfo.touchPoint.x + defaultOffsetX) : (popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth() - defaultOffsetX);
                    }
                    if (popupInfo.isCenterHorizontal) {
                        //水平居中
                        if (isShowLeft) {
                            if (isRTL) {
                                translationX += getPopupContentView().getMeasuredWidth() / 2f;
                            } else {
                                translationX -= getPopupContentView().getMeasuredWidth() / 2f;
                            }
                        } else {
                            if (isRTL) {
                                translationX -= getPopupContentView().getMeasuredWidth() / 2f;
                            } else {
                                translationX += getPopupContentView().getMeasuredWidth() / 2f;
                            }
                        }
                    }
                    if (isShowUpToTarget()) {
                        // 应显示在point上方
                        // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                        translationY = popupInfo.touchPoint.y - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
                    } else {
                        translationY = popupInfo.touchPoint.y + defaultOffsetY;
                    }
                    getPopupContentView().setTranslationX(translationX);
                    getPopupContentView().setTranslationY(translationY);
                }
            });

        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            popupInfo.getAtView().getLocationOnScreen(locations);
            final Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                    locations[1] + popupInfo.getAtView().getMeasuredHeight());
            final int centerX = (rect.left + rect.right) / 2;

            // 尽量优先放在下方，当不够的时候在显示在上方
            //假设下方放不下，超出window高度
            boolean isTallerThanWindowHeight = (rect.bottom + getPopupContentView().getMeasuredHeight()) > maxY;
            if (isTallerThanWindowHeight) {
                int centerY = (rect.top + rect.bottom) / 2;
                isShowUp = centerY > XPopupUtils.getWindowHeight(getContext()) / 2;
            } else {
                isShowUp = false;
            }
            isShowLeft = centerX < XPopupUtils.getWindowWidth(getContext()) / 2;

            //修正高度，弹窗的高有可能超出window区域
            if (!isCreated) {
                ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
                int maxHeight = isShowUpToTarget() ? (rect.top - XPopupUtils.getStatusBarHeight() - overflow)
                        : (XPopupUtils.getWindowHeight(getContext()) - rect.bottom - overflow);
                int maxWidth = isShowLeft ? (XPopupUtils.getWindowWidth(getContext()) - rect.left - overflow) : (rect.right - overflow);
                if (getPopupContentView().getMeasuredHeight() > maxHeight) {
                    params.height = maxHeight;
                }
                if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                    params.width = maxWidth;
                }
                getPopupContentView().setLayoutParams(params);
            }

            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if (isRTL) {
                        translationX = isShowLeft ? -(XPopupUtils.getWindowWidth(getContext()) - rect.left - getPopupContentView().getMeasuredWidth() - defaultOffsetX)
                                : -(XPopupUtils.getWindowWidth(getContext()) - rect.right + defaultOffsetX);
                    } else {
                        translationX = isShowLeft ? (rect.left + defaultOffsetX) : (rect.right - getPopupContentView().getMeasuredWidth() - defaultOffsetX);
                    }
                    if (popupInfo.isCenterHorizontal) {
                        //水平居中
                        if (isShowLeft)
                            if (isRTL) {
                                translationX -= (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            } else {
                                translationX += (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            }
                        else {
                            if (isRTL) {
                                translationX += (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            } else {
                                translationX -= (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            }
                        }
                    }
                    if (isShowUpToTarget()) {
                        //说明上面的空间比较大，应显示在atView上方
                        // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                        translationY = rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
                    } else {
                        translationY = rect.bottom + defaultOffsetY;
                    }
                    getPopupContentView().setTranslationX(translationX);
                    getPopupContentView().setTranslationY(translationY);
                }
            });

        }
    }

    protected boolean isShowUpToTarget() {
        return (isShowUp || popupInfo.popupPosition == PopupPosition.Top)
                && popupInfo.popupPosition != PopupPosition.Bottom;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        PopupAnimator animator;
        if (isShowUpToTarget()) {
            // 在上方展示
            animator = new ScrollScaleAnimator(getPopupContentView(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftBottom
                    : PopupAnimation.ScrollAlphaFromRightBottom);
        } else {
            // 在下方展示
            animator = new ScrollScaleAnimator(getPopupContentView(), isShowLeft ? PopupAnimation.ScrollAlphaFromLeftTop
                    : PopupAnimation.ScrollAlphaFromRightTop);
        }
        return animator;
    }
}
