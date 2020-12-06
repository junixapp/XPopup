package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopupdemo.R;

public class LoginPopup extends CenterPopupView {
    public LoginPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_login;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Button button = findViewById(R.id.btnSelect);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .hasShadowBg(false)
                        .isRequestFocus(false)
                        .atView(v)
                        .asAttachList(new String[]{"1", "2", "3", "4"}, null, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {

                    }
                }).show();
            }
        });
    }
}
