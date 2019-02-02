package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2019/1/12
 */
public class CustomBasePopupView extends BasePopupView {
    public CustomBasePopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.custom_base_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
    }
}
