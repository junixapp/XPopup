package com.lxj.xpopupdemo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.widget.PopupDrawerLayout;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
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

    private CustomDrawerPopupView drawerPopupView;

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
        view.findViewById(R.id.tv_filter).setOnClickListener(this);

        drawerPopupView = new CustomDrawerPopupView(getContext());

        final ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(i + "");
        }
        CommonAdapter adapter = new CommonAdapter<String>(android.R.layout.simple_list_item_1, data) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(android.R.id.text1, "长按我试试 - " + position);
                //必须要在事件发生之前就watch
                final XPopup.Builder builder = new XPopup.Builder(getContext()).watchView(holder.itemView);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        builder.asAttachList(new String[]{"置顶", "编辑", "删除"}, null,0,10, new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                ToastUtils.showShort(text);
                            }
                        }).show();
                        return true;
                    }
                });
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(@NonNull View view, @NonNull RecyclerView.ViewHolder holder, int position) {
                toast(data.get(position));
            }
        });
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
            case R.id.tv_price:
            case R.id.tv_sales:
                if(popupView==null){
                    popupView = (CustomPartShadowPopupView) new XPopup.Builder(getContext())
                            .atView(v)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {
                                    Toast.makeText(getActivity(), "显示了", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onDismiss() {
                                    Toast.makeText(getActivity(), "关闭了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .asCustom(new CustomPartShadowPopupView(getContext()));
                }
                popupView.toggle();
                Log.e("tag", "popupView status: "+popupView.popupStatus);
                break;
            case R.id.tv_filter:
                new XPopup.Builder(getContext())
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(drawerPopupView)
                        .show();
                break;
            case R.id.tv_select:
                new XPopup.Builder(getContext())
                        .atView(v)
                        .asCustom(new CustomPartShadowPopupView(getContext()))
                        .show();
                break;
        }
    }
}
