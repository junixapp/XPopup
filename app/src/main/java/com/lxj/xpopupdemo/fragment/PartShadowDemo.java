package com.lxj.xpopupdemo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomPartShadowPopupView;

import java.util.ArrayList;

/**
 * Description: 局部阴影的示例
 * Create by dance, at 2018/12/21
 */
public class PartShadowDemo extends BaseFragment implements View.OnClickListener {
    View ll_container;
    VerticalRecyclerView recyclerView;
    private CustomPartShadowPopupView popupView;

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

        final ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(i + "");
        }
        CommonAdapter adapter = new CommonAdapter<String>(android.R.layout.simple_list_item_1, data) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(android.R.id.text1, "商品名字 - " + position);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(@NonNull View view, @NonNull RecyclerView.ViewHolder holder, int position) {
                toast(data.get(position));
            }
        });
        recyclerView.setAdapter(adapter);

        popupView = new CustomPartShadowPopupView(getContext());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                XPopup.get(getActivity())
                    .asCustom(popupView)
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
