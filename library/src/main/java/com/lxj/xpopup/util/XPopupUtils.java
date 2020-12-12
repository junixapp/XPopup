package com.lxj.xpopup.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.FloatRange;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.core.PositionPopupView;
import com.lxj.xpopup.enums.ImageType;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class XPopupUtils {
    public static int getWindowWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    //应用界面可见高度，可能不包含导航和状态栏，看Rom实现
    public static int getAppHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    //屏幕的高度，包含状态栏，导航栏，看Rom实现
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getStatusBarHeight() {
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

    public static void setWidthHeight(View target, int width, int height) {
        if (width <= 0 && height <= 0) return;
        ViewGroup.LayoutParams params = target.getLayoutParams();
        if (width > 0) params.width = width;
        if (height > 0) params.height = height;
        target.setLayoutParams(params);
    }

    public static void applyPopupSize(final ViewGroup content, final int maxWidth, final int maxHeight,
                                      final int popupWidth, final int popupHeight, final Runnable afterApplySize) {
        content.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = content.getLayoutParams();
                View implView = content.getChildAt(0);
                ViewGroup.LayoutParams implParams = implView.getLayoutParams();
                // 假设默认Content宽是match，高是wrap
                int w = content.getMeasuredWidth();
                // response impl view wrap_content params.
                if (maxWidth > 0) {
                    //指定了最大宽度，就限制最大宽度
                    params.width = Math.min(w, maxWidth);
                    if (popupWidth > 0) {
                        params.width = Math.min(popupWidth, maxWidth);
                        implParams.width = Math.min(popupWidth, maxWidth);
                    }
                } else if (popupWidth > 0) {
                    params.width = popupWidth;
                    implParams.width = popupWidth;
                }

                int h = content.getMeasuredHeight();
                if (maxHeight > 0) {
                    params.height = Math.min(h, maxHeight);
                    if (popupHeight > 0) {
                        params.height = Math.min(popupHeight, maxHeight);
                        implParams.height = Math.min(popupHeight, maxHeight);
                    }
                } else if(popupHeight > 0) {
                    params.height = popupHeight;
                    implParams.height = popupHeight;
                }
                implView.setLayoutParams(implParams);
                content.setLayoutParams(params);
                content.post(new Runnable() {
                    @Override
                    public void run() {
                        if (afterApplySize != null) {
                            afterApplySize.run();
                        }
                    }
                });

            }
        });
    }

    public static void setCursorDrawableColor(EditText et, int color) {
        //暂时没有找到有效的方法来动态设置cursor的颜色
    }

    public static BitmapDrawable createBitmapDrawable(Resources resources, int width, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, 20, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0, 0, bitmap.getWidth(), 4, paint);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 4, bitmap.getWidth(), 20, paint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        bitmapDrawable.setGravity(Gravity.BOTTOM);
        return bitmapDrawable;
    }

    public static StateListDrawable createSelector(Drawable defaultDrawable, Drawable focusDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focusDrawable);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }

    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    private static int sDecorViewDelta = 0;

    public static int getDecorViewInvisibleHeight(final Window window) {
        final View decorView = window.getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    //监听到的keyboardHeight有一定几率是错误的，比如在同时显示导航栏和弹出输入法的时候，有一定几率会算上导航栏的高度，
    //这个不是必现的，暂时无解
    private static int correctKeyboardHeight = 0;

    public static void moveUpToKeyboard(final int keyboardHeight, final BasePopupView pv) {
        if (correctKeyboardHeight == 0) correctKeyboardHeight = keyboardHeight;
        else if (keyboardHeight != 0)
            correctKeyboardHeight = Math.min(correctKeyboardHeight, keyboardHeight);
        pv.post(new Runnable() {
            @Override
            public void run() {
                moveUpToKeyboardInternal(correctKeyboardHeight, pv);
            }
        });
    }

    private static void moveUpToKeyboardInternal(int keyboardHeight, BasePopupView pv) {
        if (pv.popupInfo == null || !pv.popupInfo.isMoveUpToKeyboard) return;
        //暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView || (pv instanceof AttachPopupView && !(pv instanceof PartShadowPopupView))) {
            return;
        }
        //判断是否盖住输入框
        ArrayList<EditText> allEts = new ArrayList<>();
        findAllEditText(allEts, pv);
        EditText focusEt = null;
        for (EditText et : allEts) {
            if (et.isFocused()) {
                focusEt = et;
                break;
            }
        }

        int dy = 0;
        int popupHeight = pv.getPopupContentView().getHeight();
        int popupWidth = pv.getPopupContentView().getWidth();
        if (pv.getPopupImplView() != null) {
            popupHeight = Math.min(popupHeight, pv.getPopupImplView().getMeasuredHeight());
            popupWidth = Math.min(popupWidth, pv.getPopupImplView().getMeasuredWidth());
        }

        int screenHeight = pv.getMeasuredHeight();
        int focusEtTop = 0;
        int focusBottom = 0;
        if (focusEt != null) {
            int[] locations = new int[2];
            focusEt.getLocationInWindow(locations);
            focusEtTop = locations[1];
            focusBottom = focusEtTop + focusEt.getMeasuredHeight();
        }
        //执行上移
        if (pv instanceof FullScreenPopupView ||
                (popupWidth == XPopupUtils.getWindowWidth(pv.getContext()) &&
                        popupHeight == screenHeight)
        ) {
            // 如果是全屏弹窗，特殊处理，只要输入框没被盖住，就不移动。
            if (focusBottom + keyboardHeight < screenHeight) {
                return;
            }
        }
        if (pv instanceof FullScreenPopupView) {
            int overflowHeight = focusBottom + keyboardHeight - screenHeight;
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        } else if (pv instanceof CenterPopupView) {
            int popupBottom = (screenHeight + popupHeight) / 2;
            int targetY = popupBottom + keyboardHeight - screenHeight;
            if (focusEt != null && focusEtTop - targetY < 0) {
                targetY += focusEtTop - targetY - getStatusBarHeight();//限制不能被状态栏遮住
            }
            dy = Math.max(0, targetY);
        } else if (pv instanceof BottomPopupView) {
            dy = keyboardHeight;
            if (focusEt != null && focusEtTop - dy < 0) {
                dy += focusEtTop - dy - getStatusBarHeight();//限制不能被状态栏遮住
            }
        } else if (isBottomPartShadow(pv) || pv instanceof DrawerPopupView) {
            int overflowHeight = (focusBottom + keyboardHeight) - screenHeight;
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        } else if (isTopPartShadow(pv)) {
            int overflowHeight = (focusBottom + keyboardHeight) - screenHeight;
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
            if (dy != 0) {
                pv.getPopupImplView().animate().translationY(-dy)
                        .setDuration(200)
                        .setInterpolator(new OvershootInterpolator(0))
                        .start();
            }
            return;
        }
        //dy=0说明没有触发移动，有些弹窗有translationY，不能影响它们
        if (dy == 0 && pv.getPopupContentView().getTranslationY() != 0) return;
        pv.getPopupContentView().animate().translationY(-dy)
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator(0))
                .start();
    }


    /**
     * app可用高度是否包含状态栏的高度
     *
     * @param context
     * @return
     */
    private static boolean isAppHeightContainStatusBar(Context context) {
        int appHeight = getAppHeight(context);
        int screenHeight = getScreenHeight(context);
        int statusBarHeight = getStatusBarHeight();
        int navHeight = getNavBarHeight();
        if (screenHeight == (appHeight + statusBarHeight) ||
                screenHeight == (appHeight + navHeight + statusBarHeight)) return false;
        return true;
    }

    private static boolean isBottomPartShadow(BasePopupView pv) {
        return pv instanceof PartShadowPopupView && ((PartShadowPopupView) pv).isShowUp;
    }

    private static boolean isTopPartShadow(BasePopupView pv) {
        return pv instanceof PartShadowPopupView && !((PartShadowPopupView) pv).isShowUp;
    }

    //    public static HashMap
    public static void moveDown(BasePopupView pv) {
        //暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView) return;
        if (!(pv instanceof PartShadowPopupView) && pv instanceof AttachPopupView) return;
        if (pv instanceof PartShadowPopupView && !isBottomPartShadow(pv)) {
            pv.getPopupImplView().animate().translationY(0)
                    .setDuration(100).start();
        } else {
            pv.getPopupContentView().animate().translationY(0)
                    .setDuration(100).start();
        }
    }

    public static boolean isNavBarVisible(Window window) {
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = decorView.getContext()
                        .getResources()
                        .getResourceEntryName(id);
                if ("navigationBarBackground".equals(resourceEntryName)
                        && child.getVisibility() == View.VISIBLE) {
                    isVisible = true;
                    break;
                }
            }
        }
        if (isVisible) {
            int visibility = decorView.getSystemUiVisibility();
            isVisible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }
        return isVisible;
    }

    public static void findAllEditText(ArrayList<EditText> list, ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v instanceof EditText && v.getVisibility() == View.VISIBLE) {
                list.add((EditText) v);
            } else if (v instanceof ViewGroup) {
                findAllEditText(list, (ViewGroup) v);
            }
        }
    }

    private static Context mContext;

    public static void saveBmpToAlbum(final Context context, final XPopupImageLoader imageLoader, final Object uri) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        mContext = context;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File source = imageLoader.getImageFile(mContext, uri);
                if (source == null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "图片不存在！", Toast.LENGTH_SHORT).show();
                            mContext = null;
                        }
                    });
                    return;
                }
                //1. create path
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_PICTURES;
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) dirFile.mkdirs();
                try {
                    ImageType type = ImageHeaderParser.getImageType(new FileInputStream(source));
                    String ext = getFileExt(type);
                    final File target = new File(dirPath, System.currentTimeMillis() + "." + ext);
                    if (target.exists()) target.delete();
                    target.createNewFile();
                    //2. save
                    writeFileFromIS(target, new FileInputStream(source));
                    //3. notify
                    MediaScannerConnection.scanFile(mContext, new String[]{target.getAbsolutePath()},
                            new String[]{"image/" + ext}, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(final String path, Uri uri) {
                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mContext != null) {
                                                Toast.makeText(mContext, "已保存到相册！", Toast.LENGTH_SHORT).show();
                                                mContext = null;
                                            }
                                        }
                                    });
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "没有保存权限，保存功能无法使用！", Toast.LENGTH_SHORT).show();
                            mContext = null;
                        }
                    });
                }
            }
        });
    }

    private static String getFileExt(ImageType type) {
        switch (type) {
            case GIF:
                return "gif";
            case PNG:
            case PNG_A:
                return "png";
            case WEBP:
            case WEBP_A:
                return "webp";
            case JPEG:
                return "jpeg";
        }
        return "jpeg";
    }

    private static boolean writeFileFromIS(final File file, final InputStream is) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[8192];
            int len;
            while ((len = is.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取应用可用的屏幕高度
//    public static int getPhoneScreenHeight(Window window) {
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        window.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
//        return outMetrics.heightPixels;
//    }

    public static Bitmap renderScriptBlur(Context context, final Bitmap src,
                                          @FloatRange(
                                                  from = 0, to = 25, fromInclusive = false
                                          ) final float radius,
                                          final boolean recycle) {
        RenderScript rs = null;
        Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        try {
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            Allocation input = Allocation.createFromBitmap(rs,
                    ret,
                    Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            blurScript.setInput(input);
            blurScript.setRadius(radius);
            blurScript.forEach(output);
            output.copyTo(ret);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
        }
        return ret;
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setDrawingCacheEnabled(true);
        view.setWillNotCacheDrawing(false);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (null == drawingCache) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            drawingCache = view.getDrawingCache();
            if (drawingCache != null) {
                bitmap = Bitmap.createBitmap(drawingCache);
            } else {
                bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            }
        } else {
            bitmap = Bitmap.createBitmap(drawingCache);
        }
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        return bitmap;
    }

    public static boolean isLayoutRtl(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Locale primaryLocale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                primaryLocale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                primaryLocale = context.getResources().getConfiguration().locale;
            }
            return TextUtils.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL;
        }
        return false;
    }

    public static Activity context2Activity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context);
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    public static Drawable createDrawable(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    public static Drawable createDrawable(int color, float tlRadius, float trRadius, float brRadius,
                                          float blRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadii(new float[]{
                tlRadius, tlRadius,
                trRadius, trRadius,
                brRadius, brRadius,
                blRadius, blRadius});
        return drawable;
    }

}
