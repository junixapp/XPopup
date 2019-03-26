package com.lxj.xpopupdemo.fragment;

import android.util.Log;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.widget.PopupDrawerLayout;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomAttachPopup;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;
import com.lxj.xpopupdemo.custom.CustomFullScreenPopup;
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
        view.findViewById(R.id.btnCustomEditPopup).setOnClickListener(this);
        view.findViewById(R.id.btnFullScreenPopup).setOnClickListener(this);
        view.findViewById(R.id.btnAttachPopup1).setOnClickListener(this);
        view.findViewById(R.id.btnAttachPopup2).setOnClickListener(this);
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

//        XPopup.get(getActivity()).watch(view.findViewById(R.id.btnAttachPopup2));
        drawerPopupView = (CustomDrawerPopupView) new CustomDrawerPopupView(getContext())
                .setDrawerPosition(PopupPosition.Right)
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
                        }, null, true)
//                        .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
//                        .autoDismiss(false)
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
                XPopup.get(getContext()).asInputConfirm("我是标题", "请输入内容。", "我是默认Hint文字",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                toast("input text: " + text);
//                                XPopup.get(getActivity()).asLoading().show();
                            }
                        })
//                        .dismissOnBackPressed(false)
                        .autoOpenSoftInput(true)
//                        .moveUpToKeyboard(false) //是否移动到软键盘上面，默认为true
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
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
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
                        .asCustom(new ZhihuCommentPopup(getContext())/*.enableDrag(false)*/)
//                        .maxWidthAndHeight(0, 1300)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .show();
                break;
            case R.id.btnFullScreenPopup:
                XPopup.get(getActivity())
                        .asCustom(new CustomFullScreenPopup(getContext()))
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .show();
                break;
            case R.id.btnAttachPopup1:
                XPopup.get(getActivity())
                        .asCustom(new CustomAttachPopup(getContext()))
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .show();
                break;
            case R.id.btnAttachPopup2:
                XPopup.get(getActivity())
                        .asCustom(new CustomAttachPopup(getContext()))
                        .atView(v)
                        .hasShadowBg(false) // 去掉半透明背景
                        .show();
                break;
            case R.id.btnCustomEditPopup:
                XPopup.get(getActivity())
                        .asCustom(new CustomEditTextBottomPopup(getContext()))
                        .autoOpenSoftInput(true)
//                        .maxWidthAndHeight(0, 1300)
                        .show();
                break;
        }
    }


}
