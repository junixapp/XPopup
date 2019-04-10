package com.lxj.xpopupdemo.fragment;

import android.util.Log;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomAttachPopup;
import com.lxj.xpopupdemo.custom.CustomAttachPopup2;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;
import com.lxj.xpopupdemo.custom.CustomFullScreenPopup;
import com.lxj.xpopupdemo.custom.ListDrawerPopupView;
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
        final XPopup.Builder builder = new XPopup.Builder(getContext())
                .watchView(view.findViewById(R.id.btnShowAttachPoint));
        view.findViewById(R.id.btnShowAttachPoint).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                builder.asAttachList(new String[]{"置顶", "复制", "删除"}, null,
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

        drawerPopupView = new CustomDrawerPopupView(getContext());
    }

    CustomDrawerPopupView drawerPopupView;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowConfirm: //带确认和取消按钮的弹窗
                new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                        .setPopupCallback(new XPopupCallback() {
                            @Override
                            public void onShow() {
                                Log.e("tag", "onShow");
                            }
                            @Override
                            public void onDismiss() {
                                Log.e("tag", "onDismiss");
                            }
                        }).asConfirm("我是标题", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                toast("click confirm");
                            }
                        }, null, false)
                        .show();
                break;
            case R.id.btnShowInputConfirm: //带确认和取消按钮，输入框的弹窗
                new XPopup.Builder(getContext())
                        //.dismissOnBackPressed(false)
                        .autoOpenSoftInput(true)
                        //.moveUpToKeyboard(false) //是否移动到软键盘上面，默认为true
                        .asInputConfirm("我是标题", "请输入内容。", "我是默认Hint文字",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                toast("input text: " + text);
//                                new XPopup.Builder(getContext()).asLoading().show();
                            }
                        })
                        .show();
                break;
            case R.id.btnShowCenterList: //在中间弹出的List列表弹窗
                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowCenterListWithCheck: //在中间弹出的List列表弹窗，带选中效果
                new XPopup.Builder(getContext())
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                        null, 1,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowLoading: //在中间弹出的Loading加载框
                final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                        .asLoading("正在加载中")
                        .show();
                loadingPopup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        if(loadingPopup.isShow())
                            loadingPopup.dismissWith(new Runnable() {
                                @Override
                                public void run() {
                                    toast("我消失了！！！");
                                }
                            });
                    }
                },2000);
                break;
            case R.id.btnShowBottomList: //从底部弹出，带手势拖拽的列表弹窗
                new XPopup.Builder(getContext())
//                        .enableDrag(false)
                        .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowBottomListWithCheck: //从底部弹出，带手势拖拽的列表弹窗,带选中效果
                new XPopup.Builder(getContext())
                        .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        null, 2,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnCustomBottomPopup: //自定义的底部弹窗
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new ZhihuCommentPopup(getContext())/*.enableDrag(false)*/)
                        .show();
                break;
            case R.id.tv1: //依附于某个View的Attach类型弹窗
            case R.id.tv2:
            case R.id.tv3:
                new XPopup.Builder(getContext())
//                        .isCenterHorizontal(true) //是否与目标水平居中对齐
//                        .offsetY(-10)
//                        .popupPosition(PopupPosition.Bottom) //手动指定弹窗的位置
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{"分享", "编辑", "不带icon"},
                        new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowDrawerLeft: //像DrawerLayout一样的Drawer弹窗
                new XPopup.Builder(getContext())
                        .asCustom(new CustomDrawerPopupView(getContext()))
//                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerRight:
                new XPopup.Builder(getContext())
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(drawerPopupView)
                        .show();
                break;
            case R.id.btnFullScreenPopup: //全屏弹窗，看起来像Activity
                new XPopup.Builder(getContext())
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .hasStatusBarShadow(true)
                        .asCustom(new CustomFullScreenPopup(getContext()))
                        .show();
                break;
            case R.id.btnAttachPopup1: //水平方向的Attach弹窗，就像微信朋友圈的点赞弹窗那样
                new XPopup.Builder(getContext())
//                        .offsetX(60) //往左偏移10
//                        .offsetY(-50)  //往下偏移10
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new CustomAttachPopup(getContext()))
                        .show();
                break;
            case R.id.btnAttachPopup2:
                new XPopup.Builder(getContext())
                        .atView(v)
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomAttachPopup2(getContext()))
                        .show();
                break;
            case R.id.btnCustomEditPopup: //自定义依附在输入法之上的Bottom弹窗
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
                        .asCustom(new CustomEditTextBottomPopup(getContext()))
                        .show();
                break;
        }
    }


}
