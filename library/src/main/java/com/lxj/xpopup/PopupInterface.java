package com.lxj.xpopup;

import android.view.View;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public interface PopupInterface {
    View getPopupView();
    View getPopupContentView();
    View getBackgroundView();
    int getAnimationDuration();
    void doShowAnimation();
    void doDismissAnimation();
}
