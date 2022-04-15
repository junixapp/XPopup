package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnClickOutsideListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.PartShadowContainer;

/**
 * Description: 局部阴影的弹窗，类似于淘宝商品列表的下拉筛选弹窗
 * Create by dance, at 2018/12/21
 */
public abstract class PartShadowPopupView extends BasePopupView {
    protected PartShadowContainer attachPopupContainer;
    public PartShadowPopupView(@NonNull Context context) {
        super(context);
        attachPopupContainer = findViewById(R.id.attachPopupContainer);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_partshadow_popup_view;
    }
    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.addView(contentView);
    }

    @Override
    protected void initPopupContent() {
        if (attachPopupContainer.getChildCount() == 0) addInnerContent();
        // 指定阴影动画的目标View
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.targetView = getPopupContentView();
        }
        getPopupContentView().setTranslationY(popupInfo.offsetY);
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(0f);
        getPopupImplView().setVisibility(INVISIBLE);
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    private void initAndStartAnimation(){
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    public boolean isShowUp;
    public void doAttach() {
        if (popupInfo.atView == null)
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");

        //1. apply width and height
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
        params.width = getMeasuredWidth();


        //1. 获取atView在屏幕上的位置
        Rect rect = popupInfo.getAtViewRect();
        rect.left -= getActivityContentLeft();
        rect.right -= getActivityContentLeft();

        //水平居中
        if (popupInfo.isCenterHorizontal && getPopupImplView() != null) {
//            getPopupImplView().setTranslationX(XPopupUtils.getAppWidth(getContext()) / 2f - getPopupContentView().getMeasuredWidth() / 2f);
            //参考目标View居中，而不是屏幕居中
            int tx = (rect.left + rect.right)/2 - getPopupImplView().getMeasuredWidth()/2;
            getPopupImplView().setTranslationX(tx);
        }else {
            int tx = rect.left + popupInfo.offsetX;
            int realWidth = getActivityContentView().getMeasuredWidth();
            if(tx + getPopupImplView().getMeasuredWidth() > realWidth){
                tx -= (tx + getPopupImplView().getMeasuredWidth() - realWidth);
            }
            getPopupImplView().setTranslationX(tx);
        }

        int centerY = rect.top + rect.height() / 2;
        View implView = getPopupImplView();
        FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
        if ((centerY > getMeasuredHeight() / 2 || popupInfo.popupPosition == PopupPosition.Top) && popupInfo.popupPosition != PopupPosition.Bottom) {
            // 说明atView在Window下半部分，PartShadow应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            implParams.gravity = Gravity.BOTTOM;
            if (getMaxHeight() != 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
        } else {
            // atView在上半部分，PartShadow应该显示在它下方，计算atView之下的高度
            params.height = getMeasuredHeight() - rect.bottom;
            isShowUp = false;
            params.topMargin = rect.bottom;
            implParams.gravity = Gravity.TOP;
            if (getMaxHeight() != 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
        }
        getPopupContentView().setLayoutParams(params);
        implView.setLayoutParams(implParams);
        getPopupContentView().post(new Runnable() {
            @Override
            public void run() {
                initAndStartAnimation();
                getPopupImplView().setVisibility(VISIBLE);
            }
        });
        attachPopupContainer.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (popupInfo.isDismissOnTouchOutside) dismiss();
                return false;
            }
        });
        attachPopupContainer.setOnClickOutsideListener(new OnClickOutsideListener() {
            @Override
            public void onClickOutside() {
                if (popupInfo.isDismissOnTouchOutside) dismiss();
            }
        });
    }
    @Override
    protected PopupAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupImplView(), getAnimationDuration(), isShowUp ?
                PopupAnimation.TranslateFromBottom : PopupAnimation.TranslateFromTop);
    }

    @Override
    protected int getMaxWidth() {
        return XPopupUtils.getAppWidth(getContext());
    }
}
