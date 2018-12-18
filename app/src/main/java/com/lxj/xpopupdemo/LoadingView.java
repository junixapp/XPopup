package com.lxj.xpopupdemo;

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

/**
 * Description:
 * Create by dance, at 2018/12/18
 */
public class LoadingView extends View {
    private Paint paint;
    private int radius;
    private int radiusOffset;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = getMeasuredWidth() / 2;
        radiusOffset = getMeasuredWidth() / 4;
    }

    int lineCount = 2;
    float avgAngle = 360f / lineCount;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getMeasuredWidth() / 2;
        float centerY = getMeasuredHeight() / 2;
        Log.e("tag", "centerX: "+centerX + " centerY: " + centerY);

        for (int i = 0; i < lineCount; i++) {
            float angle = i * avgAngle;
            double sinVal = angle%90 ==0 ? 1f : Math.sin(angle);
            double cosVal = angle%90 ==0 ? 1f : Math.cos(angle);
            float endY = (float) (centerY +  sinVal * radius * (angle>=90f?-1:1));
            float endX = (float) (centerX +  cosVal * radius  * (angle>=180f?-1:1));
            Log.e("tag", "angle: "+angle + " endX: " + endX
                + " endY: " + endY + " sinVal: "+ sinVal);
            PointF startPoint = calculatePointByPercent(0.5f, new PointF(centerX, centerY), new PointF(endX, endY));
            canvas.drawLine(startPoint.x, startPoint.y, endX, endY, paint);
        }


    }

    FloatEvaluator floatEvaluator = new FloatEvaluator();

    private PointF calculatePointByPercent(float percent, PointF start, PointF end) {
        float x = floatEvaluator.evaluate(percent, start.x, end.x);
        float y = floatEvaluator.evaluate(percent, start.y, end.y);
        return new PointF(x, y);
    }
}
