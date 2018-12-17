package com.lxj.xpopupdemo.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class BottomPopupDemo extends BaseFragment {
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
                                .position(PopupType.Bottom)
                                .popupAnimation(datas[position])
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
