package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Description: 用来消费PopupView内容外部的点击事件，包括传入自己的未处理的触摸事件
 * Create by dance, at 2018/12/9
 */
public class ClickConsumeView extends View {

    int touchSlop;
    public ClickConsumeView(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ClickConsumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickConsumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float x, y;
    long downTime;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupViewView范围内点击，则进行判断是否是点击事件
        // 如果是，则执行click
        Rect rect = new Rect();
        ((ViewGroup) getParent()).getChildAt(1).getGlobalVisibleRect(rect);
        if(!isInRect(event.getX(), event.getY(), rect)){
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
                    if(distance< touchSlop && (System.currentTimeMillis() - downTime)<350){
                        performClick();
                    }
                    x = 0;
                    y = 0;
                    downTime = 0;
                    break;
            }
        }

        return true;
    }

    private boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }
}
