package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {
    public CustomPartShadowPopupView(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup;
    }

    TextView text;
    @Override
    protected void onCreate() {
        super.onCreate();
        text = findViewById(R.id.text);
        Log.e("tag","CustomPartShadowPopupView onCreate");
        findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.ch).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText()+"\n 啦啦啦啦啦啦");

            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag","CustomPartShadowPopupView onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag","CustomPartShadowPopupView onDismiss");
    }
}
