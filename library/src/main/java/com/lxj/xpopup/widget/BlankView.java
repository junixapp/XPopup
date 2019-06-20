package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description: 大图浏览弹窗显示后的占位View
 * Create by lxj, at 2019/2/2
 */
public class BlankView extends View {
    public BlankView(Context context) {
        super(context);
    }

    public BlankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint = new Paint();
    private RectF rect = null;
    public int radius = 0;
    public int color = Color.WHITE;
    public int strokeColor = Color.parseColor("#DDDDDD");

    private void init() {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawRoundRect(rect, radius, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawRoundRect(rect, radius, radius, paint);
        paint.setStyle(Paint.Style.FILL);
    }
}
