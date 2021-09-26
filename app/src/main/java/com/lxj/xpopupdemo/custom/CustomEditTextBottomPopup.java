package com.lxj.xpopupdemo.custom;

import android.content.Context;
import androidx.annotation.NonNull;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.fragment.QuickStartDemo;

/**
 * Description: 自定义带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView {
    public CustomEditTextBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_edittext_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_finish).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    ToastUtils.showShort("自定义弹窗设置了KeyListener");
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
//        Log.e("tag", "CustomEditTextBottomPopup  onDismiss");
    }

    public String getComment(){
        EditText et = findViewById(R.id.et_comment);
        return et.getText().toString();
    }

//    @Override
//    protected int getMaxHeight() {
//        return (int) (XPopupUtils.getWindowHeight(getContext())*0.75);
//    }
}
