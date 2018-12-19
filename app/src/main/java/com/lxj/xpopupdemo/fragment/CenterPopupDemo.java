package com.lxj.xpopupdemo.fragment;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class CenterPopupDemo extends BaseFragment {
    Spinner spinner;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_popup_demo;
    }
    PopupAnimation[] datas;
    @Override
    public void init(View view) {
        spinner = view.findViewById(R.id.spinner);

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
                                .asConfirm("演示动画", "你可以为弹窗选择任意一种动画，但并不必要，因为我已经默认给每种弹窗设定了最佳动画！", null)
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
