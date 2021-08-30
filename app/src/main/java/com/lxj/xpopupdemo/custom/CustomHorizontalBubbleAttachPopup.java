package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.lxj.xpopup.core.BubbleHorizontalAttachPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.XPopupApp;

/**
 * Description: 自定义Attach弹窗，水平方向的带气泡的弹窗
 * Create by lxj, at 2019/3/13
 */
public class CustomHorizontalBubbleAttachPopup extends BubbleHorizontalAttachPopupView {
    public CustomHorizontalBubbleAttachPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_attach_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setBubbleBgColor(Color.parseColor("#4D5063"));
        setBubbleShadowSize(XPopupUtils.dp2px(getContext(), 10));
        setBubbleShadowColor(Color.BLACK);
        getPopupImplView().setBackgroundResource(0);
        findViewById(R.id.tv_zan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(XPopupApp.context, "赞", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        findViewById(R.id.tv_comment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(XPopupApp.context, "评论", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }

}
