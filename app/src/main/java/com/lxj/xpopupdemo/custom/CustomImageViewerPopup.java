package com.lxj.xpopupdemo.custom;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2019/5/8
 */
public class CustomImageViewerPopup extends ImageViewerPopupView {
    public CustomImageViewerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_image_viewer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
//        tv_pager_indicator.setVisibility(GONE);
    }
}
