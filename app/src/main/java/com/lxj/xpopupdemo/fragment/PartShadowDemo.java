package com.lxj.xpopupdemo.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custompopup.CustomPartShadowPopupView;

import java.util.ArrayList;

/**
 * Description: 局部阴影的示例
 * Create by dance, at 2018/12/21
 */
public class PartShadowDemo extends BaseFragment implements View.OnClickListener {
    View ll_container;
    VerticalRecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_part_shadow_demo;
    }

    @Override
    public void init(View view) {
        ll_container = view.findViewById(R.id.ll_container);
        recyclerView = view.findViewById(R.id.recyclerView);

        view.findViewById(R.id.tv_all).setOnClickListener(this);
        view.findViewById(R.id.tv_price).setOnClickListener(this);
        view.findViewById(R.id.tv_sales).setOnClickListener(this);
        view.findViewById(R.id.tv_select).setOnClickListener(this);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(i + "");
        }
        recyclerView.setAdapter(new CommonAdapter<String>(android.R.layout.simple_list_item_1, data) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(android.R.id.text1, "商品名字 - " + position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                XPopup.get(getActivity())
                        .asCustom(new CustomPartShadowPopupView(getContext()))
                        .atView(v)
                        .show();
                break;
            default:
                XPopup.get(getActivity())
                        .asCustom(new CustomPartShadowPopupView(getContext()))
                        .atView(ll_container)
                        .show();
                break;
        }
    }
}
