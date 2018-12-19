package com.lxj.xpopupdemo;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lxj.xpopup.util.Utils;

/**
 * Description:
 * Create by dance, at 2018/12/18
 */
public class LoadingView extends View {
    private Paint paint;
    private int radius;
    private int radiusOffset;
    // 不是固定不变的，当width为30dp时，它为2dp，当宽度变大，这个也会相应的变大
    private int stokeWidth = 2;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int startColor = Color.parseColor("#EFEFEF");
    private int endColor = Color.parseColor("#010101");

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stokeWidth = Utils.dp2px(context, stokeWidth);
        paint.setStrokeWidth(stokeWidth);
    }

    int lineCount = 12;
    float avgAngle = 360f / lineCount;
    int time = 0;
    float centerX, centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = getMeasuredWidth() / 2;
        radiusOffset = radius / 3;

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;

        stokeWidth *= getMeasuredWidth()*1f / Utils.dp2px(getContext(), 30);
        paint.setStrokeWidth(stokeWidth);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        // 1 2 3 4 5
        // 2 3 4 5 1
        // 3 4 5 1 2
        // ...
        for (int i = lineCount-1; i >=0 ; i--) {
            int temp = Math.abs(i + time) % lineCount;
            float fraction = (temp+1) * 1f / lineCount;
            int color = (int) argbEvaluator.evaluate(fraction, startColor, endColor);
            paint.setColor(color);

            float startX = centerX + radiusOffset;
            canvas.drawLine(startX, centerY, startX + radius / 2.5f, centerY, paint);
            canvas.rotate(avgAngle, centerX, centerY);
        }
        postDelayed(increaseTask, 100);
    }

    private Runnable increaseTask = new Runnable() {
        @Override
        public void run() {
            time++;
            invalidate();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(increaseTask);
    }
}
