package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by lxj, at 2019/3/12
 */
public class CustomFullScreenPopup extends FullScreenPopupView {
    public CustomFullScreenPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_fullscreen_popup;
    }
}
