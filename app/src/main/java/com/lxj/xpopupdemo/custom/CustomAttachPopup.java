package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.core.HorizontalAttachPopupView;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by lxj, at 2019/3/13
 */
public class CustomAttachPopup extends HorizontalAttachPopupView {
    public CustomAttachPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_zan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("赞");
            }
        });
        findViewById(R.id.tv_comment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("评论");
            }
        });
    }
}
