package com.lxj.xpopup.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import com.lxj.xpopup.R;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * Description:
 * Create by dance, at 2018/12/12
 */
public class VerticalRecyclerView extends RecyclerView {
    public VerticalRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setupDivider(Boolean isDark){
        SmartDivider decoration = new SmartDivider(getContext(), SmartDivider.VERTICAL);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(getResources().getColor(isDark ? R.color._xpopup_list_dark_divider : R.color._xpopup_list_divider));
        drawable.setSize(10, XPopupUtils.dp2px(getContext(), .5f));
        decoration.setDrawable(drawable);
        addItemDecoration(decoration);
    }


}
