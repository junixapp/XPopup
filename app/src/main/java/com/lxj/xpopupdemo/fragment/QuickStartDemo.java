package com.lxj.xpopupdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.widget.PopupDrawerLayout;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
import com.lxj.xpopupdemo.custom.ZhihuCommentPopup;

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
    public void init(final View view) {
        view.findViewById(R.id.btnShowConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowInputConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowCenterList).setOnClickListener(this);
        view.findViewById(R.id.btnShowCenterListWithCheck).setOnClickListener(this);
        view.findViewById(R.id.btnShowLoading).setOnClickListener(this);
        view.findViewById(R.id.btnShowBottomList).setOnClickListener(this);
        view.findViewById(R.id.btnShowBottomListWithCheck).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerLeft).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerRight).setOnClickListener(this);
        view.findViewById(R.id.btnCustomBottomPopup).setOnClickListener(this);
        view.findViewById(R.id.tv1).setOnClickListener(this);
        view.findViewById(R.id.tv2).setOnClickListener(this);
        view.findViewById(R.id.tv3).setOnClickListener(this);

        // 必须在事件发生前，调用这个方法来监视View的触摸

        XPopup.get(getActivity()).watch(view.findViewById(R.id.btnShowAttachPoint));
        view.findViewById(R.id.btnShowAttachPoint).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XPopup.get(getActivity()).asAttachList(new String[]{"置顶", "复制", "删除"}, null,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                return false;
            }
        });

        drawerPopupView = (CustomDrawerPopupView) new CustomDrawerPopupView(getContext())
                .setDrawerPosition(PopupDrawerLayout.Position.Right)
                .hasStatusBarShadow(true);   // 添加状态栏Shadow
    }

    CustomDrawerPopupView drawerPopupView;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowConfirm:
                XPopup.get(getContext()).asConfirm("我是标题", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                toast("click confirm");
                            }
                        })
//                        .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
                        .autoDismiss(false)
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() {
                                Log.e("tag", "onShow");
                            }

                            @Override
                            public void onDismiss() {
                                Log.e("tag", "onDismiss");
                            }
                        })
                        .show();
                break;
            case R.id.btnShowInputConfirm:
                XPopup.get(getContext()).asInputConfirm("我是标题", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                toast("input text: " + text);
//                                XPopup.get(getActivity()).asLoading().show();
                            }
                        })
//                        .dismissOnBackPressed(false)
                        .autoOpenSoftInput(true)
                        .show();
                break;
            case R.id.btnShowCenterList:
                XPopup.get(getActivity()).asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
//                        .setWidthAndHeight(600,0)
                        .show();
                break;
            case R.id.btnShowCenterListWithCheck:
                XPopup.get(getActivity()).asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                        null, 1,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowLoading:
                XPopup.get(getActivity()).asLoading("正在加载中")
                        .show();
                break;
            case R.id.btnShowBottomList:
                XPopup.get(getActivity()).asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowBottomListWithCheck:
                XPopup.get(getActivity()).asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        null, 2,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
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
                                toast("click " + text);
                            }
                        })
                        .atView(v)  // 依附于所点击的View
                        .show();
                break;
            case R.id.btnShowDrawerLeft:
                XPopup.get(getActivity())
                        .asCustom(new CustomDrawerPopupView(getContext()))
//                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerRight:
                XPopup.get(getActivity())
                        .asCustom(drawerPopupView)
                        .show();
                break;
            case R.id.btnCustomBottomPopup:
                XPopup.get(getActivity())
                        .asCustom(new ZhihuCommentPopup(getContext()))
//                        .popupAnimation(PopupAnimation.TranslateFromLeft)
//                        .asCustom(new CustomBasePopupView(getContext()))
//                        .maxWidthAndHeight(0, 300)
                        .show();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
