package com.lxj.xpopup.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.CheckView;
import com.lxj.xpopup.widget.VerticalRecyclerView;

import java.util.Arrays;

/**
 * Description: 底部的列表对话框
 * Create by dance, at 2018/12/16
 */
public class BottomListPopupView extends BottomPopupView {
    RecyclerView recyclerView;
    TextView tv_title, tv_cancel;
    View vv_divider;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    /**
     *
     * @param context
     * @param bindLayoutId layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tv_title的TextView
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中有id为iv_image的ImageView（非必须），和id为tv_text的TextView
     */
    public BottomListPopupView(@NonNull Context context, int bindLayoutId, int bindItemLayoutId ) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? R.layout._xpopup_bottom_impl_list : bindLayoutId;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        if(bindLayoutId!=0){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.tv_cancel);
        vv_divider = findViewById(R.id.vv_divider);
        if(tv_cancel!=null){
            tv_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        if(tv_title!=null){
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(GONE);
                if(findViewById(R.id.xpopup_divider)!=null)findViewById(R.id.xpopup_divider).setVisibility(GONE);
            } else {
                tv_title.setText(title);
            }
        }

        final EasyAdapter<String> adapter = new EasyAdapter<String>(Arrays.asList(data), bindItemLayoutId == 0 ? R.layout._xpopup_adapter_text_match : bindItemLayoutId) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.tv_text, s);
                ImageView imageView = holder.getViewOrNull(R.id.iv_image);
                if (iconIds != null && iconIds.length > position) {
                    if(imageView!=null){
                        imageView.setVisibility(VISIBLE);
                        imageView.setBackgroundResource(iconIds[position]);
                    }
                } else {
                    if(imageView!=null) imageView.setVisibility(GONE);
                }

                if(bindItemLayoutId==0){
                    if(popupInfo.isDarkTheme){
                        holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_white_color));
                    }else {
                        holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_dark_color));
                    }
                }

                // 对勾View
                if (checkedPosition != -1) {
                    if(holder.getViewOrNull(R.id.check_view)!=null){
                        holder.getView(R.id.check_view).setVisibility(position == checkedPosition ? VISIBLE : GONE);
                        holder.<CheckView>getView(R.id.check_view).setColor(XPopup.getPrimaryColor());
                    }
                    if (position == checkedPosition){
                        holder.<TextView>getView(R.id.tv_text).setTextColor(XPopup.getPrimaryColor());
                    }
                    holder.<TextView>getView(R.id.tv_text).setGravity(XPopupUtils.isLayoutRtl(getContext()) ? Gravity.END : Gravity.START);
                }else {
                    if(holder.getViewOrNull(R.id.check_view)!=null)holder.getView(R.id.check_view).setVisibility(GONE);
                    holder.<TextView>getView(R.id.tv_text).setGravity(Gravity.CENTER);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getData().get(position));
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                if (popupInfo.autoDismiss) dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        applyTheme();
    }

    CharSequence title;
    String[] data;
    int[] iconIds;

    public BottomListPopupView setStringData(CharSequence title, String[] data, int[] iconIds) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public BottomListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    /**
     * 设置默认选中的位置
     *
     * @param position
     * @return
     */
    public BottomListPopupView setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }
    protected void applyTheme(){
        if(bindLayoutId==0) {
            if(popupInfo.isDarkTheme){
                applyDarkTheme();
            }else {
                applyLightTheme();
            }
        }
    }

    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        ((VerticalRecyclerView)recyclerView).setupDivider(true);
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        if(tv_cancel!=null)tv_cancel.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        findViewById(R.id.xpopup_divider).setBackgroundColor(
                getResources().getColor(R.color._xpopup_list_dark_divider)
        );
        if(vv_divider!=null)vv_divider.setBackgroundColor(Color.parseColor("#1B1B1B"));
        getPopupImplView().setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_dark_color),
                popupInfo.borderRadius, popupInfo.borderRadius, 0,0));
    }

    @Override
    protected void applyLightTheme() {
        super.applyLightTheme();
        ((VerticalRecyclerView)recyclerView).setupDivider(false);
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_dark_color));
        if(tv_cancel!=null)tv_cancel.setTextColor(getResources().getColor(R.color._xpopup_dark_color));
        findViewById(R.id.xpopup_divider).setBackgroundColor(getResources().getColor(R.color._xpopup_list_divider));
        if(vv_divider!=null)vv_divider.setBackgroundColor(getResources().getColor(R.color._xpopup_white_color));
        getPopupImplView().setBackground(XPopupUtils.createDrawable(getResources().getColor(R.color._xpopup_light_color),
                popupInfo.borderRadius, popupInfo.borderRadius, 0,0));
    }

}
