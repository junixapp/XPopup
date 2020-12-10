package com.lxj.xpopup.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.util.FuckRomUtils;
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
        if (getWindow() == null) return;
        if (contentView != null && contentView.popupInfo.enableShowWhenAppBackground) {
            if (Build.VERSION.SDK_INT >= 26) {
                getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //处理VIVO手机8.0以上系统部分机型的状态栏问题和弹窗下移问题
        if(isFuckVIVORoom()){
            getWindow().getDecorView().setTranslationY(-XPopupUtils.getStatusBarHeight());
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, Math.max(XPopupUtils.getAppHeight(getContext()),
                    XPopupUtils.getScreenHeight(getContext())));
        }else {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, Math.max(XPopupUtils.getAppHeight(getContext()),
                    XPopupUtils.getScreenHeight(getContext())));
        }
        //设置全屏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(option);

        if(!contentView.popupInfo.isRequestFocus){
            //不获取焦点
            int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            getWindow().setFlags(flag,flag);
        }

        //remove status bar shadow
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if(contentView.popupInfo.navigationBarColor!=0)getWindow().setNavigationBarColor(contentView.popupInfo.navigationBarColor);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); //尝试兼容部分手机上的状态栏空白问题
        }
        if(Build.VERSION.SDK_INT == 19){ //解决4.4上状态栏闪烁的问题
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //隐藏导航栏
        if (!contentView.popupInfo.hasNavigationBar) {
            hideNavigationBar();
        }
        //自动设置状态色调，亮色还是暗色
        autoSetStatusBarMode();
        setContentView(contentView);
    }

    public boolean isFuckVIVORoom(){
        //vivo的X开头的8.0和8.1系统特殊，不需要处理
        boolean isXModel = android.os.Build.MODEL.contains("X") || android.os.Build.MODEL.contains("x") ;
        return FuckRomUtils.isVivo() && (Build.VERSION.SDK_INT == 26 || Build.VERSION.SDK_INT == 27) && !isXModel;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                ;
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
        if(isFuckVIVORoom()){ //VIVO的部分机型需要做特殊处理，Fuck
            event.setLocation(event.getX(), event.getY()+XPopupUtils.getStatusBarHeight());
        }
        return super.dispatchTouchEvent(event);
    }

    public void passClick(MotionEvent event) {
        if (contentView != null && contentView.getContext() instanceof Activity) {
            ((Activity) contentView.getContext()).dispatchTouchEvent(event);
        }
    }
}
