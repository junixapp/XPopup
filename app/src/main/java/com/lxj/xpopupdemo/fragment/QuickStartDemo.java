package com.lxj.xpopupdemo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.blankj.utilcode.util.ActivityUtils;
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
import com.lxj.xpopupdemo.DemoActivity;
import com.lxj.xpopupdemo.MainActivity;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomAttachPopup;
import com.lxj.xpopupdemo.custom.CustomAttachPopup2;
import com.lxj.xpopupdemo.custom.CustomCenterPopup;
import com.lxj.xpopupdemo.custom.CustomDrawerPopupView;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;
import com.lxj.xpopupdemo.custom.CustomFullScreenPopup;
import com.lxj.xpopupdemo.custom.ListDrawerPopupView;
import com.lxj.xpopupdemo.custom.LoginPopup;
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
        view.findViewById(R.id.btnShowConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnBindLayout).setOnClickListener(this);
        view.findViewById(R.id.btnShowPosition1).setOnClickListener(this);
        view.findViewById(R.id.btnShowPosition2).setOnClickListener(this);
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

        // 必须在事件发生前，调用这个方法来监视View的触摸
        final XPopup.Builder builder = new XPopup.Builder(getContext())
                .watchView(view.findViewById(R.id.btnShowAttachPoint));
        view.findViewById(R.id.btnShowAttachPoint).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XPopup.fixLongClick(v);//能保证弹窗弹出后，下层的View无法滑动
                builder.asAttachList(new String[]{"置顶11", "复制", "删除", "编辑编辑编辑编辑"
                        }, null,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
                return true;
            }
        });

        drawerPopupView = new CustomDrawerPopupView(getContext());
    }

    CustomDrawerPopupView drawerPopupView;
    AttachPopupView attachPopupView;
    AttachPopupView popup;
    BasePopupView popupView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowConfirm: //带确认和取消按钮的弹窗
                /*if(popupView==null)*/popupView = new XPopup.Builder(getContext())
                        .dismissOnBackPressed(true)
                        .dismissOnTouchOutside(true)
                        .hasNavigationBar(false)
//                        .navigationBarColor(Color.BLUE)
//                        .hasBlurBg(true)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
//                        .setPopupCallback(new DemoXPopupListener())
                        .asConfirm("哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                                "取消", "确定",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        toast("click confirm");
                                    }
                                }, null, false);
                popupView.show();

                break;
            case R.id.btnBindLayout:  //复用项目中已有布局，使用XPopup已有的交互能力
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asConfirm("复用项目已有布局", "您可以复用项目已有布局，来使用XPopup强大的交互能力和逻辑封装，弹窗的布局完全由你自己控制。\n" +
                                        "注意：你自己的布局必须提供一些控件Id，否则XPopup找不到View。\n具体需要提供哪些Id，请查看文档[内置弹窗]一章。",
                                "关闭", "XPopup牛逼",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        toast("click confirm");
                                    }
                                }, null, false, R.layout.my_confim_popup) //最后一个参数绑定已有布局
                        .show();
                break;
            case R.id.btnShowInputConfirm: //带确认和取消按钮，输入框的弹窗
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(false)
                        //.dismissOnBackPressed(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .autoOpenSoftInput(true)
                        .isDarkTheme(true)
                        .setPopupCallback(new DemoXPopupListener())
//                        .autoFocusEditText(false) //是否让弹窗内的EditText自动获取焦点，默认是true
                        //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
                        .asInputConfirm("我是标题", null, null, "我是默认Hint文字",
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
                        .isDarkTheme(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
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
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
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
                final LoadingPopupView loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                        .dismissOnBackPressed(false)
                        .asLoading("正在加载中")
                        .show();
                loadingPopup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.setTitle("正在加载长度变化了");
                    }
                }, 1000);
//                loadingPopup.smartDismiss();
//                loadingPopup.dismiss();
                loadingPopup.delayDismissWith(4000, new Runnable() {
                    @Override
                    public void run() {
                        toast("我消失了！！！");
                    }
                });
                break;
            case R.id.btnShowBottomList: //从底部弹出，带手势拖拽的列表弹窗
                new XPopup.Builder(getContext())
                            .isDarkTheme(true)
//                            .hasShadowBg(false)
//                        .hasBlurBg(true)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .enableDrag(true)
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
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .enableDrag(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                        .asCustom(new ZhihuCommentPopup(getContext())/*.enableDrag(false)*/)
                        .show();
                break;
            case R.id.btnPagerBottomPopup: //自定义的底部弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup(getContext()))
                        .show();
                break;
            case R.id.tv1: //依附于某个View的Attach类型弹窗
            case R.id.tv2:
            case R.id.tv3:
                attachPopupView = new XPopup.Builder(getContext())
                        .hasShadowBg(false)
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .isDarkTheme(true)
//                        .popupAnimation(PopupAnimation.ScrollAlphaFromTop) //NoAnimation表示禁用动画
//                        .isCenterHorizontal(true) //是否与目标水平居中对齐
//                        .offsetY(-60)
//                        .offsetX(80)
//                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
//                        .popupWidth(500)
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{"分享", "编辑", "不带icon", "分享"},
                                new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                }, 0, 0);
                ;
                attachPopupView.show();
                break;
            case R.id.btnShowDrawerLeft: //像DrawerLayout一样的Drawer弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .asCustom(new CustomDrawerPopupView(getContext()))
//                        .hasShadowBg(false)
                        .asCustom(new PagerDrawerPopup(getContext()))
//                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnShowDrawerRight:
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case R.id.btnFullScreenPopup: //全屏弹窗，看起来像Activity
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .autoOpenSoftInput(true)
                        .asCustom(new CustomFullScreenPopup(getContext()))
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
                popup = (AttachPopupView) new XPopup.Builder(getContext())
//                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(v)
                        .hasShadowBg(false) // 去掉半透明背景
                        .asCustom(new CustomAttachPopup2(getContext()));
                popup.show();
                break;
            case R.id.btnCustomEditPopup: //自定义依附在输入法之上的Bottom弹窗
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
                        .asCustom(new CustomEditTextBottomPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowPosition1:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .offsetY(300)
                        .popupAnimation(PopupAnimation.TranslateFromLeft)
                        .asCustom(new QQMsgPopup(getContext()))
                        .show();
                break;
            case R.id.btnShowPosition2:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .isCenterHorizontal(true)
                        .offsetY(200)
                        .asCustom(new QQMsgPopup(getContext()))
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
                        }, 2000);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("权限拒绝需要申请悬浮窗权限！");
                    }
                });
                break;
        }
    }


    class DemoXPopupListener extends SimpleCallback {
        @Override
        public void onCreated(BasePopupView pv) {
            Log.e("tag", "弹窗创建了");
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
            Log.e("tag", "准备消失的时候执行");
        }

        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
        @Override
        public boolean onBackPressed(BasePopupView popupView) {
            ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
            return true;
        }

        @Override
        public void onKeyBoardStateChanged(BasePopupView popupView, int height) {
            super.onKeyBoardStateChanged(popupView, height);
            Log.e("tag", "onKeyBoardStateChanged height: " + height);
        }
    }

    ;

}
