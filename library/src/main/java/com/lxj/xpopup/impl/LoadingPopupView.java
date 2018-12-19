package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopupConfig;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.Utils;
import com.lxj.xpopup.widget.LoadingView;

/**
 * Description: 确定和取消的对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {
    LoadingView loadingView;
    public LoadingPopupView(@NonNull Context context) {
        super(context);
    }

    public LoadingPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_loading;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        loadingView = findViewById(R.id.loadingView);
        // 去除背景
        centerPopupContainer.setBackgroundResource(0);

        applyPrimaryColor();

    }

    protected void applyPrimaryColor(){
    }


}
