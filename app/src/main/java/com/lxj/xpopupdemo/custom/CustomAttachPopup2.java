package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description: 自定义背景的Attach弹窗
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
