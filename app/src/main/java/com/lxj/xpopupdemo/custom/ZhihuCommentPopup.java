package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.DemoActivity;
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
        for (int i = 0; i < 15; i++) {
            strings.add("");
        }
        final CommonAdapter<String> commonAdapter = new CommonAdapter<String>(R.layout.adapter_zhihu_comment, strings) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {}
        };
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //不要直接这样做，会导致消失动画未执行完就跳转界面，不流畅。可以将消失后的逻辑移到onDismiss回调方法中
//                dismiss();
//                getContext().startActivity(new Intent(getContext(), DemoActivity.class))
//                dismiss();
                XPopup.get(getContext()).autoDismiss(false).asConfirm("测试a", "aaaa", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        XPopup.get(getContext()).autoDismiss(false).asConfirm("测试b", "bbbb", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                XPopup.get(getContext()).autoDismiss(false).asConfirm("测试c", "cccc", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        XPopup.get(getContext()).dismiss();
                                    }
                                }).show();
                            }
                        }).show();
                    }
                }).show();

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commonAdapter);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        getContext().startActivity(new Intent(getContext(), DemoActivity.class));
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }
}