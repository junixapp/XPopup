package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Rect;
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
        attachPopupContainer.popupView = this;
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

        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        getPopupImplView().setAlpha(0);
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    @Override
    protected void doMeasure() {
        super.doMeasure();
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), new Runnable() {
                    @Override
                    public void run() {
                        doAttach();
                    }
                });
    }

    private boolean hasInit = false;
    private void initAndStartAnimation(){
        if(hasInit) return;
        hasInit = true;
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        hasInit = false;
    }

    public boolean isShowUp;
    public void doAttach() {
        if (popupInfo.atView == null)
            throw new IllegalArgumentException("atView() must be called before show()！");

        //1. apply width and height
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
        //1. 获取atView在屏幕上的位置
        Rect rect = popupInfo.getAtViewRect();
        int centerY = rect.top + rect.height() / 2;
        View implView = getPopupImplView();
        FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
        if(implParams==null) implParams = new FrameLayout.LayoutParams(-2,-2);
        if ((centerY > getMeasuredHeight() / 2 || popupInfo.popupPosition == PopupPosition.Top) && popupInfo.popupPosition != PopupPosition.Bottom) {
            // 说明atView在Window下半部分，PartShadow应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            implParams.gravity = Gravity.BOTTOM;
            if (getMaxHeight() > 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
        } else {
            // atView在上半部分，PartShadow应该显示在它下方，计算atView之下的高度
            params.height = getMeasuredHeight() - rect.bottom;
            isShowUp = false;
            params.topMargin = rect.bottom;
            implParams.gravity = Gravity.TOP;
            if (getMaxHeight() > 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
        }

        getPopupContentView().setLayoutParams(params);
        implView.setLayoutParams(implParams);
        getPopupContentView().post(new Runnable() {
            @Override
            public void run() {
                initAndStartAnimation();
                getPopupImplView().setAlpha(1);
            }
        });
        attachPopupContainer.notDismissArea = popupInfo.notDismissWhenTouchInArea;
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
}
