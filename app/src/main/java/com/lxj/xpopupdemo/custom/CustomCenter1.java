package com.lxj.xpopupdemo.custom;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopupdemo.R;

public class CustomCenter1 extends CenterPopupView {
    public CustomCenter1(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.temp2;
    }

    @Override
    protected int getMaxWidth() {
        return 0;
    }
}
