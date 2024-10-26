package com.lxj.xpopup.interfaces;

import android.view.View;

import com.lxj.xpopup.core.BasePopupView;

/**
 * Description:
 * Create by dance, at 2018/12/17
 */
public interface OnInputConfirmListener {

    default void onConfirm(String text){}

    //对于自定义输入布局，不适用onConfirm可以用onNewConfirm去findViewById处理其他控件
    default void onNewConfirm(BasePopupView view){}
}
