package com.lxj.xpopupdemo.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class CustomPopupDemo extends BaseFragment {
    Spinner spinner;
    TextView temp;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_animator_demo;
    }
    PopupAnimation[] datas;
    @Override
    public void init(View view) {
        spinner = view.findViewById(R.id.spinner);
        temp = view.findViewById(R.id.temp);
        temp.setText("演示如何自定义弹窗，并给自定义的弹窗应用不同的内置动画方案；你也可以为自己的弹窗编写自定义的动画。");

        datas = PopupAnimation.values();
        spinner.setAdapter(new ArrayAdapter<PopupAnimation>(getContext(), android.R.layout.simple_list_item_1, datas));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                spinner.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XPopup.get(getContext())
                                .popupAnimation(datas[position])
                                .asCustom(new CustomPopup(getContext()))
                                .show();
                    }
                },200); //确保spinner的消失动画不影响XPopup动画，可以看得更清晰

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    static class CustomPopup extends CenterPopupView{
        public CustomPopup(@NonNull Context context) {
            super(context);
        }
        @Override
        protected int getImplLayoutId() {
            return R.layout.custom_popup;
        }
        @Override
        protected void initPopupContent() {
            super.initPopupContent();
            findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

    }
}
