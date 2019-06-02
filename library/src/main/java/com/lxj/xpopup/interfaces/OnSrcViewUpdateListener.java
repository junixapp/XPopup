package com.lxj.xpopup.interfaces;

import android.support.annotation.NonNull;

import com.lxj.xpopup.core.ImageViewerPopupView;

/**
 * Description:
 * Create by dance, at 2019/1/29
 */
public interface OnSrcViewUpdateListener {
    void onSrcViewUpdate(@NonNull ImageViewerPopupView popupView, int position);
}
