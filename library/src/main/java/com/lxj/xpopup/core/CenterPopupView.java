package com.lxj.xpopup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.R;
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
        return R.layout._xpopup_center_popup_view;
    }


    TextView text;

    @Override
    protected void initPopupContent() {

        text = findViewById(R.id.text);
        text.setText("床前明月光，\n疑是地上霜；\n举头望明月，\n低头思故乡。");

        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "aa", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 限制宽高
        int maxHeight = (int) (Utils.getWindowHeight(getContext()) * 0.85f);
        int maxWidth = (int) (Utils.getWindowWidth(getContext()) * 0.8f);
        int widthSize = getPopupContentView().getMeasuredWidth();
        int heightSize = getPopupContentView().getMeasuredHeight();
        getPopupContentView().measure(MeasureSpec.makeMeasureSpec(Math.min(maxWidth, widthSize), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(Math.min(maxHeight, heightSize), MeasureSpec.EXACTLY));
    }

}
