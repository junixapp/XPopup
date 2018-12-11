package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.lxj.xpopup.R;
import com.lxj.xpopup.util.Utils;

/**
 * Description:
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    public BottomPopupView(@NonNull Context context) {
        super(context);
    }

    public BottomPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.xpopup_bottom_popup_view;
    }

    @Override
    protected void applyWidthAndHeight() {
        post(new Runnable() {
            @Override
            public void run() {
                int maxHeight = (int) (Utils.getWindowHeight(getContext()) * 0.85f);
                Utils.limitWidthAndHeight(getPopupContentView(), getPopupContentView().getMeasuredWidth(), maxHeight);
            }
        });
    }

    @Override
    protected void initPopup() {
        super.initPopup();
    }
}
