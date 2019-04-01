package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.util.XPopupUtils;

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

    Paint paint;
    Rect shadowRect;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(popupInfo.hasStatusBarShadow){
            if(paint==null){
                paint = new Paint();
                paint.setColor(XPopup.statusBarShadowColor);
                shadowRect = new Rect(0,0, getMeasuredHeight(), XPopupUtils.getStatusBarHeight());
            }
            canvas.drawRect(shadowRect, paint);
        }
    }
}
