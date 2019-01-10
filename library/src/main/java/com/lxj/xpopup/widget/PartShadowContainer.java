package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.lxj.xpopup.interfaces.OnClickOutsideListener;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description:
 * Create by dance, at 2019/1/10
 */
public class PartShadowContainer extends CardView {
    public boolean isDismissOnTouchOutside = true;
    public PartShadowContainer(@NonNull Context context) {
        super(context);
    }

    public PartShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PartShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float x, y;
    private long downTime;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算OutSide的Rect
        View implView = getChildAt(0);
        Rect outsideRect = null;
        FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
        if(implParams.gravity==Gravity.TOP){
            outsideRect = new Rect(0, implView.getHeight(), getWidth(), getHeight());
        }else {
            outsideRect = new Rect(0, 0,  getWidth(),  getHeight()-implView.getHeight());
        }

        if(XPopupUtils.isInRect(event.getX(), event.getY(), outsideRect)){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
                    if(distance< ViewConfiguration.get(getContext()).getScaledTouchSlop() && (System.currentTimeMillis() - downTime)<350){
                        if (isDismissOnTouchOutside){
                            if(listener!=null)listener.onClickOutside();
                        }
                    }
                    x = 0;
                    y = 0;
                    downTime = 0;
                    break;
            }
        }
        return true;
    }
    private OnClickOutsideListener listener;
    public void setOnClickOutsideListener(OnClickOutsideListener listener){
        this.listener = listener;
    }
}
