package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.HorizontalAttachPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup2 extends AttachPopupView {
    public CustomAttachPopup2(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

    }
}
