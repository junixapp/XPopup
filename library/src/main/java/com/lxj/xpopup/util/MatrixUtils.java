package com.lxj.xpopup.util;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Description:
 * Create by lxj, at 2019/1/22
 */
public class MatrixUtils {


    public static Matrix getImageMatrix(ImageView imageView) {
        int left = imageView.getLeft();
        int top = imageView.getTop();
        int right = imageView.getRight();
        int bottom = imageView.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);

        Drawable drawable = imageView.getDrawable();

        Matrix matrix;
        ImageView.ScaleType scaleType = imageView.getScaleType();
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            matrix = imageView.getImageMatrix();
            if (!matrix.isIdentity()) {
                matrix = new Matrix(matrix);
            } else {
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                if (drawableWidth > 0 && drawableHeight > 0) {
                    float scaleX = ((float) bounds.width()) / drawableWidth;
                    float scaleY = ((float) bounds.height()) / drawableHeight;
                    matrix = new Matrix();
                    matrix.setScale(scaleX, scaleY);
                } else {
                    matrix = null;
                }
            }
        } else {
            matrix = new Matrix(imageView.getImageMatrix());
        }

        return matrix;
    }

}
