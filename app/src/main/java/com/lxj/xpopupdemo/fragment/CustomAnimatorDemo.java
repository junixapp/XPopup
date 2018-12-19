package com.lxj.xpopupdemo.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupType;
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
            XPopup.get(getContext())
//                    .popupAnimation(datas[position])
                    .asConfirm("演示自定义动画", "当前应用的动画是一个自定义动画，类库中没有内置，效果不错呦！", null)
//                    .customAnimator(new DropAnimator())
                    .show();
        }
    };


    static class DropAnimator extends PopupAnimator{

        public DropAnimator(View target, int duration) {
            super(target, duration);
        }

        @Override
        public void initAnimator() {
            targetView.setRotation(270);
        }

        @Override
        public void animateShow() {
            targetView.animate().rotation(0).setDuration(animateDuration).start();
        }

        @Override
        public void animateDismiss() {
            targetView.animate().rotation(270).setDuration(animateDuration).start();
        }
    }
}
