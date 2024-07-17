package com.lxj.xpopup.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.FloatRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.lxj.xpopup.R;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.BubbleAttachPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.core.PositionPopupView;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class XPopupUtils {

    //应用界面可见高度，可能不包含导航和状态栏，看Rom实现
    public static int getAppHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static int getAppWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    //屏幕的高度，包含状态栏，导航栏，看Rom实现
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(Window window) {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    public static int getNavBarHeight(Window window) {
        if(!isNavBarVisible(window)) return 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && window!=null) {
            View view = window.findViewById(android.R.id.navigationBarBackground);
            if (view == null) return 0;
            return view.getVisibility()==View.VISIBLE ? view.getHeight() : 0;
        }else {
            Resources res = Resources.getSystem();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId != 0) {
                return res.getDimensionPixelSize(resourceId);
            } else {
                return 0;
            }
        }
    }

    public static int getActionBarHeight(Context context) {
        Activity activity = context2Activity(context);
        if(activity==null) return 0;
        if(activity instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            return supportActionBar==null ? 0 : supportActionBar.getHeight();
        }
        ActionBar actionBar = activity.getActionBar();
        return actionBar==null ? 0 : actionBar.getHeight();
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
        content.post(() -> {
            ViewGroup.LayoutParams params = content.getLayoutParams();
            if(content==null) return;
            View implView = content.getChildAt(0);
            if(implView==null) return;
            ViewGroup.LayoutParams implParams = implView.getLayoutParams();
            // 假设默认Content宽是match，高是wrap
            int w = content.getMeasuredWidth();
            // response impl view wrap_content params.
            if (maxWidth > 0) {
                //指定了最大宽度，就限制最大宽度
                if(w > maxWidth) params.width = Math.min(w, maxWidth);
                if (implParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    implParams.width = Math.min(w, maxWidth);
                    if (implParams instanceof ViewGroup.MarginLayoutParams) {
                        ViewGroup.MarginLayoutParams mp = ((ViewGroup.MarginLayoutParams) implParams);
                        implParams.width = implParams.width - mp.leftMargin - mp.rightMargin;
                    }
                }
                if (popupWidth > 0) {
                    params.width = Math.min(popupWidth, maxWidth);
                    implParams.width = Math.min(popupWidth, maxWidth);
                }
            } else if (popupWidth > 0) {
                params.width = popupWidth;
                implParams.width = popupWidth;
            }

            if (maxHeight > 0) {
                int h = content.getMeasuredHeight();
                if(h > maxHeight) params.height = Math.min(h, maxHeight);
                if (popupHeight > 0) {
                    params.height = Math.min(popupHeight, maxHeight);
                    implParams.height = Math.min(popupHeight, maxHeight);
                }
            } else if (popupHeight > 0) {
                params.height = popupHeight;
                implParams.height = popupHeight;
            } else {
//                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                implParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            implView.setLayoutParams(implParams);
            content.setLayoutParams(params);
            content.post(() -> {
                if (afterApplySize != null) {
                    afterApplySize.run();
                }
            });

        });
    }

    public static void setCursorDrawableColor(EditText et, int color) {
        //暂时没有找到有效的方法来动态设置cursor的颜色
    }

    public static BitmapDrawable createBitmapDrawable(Context context, int width, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, dp2px(context, 1.5f), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
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
        if (window == null) return 0;
        final View decorView = window.getDecorView();
        if (decorView == null) return 0;
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight(window)) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    //监听到的keyboardHeight有一定几率是错误的，比如在同时显示导航栏和弹出输入法的时候，有一定几率会算上导航栏的高度，
    //这个不是必现的，暂时无解
    private static int preKeyboardHeight = 0;

    public static void moveUpToKeyboard(final int keyboardHeight, final BasePopupView pv) {
        preKeyboardHeight = keyboardHeight;
        pv.post(new Runnable() {
            @Override
            public void run() {
                moveUpToKeyboardInternal(preKeyboardHeight, pv);
            }
        });
    }

    private static void moveUpToKeyboardInternal(int keyboardHeight, BasePopupView pv) {
        if (pv.popupInfo == null || !pv.popupInfo.isMoveUpToKeyboard) return;
        //暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView || pv instanceof AttachPopupView || pv instanceof BubbleAttachPopupView) {
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
        //执行上移的逻辑
        if (pv instanceof FullScreenPopupView || pv instanceof DrawerPopupView) {
            int overflowHeight = (int) ((focusBottom + keyboardHeight) - screenHeight
                    - pv.getPopupContentView().getTranslationY());
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        } else if (pv instanceof CenterPopupView) {
            int popupBottom = (screenHeight + popupHeight) / 2;
            int targetY = popupBottom + keyboardHeight - screenHeight;
            if (focusEt != null && focusEtTop - targetY < 0) {
//                targetY += focusEtTop - targetY /*- getStatusBarHeight()*/;//限制不能被状态栏遮住
            }
            dy = Math.max(0, targetY);
        } else if (pv instanceof BottomPopupView) {
            dy = keyboardHeight;
        } else if (pv instanceof PartShadowPopupView) {
            int overflowHeight = (int) ((focusBottom + keyboardHeight) - screenHeight
                    - pv.getPopupContentView().getTranslationY());
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        }
        int animDuration = 180;
        pv.getPopupContentView().animate().translationY(-dy)
                .setDuration(animDuration)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .start();
    }

    public static void moveDown(BasePopupView pv) {
        //暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView || pv instanceof AttachPopupView || pv instanceof BubbleAttachPopupView)
            return;
        if (pv instanceof FullScreenPopupView && pv.getPopupContentView().hasTransientState()) {
            //如果正在执行动画，则不下移
            return;
        }
        pv.getPopupContentView().animate().translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(100).start();
    }

    public static boolean isNavBarVisible(Window window) {
        if (window == null) return false;
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        if (decorView == null) return false;
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                try {
                    String resourceEntryName = window.getContext().getResources().getResourceEntryName(id);
                    if ("navigationBarBackground".equals(resourceEntryName)
                            && child.getVisibility() == View.VISIBLE) {
                        isVisible = true;
                        break;
                    }
                } catch (Resources.NotFoundException e) {
                    break;
                }
            }
        }
        if (isVisible) {
            // 对于三星手机，android10以下非OneUI2的版本，比如 s8，note8 等设备上，
            // 导航栏显示存在bug："当用户隐藏导航栏时显示输入法的时候导航栏会跟随显示"，会导致隐藏输入法之后判断错误
            // 这个问题在 OneUI 2 & android 10 版本已修复
            if (FuckRomUtils.isSamsung()
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    return Settings.Global.getInt(window.getContext().getContentResolver(), "navigationbar_hide_bar_enabled") == 0;
                } catch (Exception ignore) {
                }
            }

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

    public static void saveBmpToAlbum(final Context context, final XPopupImageLoader imageLoader, final Object uri) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File source = imageLoader.getImageFile(context, uri);
                if (source == null) {
                    showToast(context, context.getString(R.string.xpopup_image_not_exist));
                    return;
                }
                try {
                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), context.getPackageName());
                    if (!dir.exists()) dir.mkdirs();
                    File destFile = new File(dir, System.currentTimeMillis() + "." + getImageType(source));
                    if (Build.VERSION.SDK_INT < 29) {
                        if (destFile.exists()) destFile.delete();
                        destFile.createNewFile();
                        //android10以下直接insertImage
                        try (OutputStream out = new FileOutputStream(destFile)) {
                            writeFileFromIS(out, new FileInputStream(source));
                        }
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.parse("file://" + destFile.getAbsolutePath()));
                        context.sendBroadcast(intent);
                    } else {
                        //android10以上，增加了新字段，自己insert，因为RELATIVE_PATH，DATE_EXPIRES，IS_PENDING是29新增字段
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, destFile.getName());
                        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                        Uri contentUri;
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else {
                            contentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                        }
                        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/" + context.getPackageName());
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);
                        Uri uri = context.getContentResolver().insert(contentUri, contentValues);
                        if (uri == null) {
                            showToast(context, context.getString(R.string.xpopup_saved_fail));
                            return;
                        }

                        ContentResolver resolver = context.getContentResolver();
                        try (OutputStream out = resolver.openOutputStream(uri)) {
                            writeFileFromIS(out, new FileInputStream(source));
                        }
                        // Everything went well above, publish it!
                        contentValues.clear();
                        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
//                            contentValues.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
                        resolver.update(uri, contentValues, null, null);
                    }
                    showToast(context, context.getString(R.string.xpopup_saved_to_gallery));
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(context, context.getString(R.string.xpopup_saved_fail));
                }
            }
        });
    }

    private static void showToast(final Context context, final String text) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static boolean writeFileFromIS(final OutputStream fos, final InputStream is) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(fos);
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

    public static Bitmap view2Bitmap(final View view) {
        return view2Bitmap(view, -1, 1);
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view, int clipHeight, int scale) {
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
                bitmap = Bitmap.createBitmap(drawingCache, 0,0,drawingCache.getWidth(),clipHeight>0 ?clipHeight: drawingCache.getHeight());
            } else {
                bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), clipHeight>0 ?clipHeight: view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            }
        } else {
            bitmap = Bitmap.createBitmap(drawingCache,0,0,drawingCache.getWidth(),clipHeight>0 ?clipHeight: drawingCache.getHeight());
        }
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        Bitmap small = Bitmap.createScaledBitmap(bitmap, view.getMeasuredWidth() / scale, view.getMeasuredHeight() / scale, true);
        if (!bitmap.isRecycled() && bitmap!=small ) bitmap.recycle();
        return small;
    }

    public static boolean isLayoutRtl(Context context) {
        Locale primaryLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            primaryLocale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            primaryLocale = context.getResources().getConfiguration().locale;
        }
        return TextUtils.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL;
    }

    public static Activity context2Activity(Context ctx) {
        Context context = ctx;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context);
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
    public static Activity context2Activity(View view) {
        return context2Activity(view.getContext());
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

    public static boolean hasSetKeyListener(View view) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObj = listenerInfoMethod.invoke(view);
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            Field mOnKeyListenerField = listenerInfoClazz.getDeclaredField("mOnKeyListener");
            if (!mOnKeyListenerField.isAccessible()) {
                mOnKeyListenerField.setAccessible(true);
            }
            Object keyListener = mOnKeyListenerField.get(listenerInfoObj);
            return keyListener != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static int calculateInSampleSize(final BitmapFactory.Options options,
                                            final int maxWidth,
                                            final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int[] getImageSize(File file) {
        if (file == null) return new int[]{0, 0};
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    public static String getImageType(final File file) {
        if (file == null) return "";
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] bytes = new byte[12];
            if (is.read(bytes) != -1) {
                String type = bytes2HexString(bytes, true).toUpperCase();
                if (type.contains("FFD8FF")) {
                    return "jpg";
                } else if (type.contains("89504E47")) {
                    return "png";
                } else if (type.contains("47494638")) {
                    return "gif";
                } else if (type.contains("49492A00") || type.contains("4D4D002A")) {
                    return "tiff";
                } else if (type.contains("424D")) {
                    return "bmp";
                } else if (type.startsWith("52494646") && type.endsWith("57454250")) {//524946461c57000057454250-12个字节
                    return "webp";
                } else if (type.contains("00000100") || type.contains("00000200")) {
                    return "ico";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static final char[] HEX_DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
        if (bytes == null) return "";
        char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


    public static int getRotateDegree(final String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            );
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap rotate(final Bitmap src,
                                final int degrees,
                                final float px,
                                final float py) {
        if (degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return ret;
    }

    public static Rect getViewRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void setVisible(View view, boolean isVisible) {
        if (view != null) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
