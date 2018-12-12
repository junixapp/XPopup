package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.util.Utils;

/**
 * Description:
 * Create by dance, at 2018/12/11
 */
public class AttachPopupView extends BasePopupView {
    int defaultOffsetY = 6;

    public AttachPopupView(@NonNull Context context) {
        super(context);
        defaultOffsetY = Utils.dp2px(context, defaultOffsetY);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.xpopup_attach_popup_view;
    }

    Rect rect;
    boolean isShowUp;

    @Override
    protected void initPopupContent() {
        if (popupInfo.getAtView() == null)
            throw new IllegalArgumentException("atView must not be null for AttachView type！");

        //1. 获取atView在屏幕上的位置
        int[] locations = new int[2];
        popupInfo.getAtView().getLocationOnScreen(locations);

        rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                locations[1] + popupInfo.getAtView().getMeasuredHeight());
        // 弹窗显示的位置不能超越状态栏和导航栏，隐藏需要减去2个高度
        int minY = Utils.getStatusBarHeight();
        int maxY = Utils.getWindowHeight(getContext()) - Utils.getNavBarHeight();
        int maxX = rect.right - getPopupContentView().getMeasuredWidth();

        isShowUp = (rect.top - minY) > (maxY - rect.bottom);
//        Log.e("tag", "rect: " + rect.toString() + " minY: " + minY + " maxY: " + maxY);
        if (isShowUp) {
            //说明上面的空间比较大，应显示在atView上方
            getPopupContentView().setTranslationX(Math.min(rect.left, maxX));
            getPopupContentView().setTranslationY(rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY);

        } else {
            // 应该显示在atView下方
            getPopupContentView().setTranslationX(Math.min(rect.left, maxX));
            getPopupContentView().setTranslationY(rect.bottom + defaultOffsetY);

        }
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        PopupAnimator animator;
        int centerX = (rect.left + rect.right) / 2;
        if (isShowUp) {
            // 在上方展示
            if (centerX < Utils.getWindowWidth(getContext()) / 2) {
                animator = new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.ScrollAlphaFromLeftBottom);
            } else {
                animator = new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.ScrollAlphaFromRightBottom);
            }
        } else {
            // 在下方展示
            if (centerX < Utils.getWindowWidth(getContext()) / 2) {
                animator = new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.ScrollAlphaFromLeftTop);
            } else {
                animator = new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), PopupAnimation.ScrollAlphaFromRightTop);
            }
        }
        return animator;
    }
}
