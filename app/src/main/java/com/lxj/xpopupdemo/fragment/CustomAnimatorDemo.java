package com.lxj.xpopupdemo.fragment;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class CustomAnimatorDemo extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_animator_demo;
    }

    public void init(View view) {
        view.findViewById(R.id.btn_show).setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new XPopup.Builder(getContext())
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .customAnimator(new RotateAnimator())
                    .asConfirm("演示自定义动画", "当前的动画是一个自定义的旋转动画，无论是自定义弹窗还是自定义动画，已经被设计得非常简单；这个动画代码只有6行即可完成！", null)
                    .show();
        }
    };


    static class RotateAnimator extends PopupAnimator{
        @Override
        public void initAnimator() {
            targetView.setScaleX(0);
            targetView.setScaleY(0);
            targetView.setAlpha(0);
            targetView.setRotation(360);
        }
        @Override
        public void animateShow() {
            targetView.animate().rotation(0).scaleX(1).scaleY(1).alpha(1).setInterpolator(new FastOutSlowInInterpolator()).setDuration(340)
                    .start();
        }
        @Override
        public void animateDismiss() {
            targetView.animate().rotation(720).scaleX(0).scaleY(0).alpha(0).setInterpolator(new FastOutSlowInInterpolator()).setDuration(340)
                    .start();
        }
    }

}
