package com.lxj.xpopup.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.widget.PopupDrawerLayout;

/**
 * Description: 带Drawer的弹窗
 * Create by dance, at 2018/12/20
 */
public abstract class DrawerPopupView extends BasePopupView {
    PopupDrawerLayout drawerLayout;
    protected FrameLayout drawerContentContainer;

    public DrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerContentContainer = findViewById(R.id.drawerContentContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), drawerContentContainer, false);
        drawerContentContainer.addView(contentView);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_drawer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        drawerLayout.enableShadow = popupInfo.hasShadowBg;
        drawerLayout.isCanClose = popupInfo.isDismissOnTouchOutside;
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                doAfterDismiss();
            }
            @Override
            public void onOpen() {
                DrawerPopupView.super.doAfterShow();
            }
            @Override
            public void onDismissing(float fraction) {
                drawerLayout.isDrawStatusBarShadow = popupInfo.hasStatusBarShadow;
            }
        });
        getPopupImplView().setTranslationX(popupInfo.offsetX);
        getPopupImplView().setTranslationY(popupInfo.offsetY);
        drawerLayout.setDrawerPosition(popupInfo.popupPosition == null ? PopupPosition.Left : popupInfo.popupPosition);
        drawerLayout.enableDrag = popupInfo.enableDrag;
        drawerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void doAfterShow() { }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
    }

    @Override
    public void doDismissAnimation() {
    }

    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        clearFocus();
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
        super.dismiss();
    }
    @Override
    protected PopupAnimator getPopupAnimator() {
        return null;
    }
    @Override
    protected View getTargetSizeView() {
        return getPopupImplView();
    }
}
