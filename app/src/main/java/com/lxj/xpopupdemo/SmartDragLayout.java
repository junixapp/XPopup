package com.lxj.xpopupdemo;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Description:
 * Create by dance, at 2018/12/23
 */
public class SmartDragLayout extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "SmartDragLayout";
    private View child;
    OverScroller scroller;
    public SmartDragLayout(Context context) {
        this(context, null);
    }

    public SmartDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SmartDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new OverScroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        child = getChildAt(0);
    }

    int maxY;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxY = (int) Math.min(getMeasuredHeight()*.8f, child.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        child.layout(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + child.getMeasuredHeight());
    }

    float touchY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - touchY);
                scrollTo(getScrollX(), getScrollY() - dy);
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                finishScroll();
                break;
        }
        return true;
    }

    private void finishScroll(){
        int dy = (getScrollY() > maxY/2 ? maxY : 0) - getScrollY();
        scroller.startScroll(getScrollX(), getScrollY(), 0 , dy,400 );
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if(y>maxY) y = maxY;
        if(y<0) y = 0;
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    @Override
    public void onStopNestedScroll(View target) {
        finishScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll  dyConsumed: " + dyConsumed + "  dyUnconsumed: " + dyUnconsumed);
        scrollTo(getScrollX(), getScrollY() + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll  dy： " + dy + "  consumedY: " + consumed[1]);
        if(dy>0 && getScrollY()<maxY){
            int newY = getScrollY() + dy;
            scrollTo(getScrollX(), newY);
            if(newY>maxY){
                consumed[1] = newY - maxY; // dy不一定能消费完
            }else {
                consumed[1] = dy;
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling  velocityY： " + velocityY);

//        scroller.fling(getScrollX(), getScrollY(), (int)velocityX,  (int)velocityY, 0,0,0,maxY);
//        invalidate();

        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
}
