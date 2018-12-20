package com.lxj.xpopup.core;

import android.view.View;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public interface PopupInterface {
    View getPopupContentView();
    View getBackgroundView();
    int getAnimationDuration();
    void doShowAnimation();
    void doDismissAnimation();
    void init(final Runnable afterAnimationStarted);
}
