package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView2 extends PartShadowPopupView {
    int gravity;
    public CustomPartShadowPopupView2(@NonNull Context context, int gravity) {
        super(context);
        this.gravity = gravity;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        FrameLayout.LayoutParams params = (LayoutParams) findViewById(R.id.ll).getLayoutParams();
        params.gravity = gravity;
    }
}
