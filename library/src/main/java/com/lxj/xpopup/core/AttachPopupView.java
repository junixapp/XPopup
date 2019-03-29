package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    protected int defaultOffsetY = 6;
    protected int defaultOffsetX = 0;
    protected PartShadowContainer attachPopupContainer;

    public AttachPopupView(@NonNull Context context) {
        super(context);
        defaultOffsetY = XPopupUtils.dp2px(context, defaultOffsetY);
        defaultOffsetX = XPopupUtils.dp2px(context, defaultOffsetX);
        attachPopupContainer = findViewById(R.id.attachPopupContainer);

        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.addView(contentView);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_attach_popup_view;
    }

    protected boolean isShowUp;
    boolean isShowLeft;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (popupInfo.getAtView() == null && popupInfo.touchPoint == null)
            throw new IllegalArgumentException("atView() or touchPoint must not be null for AttachPopupView ！");
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });

    }

    /**
     * 执行倚靠逻辑
     */
    protected void doAttach() {
        // 弹窗显示的位置不能超越Window高度
        float maxY = XPopupUtils.getWindowHeight(getContext());
        float maxX = 0; // 显示在右边时候的最大值

        float translationX = 0, translationY = 0;
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            // 依附于指定点
            maxX = Math.max(popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth(), 0);
            // 尽量优先放在下方，当不够的时候在显示在上方
            isShowUp = (popupInfo.touchPoint.y + getPopupContentView().getMeasuredHeight()) > maxY;
            isShowLeft = popupInfo.touchPoint.x < XPopupUtils.getWindowWidth(getContext()) / 2;

            if (isShowUpToTarget()) {
                // 应显示在point上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationX = (isShowLeft ? popupInfo.touchPoint.x : maxX) + defaultOffsetX;
                translationY = popupInfo.touchPoint.y - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
            } else {
                translationX = (isShowLeft ? popupInfo.touchPoint.x : maxX) + defaultOffsetX;
                translationY = popupInfo.touchPoint.y + defaultOffsetY;
            }
        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            popupInfo.getAtView().getLocationOnScreen(locations);
            Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                    locations[1] + popupInfo.getAtView().getMeasuredHeight());

            maxX = Math.max(rect.right - getPopupContentView().getMeasuredWidth(), 0);
            int centerX = (rect.left + rect.right) / 2;

            // 尽量优先放在下方，当不够的时候在显示在上方
            isShowUp = (rect.bottom + getPopupContentView().getMeasuredHeight()) > maxY; // 不能正好贴着底边
            isShowLeft = centerX < XPopupUtils.getWindowWidth(getContext()) / 2;

            if (isShowUpToTarget()) {
                //说明上面的空间比较大，应显示在atView上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationX = (isShowLeft ? rect.left : maxX) + defaultOffsetX;
                translationY = rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
            } else {
                translationX = (isShowLeft ? rect.left : maxX) + defaultOffsetX;
                translationY = rect.bottom + defaultOffsetY;
            }
        }

        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
    }

    private boolean isShowUpToTarget() {
        return (isShowUp || popupInfo.popupPosition == PopupPosition.Top)
                && popupInfo.popupPosition != PopupPosition.Bottom;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        PopupAnimator animator;
        if (isShowUpToTarget()) {
            // 在上方展示
            if (isShowLeft) {
                animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromLeftBottom);
            } else {
                animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromRightBottom);
            }
        } else {
            // 在下方展示
            if (isShowLeft) {
                animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromLeftTop);
            } else {
                animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromRightTop);
            }
        }
        return animator;
    }

}
