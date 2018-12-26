package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 智能的拖拽布局，优先滚动整体，整体滚到头，则滚动内部能滚动的View
 * Create by dance, at 2018/12/23
 */
public class SmartDragLayout extends CardView implements NestedScrollingParent {
    private static final String TAG = "SmartDragLayout";
    private View child;
    Scroller scroller;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();
    public SmartDragLayout(Context context) {
        this(context, null);
    }

    public SmartDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        setCardElevation(XPopupUtils.dp2px(context, 10));
        setBackgroundColor(Color.TRANSPARENT);
    }

    int maxY;
    int minY;

    @Override
    public void onViewAdded(View c) {
        super.onViewAdded(c);
        child = c;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        child.measure(widthMeasureSpec, maxHeight==0 ?
                Math.min((int) (getMeasuredHeight() * .7f), child.getMeasuredHeight()) : maxHeight);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxY = child.getMeasuredHeight();
        minY = 0;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        child.layout(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + child.getMeasuredHeight());
    }

    float touchX,touchY;
    long downTime;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                }
                touchX = event.getX();
                touchY = event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - touchY);
                scrollTo(getScrollX(), getScrollY() - dy);
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                finishScroll();

                float distance = (float) Math.sqrt(Math.pow(event.getX()-touchX,2)+Math.pow(event.getY()-touchY,2) );
                long duration = System.currentTimeMillis() - downTime;
                if(distance < ViewConfiguration.get(getContext()).getScaledTouchSlop() && duration<350){
                    performClick();
                }
                break;
        }
        return true;
    }

    private void finishScroll() {
        int threshold = isScrollUp ? (maxY - minY) / 3: (maxY - minY)*2 / 3;

        int dy = (getScrollY() > threshold ? maxY : minY) - getScrollY();
        scroller.startScroll(getScrollX(), getScrollY(), 0, dy, 400);
        invalidate();
    }

    boolean isScrollUp;

    @Override
    public void scrollTo(int x, int y) {
        if (y > maxY) y = maxY;
        if (y < minY) y = minY;
        float fraction = (y - minY) * 1f / (maxY - minY);
        setBackgroundColor(bgAnimator.calculateBgColor(fraction));
        if(fraction==0f && listener!=null){
            listener.onClose();
        }
        isScrollUp = y > getScrollY();
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isScrollUp = false;
    }

    public void open() {
        scroller.startScroll(getScrollX(), getScrollY(), 0, maxY-getScrollY(), 500);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void close() {
        scroller.startScroll(getScrollX(), getScrollY(), 0, minY-getScrollY(), 600);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
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
//        Log.e(TAG, "onNestedScroll  dyConsumed: " + dyConsumed + "  dyUnconsumed: " + dyUnconsumed);
        scrollTo(getScrollX(), getScrollY() + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Log.e(TAG, "onNestedPreScroll  dy： " + dy + "  consumedY: " + consumed[1]);
        if (dy > 0 && getScrollY() < maxY) {
            int newY = getScrollY() + dy;
            scrollTo(getScrollX(), newY);
            if (newY > maxY) {
                consumed[1] = newY - maxY; // dy不一定能消费完
            } else {
                consumed[1] = dy;
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    private int maxHeight;
    public void setMaxHeight(int maxHeight){
        this.maxHeight = maxHeight;
    }

    private OnCloseListener listener;
    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }
    public interface OnCloseListener{
        void onClose();
    }
}
