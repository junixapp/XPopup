package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 智能的拖拽布局，优先滚动整体，整体滚到头，则滚动内部能滚动的View
 * Create by dance, at 2018/12/23
 */
public class SmartDragLayout extends CardView implements NestedScrollingParent {
    private static final String TAG = "SmartDragLayout";
    private View child;
    OverScroller scroller;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();
    boolean enableGesture = true;//是否启用手势
    boolean dismissOnTouchOutside = true;
    boolean hasShadowBg = true;
    boolean isUserClose = false;
    public SmartDragLayout(Context context) {
        this(context, null);
    }

    public SmartDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (enableGesture) {
            scroller = new OverScroller(context);
            setCardElevation(XPopupUtils.dp2px(context, 10));
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    int maxY;
    int minY;

    @Override
    public void onViewAdded(View c) {
        super.onViewAdded(c);
        child = c;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        maxY = child.getMeasuredHeight();
        minY = 0;
        int l = getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2;
        if (enableGesture) {
            // horizontal center
            child.layout(l, getMeasuredHeight(), l + child.getMeasuredWidth(), getMeasuredHeight() + child.getMeasuredHeight());
        } else {
            // like bottom gravity
            child.layout(l, getMeasuredHeight() - child.getMeasuredHeight(), l + child.getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        isUserClose = true;
        return super.dispatchTouchEvent(ev);
    }

    float touchX, touchY;
    long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                touchX = event.getX();
                touchY = event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (enableGesture) {
                    int dy = (int) (event.getY() - touchY);
                    scrollTo(getScrollX(), getScrollY() - dy);
                    touchY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                finishScroll();
                // click in child rect
                Rect rect = new Rect();
                child.getGlobalVisibleRect(rect);
                if (!XPopupUtils.isInRect(event.getX(), event.getY(), rect) && dismissOnTouchOutside) {
                    float distance = (float) Math.sqrt(Math.pow(event.getX() - touchX, 2) + Math.pow(event.getY() - touchY, 2));
                    long duration = System.currentTimeMillis() - downTime;
                    if (distance < ViewConfiguration.get(getContext()).getScaledTouchSlop() && duration < 350) {
                        performClick();
                    }
                }
                break;
        }
        return true;
    }

    private void finishScroll() {
        if (enableGesture) {
            int threshold = isScrollUp ? (maxY - minY) / 3 : (maxY - minY) * 2 / 3;
            int dy = (getScrollY() > threshold ? maxY : minY) - getScrollY();
            scroller.startScroll(getScrollX(), getScrollY(), 0, dy, 400);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    boolean isScrollUp;

    @Override
    public void scrollTo(int x, int y) {
        if (y > maxY) y = maxY;
        if (y < minY) y = minY;
        float fraction = (y - minY) * 1f / (maxY - minY);
        if(hasShadowBg)
            setBackgroundColor(bgAnimator.calculateBgColor(fraction));
        if (isUserClose && fraction == 0f && listener != null) {
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
        isUserClose = false;
    }

    public void open() {
        scroller.startScroll(getScrollX(), getScrollY(), 0, maxY - getScrollY(), 500);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void close() {
        isUserClose = true;
        scroller.startScroll(getScrollX(), getScrollY(), 0, minY - getScrollY(), 500);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && enableGesture;
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
        scrollTo(getScrollX(), getScrollY() + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            //scroll up
            int newY = getScrollY() + dy;
            if (newY < maxY) {
                consumed[1] = dy; // dy不一定能消费完
            }
            scrollTo(getScrollX(), newY);
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

    public void enableGesture(boolean enableGesture) {
        this.enableGesture = enableGesture;
    }
    public void dismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }
    public void hasShadowBg(boolean hasShadowBg) {
        this.hasShadowBg = hasShadowBg;
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {
        void onClose();
    }
}
