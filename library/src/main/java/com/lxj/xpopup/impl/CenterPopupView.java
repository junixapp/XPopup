package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.R;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAnimator;
import com.lxj.xpopup.util.Utils;

/**
 * Description: 在中间显示的Popup
 * Create by dance, at 2018/12/8
 */
public class CenterPopupView extends BasePopupView {
    public CenterPopupView(@NonNull Context context) {
        super(context);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.xpopup_center_popup_view;
    }


    TextView text;

    @Override
    protected void initPopup() {
        super.initPopup();

        text = findViewById(R.id.text);
        text.setText("床前明月光，\n疑是地上霜；\n举头望明月，\n低头思故乡。");

        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "aa", Toast.LENGTH_LONG).show();
            }
        });

        Log.e("tag", "init Popiup");
    }

    /**
     * 限制内容的宽高，Center类型的弹窗宽高都有限制，宽高最大为window的90%
     */
    @Override
    protected void applyWidthAndHeight() {
        post(new Runnable() {
            @Override
            public void run() {
                int maxHeight = (int) (Utils.getWindowHeight(getContext()) * 0.85f);
                int maxWidth = (int) (Utils.getWindowWidth(getContext()) * 0.8f);
                Utils.limitWidthAndHeight(getPopupContentView(), maxWidth, maxHeight);
            }
        });
    }

}
