package com.lxj.xpopupdemo.fragment;

import android.animation.FloatEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.util.XPermission;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.DemoActivity;
import com.lxj.xpopupdemo.MainActivity;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomAttachPopup;
import com.lxj.xpopupdemo.custom.CustomAttachPopup2;
import com.lxj.xpopupdemo.custom.CustomBubbleAttachPopup;
import com.lxj.xpopupdemo.custom.CustomCenter1;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;
import com.lxj.xpopupdemo.custom.CustomFullScreenPopup;
import com.lxj.xpopupdemo.custom.CustomHorizontalBubbleAttachPopup;
import com.lxj.xpopupdemo.custom.ListDrawerPopupView;
import com.lxj.xpopupdemo.custom.LoginPopup;
import com.lxj.xpopupdemo.custom.NotificationMsgPopup;
import com.lxj.xpopupdemo.custom.PagerBottomPopup;
import com.lxj.xpopupdemo.custom.PagerDrawerPopup;
import com.lxj.xpopupdemo.custom.QQMsgPopup;
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
        view.findViewById(R.id.tvEditText).requestFocus();
        view.findViewById(R.id.btnShowConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnBindLayout).setOnClickListener(this);
        view.findViewById(R.id.btnShowPosition1).setOnClickListener(this);
        view.findViewById(R.id.btnShowPosition2).setOnClickListener(this);
        view.findViewById(R.id.btnShowPosition3).setOnClickListener(this);
        view.findViewById(R.id.btnShowInputConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowCenterList).setOnClickListener(this);
        view.findViewById(R.id.btnShowCenterListWithCheck).setOnClickListener(this);
        view.findViewById(R.id.btnShowLoading).setOnClickListener(this);
        view.findViewById(R.id.btnShowBottomList).setOnClickListener(this);
        view.findViewById(R.id.btnShowBottomListWithCheck).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerLeft).setOnClickListener(this);
        view.findViewById(R.id.btnShowDrawerRight).setOnClickListener(this);
        view.findViewById(R.id.btnCustomBottomPopup).setOnClickListener(this);
        view.findViewById(R.id.btnPagerBottomPopup).setOnClickListener(this);
        view.findViewById(R.id.btnCustomEditPopup).setOnClickListener(this);
        view.findViewById(R.id.btnFullScreenPopup).setOnClickListener(this);
        view.findViewById(R.id.btnAttachPopup1).setOnClickListener(this);
        view.findViewById(R.id.btnAttachPopup2).setOnClickListener(this);
        view.findViewById(R.id.tv1).setOnClickListener(this);
        view.findViewById(R.id.tv2).setOnClickListener(this);
        view.findViewById(R.id.tv3).setOnClickListener(this);
        view.findViewById(R.id.btnMultiPopup).setOnClickListener(this);
        view.findViewById(R.id.btnShowInBackground).setOnClickListener(this);
        view.findViewById(R.id.btnBubbleAttachPopup1).setOnClickListener(this);
        view.findViewById(R.id.btnBubbleAttachPopup2).setOnClickListener(this);
        view.findViewById(R.id.test).setOnClickListener(this);

        // 必须在事件发生前，调用这个方法来监视View的触摸
        final XPopup.Builder builder = new XPopup.Builder(getContext())
//                .isCenterHorizontal(true)
                .watchView(view.findViewById(R.id.btnShowAttachPoint));
        view.findViewById(R.id.btnShowAttachPoint).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XPopup.fixLongClick(v);//能保证弹窗弹出后，下层的View无法滑动
//                builder.asAttachList(new String[]{"置顶11", "复制", "删除", "编辑编辑编辑编辑"
//                                }, null,
//                                new OnSelectListener() {
//                                    @Override
//                                    public void onSelect(int position, String text) {
//                                        toast("click " + text);
//                                    }
//                                })
//                        .show();

                builder.isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(v)
                        .isViewMode(true)
//                        .offsetY(-XPopupUtils.dp2px(getContext(), 30))
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomHorizontalBubbleAttachPopup(getContext()))
                        .show();
                return true;
            }
        });

        drawerPopupView = new CustomDrawerPopupView(getContext());
    }

    CustomDrawerPopupView drawerPopupView;
    AttachPopupView attachPopupView;
    BasePopupView popupView;
    LoadingPopupView loadingPopup;
    CustomAttachPopup2 customAttach2;

    private void loopPopup() {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .asConfirm("哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                        "取消", "确定",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                loopPopup();
                            }
                        }, null, false)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true)
                        .hasStatusBar(false)
                        .isRequestFocus(false)
                        .asCustom(new CustomCenter1(getContext()))
                        .show();
                break;
            case R.id.btnShowConfirm: //带确认和取消按钮的弹窗
                /*if(popupView==null)*/
                popupView = new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true)
//                        .isTouchThrough(true)
//                        .dismissOnBackPressed(false)
//                        .isViewMode(true)
//                        .hasBlurBg(true)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                        .asConfirm("我是标题", "床前明月光，疑是地上霜；举头望明月，低头思故乡。"
                                + "床前明月光，疑是地上霜；举头望明月，低头思故乡。" ,
                                "取消", "确定",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                    }
                                }, null, true);
                popupView.show();
                break;
            case R.id.btnBindLayout:  //复用项目中已有布局，使用XPopup已有的交互能力
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asInputConfirm("复用项目已有布局", null, "您可以复用项目已有布局，来使用XPopup强大的交互能力和逻辑封装，弹窗的布局完全由你自己控制。\n" +
                                        "注意：你自己的布局必须提供一些控件Id，否则XPopup找不到View。\n具体需要提供哪些Id，请查看文档[内置弹窗]一章。", null,
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {

                                    }
                                }, null, R.layout.my_confim_popup) //最后一个参数绑定已有布局
                        .show();
                break;
            case R.id.btnShowInputConfirm: //带确认和取消按钮，输入框的弹窗
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(false)
                        .hasNavigationBar(false)
                        //.dismissOnBackPressed(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗对象，推荐设置这个
                        .autoOpenSoftInput(true)
                        .isDarkTheme(true)
//                        .isViewMode(true)
                        .setPopupCallback(new DemoXPopupListener())
//                        .autoFocusEditText(false) //是否让弹窗内的EditText自动获取焦点，默认是true
                        //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
                        .asInputConfirm("我是标题", "大萨达撒大所大所大", null, "我是默认Hint文字",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
//                                          new XPopup.Builder(getContext()).asLoading().show();
                                    }
                                })
                        .show();
                break;
            case R.id.btnShowCenterList: //在中间弹出的List列表弹窗
                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .maxHeight(800)
                        .isDarkTheme(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目1", "条目2", "条目3", "条目4",
                                        "条目1", "条目2", "条目3", "条目4", "条目1", "条目2", "条目3", "条目4",
                                        "条目1", "条目2", "条目3", "条目4", "条目1", "条目2", "条目3", "条目4",
                                        "条目1", "条目2", "条目3", "条目4", "条目1", "条目2", "条目3", "条目4",
                                        "条目1", "条目2", "条目3", "条目4", "条目1", "条目2", "条目3", "条目4",},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                })
//                        .bindLayout(R.layout.my_custom_attach_popup) //自定义布局
                        .show();
                break;
            case R.id.btnShowCenterListWithCheck: //在中间弹出的List列表弹窗，带选中效果
                new XPopup.Builder(getContext())
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
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
                if (loadingPopup == null) {
                    loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                            .dismissOnBackPressed(false)
                            .isLightNavigationBar(true)
//                            .asLoading(null, R.layout.custom_loading_popup)
                            .asLoading("少时诵诗书", LoadingPopupView.Style.ProgressBar)
                            .show();
                } else {
                    loadingPopup.setStyle(LoadingPopupView.Style.ProgressBar);
                    loadingPopup.show();
                }
                loadingPopup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.setTitle("加载中长度变化啊");
                        loadingPopup.setStyle(LoadingPopupView.Style.Spinner);
                        loadingPopup.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingPopup.setTitle("");
                            }
                        }, 2000);
                    }
                }, 2000);
                loadingPopup.delayDismissWith(6000, new Runnable() {
                    @Override
                    public void run() {
                        toast("我消失了！！！");
                    }
                });
                break;
            case R.id.btnShowBottomList: //从底部弹出，带手势拖拽的列表弹窗
                popupView = new XPopup.Builder(getContext())
                        .isDarkTheme(true)
                        .hasShadowBg(false)
                        .customHostLifecycle(getLifecycle())
                        .moveUpToKeyboard(false)
                        .isDestroyOnDismiss(false)
                        .borderRadius(XPopupUtils.dp2px(getContext(), 15))
//                        .popupHeight(XPopupUtils.dp2px(getContext(), 397f))
//                        .isViewMode(true)
//                            .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5", "条目6", "条目7"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                });

                popupView.show();
                break;
            case R.id.btnShowBottomListWithCheck: //从底部弹出，带手势拖拽的列表弹窗,带选中效果
                new XPopup.Builder(getContext())
//                        .isDarkTheme(true)
                        .isViewMode(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asBottomList("标题可以没有", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
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
                        .hasShadowBg(false)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .isViewMode(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                        .asCustom(new ZhihuCommentPopup(getContext()))
                        .show();
                break;
            case R.id.btnPagerBottomPopup: //自定义的底部弹窗
                new XPopup.Builder(getContext())
//                        .hasShadowBg(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isViewMode(true)
                        .asCustom(new PagerBottomPopup(getContext()))
                        .show();
                break;
            case R.id.tv1: //依附于某个View的Attach类型弹窗
            case R.id.tv2:
            case R.id.tv3:
//                AttachPopupView attachPopupView = new XPopup.Builder(getContext())
//                        .hasStatusBarShadow(false)
////                        .isRequestFocus(false)
//                        .isCoverSoftInput(true)
//                        .hasShadowBg(false)
//                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
//                        .asAttachList(new String[]{"分享", "编辑", "不带icon", "分享分享分享",
//                                },
//                                new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
//                                new OnSelectListener() {
//                                    @Override
//                                    public void onSelect(int position, String text) {
//                                        toast("click " + text);
//                                    }
//                                }, 0, 0/*, Gravity.LEFT*/);
//                attachPopupView.show();
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .offsetX(50) //偏移10
//                        .offsetY(10)  //往下偏移10
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new CustomAttachPopup(getContext()))
                        .show();
                break;
            case R.id.btnAttachPopup1: //水平方向的Attach弹窗，就像微信朋友圈的点赞弹窗那样
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .offsetX(50) //偏移10
//                        .offsetY(10)  //往下偏移10
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new CustomAttachPopup(getContext()))
                        .show();
                break;
            case R.id.btnAttachPopup2:
//                customAttach2 = new CustomAttachPopup2(getContext());
                if(customAttach2==null){
                    customAttach2 = (CustomAttachPopup2) new XPopup.Builder(getContext())
                            .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                            .atView(v)
                            .asCustom(new CustomAttachPopup2(getContext()))
                            .show();
                }else{
                    customAttach2.show();
                }

                break;
            case R.id.btnBubbleAttachPopup1: //水平方向带气泡弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(v)
                        .isViewMode(true)
                        .offsetY(XPopupUtils.dp2px(getContext(), 10))
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomHorizontalBubbleAttachPopup(getContext()))
                        .show();
//                new XPopup.Builder(getContext())
//                        .isTouchThrough(true)
//                        .isViewMode(true)
//                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .atView(view.findViewById(R.id.vv))
//                        .hasShadowBg(false) // 去掉半透明背景
//                        .offsetX(-XPopupUtils.dp2px(getContext(), 60))
////                        .offsetX(XPopupUtils.dp2px(getContext(), 20))
//                        .asCustom(new CustomBubbleAttachPopup(getContext()))
//                        .show();
                break;
            case R.id.btnBubbleAttachPopup2: //垂直方向带气泡弹窗
                new XPopup.Builder(getContext())
                        .hasShadowBg(false)
                        .isTouchThrough(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(view.findViewById(R.id.vv))
                        .isCenterHorizontal(true)
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomBubbleAttachPopup(getContext()))
                        .show();
                new XPopup.Builder(getContext())
                        .isTouchThrough(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(view.findViewById(R.id.vv2))
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomBubbleAttachPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerLeft: //像DrawerLayout一样的Drawer弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .asCustom(new CustomDrawerPopupView(getContext()))
//                        .hasShadowBg(false)
//                        .maxWidth(100)
                        .isViewMode(true) //使用了Fragment，必须开启View模式
                        .asCustom(new PagerDrawerPopup(getContext()))
//                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerRight:
                popupView = new XPopup.Builder(getContext())
//                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .autoOpenSoftInput(true)
//                        .popupWidth(300)
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .setPopupCallback(new DemoXPopupListener())
                        .asCustom(new ListDrawerPopupView(getContext()));
                popupView.show();
                break;
            case R.id.btnFullScreenPopup: //全屏弹窗，看起来像Activity
                popupView = new CustomFullScreenPopup(getContext());
                new XPopup.Builder(getContext())
//                        .hasStatusBar(false)
//                        .hasStatusBarShadow(true)
//                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isLightStatusBar(true)
                        .autoOpenSoftInput(true)
                        .asCustom(popupView)
                        .show();
                break;
            case R.id.btnCustomEditPopup: //自定义依附在输入法之上的Bottom弹窗
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
//                        .isViewMode(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asCustom(new CustomEditTextBottomPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowPosition1:
                new XPopup.Builder(getContext())
//                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .offsetY(300)
                        .offsetX(-100)
                        .hasShadowBg(false)
                        .hasBlurBg(true)
                        .popupAnimation(PopupAnimation.TranslateFromLeft)
                        .asCustom(new QQMsgPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowPosition2:
                new XPopup.Builder(getContext())
                        .hasShadowBg(false)
                        .hasBlurBg(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isCenterHorizontal(true)
                        .offsetY(200)
                        .asCustom(new QQMsgPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowPosition3:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .popupAnimation(PopupAnimation.TranslateFromTop)
                        .asCustom(new NotificationMsgPopup(getContext()))
                        .show();
                break;
            case R.id.btnMultiPopup:
                startActivity(new Intent(getContext(), DemoActivity.class));
                break;
            case R.id.btnShowInBackground:
                //申请悬浮窗权限
                XPopup.requestOverlayPermission(getContext(), new XPermission.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        ToastUtils.showShort("等待2秒后弹出XPopup！！！");
                        ActivityUtils.startHomeActivity();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new XPopup.Builder(getContext())
                                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                        .enableShowWhenAppBackground(true)  //运行在应用后台弹出
                                        .asConfirm("XPopup牛逼", "XPopup支持直接在后台弹出！", new OnConfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                startActivity(new Intent(getContext(), MainActivity.class));
                                            }
                                        }).show();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("权限拒绝需要申请悬浮窗权限！");
                    }
                });
                break;
        }
    }


    static class DemoXPopupListener extends SimpleCallback {
        FloatEvaluator fEvaluator = new FloatEvaluator();
        FloatEvaluator iEvaluator = new FloatEvaluator();

        @Override
        public void onCreated(BasePopupView pv) {
            Log.e("tag", "onCreated");
        }

        @Override
        public void onShow(BasePopupView popupView) {
            Log.e("tag", "onShow");
        }

        @Override
        public void onDismiss(BasePopupView popupView) {
            Log.e("tag", "onDismiss");
        }

        @Override
        public void beforeDismiss(BasePopupView popupView) {
            Log.e("tag", "beforeDismiss");
        }

        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
        @Override
        public boolean onBackPressed(BasePopupView popupView) {
            Log.e("tag", "拦截的返回按键，按返回键XPopup不会关闭了");
            Toast.makeText(popupView.getContext(), "onBackPressed返回true，拦截了返回按键，按返回键XPopup不会关闭了", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onDrag(BasePopupView popupView, int value, float percent, boolean upOrLeft) {
            super.onDrag(popupView, value, percent, upOrLeft);
//            Log.e("tag", "value: " + value + "  percent: " + percent);
//            ((Activity) popupView.getContext()).getWindow().getDecorView().setTranslationX(value);
//            float e = fEvaluator.evaluate(percent, 1.0, 0.8);
//            View decorView = ((Activity) popupView.getContext()).getWindow().getDecorView();
//            decorView.setScaleX(e);
//            decorView.setScaleY(e);
//            FloatEvaluator iEvaluator = new FloatEvaluator();
//            View decorView = ((Activity) popupView.getContext()).getWindow().getDecorView();
//            float t = iEvaluator.evaluate(percent, 0, -popupView.getMeasuredWidth()/2);
//            decorView.setTranslationX(t);
        }

        @Override
        public void onKeyBoardStateChanged(BasePopupView popupView, int height) {
            super.onKeyBoardStateChanged(popupView, height);
            Log.e("tag", "onKeyBoardStateChanged height: " + height);
        }

        @Override
        public void onClickOutside(BasePopupView popupView) {
            super.onClickOutside(popupView);
            Log.e("tag", "onClickOutside");
        }
    }
}
