package com.lxj.xpopup.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;

/**
 * 所有弹窗的宿主
 */
public class FullScreenDialog extends Dialog {
    public FullScreenDialog(@NonNull Context context) {
        super(context, R.style._XPopup_TransparentDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() == null) return;
        if (contentView != null && contentView.popupInfo.enableShowWhenAppBackground) {
            if (Build.VERSION.SDK_INT >= 26) {
                getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //remove status bar shadow
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        //隐藏导航栏
        if (!contentView.popupInfo.hasNavigationBar) {
            hideNavigationBar();
        }
        //自动设置状态色调，亮色还是暗色
        autoSetStatusBarMode();
        setContentView(contentView);
    }

    public boolean isActivityStatusBarLightMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = ((Activity) contentView.getContext()).getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            return (vis & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
        }
        return false;
    }

    public void setWindowFlag(final int bits, boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }

    /**
     * 是否是亮色调状态栏
     *
     * @return
     */
    public void autoSetStatusBarMode() {
        //隐藏状态栏
        if (!contentView.popupInfo.hasStatusBar) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            boolean isLightMode = isActivityStatusBarLightMode();
            if (isLightMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getResNameById(id);
                if ("navigationBarBackground".equals(resourceEntryName)) {
                    child.setVisibility(View.INVISIBLE);
                }
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ;
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
    }
    private  String getResNameById(int id) {
        try {
            return getContext().getResources().getResourceEntryName(id);
        } catch (Exception ignore) {
            return "";
        }
    }

    BasePopupView contentView;

    public FullScreenDialog setContent(BasePopupView view) {
        this.contentView = view;
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (contentView!=null && contentView.getContext() instanceof Activity){
//            ((Activity) contentView.getContext()).dispatchTouchEvent(event);
//        }
        return super.dispatchTouchEvent(event);
    }

    public void passClick(MotionEvent event) {
        if (contentView != null && contentView.getContext() instanceof Activity) {
            ((Activity) contentView.getContext()).dispatchTouchEvent(event);
        }
    }
}
