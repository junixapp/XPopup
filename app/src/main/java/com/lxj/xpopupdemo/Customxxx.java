package com.lxj.xpopupdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;

/**
 * Description:
 * Create by lxj, at 2019/3/15
 */
public class Customxxx extends BottomPopupView {
    public Customxxx(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.customxxx;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)listener.onConfirm();
                XPopup.get(getContext()).dismiss();
            }
        });
    }

    public OnCustomConfirmListener listener;
    public interface OnCustomConfirmListener{
        void onConfirm();
    }
}
