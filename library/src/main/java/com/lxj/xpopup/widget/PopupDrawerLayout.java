package com.lxj.xpopup.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.enums.LayoutStatus;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 根据手势拖拽子View的layout，这种类型的弹窗比较特殊，不需要额外的动画器，因为
 * 动画是根据手势滑动而发生的
 * Create by dance, at 2018/12/20
 */
public class PopupDrawerLayout extends FrameLayout {
    
    LayoutStatus status = null;
    ViewDragHelper dragHelper;
    View mChild;
    PopupPosition position = PopupPosition.Left;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int defaultColor = Color.TRANSPARENT;
    public boolean isDrawStatusBarShadow = false;
    float fraction = 0f;
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

    public void setDrawerPosition(PopupPosition position) {
        this.position = position;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = getChildAt(0);
    }

    boolean hasLayout = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!hasLayout) {
            if (position == PopupPosition.Left) {
                mChild.layout(-mChild.getMeasuredWidth(), 0, 0, getMeasuredHeight());
            } else {
                mChild.layout(getMeasuredWidth(), 0, getMeasuredWidth() + mChild.getMeasuredWidth(), getMeasuredHeight());
            }
            hasLayout = true;
        } else {
            mChild.layout(mChild.getLeft(), mChild.getTop(), mChild.getRight(), mChild.getBottom());
        }
    }

    boolean isIntercept = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isIntercept = dragHelper.shouldInterceptTouchEvent(ev);
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(dragHelper.continueSettling(true))return true;
        dragHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return !dragHelper.continueSettling(true);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return fixLeft(left);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            // fraction = (now - start) * 1f / (end - start)
            if (position == PopupPosition.Left) {
                fraction = (left + mChild.getMeasuredWidth()) * 1f / mChild.getMeasuredWidth();
                if (left == -mChild.getMeasuredWidth() && listener != null && status != LayoutStatus.Close) {
                    status = LayoutStatus.Close;
                    listener.onClose();
                }
            } else if (position == PopupPosition.Right){
                fraction = (getMeasuredWidth() - left) * 1f / mChild.getMeasuredWidth();
                if (left == getMeasuredWidth() && listener != null&& status != LayoutStatus.Close){
                    status = LayoutStatus.Close;
                    listener.onClose();
                }
            }
            setBackgroundColor(bgAnimator.calculateBgColor(fraction));
            if (listener != null) {
                listener.onDismissing(fraction);
                if (fraction == 1f && status != LayoutStatus.Open) {
                    status = LayoutStatus.Open;
                    listener.onOpen();
                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int centerLeft = 0;
            int finalLeft = 0;
            if (position == PopupPosition.Left) {
                if (xvel < -1000) {
                    finalLeft = -mChild.getMeasuredWidth();
                } else {
                    centerLeft = -mChild.getMeasuredWidth() / 2;
                    finalLeft = mChild.getLeft() < centerLeft ? -mChild.getMeasuredWidth() : 0;
                }
            } else {
                if (xvel > 1000) {
                    finalLeft = getMeasuredWidth();
                } else {
                    centerLeft = getMeasuredWidth() - mChild.getMeasuredWidth() / 2;
                    finalLeft = releasedChild.getLeft() < centerLeft ? getMeasuredWidth() - mChild.getMeasuredWidth() : getMeasuredWidth();
                }
            }
            dragHelper.smoothSlideViewTo(releasedChild, finalLeft, releasedChild.getTop());
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        }
    };

    private int fixLeft(int left){
        if (position == PopupPosition.Left) {
            if (left < -mChild.getMeasuredWidth()) left = -mChild.getMeasuredWidth();
            if (left > 0) left = 0;
        } else if (position == PopupPosition.Right) {
            if (left < (getMeasuredWidth() - mChild.getMeasuredWidth()))
                left = (getMeasuredWidth() - mChild.getMeasuredWidth());
            if (left > getMeasuredWidth()) left = getMeasuredWidth();
        }
        return left;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    Paint paint;
    Rect shadowRect;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(isDrawStatusBarShadow){
            if(paint==null){
                paint = new Paint();
                shadowRect = new Rect(0,0, getMeasuredHeight(), XPopupUtils.getStatusBarHeight());
            }
            paint.setColor((Integer) argbEvaluator.evaluate(fraction, defaultColor, XPopup.statusBarShadowColor));
            canvas.drawRect(shadowRect, paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        status = null;
        fraction = 0f;
    }

    /**
     * 打开Drawer
     */
    public void open() {
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.smoothSlideViewTo(mChild, position == PopupPosition.Left ? 0 : (mChild.getLeft() - mChild.getMeasuredWidth()), getTop());
                ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
            }
        });
    }

    /**
     * 关闭Drawer
     */
    public void close() {
        if(dragHelper.continueSettling(true))return;
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.smoothSlideViewTo(mChild, position == PopupPosition.Left ? -mChild.getMeasuredWidth() : getMeasuredWidth(), getTop());
                ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
            }
        });
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {
        void onClose();

        void onOpen();

        /**
         * 关闭过程中执行
         *
         * @param fraction 关闭的百分比
         */
        void onDismissing(float fraction);
    }
}
