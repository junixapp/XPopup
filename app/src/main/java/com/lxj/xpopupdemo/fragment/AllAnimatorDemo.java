package com.lxj.xpopupdemo.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class AllAnimatorDemo extends BaseFragment {
    Spinner spinner;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_animator_demo;
    }
    PopupAnimation[] data;
    @Override
    public void init(View view) {
        spinner = view.findViewById(R.id.spinner);

        data = PopupAnimation.values();
        spinner.setAdapter(new ArrayAdapter<PopupAnimation>(getContext(), android.R.layout.simple_list_item_1, data));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                spinner.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new XPopup.Builder(getContext())
                                .popupAnimation(data[position])
                                .asConfirm("演示应用不同的动画", "你可以为弹窗选择任意一种动画，但这并不必要，因为我已经默认给每种弹窗设定了最佳动画！对于你自定义的弹窗，可以随心选择心仪的动画方案。", null)
                                .show();
                    }
                },200); //确保spinner的消失动画不影响XPopup动画，可以看得更清晰

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


}
