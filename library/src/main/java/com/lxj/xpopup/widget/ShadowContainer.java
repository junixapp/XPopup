package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description:
 * Create by dance, at 2019/4/3
 */
public class ShadowContainer extends FrameLayout {
    public ShadowContainer(@NonNull Context context) {
        this(context, null);
    }

    public ShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setLayerType(LAYER_TYPE_SOFTWARE, null); //开启硬件加速
        int padding = XPopupUtils.dp2px(context, 10);
        setPadding(padding, padding, padding, padding); //设置一个padding用来绘制阴影背景
//        setGravity(Gravity.CENTER);
        setWillNotDraw(true);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        //初始化一个绘制背景的画笔
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.BLUE);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAntiAlias(true);
        //计算阴影的偏移量 和 给画笔设置阴影
        int radius = getMeasuredHeight() / 12 > 40 ? 40 : getMeasuredHeight() / 12;
        int shadowColor = getMeasuredHeight() / 16 > 28 ? 28 : getMeasuredHeight() / 16;
        shadowPaint.setShadowLayer(radius, 0, shadowColor, Color.RED);

        //确定阴影的的整个大小
        RectF rectF = new RectF(getX() - (getMeasuredWidth() / 20), getY(), getX() + getMeasuredWidth() - (getMeasuredWidth() / 20), getY() + getMeasuredWidth() - ((getMeasuredWidth() / 40)));
        canvas.drawRoundRect(rectF, 0, 0, shadowPaint);
        canvas.save();
        super.dispatchDraw(canvas);
    }
}
