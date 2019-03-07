package com.lxj.xpopup.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.lxj.xpopup.interfaces.OnDragChangeListener;

/**
 * wrap ViewPager, process drag event.
 */
public class PhotoViewContainer extends FrameLayout {
    private static final String TAG = "PhotoViewContainer";
    private ViewDragHelper dragHelper;
    private ViewPager viewPager;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int HideTopThreshold = 80;
    private int maxOffset;
    private OnDragChangeListener dragChangeListener;
    public int blackColor = Color.rgb(32, 36, 46);
    public boolean isReleaseing = false;

    public PhotoViewContainer(@NonNull Context context) {
        this(context, null);
    }

    public PhotoViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        HideTopThreshold = dip2px(HideTopThreshold);
        dragHelper = ViewDragHelper.create(this, cb);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewPager = (ViewPager) getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxOffset = getHeight() / 3;
    }

    boolean isVertical = false;
    private float touchX, touchY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = ev.getX();
                touchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - touchX;
                float dy = ev.getY() - touchY;
                viewPager.dispatchTouchEvent(ev);
                isVertical = (Math.abs(dy) > Math.abs(dx));
                touchX = ev.getX();
                touchY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchX = 0;
                touchY = 0;
                isVertical = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1) return false;
        return dragHelper.shouldInterceptTouchEvent(ev) && isVertical;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        dragHelper.processTouchEvent(ev);
        return true;
    }

    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return !isReleaseing;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int t = viewPager.getTop() + dy / 2;
            if (t >= 0) {
                return Math.min(t, maxOffset);
            } else {
                return -Math.min(-t, maxOffset);
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView != viewPager) {
                viewPager.offsetTopAndBottom(dy);
            }
            float fraction = Math.abs(top) * 1f / maxOffset;
            float pageScale = 1 - fraction * .2f;
            viewPager.setScaleX(pageScale);
            viewPager.setScaleY(pageScale);
            changedView.setScaleX(pageScale);
            changedView.setScaleY(pageScale);
            applyBgAnimation(fraction);
            if (dragChangeListener != null) {
                dragChangeListener.onDragChange(dy, pageScale, fraction);
            }

        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (Math.abs(releasedChild.getTop()) > HideTopThreshold) {
                if (dragChangeListener != null) dragChangeListener.onRelease();
            } else {
                dragHelper.smoothSlideViewTo(viewPager, 0, 0);
                dragHelper.smoothSlideViewTo(releasedChild, 0, 0);
                ViewCompat.postInvalidateOnAnimation(PhotoViewContainer.this);
            }
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(PhotoViewContainer.this);
        }
    }

    private void applyBgAnimation(float fraction) {
        setBackgroundColor((Integer) argbEvaluator.evaluate(fraction * .8f, blackColor, Color.TRANSPARENT));
    }

    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnDragChangeListener(OnDragChangeListener listener) {
        this.dragChangeListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isReleaseing = false;
    }
}
