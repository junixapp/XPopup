package com.lxj.xpopup.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.R;
import com.lxj.xpopup.core.AttachPopupView;

import java.util.ArrayList;

/**
 * Description:
 * Create by dance, at 2018/12/12
 */
public class ListPopupView extends AttachPopupView {
    RecyclerView recyclerView;
    public ListPopupView(@NonNull Context context) {
        super(context);
    }

    public ListPopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_attach_impl_list;
    }

    @Override
    protected void initPopupContent() {


        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<String> data = new ArrayList<>();
        data.add("添加说说啊啊啊");
        data.add("删除");
        data.add("分享");
        data.add("举报");
        CommonAdapter<String> adapter = new CommonAdapter<String>(R.layout._xpopup_adapter_text, data){
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.text, s);
            }
        };
        recyclerView.setAdapter(adapter);
        super.initPopupContent();
    }
}
