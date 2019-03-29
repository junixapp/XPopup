package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.lxj.xpopup.R;
import com.lxj.xpopup.util.XPopupUtils;

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

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_center_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight());
    }

    @Override
    protected void applyOffset() {
        getPopupContentView().setTranslationX(popupInfo.offsetX);
        getPopupContentView().setTranslationY(popupInfo.offsetY);
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
        return popupInfo.maxWidth==0 ? (int) (XPopupUtils.getWindowWidth(getContext()) * 0.86f)
                : popupInfo.maxWidth;
    }

}
