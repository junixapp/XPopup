package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lxj.xpopup.R;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    FrameLayout bottomPopupContainer;
    public BottomPopupView(@NonNull Context context) {
        super(context);
        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), bottomPopupContainer, false);
        bottomPopupContainer.addView(contentView);
    }

    public BottomPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_bottom_popup_view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 限制宽高
        int maxHeight = (int) (Utils.getWindowHeight(getContext()) * 0.85f);
        int heightSize = getPopupContentView().getMeasuredHeight();
        getPopupContentView().measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(Math.min(maxHeight, heightSize), MeasureSpec.EXACTLY));
    }


    /**
     * 具体实现的类的布局
     * @return
     */
    protected int getImplLayoutId(){
        return 0;
    }
}
