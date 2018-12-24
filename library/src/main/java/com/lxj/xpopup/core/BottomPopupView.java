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
import com.lxj.xpopup.widget.PopupDrawerLayout;
import com.lxj.xpopup.widget.SmartDragLayout;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    FrameLayout bottomPopupContainer;
    public BottomPopupView(@NonNull Context context) {
        super(context);
        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
//        bottomPopupContainer.setMaxHeight(getMaxHeight());
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

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
//        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
//            @Override
//            public void onClose() {
//                BottomPopupView.super.dismiss();
//            }
//        });
//        drawerLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.close();
//            }
//        });
    }

//    @Override
//    public void doShowAnimation() {
//        bottomPopupContainer.open();
//    }
//
//    @Override
//    public void doDismissAnimation() {
//        bottomPopupContainer.close();
//    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
     * @return
     */
//    @Override
//    public int getAnimationDuration() {
//        return 0;
//    }
//
//    @Override
//    public void dismiss() {
//        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
//        bottomPopupContainer.close();
//    }


    /**
     * 具体实现的类的布局
     * @return
     */
    protected int getImplLayoutId(){
        return 0;
    }

//    @Override
//    protected int getMaxHeight() {
//        return (int) (Utils.getWindowHeight(getContext()));
//    }
}
