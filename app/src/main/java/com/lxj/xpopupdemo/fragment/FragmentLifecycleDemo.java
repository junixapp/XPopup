package com.lxj.xpopupdemo.fragment;

import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopupdemo.DemoActivity;
import com.lxj.xpopupdemo.R;

/**
 * 演示传入Fragment的Lifecycle，当Fragment销毁时，弹窗自动销毁，无内存泄漏
 */
public class FragmentLifecycleDemo extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lifecycle_demo;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .customHostLifecycle(getLifecycle())
                        .setPopupCallback(new SimpleCallback(){
                            @Override
                            public void onDismiss(BasePopupView popupView) {

                            }
                        })
                        .asConfirm("演示自定义UI生命周期", "3秒后当前Fragment会被销毁，弹窗也自动销毁，避免内存泄漏", () -> {

                }).show();
                ((DemoActivity)getActivity()).delayDestroy();
            }
        });
    }

}
