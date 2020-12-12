package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.enums.LayoutStatus;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 智能的拖拽布局，优先滚动整体，整体滚到头，则滚动内部能滚动的View
 * Create by dance, at 2018/12/23
 */
public class SmartDragLayout extends FrameLayout implements NestedScrollingParent {
    private View child;
    OverScroller scroller;
    VelocityTracker tracker;
    boolean enableDrag = true;//是否启用手势拖拽
    boolean dismissOnTouchOutside = true;
    boolean isUserClose = false;
    boolean isThreeDrag = false;  //是否开启三段拖拽
    LayoutStatus status = LayoutStatus.Close;
    public SmartDragLayout(Context context) {
        this(context, null);
    }

    public SmartDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (enableDrag) {
            scroller = new OverScroller(context);
        }
    }

    int maxY;
    int minY;

    @Override
    public void onViewAdded(View c) {
        super.onViewAdded(c);
        child = c;
    }

    int lastHeight;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        maxY = child.getMeasuredHeight();
        minY = 0;
        int l = getMeasuredWidth() / 2 - child.getMeasuredWidth() / 2;
        if (enableDrag) {
            // horizontal center
            child.layout(l, getMeasuredHeight(), l + child.getMeasuredWidth(), getMeasuredHeight() + maxY);
            if (status == LayoutStatus.Open) {
                if(isThreeDrag){
                    //通过scroll上移
                    scrollTo(getScrollX(), getScrollY() - (lastHeight - maxY));
                }else {
                    //通过scroll上移
                    scrollTo(getScrollX(), getScrollY() - (lastHeight - maxY));
                }
            }
        } else {
            // like bottom gravity
            child.layout(l, getMeasuredHeight() - child.getMeasuredHeight(), l + child.getMeasuredWidth(), getMeasuredHeight());
        }
        lastHeight = maxY;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        isUserClose = true;
        return super.dispatchTouchEvent(ev);
    }

    float touchX, touchY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enableDrag && scroller.computeScrollOffset()) {
            touchX = 0;
            touchY = 0;
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (enableDrag){
                    if(tracker!=null)tracker.clear();
                    tracker = VelocityTracker.obtain();
                }
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (enableDrag && tracker!=null) {
                    tracker.addMovement(event);
                    tracker.computeCurrentVelocity(1000);
                    int dy = (int) (event.getY() - touchY);
                    scrollTo(getScrollX(), getScrollY() - dy);
                    touchY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // click in child rect
                Rect rect = new Rect();
                child.getGlobalVisibleRect(rect);
                if (!XPopupUtils.isInRect(event.getRawX(), event.getRawY(), rect) && dismissOnTouchOutside) {
                    float distance = (float) Math.sqrt(Math.pow(event.getX() - touchX, 2) + Math.pow(event.getY() - touchY, 2));
                    if (distance < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        performClick();
                    }
                }else {

                }
                if (enableDrag && tracker!=null) {
                    float yVelocity = tracker.getYVelocity();
                    if (yVelocity > 1500 && !isThreeDrag) {
                        close();
                    } else {
                        finishScroll();
                    }
//                    tracker.recycle();
                    tracker = null;
                }

                break;
        }
        return true;
    }

    private void finishScroll() {
        if (enableDrag) {
            int threshold = isScrollUp ? (maxY - minY) / 3 : (maxY - minY) * 2 / 3;
            int dy = (getScrollY() > threshold ? maxY : minY) - getScrollY();
            if(isThreeDrag){
                int per = maxY/3;
                if(getScrollY()>per*2.5f) {
                    dy = maxY-getScrollY();
                }else if(getScrollY() <= per*2.5f && getScrollY()>per*1.5f){
                    dy = per*2-getScrollY();
                }else if(getScrollY() >per){
                    dy = per-getScrollY();
                }else {
                    dy = minY-getScrollY();
                }
            }
            scroller.startScroll(getScrollX(), getScrollY(), 0, dy, XPopup.getAnimationDuration());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    boolean isScrollUp;

    @Override
    public void scrollTo(int x, int y) {
        if (y > maxY) y = maxY;
        if (y < minY) y = minY;
        float fraction = (y - minY) * 1f / (maxY - minY);
        isScrollUp = y > getScrollY();
        if (listener != null) {
            if (isUserClose && fraction == 0f && status != LayoutStatus.Close) {
                status = LayoutStatus.Close;
                listener.onClose();
            } else if (fraction == 1f && status != LayoutStatus.Open) {
                status = LayoutStatus.Open;
                listener.onOpen();
            }
            listener.onDrag(y, fraction, isScrollUp);
        }
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
        setTranslationY(0);
    }

    public void open() {
        post(new Runnable() {
            @Override
            public void run() {
                int dy = maxY - getScrollY();
                smoothScroll( enableDrag && isThreeDrag ? dy/3 : dy, true);
                status = LayoutStatus.Opening;
            }
        });
    }

    public void close() {
        isUserClose = true;
        post(new Runnable() {
            @Override
            public void run() {
                scroller.abortAnimation();
                smoothScroll(minY - getScrollY(), false);
                status = LayoutStatus.Closing;
            }
        });
    }

    public void smoothScroll(final int dy, final boolean isOpen) {
        post(new Runnable() {
            @Override
            public void run() {
                scroller.startScroll(getScrollX(), getScrollY(), 0, dy, (int)(isOpen ? XPopup.getAnimationDuration() : XPopup.getAnimationDuration()*0.8f));
                ViewCompat.postInvalidateOnAnimation(SmartDragLayout.this);
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && enableDrag;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        //必须要取消，否则会导致滑动初次延迟
        scroller.abortAnimation();
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
        boolean isDragging = getScrollY() > minY && getScrollY() < maxY;
        if (isDragging && velocityY < -1500 && !isThreeDrag) {
            close();
        }
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

    public void isThreeDrag(boolean isThreeDrag) {
        this.isThreeDrag = isThreeDrag;
    }

    public void enableDrag(boolean enableDrag) {
        this.enableDrag = enableDrag;
    }

    public void dismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {
        void onClose();
        void onDrag(int y, float percent, boolean isScrollUp);
        void onOpen();
    }
}
