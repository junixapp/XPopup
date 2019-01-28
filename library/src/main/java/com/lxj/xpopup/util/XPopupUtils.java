package com.lxj.xpopup.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.BasePopupView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class XPopupUtils {
    public static int getWindowWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(){
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    public static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 全网唯一能兼容所有手机（包括全面屏）判断是否有导航栏的方法
     * @param context
     * @return
     */
    public static boolean hasNavigationBar(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);
        return appUsableSize.y + XPopupUtils.getStatusBarHeight() < realScreenSize.y;
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        }

        return size;
    }

    /**
     * 是否是特殊设备，目前发现有：小米MIX系列
     * @return
     */
    public static boolean isFuckDevice(){
        String model = XPopupUtils.getModel();
        return model.equalsIgnoreCase("MIX2") || model.equalsIgnoreCase("MIX2S");
    }

    public static void widthAndHeight(final View target, final int maxWidth, final int maxHeight){
        widthAndHeight(target, maxWidth, maxHeight, false);
    }
    public static void widthAndHeight(final View target, final int maxWidth, final int maxHeight, boolean isCenter){
        target.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = target.getLayoutParams();
                View implView = ((ViewGroup) target).getChildAt(0);
                ViewGroup.LayoutParams implParams = implView.getLayoutParams();
                // 默认PopupContent宽是match，高是wrap
                int w = target.getMeasuredWidth();
                // response impl view wrap_content params.
                if(implParams.width == FrameLayout.LayoutParams.WRAP_CONTENT){
                    w = Math.min(w, implView.getMeasuredWidth());
                }
                if(maxWidth!=0){
                    params.width = Math.min(w, maxWidth);
                }else {
                    params.width = w;
                }

                int h = target.getMeasuredHeight();
                // response impl view match_parent params.
                if(implParams.height == FrameLayout.LayoutParams.MATCH_PARENT){
                    h = ((ViewGroup)target.getParent()).getMeasuredHeight();
                    params.height = h;
                }
                if(maxHeight!=0){
                    params.height = Math.min(h, maxHeight);
                }
                target.setLayoutParams(params);
            }
        });
    }

    public static void setCursorDrawableColor(EditText editText, int color) {
        try {
            Field fCursorDrawableRes =
                    TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            Resources res = editText.getContext().getResources();
            drawables[0] = res.getDrawable(mCursorDrawableRes);
            drawables[1] = res.getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {
        }
    }

    public static BitmapDrawable createBitmapDrawable(Resources resources,int width, int color){
        Bitmap bitmap = Bitmap.createBitmap(width, 20, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0,0, bitmap.getWidth(), 4, paint);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0,4, bitmap.getWidth(), 20, paint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources,bitmap);
        bitmapDrawable.setGravity(Gravity.BOTTOM);
        return bitmapDrawable;
    }

    public static StateListDrawable createSelector(Drawable defaultDrawable, Drawable focusDrawable){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focusDrawable);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }


    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    /**
     * Return whether soft input is visible.
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        return getDecorViewInvisibleHeight(activity) > 0;
    }

    private static int sDecorViewDelta = 0;

    public static int getDecorViewInvisibleHeight(final Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    public static void moveUpToKeyboard(int keyboardHeight, BasePopupView pv) {
        int targetY = keyboardHeight + pv.getPopupContentView().getHeight() / 2 - XPopupUtils.getWindowHeight(pv.getContext()) / 2;
        pv.getPopupContentView().animate().translationY(-Math.abs(targetY))
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator(1))
                .start();
    }
    public static void moveDown(BasePopupView pv){
        pv.getPopupContentView().animate().translationY(0)
                .setInterpolator(new OvershootInterpolator(1))
                .setDuration(300).start();
    }
}
