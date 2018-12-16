package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
 * Description:
 * Create by dance, at 2018/12/16
 */
public class ConfirmPopupView extends CenterPopupView {
    FrameLayout centerPopupContainer;
    public ConfirmPopupView(@NonNull Context context) {
        super(context);

        centerPopupContainer = findViewById(R.id.centerPopupContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), centerPopupContainer, false);
        centerPopupContainer.addView(contentView);
    }

    public ConfirmPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConfirmPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_confirm;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

    }
}
