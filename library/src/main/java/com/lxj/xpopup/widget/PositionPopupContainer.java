package com.lxj.xpopup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import com.lxj.xpopup.enums.DragOrientation;

/**
 * PositionPopupView的容器.
 */
public class PositionPopupContainer extends FrameLayout {
    private static final String TAG = "PositionPopupContainer";
    private ViewDragHelper dragHelper;
    public View child;
    public float dragRatio = 0.2f;
    private OnPositionDragListener positionDragListener;
    public boolean enableDrag = false;
    public DragOrientation dragOrientation = DragOrientation.DragToUp;
    int touchSlop;
    public PositionPopupContainer(@NonNull Context context) {
        this(context, null);
    }
    public PositionPopupContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PositionPopupContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dragHelper = ViewDragHelper.create(this, cb);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        child = getChildAt(0);
    }

    private float touchX, touchY;
    boolean canIntercept = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1 || !enableDrag) return super.dispatchTouchEvent(ev);
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = ev.getX();
                    touchY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = ev.getX() - touchX;
                    float dy = ev.getY() - touchY;
                    canIntercept = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) > touchSlop;
                    touchX = ev.getX();
                    touchY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchX = 0;
                    touchY = 0;
                    break;
            }
        }catch (Exception e){ }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!enableDrag) return super.onInterceptTouchEvent(ev);
        boolean result = dragHelper.shouldInterceptTouchEvent(ev);
        return dragHelper.shouldInterceptTouchEvent(ev) || canIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1 || !enableDrag) return false;
        try {
            dragHelper.processTouchEvent(ev);
        }catch (Exception e){}
        return true;
    }

    ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return view==child && enableDrag;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return dragOrientation==DragOrientation.DragToUp || dragOrientation==DragOrientation.DragToBottom ? 1: 0;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return dragOrientation==DragOrientation.DragToLeft || dragOrientation==DragOrientation.DragToRight ? 1:0;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if(dragOrientation==DragOrientation.DragToUp){
                return dy<0 ? top : 0;
            }
            return dragOrientation==DragOrientation.DragToBottom && dy>0 ? top : 0;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if(dragOrientation==DragOrientation.DragToLeft){
                return dx<0 ? left : 0;
            }
            return dragOrientation==DragOrientation.DragToRight && dx>0 ? left : 0;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            float maxX = releasedChild.getMeasuredWidth() * dragRatio;
            float maxY = releasedChild.getMeasuredHeight() * dragRatio;
            if ((dragOrientation==DragOrientation.DragToLeft && releasedChild.getLeft()<-maxX)
            || (dragOrientation==DragOrientation.DragToRight && releasedChild.getRight()>(releasedChild.getMeasuredWidth()+maxX))
            || (dragOrientation==DragOrientation.DragToUp && releasedChild.getTop()<-maxY)
            || (dragOrientation==DragOrientation.DragToBottom && releasedChild.getBottom()>(releasedChild.getMeasuredHeight()+maxY))) {
                    positionDragListener.onDismiss();
            }else {
                dragHelper.smoothSlideViewTo(releasedChild, 0, 0);
                ViewCompat.postInvalidateOnAnimation(PositionPopupContainer.this);
            }
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(PositionPopupContainer.this);
        }
    }
    public void setOnPositionDragChangeListener(OnPositionDragListener positionDragListener) {
        this.positionDragListener = positionDragListener;
    }

    public static interface OnPositionDragListener{
        void onDismiss();
    }
}
