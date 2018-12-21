package com.lxj.xpopup.interfaces;

/**
 * Description: XPopup显示和隐藏的回调接口
 * Create by dance, at 2018/12/21
 */
public interface XPopupCallback {
    /**
     * 完全显示的时候执行
     */
    void onShow();

    /**
     * 完全消失的时候执行
     */
    void onDismiss();
}
