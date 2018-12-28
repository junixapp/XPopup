package com.lxj.xpopupdemo.custompopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;

/**
 * Description: 仿知乎底部评论弹窗
 * Create by dance, at 2018/12/25
 */
public class ZhihuCommentPopup extends BottomPopupView {
    VerticalRecyclerView recyclerView;
    public ZhihuCommentPopup(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bottom_popup;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.recyclerView);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            strings.add("");
        }
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(R.layout.adapter_zhihu_comment, strings) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {}
        };
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                dismiss();
            }
        });
        recyclerView.setAdapter(commonAdapter);
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }
}