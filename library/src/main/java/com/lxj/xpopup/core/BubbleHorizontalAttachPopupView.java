package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BubbleLayout;

/**
 * Description: 水平方向带气泡的Attach弹窗
 */
public class BubbleHorizontalAttachPopupView extends BubbleAttachPopupView {
    public BubbleHorizontalAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initPopupContent() {
        bubbleContainer.setLook(BubbleLayout.Look.LEFT); //解决高度不正确的问题
        super.initPopupContent();
        defaultOffsetY = popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX == 0 ? XPopupUtils.dp2px(getContext(), 2) : popupInfo.offsetX;
    }

    /**
     * 执行附着逻辑
     */
    float translationX = 0, translationY = 0;
    public void doAttach() {
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if(XPopup.longClickPoint!=null) popupInfo.touchPoint = XPopup.longClickPoint;
            popupInfo.touchPoint.x -= getActivityContentLeft();
            isShowLeft = popupInfo.touchPoint.x > XPopupUtils.getAppWidth(getContext()) / 2f;
            ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
            int maxWidth = 0;
            if(isRTL){
                maxWidth = (int) (isShowLeft ? (popupInfo.touchPoint.x - overflow) : (XPopupUtils.getAppWidth(getContext()) - popupInfo.touchPoint.x - overflow));
            }else {
                maxWidth = (int) (isShowLeft ? (popupInfo.touchPoint.x - overflow) : (XPopupUtils.getAppWidth(getContext()) - popupInfo.touchPoint.x - overflow));
            }
            if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                params.width = Math.max(maxWidth, getPopupWidth());
            }
            getPopupContentView().setLayoutParams(params);
            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if(popupInfo==null) return;
                    // translationX: 在左边就和点左边对齐，在右边就和其右边对齐
                    if(isRTL){
                        translationX = isShowLeft ?  -(XPopupUtils.getAppWidth(getContext())-popupInfo.touchPoint.x+defaultOffsetX)
                                : -(XPopupUtils.getAppWidth(getContext())-popupInfo.touchPoint.x-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
                    }else {
                        translationX = isShowLeftToTarget() ? (popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth() - defaultOffsetX) : (popupInfo.touchPoint.x + defaultOffsetX);
                    }
                    translationY = popupInfo.touchPoint.y - getPopupContentView().getMeasuredHeight() * .5f + defaultOffsetY;
                    doBubble();
                }
            });
        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            Rect rect = popupInfo.getAtViewRect();
            rect.left -= getActivityContentLeft();
            rect.right -= getActivityContentLeft();

            int centerX = (rect.left + rect.right) / 2;
            isShowLeft = centerX > XPopupUtils.getAppWidth(getContext()) / 2;
            ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
            int maxWidth = 0;
            if(isRTL){
                maxWidth = isShowLeft ? (rect.left - overflow) : (XPopupUtils.getAppWidth(getContext()) - rect.right - overflow);
            }else {
                maxWidth = isShowLeft ? (rect.left - overflow) : (XPopupUtils.getAppWidth(getContext()) - rect.right - overflow);
            }
            if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                params.width = Math.max(maxWidth, getPopupWidth());
            }
            getPopupContentView().setLayoutParams(params);
            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if(isRTL){
                        translationX = isShowLeft ?  -(XPopupUtils.getAppWidth(getContext())-rect.left + defaultOffsetX)
                                : -(XPopupUtils.getAppWidth(getContext())-rect.right-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
                    }else {
                        translationX = isShowLeftToTarget() ? (rect.left - getPopupContentView().getMeasuredWidth() - defaultOffsetX) : (rect.right + defaultOffsetX);
                    }
                    translationY = rect.top + (rect.height()-getPopupContentView().getMeasuredHeight() )/2f + defaultOffsetY;
                    doBubble();
                }
            });
        }
    }

    private void doBubble(){
        //设置气泡相关
        if(isShowLeftToTarget()){
            bubbleContainer.setLook(BubbleLayout.Look.RIGHT);
        }else {
            bubbleContainer.setLook(BubbleLayout.Look.LEFT);
        }
        if(defaultOffsetY==0){
            bubbleContainer.setLookPositionCenter(true);
        }else {
            bubbleContainer.setLookPosition(Math.max(0, (int) (bubbleContainer.getMeasuredHeight()/2f- defaultOffsetY- bubbleContainer.mLookLength/2)));
        }
        bubbleContainer.invalidate();

        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
        initAndStartAnimation();
    }

    private boolean isShowLeftToTarget() {
        return (isShowLeft || popupInfo.popupPosition == PopupPosition.Left)
                && popupInfo.popupPosition != PopupPosition.Right;
    }
}
