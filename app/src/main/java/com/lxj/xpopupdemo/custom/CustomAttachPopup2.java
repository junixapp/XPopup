package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.R;

/**
 * Description: 自定义背景的Attach弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup2 extends AttachPopupView {
    public CustomAttachPopup2(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        final TextView tv = findViewById(R.id.tv);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                tv.setText(tv.getText() + "\n 啊哈哈哈啊哈");
//                tv.setText("\n 啊哈哈哈啊哈");
            }
        });


    }

    @Override
    public void doAttach() {
        super.doAttach();
        ToastUtils.showShort(isShowUpToTarget() ? "显示在上" : "显示在下");
    }

//    @Override
//    protected int getPopupWidth() {
//        return XPopupUtils.getWindowWidth(getContext());
//    }

}
