package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * Description: 局部阴影的弹窗，类似于淘宝商品列表的下拉筛选弹窗
 * Create by dance, at 2018/12/21
 */
public abstract class PartShadowPopupView extends AttachPopupView {
    public PartShadowPopupView(@NonNull Context context) {
        super(context);
        defaultOffsetY = 0;
    }

    @Override
    protected void doAttach() {
        if (popupInfo.getAtView() == null)
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");

        // 指定阴影动画的目标View
        shadowBgAnimator.targetView = getPopupContentView();

        //1. apply width and height
        ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
        params.width = getMeasuredWidth(); // 满宽

        //1. 获取atView在屏幕上的位置
        int[] locations = new int[2];
        popupInfo.getAtView().getLocationOnScreen(locations);
        Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                locations[1] + popupInfo.getAtView().getMeasuredHeight());
        int centerY = rect.top + rect.height()/2;
        if(centerY > getMeasuredHeight()/2){
            // 说明atView在Window下半部分，PartShadow应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            getPopupContentView().setTranslationY(-defaultOffsetY);

            // 同时自定义的impl View应该Gravity居于底部
            View implView = ((ViewGroup)getPopupContentView()).getChildAt(0);
            FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
            implParams.gravity = Gravity.BOTTOM;
            implView.setLayoutParams(implParams);

        } else {
            // atView在上半部分，PartShadow应该显示在它下方，计算atView之下的高度
            params.height = getMeasuredHeight() - rect.bottom;
            isShowUp = false;
            getPopupContentView().setTranslationY(rect.bottom + defaultOffsetY);

            // 同时自定义的impl View应该Gravity居于顶部
            View implView = ((ViewGroup)getPopupContentView()).getChildAt(0);
            FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
            implParams.gravity = Gravity.TOP;
            implView.setLayoutParams(implParams);
        }
        getPopupContentView().setLayoutParams(params);

        attachPopupContainer.setCardBackgroundColor(Color.TRANSPARENT);
        attachPopupContainer.setCardElevation(0);
        attachPopupContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        View implView = ((ViewGroup)getPopupContentView()).getChildAt(0);
        return new TranslateAnimator(implView, isShowUp ?
                PopupAnimation.TranslateFromBottom: PopupAnimation.TranslateFromTop);
    }
}
