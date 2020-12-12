package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 水平方向的依附于某个View或者某个点的弹窗，可以轻松实现微信朋友圈点赞的弹窗效果。
 * 支持通过popupPosition()方法手动指定想要出现在目标的左边还是右边，但是对Top和Bottom则不生效。
 * Create by lxj, at 2019/3/13
 */
public class HorizontalAttachPopupView extends AttachPopupView {
    public HorizontalAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        defaultOffsetY = popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX == 0 ? XPopupUtils.dp2px(getContext(), 4) : popupInfo.offsetX;
    }

    /**
     * 执行附着逻辑
     */
    public void doAttach() {
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        float translationX = 0, translationY = 0;
        int w = getPopupContentView().getMeasuredWidth();
        int h = getPopupContentView().getMeasuredHeight();
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if(XPopup.longClickPoint!=null) popupInfo.touchPoint = XPopup.longClickPoint;
            // 依附于指定点
            isShowLeft = popupInfo.touchPoint.x > XPopupUtils.getWindowWidth(getContext()) / 2;

            // translationX: 在左边就和点左边对齐，在右边就和其右边对齐
            if(isRTL){
                translationX = isShowLeft ?  -(XPopupUtils.getWindowWidth(getContext())-popupInfo.touchPoint.x+defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext())-popupInfo.touchPoint.x-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
            }else {
                translationX = isShowLeftToTarget() ? (popupInfo.touchPoint.x - w - defaultOffsetX) : (popupInfo.touchPoint.x + defaultOffsetX);
            }
            translationY = popupInfo.touchPoint.y - h * .5f + defaultOffsetY;
        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            popupInfo.getAtView().getLocationOnScreen(locations);
            Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                    locations[1] + popupInfo.getAtView().getMeasuredHeight());

            int centerX = (rect.left + rect.right) / 2;

            isShowLeft = centerX > XPopupUtils.getWindowWidth(getContext()) / 2;
            if(isRTL){
                translationX = isShowLeft ?  -(XPopupUtils.getWindowWidth(getContext())-rect.left + defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext())-rect.right-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
            }else {
                translationX = isShowLeftToTarget() ? (rect.left - w - defaultOffsetX) : (rect.right + defaultOffsetX);
            }
            translationY = rect.top + (rect.height()-h)/2 + defaultOffsetY;
        }
        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
        initAndStartAnimation();
    }

    private boolean isShowLeftToTarget() {
        return (isShowLeft || popupInfo.popupPosition == PopupPosition.Left)
                && popupInfo.popupPosition != PopupPosition.Right;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        ScrollScaleAnimator animator;
        if (isShowLeftToTarget()) {
            animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromRight);
        } else {
            animator = new ScrollScaleAnimator(getPopupContentView(), PopupAnimation.ScrollAlphaFromLeft);
        }
        animator.isOnlyScaleX = true;
        return animator;
    }
}
