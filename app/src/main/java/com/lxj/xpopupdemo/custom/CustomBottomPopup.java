package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2019/2/27
 */
public class CustomBottomPopup extends BottomPopupView {
    public CustomBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bottom_popup2;
    }
}
