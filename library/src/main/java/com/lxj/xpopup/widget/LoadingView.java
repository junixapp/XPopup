package com.lxj.xpopup.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 加载View
 * Create by dance, at 2018/12/18
 */
public class LoadingView extends View {
    private Paint paint;
    private float radius;
    private float radiusOffset;
    // 不是固定不变的，当width为30dp时，它为2dp，当宽度变大，这个也会相应的变大
    private float stokeWidth = 2f;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int startColor = Color.parseColor("#EEEEEE");
    private int endColor = Color.parseColor("#111111");
    int lineCount = 10; // 共12条线
    float avgAngle = 360f / lineCount;
    int time = 0; // 重复次数
    float centerX, centerY; // 中心x，y

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stokeWidth = XPopupUtils.dp2px(context, stokeWidth);
        paint.setStrokeWidth(stokeWidth);
    }

    float startX, endX;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = getMeasuredWidth() / 2;
        radiusOffset = radius / 2.5f;

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;

        stokeWidth *= getMeasuredWidth() * 1f / XPopupUtils.dp2px(getContext(), 30);
        paint.setStrokeWidth(stokeWidth);
        startX = centerX + radiusOffset;
        endX = startX + radius / 3f;
        removeCallbacks(increaseTask);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        // 1 2 3 4 5
        // 2 3 4 5 1
        // 3 4 5 1 2
        // ...
        for (int i = lineCount - 1; i >= 0; i--) {
            int temp = Math.abs(i + time) % lineCount;
            float fraction = (temp + 1) * 1f / lineCount;
            int color = (int) argbEvaluator.evaluate(fraction, startColor, endColor);
            paint.setColor(color);

            canvas.drawLine(startX, centerY, endX, centerY, paint);
            // 线的两端画个点，看着圆滑
            canvas.drawCircle(startX, centerY, stokeWidth / 2, paint);
            canvas.drawCircle(endX, centerY, stokeWidth / 2, paint);
            canvas.rotate(avgAngle, centerX, centerY);
        }
        postDelayed(increaseTask, 70);
    }

    private Runnable increaseTask = new Runnable() {
        @Override
        public void run() {
            time++;
            postInvalidate(0,0,getMeasuredWidth(), getMeasuredHeight());
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(increaseTask);
    }

}
