package com.lxj.xpopup.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.animator.ShadowBgAnimator;

/**
 * Description: 根据手势拖拽子View的layout，这种类型的弹窗比较特殊，不需要额外的动画器，因为
 * 动画是根据手势滑动而发生的
 * Create by dance, at 2018/12/20
 */
public class PopupDrawerLayout extends FrameLayout {

    public enum Position {
        Left, Right
    }

    ViewDragHelper dragHelper;
    View child;
    Position position = Position.Left;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();

    public PopupDrawerLayout(Context context) {
        this(context, null);
    }

    public PopupDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, callback);
    }

    public void setDrawerPosition(Position position) {
        this.position = position;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        child = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (position == Position.Left) {
            child.layout(-child.getMeasuredWidth(), 0, 0, getMeasuredHeight());
        } else {
            child.layout(getMeasuredWidth(), 0, getMeasuredWidth() + child.getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (position == Position.Left) {
                if (left < -child.getMeasuredWidth()) left = -child.getMeasuredWidth();
                if (left > 0) left = 0;
            } else {
                if (left < (getMeasuredWidth() - child.getMeasuredWidth()))
                    left = (getMeasuredWidth() - child.getMeasuredWidth());
                if (left > getMeasuredWidth()) left = getMeasuredWidth();
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float fraction = 0f;
            if(position==Position.Left){
                // fraction = (now - start)*1f / (end - start)
                fraction = (left + child.getMeasuredWidth())*1f / child.getMeasuredWidth();
                if(left==-child.getMeasuredWidth() && listener!=null)
                    listener.onClose();
            }else {
                fraction = (left - getMeasuredWidth())*1f / -child.getMeasuredWidth();
                if(left==getMeasuredWidth() && listener!=null)
                    listener.onClose();
            }
            setBackgroundColor(bgAnimator.calculateBgColor(fraction));
            if(listener!=null){
                listener.onDismissing(fraction);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int centerLeft = 0;
            if (position == Position.Left) {
                if(xvel < -1000){
                    dragHelper.smoothSlideViewTo(releasedChild, -child.getMeasuredWidth(), releasedChild.getTop());
                }else {
                    centerLeft = -child.getMeasuredWidth() / 2;
                    int finalLeft = releasedChild.getLeft() < centerLeft ? -child.getMeasuredWidth() : 0;
                    dragHelper.smoothSlideViewTo(releasedChild, finalLeft, releasedChild.getTop());
                }
            } else {
                if(xvel > 1000){
                    dragHelper.smoothSlideViewTo(releasedChild, getMeasuredWidth(), releasedChild.getTop());
                }else {
                    centerLeft = getMeasuredWidth() + child.getMeasuredWidth() / 2;
                    int finalLeft = releasedChild.getLeft() < centerLeft ? getMeasuredWidth() : getMeasuredWidth() + child.getMeasuredWidth();
                    dragHelper.smoothSlideViewTo(releasedChild, finalLeft, releasedChild.getTop());
                }
            }
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开Drawer
     */
    public void open() {
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.smoothSlideViewTo(child, position == Position.Left ? 0 : (getMeasuredWidth() - child.getMeasuredWidth()), getTop());
                ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
            }
        });
    }

    /**
     * 关闭Drawer
     */
    public void close() {
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.smoothSlideViewTo(child, position == Position.Left ? -child.getMeasuredWidth() : getMeasuredWidth(), getTop());
                ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
            }
        });
    }

    private OnCloseListener listener;
    public void setOnCloseListener(OnCloseListener listener){
        this.listener = listener;
    }
    public interface OnCloseListener{
        void onClose();

        /**
         * 关闭过程中执行
         * @param fraction 关闭的百分比
         */
        void onDismissing(float fraction);
    }
}
