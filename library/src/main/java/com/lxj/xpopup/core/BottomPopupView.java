package com.lxj.xpopup.core;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.SmartDragLayout;

/**
 * Description: 在底部显示的Popup
 * Create by lxj, at 2018/12/11
 */
public class BottomPopupView extends BasePopupView {
    protected SmartDragLayout bottomPopupContainer;
    public BottomPopupView(@NonNull Context context) {
        super(context);
        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
    }

    protected void addInnerContent(){
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), bottomPopupContainer, false);
        bottomPopupContainer.addView(contentView);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_bottom_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if(bottomPopupContainer.getChildCount()==0){
            addInnerContent();
        }
        bottomPopupContainer.setDuration(getAnimationDuration());
        bottomPopupContainer.enableDrag(popupInfo.enableDrag);
        if(popupInfo.enableDrag) {
            popupInfo.popupAnimation = null;
            getPopupImplView().setTranslationX(popupInfo.offsetX);
            getPopupImplView().setTranslationY(popupInfo.offsetY);
        }else {
            getPopupContentView().setTranslationX(popupInfo.offsetX);
            getPopupContentView().setTranslationY(popupInfo.offsetY);
        }
        bottomPopupContainer.dismissOnTouchOutside(popupInfo.isDismissOnTouchOutside);
        bottomPopupContainer.isThreeDrag(popupInfo.isThreeDrag);


        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight()
                , getPopupWidth(), getPopupHeight(), null);

        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                beforeDismiss();
                if(popupInfo!=null && popupInfo.xPopupCallback!=null) popupInfo.xPopupCallback.beforeDismiss(BottomPopupView.this);
                doAfterDismiss();
            }

            @Override
            public void onDrag(int value, float percent, boolean isScrollUp) {
                if(popupInfo==null)return;
                if(popupInfo.xPopupCallback!=null) popupInfo.xPopupCallback.onDrag(BottomPopupView.this, value, percent,isScrollUp);
                if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg) setBackgroundColor(shadowBgAnimator.calculateBgColor(percent));
            }

            @Override
            public void onOpen() { }
        });

        bottomPopupContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupInfo!=null){
                    if(popupInfo.xPopupCallback!=null){
                        popupInfo.xPopupCallback.onClickOutside(BottomPopupView.this);
                    }
                    if(popupInfo.isDismissOnTouchOutside!=null){
                        dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void doShowAnimation() {
        if(popupInfo==null) return;
        if(popupInfo.enableDrag){
            if (popupInfo.hasBlurBg && blurAnimator!=null) {
                blurAnimator.animateShow();
            }
            bottomPopupContainer.open();
        }else {
            super.doShowAnimation();
        }
    }

    @Override
    public void doDismissAnimation() {
        if(popupInfo==null) return;
        if(popupInfo.enableDrag){
            if(popupInfo.hasBlurBg && blurAnimator!=null){
                blurAnimator.animateDismiss();
            }
            bottomPopupContainer.close();
        }else {
            super.doDismissAnimation();
        }
    }

    protected void doAfterDismiss() {
        if(popupInfo==null) return;
        if(popupInfo.enableDrag){
            if (popupInfo.autoOpenSoftInput)
                KeyboardUtils.hideSoftInput(this);
            handler.removeCallbacks(doAfterDismissTask);
            handler.postDelayed(doAfterDismissTask, 0);
        }else {
            super.doAfterDismiss();
        }
    }

    private TranslateAnimator translateAnimator ;
    @Override
    protected PopupAnimator getPopupAnimator() {
        if(popupInfo==null) return null;
        if(translateAnimator==null) translateAnimator = new TranslateAnimator(getPopupContentView(), getAnimationDuration(),
                PopupAnimation.TranslateFromBottom);
        return popupInfo.enableDrag ? null : translateAnimator;
    }

    @Override
    public void dismiss() {
        if(popupInfo==null) return;
        if(popupInfo.enableDrag){
            if (popupStatus == PopupStatus.Dismissing) return;
            popupStatus = PopupStatus.Dismissing;
            if (popupInfo.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
            clearFocus();
            bottomPopupContainer.close();
        }else {
            super.dismiss();
        }
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
        return popupInfo.maxWidth == 0 ? XPopupUtils.getAppWidth(getContext())
                : popupInfo.maxWidth;
    }

    @Override
    protected void onDetachedFromWindow() {
        if(popupInfo!=null && !popupInfo.enableDrag && translateAnimator!=null){
            getPopupContentView().setTranslationX(translateAnimator.startTranslationX);
            getPopupContentView().setTranslationY(translateAnimator.startTranslationY);
            translateAnimator.hasInit = true;
        }
        super.onDetachedFromWindow();
    }
}
