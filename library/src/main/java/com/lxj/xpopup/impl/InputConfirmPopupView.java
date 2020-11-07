package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description: 带输入框，确定和取消的对话框
 * Create by dance, at 2018/12/16
 */
public class InputConfirmPopupView extends ConfirmPopupView implements View.OnClickListener {

    /**
     * @param context
     * @param bindLayoutId 在Confirm弹窗基础上需要增加一个id为et_input的EditText
     */
    public InputConfirmPopupView(@NonNull Context context, int bindLayoutId) {
        super(context, bindLayoutId);
    }

    public CharSequence inputContent;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        et_input.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(hint)) {
            et_input.setHint(hint);
        }
        if (!TextUtils.isEmpty(inputContent)) {
            et_input.setText(inputContent);
            et_input.setSelection(inputContent.length());
        }

        XPopupUtils.setCursorDrawableColor(et_input, XPopup.getPrimaryColor());
        et_input.post(new Runnable() {
            @Override
            public void run() {
                BitmapDrawable defaultDrawable = XPopupUtils.createBitmapDrawable(getResources(), et_input.getMeasuredWidth(), Color.parseColor("#888888"));
                BitmapDrawable focusDrawable = XPopupUtils.createBitmapDrawable(getResources(), et_input.getMeasuredWidth(), XPopup.getPrimaryColor());
                et_input.setBackgroundDrawable(XPopupUtils.createSelector(defaultDrawable, focusDrawable));
            }
        });
    }

    public EditText getEditText() {
        return et_input;
    }

    protected void applyLightTheme() {
        super.applyLightTheme();
        et_input.setHintTextColor(Color.parseColor("#888888"));
        et_input.setTextColor(Color.parseColor("#333333"));
    }
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        et_input.setHintTextColor(Color.parseColor("#888888"));
        et_input.setTextColor(Color.parseColor("#dddddd"));
    }

    OnCancelListener cancelListener;
    OnInputConfirmListener inputConfirmListener;

    public void setListener(OnInputConfirmListener inputConfirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.inputConfirmListener = inputConfirmListener;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        } else if (v == tv_confirm) {
            if (inputConfirmListener != null)
                inputConfirmListener.onConfirm(et_input.getText().toString().trim());
            if (popupInfo.autoDismiss) dismiss();
        }
    }
}
