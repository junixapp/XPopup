package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 依附于某个View的弹窗
 * Create by dance, at 2018/12/11
 */
public abstract class AttachPopupView extends BasePopupView {
    protected int defaultOffsetY = 6;
    protected int defaultOffsetX = 0;
    protected CardView attachPopupContainer;

    public AttachPopupView(@NonNull Context context) {
        super(context);
        defaultOffsetY = Utils.dp2px(context, defaultOffsetY);
        defaultOffsetX = Utils.dp2px(context, defaultOffsetX);
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
            throw new IllegalArgumentException("atView() or touchPoint must not be null for AttachView type！");

        post(new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    /**
     * 执行附着逻辑
     */
    protected void doAttach(){
        // 弹窗显示的位置不能超越Window高度
        float maxY = Utils.getWindowHeight(getContext()) ;
        float maxX = 0; // 显示在右边时候的最大值

        float translationX = 0, translationY = 0;
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            // 依附于指定点
            maxX = Math.max(popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth(), 0);
            // 尽量优先放在下方，当不够的时候在显示在上方
            isShowUp = (popupInfo.touchPoint.y + getPopupContentView().getMeasuredHeight()) > maxY ;
            isShowLeft = popupInfo.touchPoint.x < Utils.getWindowWidth(getContext()) / 2;

            if (isShowUp) {
                // 应显示在point上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationX = (isShowLeft ?popupInfo.touchPoint.x : maxX) + defaultOffsetX;
                translationY = popupInfo.touchPoint.y - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
            }else {
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
            isShowUp = (rect.bottom + getPopupContentView().getMeasuredHeight()) > maxY ; // 不能正好贴着底边
            isShowLeft = centerX < Utils.getWindowWidth(getContext()) / 2;

            if (isShowUp) {
                //说明上面的空间比较大，应显示在atView上方
                // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                translationX = (isShowLeft ? rect.left : maxX) + defaultOffsetX;
                translationY = rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
            }else {
                translationX = (isShowLeft ? rect.left : maxX) + defaultOffsetX;
                translationY = rect.bottom + defaultOffsetY;
            }
        }

        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        PopupAnimator animator;
        if (isShowUp) {
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
