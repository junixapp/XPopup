package com.lxj.xpopupdemo.custompopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {
    View container;
    public CustomPartShadowPopupView(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        container = findViewById(R.id.container);
        findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(isBottomGravity){
            FrameLayout.LayoutParams params = (LayoutParams) container.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            container.setLayoutParams(params);
        }
    }

    boolean isBottomGravity = false;
    public CustomPartShadowPopupView setBottomGravity(){
        isBottomGravity = true;
        return this;
    }

}
