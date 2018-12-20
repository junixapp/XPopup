package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.R;
import com.lxj.xpopup.widget.PopupDrawerLayout;

/**
 * Description: 带Drawer的弹窗
 * Create by dance, at 2018/12/20
 */
public abstract class DrawerPopupView extends BasePopupView {
    PopupDrawerLayout drawerLayout;
    FrameLayout drawerContentContainer;
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
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                DrawerPopupView.super.dismiss();
            }
        });
        drawerLayout.setDrawerPosition(position);
        drawerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
            }
        });
    }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
    }

    @Override
    public void doDismissAnimation() {
        drawerLayout.close();
    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器
     * @return
     */
    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
    }

    PopupDrawerLayout.Position position = PopupDrawerLayout.Position.Left;
    public DrawerPopupView setDrawerPosition(PopupDrawerLayout.Position position){
        this.position = position;
        return this;
    }
}
