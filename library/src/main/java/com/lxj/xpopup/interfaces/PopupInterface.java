package com.lxj.xpopup.interfaces;

import android.view.View;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public interface PopupInterface {
    View getPopupContentView();
    int getAnimationDuration();
    void doShowAnimation();
    void doDismissAnimation();
    void init(final Runnable afterAnimationStarted);
}
