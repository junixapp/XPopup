package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 确定和取消的对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {

    public LoadingPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_loading;
    }

    @Override
    protected void initPopupContent() {
        // 去除背景
//        centerPopupContainer.setBackgroundResource(0);
    }

    @Override
    protected int getMaxWidth() {
        return Utils.dp2px(getContext(), 85);
    }
}
