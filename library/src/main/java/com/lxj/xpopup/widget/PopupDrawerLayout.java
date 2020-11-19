package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.viewpager.widget.ViewPager;

import com.lxj.xpopup.animator.ShadowBgAnimator;
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
    View placeHolder, mChild;
    public PopupPosition position = PopupPosition.Left;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();
    public boolean isDrawStatusBarShadow = false;
    float fraction = 0f;
    public boolean enableShadow = true;
    public boolean enableDrag = true;

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
        placeHolder = getChildAt(0);
        mChild = getChildAt(1);
    }

    float ty;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ty = getTranslationY();
    }

    boolean hasLayout = false;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        placeHolder.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        if (!hasLayout) {
            if (position == PopupPosition.Left) {
                mChild.layout(-mChild.getMeasuredWidth(), 0, 0, getMeasuredHeight());
            } else {
                mChild.layout(getMeasuredWidth(), 0, getMeasuredWidth() + mChild.getMeasuredWidth(), getMeasuredHeight());
            }
            hasLayout = true;
        } else {
            mChild.layout(mChild.getLeft(), mChild.getTop(), mChild.getRight(), mChild.getMeasuredHeight());
        }
    }

    boolean isIntercept = false;
    float x, y;
    float downX, downY;
    boolean isToLeft, canChildScrollLeft;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!enableDrag) return super.onInterceptTouchEvent(ev);
        if (dragHelper.continueSettling(true) || status==LayoutStatus.Close) return true;
        isToLeft = ev.getX() < x;
        x = ev.getX();
        y = ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - downX);
                float dy = Math.abs(y - downY);
                if(dy > dx){
                    //垂直方向滑动
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                x = 0;
                y = 0;
                break;
        }
//        boolean canChildScrollRight = canScroll(this, ev.getX(), ev.getY(), -1);
        canChildScrollLeft = canScroll(this, ev.getX(), ev.getY(), 1);
//        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
//            x = 0;
//            y = 0;
//        }
        isIntercept = dragHelper.shouldInterceptTouchEvent(ev);
        if (isToLeft && !canChildScrollLeft) {
            return isIntercept;
        }

        boolean canChildScrollHorizontal = canScroll(this, ev.getX(), ev.getY());
        if (!canChildScrollHorizontal) return isIntercept;

        return super.onInterceptTouchEvent(ev);
    }

    private boolean canScroll(ViewGroup group, float x, float y, int direction) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            int[] location = new int[2];
            child.getLocationInWindow(location);
            Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(),
                    location[1] + child.getHeight());
            boolean inRect = XPopupUtils.isInRect(x, y, rect);
            if (inRect && child instanceof ViewGroup) {
                if (child instanceof ViewPager) {
                    ViewPager pager = (ViewPager) child;
                    if (direction == 0) {
                        return pager.canScrollHorizontally(-1) || pager.canScrollHorizontally(1);
                    }
                    return pager.canScrollHorizontally(direction);
                } else if (child instanceof HorizontalScrollView) {
                    HorizontalScrollView hsv = (HorizontalScrollView) child;
                    if (direction == 0) {
                        return hsv.canScrollHorizontally(-1) || hsv.canScrollHorizontally(1);
                    }
                    return hsv.canScrollHorizontally(direction);
                } else {
                    return canScroll((ViewGroup) child, x, y, direction);
                }
            }
        }
        return false;
    }

    private boolean canScroll(ViewGroup group, float x, float y) {
        return canScroll(group, x, y, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enableDrag) return super.onTouchEvent(event);
        if (dragHelper.continueSettling(true)) return true;
        dragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return !dragHelper.continueSettling(true) && status!=LayoutStatus.Close;
        }
        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == placeHolder) return left;
            return fixLeft(left);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == placeHolder) {
                placeHolder.layout(0, 0, placeHolder.getMeasuredWidth(), placeHolder.getMeasuredHeight());
                int newLeft = fixLeft(mChild.getLeft() + dx);
                mChild.layout(newLeft, mChild.getTop(), newLeft + mChild.getMeasuredWidth(), mChild.getBottom());
                calcFraction(newLeft, dx);
            } else {
                calcFraction(left,dx);
            }
        }

        private void calcFraction(int left,int dx) {
            // fraction = (now - start) * 1f / (end - start)
            if (position == PopupPosition.Left) {
                fraction = (left + mChild.getMeasuredWidth()) * 1f / mChild.getMeasuredWidth();
                if (left == -mChild.getMeasuredWidth() && listener != null && status != LayoutStatus.Close) {
                    status = LayoutStatus.Close;
                    listener.onClose();
                }
            } else if (position == PopupPosition.Right) {
                fraction = (getMeasuredWidth() - left) * 1f / mChild.getMeasuredWidth();
                if (left == getMeasuredWidth() && listener != null && status != LayoutStatus.Close) {
                    status = LayoutStatus.Close;
                    listener.onClose();
                }
            }
            if (enableShadow) setBackgroundColor(bgAnimator.calculateBgColor(fraction));
            if (listener != null) {
                listener.onDrag(left, fraction, dx<0);
                if (fraction == 1f && status != LayoutStatus.Open) {
                    status = LayoutStatus.Open;
                    listener.onOpen();
                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild == placeHolder && xvel == 0) {
                if(isDismissOnTouchOutside) close();
                return;
            }
            if (releasedChild == mChild && isToLeft && !canChildScrollLeft && xvel < -500) {
                close();
                return;
            }

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
            dragHelper.smoothSlideViewTo(mChild, finalLeft, releasedChild.getTop());
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        }
    };

    private int fixLeft(int left) {
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
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        status = null;
        hasLayout = false;
        fraction = 0f;
        setTranslationY(ty);
    }

    /**
     * 打开Drawer
     */
    public void open() {
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.smoothSlideViewTo(mChild, position == PopupPosition.Left ? 0 : (mChild.getLeft() - mChild.getMeasuredWidth()), 0);
                ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
            }
        });
    }

    public boolean isDismissOnTouchOutside = true;

    /**
     * 关闭Drawer
     */
    public void close() {
        post(new Runnable() {
            @Override
            public void run() {
                dragHelper.abort();
                dragHelper.smoothSlideViewTo(mChild, position == PopupPosition.Left ? -mChild.getMeasuredWidth() : getMeasuredWidth(), 0);
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
        void onDrag(int x,float fraction, boolean isToLeft);
    }
}
