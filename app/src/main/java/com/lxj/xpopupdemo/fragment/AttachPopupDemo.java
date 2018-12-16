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
public class AttachPopupDemo extends BaseFragment {
    Spinner spinner;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attach_demo;
    }
    PopupAnimation[] datas;
    @Override
    public void init(View view) {

        view.findViewById(R.id.text1).setOnClickListener(listener);
        view.findViewById(R.id.text2).setOnClickListener(listener);
        view.findViewById(R.id.text3).setOnClickListener(listener);
        view.findViewById(R.id.text4).setOnClickListener(listener);
        view.findViewById(R.id.text5).setOnClickListener(listener);


    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            XPopup.get()
//                    .popupAnimation(datas[position])
                    .hasShadowBg(false)
                    .atView(v)
                    .show(getContext());
        }
    };
}
