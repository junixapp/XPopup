package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;

/**
 * Description: 宽高撑满的全屏弹窗
 * Create by lxj, at 2019/2/1
 */
public class FullScreenPopupView extends CenterPopupView {
    public FullScreenPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getMaxWidth() {
        return 0;
    }
}
