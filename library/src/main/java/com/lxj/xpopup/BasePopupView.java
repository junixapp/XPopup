package com.lxj.xpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class BasePopupView extends FrameLayout {
    public BasePopupView(@NonNull Context context) {
        this(context, null);
        init();
    }

    public BasePopupView( @NonNull Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public BasePopupView( @NonNull Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
//        inflate(getContext(), get)
    }
}
