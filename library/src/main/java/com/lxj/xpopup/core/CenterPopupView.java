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
 * Description: 在中间显示的Popup
 * Create by dance, at 2018/12/8
 */
public class CenterPopupView extends BasePopupView {
    protected FrameLayout centerPopupContainer;

    public CenterPopupView(@NonNull Context context) {
        super(context);

        centerPopupContainer = findViewById(R.id.centerPopupContainer);
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), centerPopupContainer, false);
        centerPopupContainer.addView(contentView);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_center_popup_view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            // popupContent限制宽高
            if (child == getPopupContentView()) {
                measureChild(child, MeasureSpec.makeMeasureSpec(Math.min(getMaxWidth(), width), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(Math.min(getMaxHeight(), height), MeasureSpec.EXACTLY));
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 具体实现的类的布局
     *
     * @return
     */
    protected int getImplLayoutId() {
        return 0;
    }

    protected int getMaxWidth() {
        return (int) (Utils.getWindowWidth(getContext()) * 0.86f);
    }

    protected int getMaxHeight() {
        return (int) (Utils.getWindowHeight(getContext()) * 0.85f);
    }

}
