package com.lxj.xpopup.core;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.util.FuckRomUtils;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;

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
        if (getWindow() == null  || contentView==null || contentView.popupInfo==null) return;
        if (contentView.popupInfo.enableShowWhenAppBackground) {
            if (Build.VERSION.SDK_INT >= 26) {
                getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }

        if(contentView.popupInfo.keepScreenOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        getWindow().setBackgroundDrawable(null);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置全屏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(option);

        //remove status bar shadow
        if(Build.VERSION.SDK_INT == 19){  //解决4.4上状态栏闪烁的问题
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if (Build.VERSION.SDK_INT == 20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }else if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int navigationBarColor = getNavigationBarColor();
            if(navigationBarColor!=0) getWindow().setNavigationBarColor(navigationBarColor);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); //尝试兼容部分手机上的状态栏空白问题
        }

        if(!contentView.popupInfo.isRequestFocus){//不获取焦点
            int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            if(contentView.popupInfo.isCoverSoftInput){
                flag |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
            }
            setWindowFlag(flag, true);
        }else if(contentView.popupInfo.isCoverSoftInput){
            setWindowFlag(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, true);
        }

        setStatusBarLightMode();
        setNavBarLightMode();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //处理VIVO手机8.0以上系统部分机型的状态栏问题和弹窗下移问题
        boolean isPortrait = getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
        if(isFuckVIVORoom() && isPortrait){
            getWindow().getDecorView().setTranslationY(-XPopupUtils.getStatusBarHeight());
            getWindow().setLayout(XPopupUtils.getAppWidth(getContext()), Math.max(XPopupUtils.getAppHeight(getContext()),
                    XPopupUtils.getScreenHeight(getContext())));
        }
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.height = getWindow().getAttributes().height;
//        setContentView(contentView, layoutParams);
        initListener();
        setContentView(contentView);

    }

    private int getNavigationBarColor(){
        return contentView.popupInfo.navigationBarColor==0 ? XPopup.getNavigationBarColor()
                : contentView.popupInfo.navigationBarColor;
    }

    private void initListener() {
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (!contentView.popupInfo.hasNavigationBar) {
                    hideNavigationBar();
                }
                if (!contentView.popupInfo.hasStatusBar) {
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        });
    }

    public boolean isFuckVIVORoom(){
        //vivo的Y和V开头的8.0和8.1系统特殊(y91 y85 y97)：dialog无法覆盖到状态栏，并且坐标系下移了一个状态栏的距离
        boolean isFuckModel = android.os.Build.MODEL.startsWith("Y")
                || android.os.Build.MODEL.startsWith("y")
                || android.os.Build.MODEL.startsWith("V")
                || android.os.Build.MODEL.startsWith("v");
        return FuckRomUtils.isVivo() && (Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27) && isFuckModel;
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

    private void setStatusBarLightMode() {
        //隐藏状态栏
        if (!contentView.popupInfo.hasStatusBar) {
            final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_FULLSCREEN;
            getWindow().getDecorView().setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
            return;
        }
        int light = contentView.popupInfo.isLightStatusBar == 0 ? XPopup.isLightStatusBar : contentView.popupInfo.isLightStatusBar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && light!=0) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (light > 0 ) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
            getWindow().setStatusBarColor(contentView.popupInfo.statusBarBgColor);
        }
    }

    /**
     * copy from AndroidUtilCode/BarUtils
     */
    public void hideNavigationBar() {
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
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
    }

    private  String getResNameById(int id) {
        try {
            return getContext().getResources().getResourceEntryName(id);
        } catch (Exception ignore) {
            return "";
        }
    }

    public void setNavBarLightMode() {
        //隐藏导航栏
        if (!contentView.popupInfo.hasNavigationBar) {
            hideNavigationBar();
        }
        int light = contentView.popupInfo.isLightNavigationBar == 0 ? XPopup.isLightNavigationBar : contentView.popupInfo.isLightNavigationBar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && light!=0) {
            View decorView = getWindow().getDecorView();
            int vis = decorView.getSystemUiVisibility();
            if (light > 0) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    BasePopupView contentView;
    public FullScreenDialog setContent(BasePopupView view) {
        if(view.getParent()!=null){
            ((ViewGroup)view.getParent()).removeView(view);
        }
        this.contentView = view;
        return this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setStatusBarLightMode();
        setNavBarLightMode();
        if(hasFocus && contentView!=null && contentView.hasMoveUp && contentView.popupStatus== PopupStatus.Show){
            contentView.focusAndProcessBackPress();
            KeyboardUtils.showSoftInput(contentView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(isFuckVIVORoom()){ //VIVO的部分机型需要做特殊处理，Fuck
            event.setLocation(event.getX(), event.getY()+XPopupUtils.getStatusBarHeight());
        }
        return super.dispatchTouchEvent(event);
    }

}
