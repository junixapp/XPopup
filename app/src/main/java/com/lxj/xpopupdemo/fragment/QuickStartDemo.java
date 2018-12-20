package com.lxj.xpopupdemo.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.widget.PopupDrawerLayout;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by lxj, at 2018/12/11
 */
public class QuickStartDemo extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quickstart;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.btnShowConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowInputConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowCenterList).setOnClickListener(this);
        view.findViewById(R.id.btnShowLoading).setOnClickListener(this);
        view.findViewById(R.id.btnShowBottomList).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerLeft).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerRight).setOnClickListener(this);
        view.findViewById(R.id.tv1).setOnClickListener(this);
        view.findViewById(R.id.tv2).setOnClickListener(this);
        view.findViewById(R.id.tv3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnShowConfirm:
                XPopup.get(getContext()).asConfirm("我是标题", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                               toast("click confirm");
                            }
                        })
//                        .dismissOnBackPressed(false)
//                        .dismissOnTouchOutside(false)
                        .show();
                break;
            case R.id.btnShowInputConfirm:
                XPopup.get(getContext()).asInputConfirm("我是标题", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                toast("input text: " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowCenterList:
                XPopup.get(getActivity()).asCenterList("请选择一项",new String[]{"条目1", "条目2", "条目3", "条目4"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click "+text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowLoading:
                XPopup.get(getActivity()).asLoading().show();
                break;
            case R.id.btnShowBottomList:
                XPopup.get(getActivity()).asBottomList("请选择一项",new String[]{"条目1", "条目2", "条目3", "条目4","条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click "+text);
                            }
                        })
                        .show();
                break;
            case R.id.tv1:
            case R.id.tv2:
            case R.id.tv3:
                XPopup.get(getActivity()).asAttachList(new String[]{"分享", "编辑", "不带icon"},
                        new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click "+text);
                            }
                        })
                        .atView(v)  // 依附于所点击的View
                        .show();
                break;
            case R.id.btnShowDrawerLeft:
                XPopup.get(getActivity())
                        .asCustom(new CustomDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerRight:
                XPopup.get(getActivity())
                        .asCustom(
                                new CustomDrawerPopupView(getContext())
                                .setDrawerPosition(PopupDrawerLayout.Position.Right)
                        )
                        .show();
                break;
        }
    }


}
