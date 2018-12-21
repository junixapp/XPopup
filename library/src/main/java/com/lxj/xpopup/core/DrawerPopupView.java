package com.lxj.xpopup.core;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
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
    View view_statusbar_shadow;
    boolean isAddStatusBarShadow;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int defaultColor = Color.TRANSPARENT;
    int shadowColor = Color.parseColor("#55444444");
    public DrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        view_statusbar_shadow = findViewById(R.id.view_statusbar_shadow);
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

            @Override
            public void onDismissing(float fraction) {
                // 是否显示状态栏的遮罩
                if(isAddStatusBarShadow){
                    view_statusbar_shadow.setBackgroundColor((Integer) argbEvaluator.evaluate(fraction, defaultColor, shadowColor));
                }
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
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
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

    /**
     * 设置Drawer的位置
     * @param position
     * @return
     */
    public DrawerPopupView setDrawerPosition(PopupDrawerLayout.Position position){
        this.position = position;
        return this;
    }

    /**
     * 设置是否给StatusBar添加阴影，如果你的Drawer的背景是白色，建议设置为true，因为状态栏文字的颜色也往往
     * 是白色，会导致状态栏文字看不清；如果Drawer的背景色不是白色，则忽略即可
     * @param hasStatusBarShadow
     * @return
     */
    public DrawerPopupView hasStatusBarShadow(boolean hasStatusBarShadow){
        isAddStatusBarShadow = hasStatusBarShadow;
        return this;
    }
}
