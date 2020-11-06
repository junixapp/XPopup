package com.lxj.xpopup.interfaces;

import com.lxj.xpopup.core.BasePopupView;

/**
 * Description:
 * Create by dance, at 2019/6/13
 */
public class SimpleCallback implements XPopupCallback {
    @Override
    public void onCreated(BasePopupView popupView) {

    }
    @Override
    public void beforeShow(BasePopupView popupView) {

    }

    @Override
    public void onShow(BasePopupView popupView) {

    }
    @Override
    public void onDismiss(BasePopupView popupView) {

    }

    @Override
    public void beforeDismiss(BasePopupView popupView) {

    }

    @Override
    public boolean onBackPressed(BasePopupView popupView) {
        return false;
    }

    @Override
    public void onKeyBoardStateChanged(BasePopupView popupView, int height){}

    @Override
    public void onDrag(BasePopupView popupView, int value, float percent, boolean upOrLeft) {
    }
}
