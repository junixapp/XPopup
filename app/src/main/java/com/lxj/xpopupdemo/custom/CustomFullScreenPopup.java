package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description: 自定义全屏弹窗
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

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag", "CustomFullScreenPopup onShow");
    }
    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag", "CustomFullScreenPopup onDismiss");
    }
}
