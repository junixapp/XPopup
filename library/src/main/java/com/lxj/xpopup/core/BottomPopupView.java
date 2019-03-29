package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    protected SmartDragLayout bottomPopupContainer;
    boolean enableDrag = true; //是否启用手势交互，默认启用
    public BottomPopupView(@NonNull Context context) {
        super(context);
        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), bottomPopupContainer, false);
        bottomPopupContainer.addView(contentView);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_bottom_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        bottomPopupContainer.enableDrag(enableDrag);
        bottomPopupContainer.dismissOnTouchOutside(popupInfo.isDismissOnTouchOutside);
        bottomPopupContainer.hasShadowBg(popupInfo.hasShadowBg);
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight());

        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                doAfterDismiss();
            }
            @Override
            public void onOpen() {
                BottomPopupView.super.doAfterShow();
            }
        });

        bottomPopupContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void doAfterShow() {
        if(enableDrag){
            //do nothing self.
        }else {
            super.doAfterShow();
        }
    }

    @Override
    public void doShowAnimation() {
        if (enableDrag) {
            bottomPopupContainer.open();
        } else {
            super.doShowAnimation();
        }
    }

    @Override
    public void doDismissAnimation() {
        if (enableDrag) {
            bottomPopupContainer.close();
        } else {
            super.doDismissAnimation();
        }
    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
     *
     * @return
     */
    @Override
    public int getAnimationDuration() {
        return enableDrag ? 0 : super.getAnimationDuration();
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        // 移除默认的动画器
        return enableDrag ? null : super.getPopupAnimator();
    }

    @Override
    public void dismiss() {
        if (enableDrag) {
            if (popupStatus == PopupStatus.Dismissing) return;
            popupStatus = PopupStatus.Dismissing;
            // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
            bottomPopupContainer.close();
        } else {
            super.dismiss();
        }
    }

    /**
     * 具体实现的类的布局
     *
     * @return
     */
    protected int getImplLayoutId() {
        return 0;
    }

    protected int getMaxWidth() {
        return popupInfo.maxWidth == 0 ? XPopupUtils.getWindowWidth(getContext())
                : popupInfo.maxWidth;
    }

    public BottomPopupView enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
        return this;
    }
}
