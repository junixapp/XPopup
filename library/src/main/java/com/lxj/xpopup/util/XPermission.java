package com.lxj.xpopup.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Description: copy from https://github.com/Blankj/AndroidUtilCode
 * Create by dance, at 2019/4/1
 */
public final class XPermission {

    private static XPermission sInstance;
    private Context context;
    private SimpleCallback mSimpleCallback;
    /**
     * Return whether the app can draw on top of other apps.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isGrantedDrawOverlays() {
        return Settings.canDrawOverlays(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestDrawOverlays(final SimpleCallback callback) {
        if (isGrantedDrawOverlays()) {
            if (callback != null) callback.onGranted();
            return;
        }
        mSimpleCallback = callback;
        PermissionActivity.start(context, PermissionActivity.TYPE_DRAW_OVERLAYS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void startOverlayPermissionActivity(final Activity activity, final int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        if (!isIntentAvailable(intent)) {
            launchAppDetailsSettings();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Launch the application's details settings.
     */
    public void launchAppDetailsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        if (!isIntentAvailable(intent)) return;
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Set the permissions.
     *
     * @return the single {@link XPermission} instance
     */
    public static XPermission create(Context context) {
        if(sInstance == null) return new XPermission(context);
        sInstance.context = context;
        return sInstance;
    }

    private boolean isIntentAvailable(final Intent intent) {
        return context
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    private XPermission(Context ctx) {
        sInstance = this;
        context = ctx;
    }
    private void onRequestPermissionsResult(final Activity activity) { }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static class PermissionActivity extends Activity {

        private static final String TYPE = "TYPE";
        public static final int TYPE_DRAW_OVERLAYS = 0x03;

        public static void start(final Context context, int type) {
            Intent starter = new Intent(context, PermissionActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            starter.putExtra(TYPE, type);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().alpha = 0;
            int byteExtra = getIntent().getIntExtra(TYPE, TYPE_DRAW_OVERLAYS);
           if (byteExtra == TYPE_DRAW_OVERLAYS) {
                super.onCreate(savedInstanceState);
                sInstance.startOverlayPermissionActivity(this, TYPE_DRAW_OVERLAYS);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            sInstance.onRequestPermissionsResult(this);
            finish();
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            finish();
            return true;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == TYPE_DRAW_OVERLAYS) {
                if (sInstance.mSimpleCallback == null) return;
                if (sInstance.isGrantedDrawOverlays()) {
                    sInstance.mSimpleCallback.onGranted();
                } else {
                    sInstance.mSimpleCallback.onDenied();
                }
            }
            finish();
        }
    }

    public interface SimpleCallback {
        void onGranted();

        void onDenied();
    }

}