package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.lxj.xpopup.R;
import com.lxj.xpopup.util.Utils;

/**
 * Description:
 * Create by dance, at 2018/12/11
 */
public class AttachPopupView extends BasePopupView {
    int defaultOffsetY = 6;
    public AttachPopupView(@NonNull Context context) {
        super(context);
        defaultOffsetY = Utils.dp2px(context, defaultOffsetY);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.xpopup_attach_popup_view;
    }

    @Override
    protected void initPopup() {
        super.initPopup();

        if(popupInfo.getAtView()==null)throw new IllegalArgumentException("atView must not be null for AttachView type！");


        post(new Runnable() {
            @Override
            public void run() {
                //1. 获取atView在屏幕上的位置
                int[] locations = new int[2];
                popupInfo.getAtView().getLocationOnScreen(locations);

                Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                        locations[1] + popupInfo.getAtView().getMeasuredHeight());
                // 弹窗显示的位置不能超越状态栏和导航栏，隐藏需要减去2个高度
                int minY = Utils.getStatusBarHeight();
                int maxY = Utils.getWindowHeight(getContext()) - Utils.getNavBarHeight();
//                int maxX = Utils.getWindowWidth(getContext())- getPopupContentView().getMeasuredWidth();
                int maxX = rect.right - getPopupContentView().getMeasuredWidth() ;
                Log.e("tag", "rect: "+ rect.toString() + " minY: "+ minY + " maxY: "+maxY
                );
                if( (rect.top - minY) > (maxY - rect.bottom) ){
                    //说明上面的空间比较大，应显示在atView上方
                    getPopupContentView().setTranslationX(Math.min(rect.left, maxX));
                    getPopupContentView().setTranslationY(rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY);
                }else {
                    // 应该显示在atView下方
                    getPopupContentView().setTranslationX(Math.min(rect.left, maxX));
                    getPopupContentView().setTranslationY(rect.bottom + defaultOffsetY);

                }

            }
        });

    }
}
