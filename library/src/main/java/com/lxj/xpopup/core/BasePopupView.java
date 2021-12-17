package com.lxj.xpopup.core;

import static com.lxj.xpopup.enums.PopupAnimation.NoAnimation;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.BlurAnimator;
import com.lxj.xpopup.animator.EmptyAnimator;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.animator.TranslateAlphaAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹窗基类
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends FrameLayout implements LifecycleObserver, LifecycleOwner,
        ViewCompat.OnUnhandledKeyEventListenerCompat{
    public PopupInfo popupInfo;
    protected PopupAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    protected BlurAnimator blurAnimator;
    private final int touchSlop;
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    protected boolean isCreated = false;
    private boolean hasModifySoftMode = false;
    private int preSoftMode = -1;
    private boolean hasMoveUp = false;
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected LifecycleRegistry lifecycleRegistry;

    public BasePopupView(@NonNull Context context) {
        super(context);
        if (context instanceof Application) {
            throw new IllegalArgumentException("XPopup的Context必须是Activity类型！");
        }
        lifecycleRegistry = new LifecycleRegistry(this);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setId(View.generateViewId());
        View contentView = LayoutInflater.from(context).inflate(getInnerLayoutId(), this, false);
        // 事先隐藏，等测量完毕恢复，避免影子跳动现象。
        contentView.setAlpha(0);
        addView(contentView);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public BasePopupView show() {
        Activity activity = XPopupUtils.context2Activity(this);
        if (activity == null || activity.isFinishing() || popupInfo == null) {
            return this;
        }
        if (popupStatus == PopupStatus.Showing || popupStatus == PopupStatus.Dismissing)
            return this;
        popupStatus = PopupStatus.Showing;
        if (popupInfo.isRequestFocus) KeyboardUtils.hideSoftInput(activity.getWindow());
        if (!popupInfo.isViewMode && dialog != null && dialog.isShowing())
            return BasePopupView.this;
        getActivityContentView().post(attachTask);
        return this;
    }

    private final Runnable attachTask = new Runnable() {
        @Override
        public void run() {
            // 1. add PopupView to its host.
            attachToHost();

            //2. 注册对话框监听器
            KeyboardUtils.registerSoftInputChangedListener(getHostWindow(), BasePopupView.this, new KeyboardUtils.OnSoftInputChangedListener() {
                @Override
                public void onSoftInputChanged(int height) {
                    onKeyboardHeightChange(height);
                    if (popupInfo != null && popupInfo.xPopupCallback != null) {
                        popupInfo.xPopupCallback.onKeyBoardStateChanged(BasePopupView.this, height);
                    }
                    if (height == 0) { // 说明输入法隐藏
                        XPopupUtils.moveDown(BasePopupView.this);
                        hasMoveUp = false;
                    } else {
                        //when show keyboard, move up
                        if (BasePopupView.this instanceof PartShadowPopupView && popupStatus == PopupStatus.Showing) {
                            return;
                        }
                        XPopupUtils.moveUpToKeyboard(height, BasePopupView.this);
                        hasMoveUp = true;
                    }
                }
            });

            // 3. do init，game start.
            init();
        }
    };

    public FullScreenDialog dialog;

    private void attachToHost() {
        if (popupInfo == null) {
            throw new IllegalArgumentException("如果弹窗对象是复用的，则不要设置isDestroyOnDismiss(true)");
        }
        if(popupInfo.hostLifecycle!=null){
            popupInfo.hostLifecycle.addObserver(this);
        }else {
            if (getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().addObserver(this);
            }
        }

        if(getLayoutParams()==null){
            //设置自己的大小，和Activity的contentView保持一致
            int navHeight = 0;
            View decorView = ((Activity) getContext()).getWindow().getDecorView();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                View navBarView = decorView.findViewById(android.R.id.navigationBarBackground);
                if(navBarView!=null) navHeight = XPopupUtils.isLandscape(getContext()) && !XPopupUtils.isTablet()  ?
                        navBarView.getMeasuredWidth() : navBarView.getMeasuredHeight();
            }else {
                navHeight = XPopupUtils.isNavBarVisible(((Activity) getContext()).getWindow()) ?
                        XPopupUtils.getNavBarHeight() : 0;
            }

            View activityContent = getActivityContentView();
            ViewGroup.MarginLayoutParams params = new MarginLayoutParams(activityContent.getMeasuredWidth(),
                    decorView.getMeasuredHeight() -
                            ( XPopupUtils.isLandscape(getContext()) && !XPopupUtils.isTablet() ? 0 : navHeight));
            if(XPopupUtils.isLandscape(getContext())) params.leftMargin = getActivityContentLeft();
            setLayoutParams(params);
        }

        if (popupInfo.isViewMode) {
            //view实现
            ViewGroup decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
            if(getParent()!=null) ((ViewGroup)getParent()).removeView(this);
            decorView.addView(this);
        } else {
            //dialog实现
            if (dialog == null) {
                dialog = new FullScreenDialog(getContext()).setContent(this);
            }
            if(!dialog.isShowing()) dialog.show(); 
        }
    }

    protected View getWindowDecorView() {
        if (getHostWindow() == null) return null;
        return (ViewGroup) getHostWindow().getDecorView();
    }

    public View getActivityContentView() {
        return ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    }

    protected int getActivityContentLeft(){
        if(!XPopupUtils.isLandscape(getContext())) return 0;
        //以Activity的content的left为准
        View decorView = ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        int[] loc = new int[2];
        decorView.getLocationInWindow(loc);
        return loc[0];
    }

    /**
     * 执行初始化
     */
    protected void init() {
        if (shadowBgAnimator == null)
            shadowBgAnimator = new ShadowBgAnimator(this, getAnimationDuration(), getShadowBgColor());
        if (popupInfo.hasBlurBg) {
            blurAnimator = new BlurAnimator(this, getShadowBgColor());
            blurAnimator.hasShadowBg = popupInfo.hasShadowBg;
            blurAnimator.decorBitmap = XPopupUtils.view2Bitmap((XPopupUtils.context2Activity(this)).getWindow().getDecorView());
        }

        //1. 初始化Popup
        if (this instanceof AttachPopupView || this instanceof BubbleAttachPopupView
                || this instanceof PartShadowPopupView || this instanceof PositionPopupView) {
            initPopupContent();
        } else if (!isCreated) {
            initPopupContent();
        }
        if (!isCreated) {
            isCreated = true;
            onCreate();
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            if (popupInfo.xPopupCallback != null) popupInfo.xPopupCallback.onCreated(this);
        }
        handler.postDelayed(initTask, 10);
    }

    private final Runnable initTask = new Runnable() {
        @Override
        public void run() {
            if (getHostWindow() == null) return;
            if (popupInfo.xPopupCallback != null)
                popupInfo.xPopupCallback.beforeShow(BasePopupView.this);
            beforeShow();
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            if (!(BasePopupView.this instanceof FullScreenPopupView)) focusAndProcessBackPress();

            //由于部分弹窗有个位置设置过程，需要在位置设置完毕自己开启动画
            if (!(BasePopupView.this instanceof AttachPopupView) && !(BasePopupView.this instanceof BubbleAttachPopupView)
                    && !(BasePopupView.this instanceof PositionPopupView)
                    && !(BasePopupView.this instanceof PartShadowPopupView)) {
                initAnimator();

                doShowAnimation();

                doAfterShow();
            }
        }
    };

    protected void initAnimator() {
        getPopupContentView().setAlpha(1f);
        // 优先使用自定义的动画器
        if (popupInfo.customAnimator != null) {
            popupContentAnimator = popupInfo.customAnimator;
            popupContentAnimator.targetView = getPopupContentView();
        } else {
            // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
            popupContentAnimator = genAnimatorByPopupType();
            if (popupContentAnimator == null) {
                popupContentAnimator = getPopupAnimator();
            }
        }

        //3. 初始化动画执行器
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.initAnimator();
        }
        if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.initAnimator();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.initAnimator();
        }
    }

    private void detachFromHost() {
        if (popupInfo != null && popupInfo.isViewMode) {
            ViewGroup decorView = (ViewGroup) getParent();
            if (decorView != null) decorView.removeView(this);
        } else {
            if (dialog != null) dialog.dismiss();
        }
    }

    public Window getHostWindow() {
        if (popupInfo != null && popupInfo.isViewMode) return ((Activity) getContext()).getWindow();
        return dialog == null ? null : dialog.getWindow();
    }

    protected void doAfterShow() {
        handler.removeCallbacks(doAfterShowTask);
        handler.postDelayed(doAfterShowTask, getAnimationDuration());
    }

    protected Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Show;
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            onShow();
            if (BasePopupView.this instanceof FullScreenPopupView) focusAndProcessBackPress();
            if (popupInfo != null && popupInfo.xPopupCallback != null)
                popupInfo.xPopupCallback.onShow(BasePopupView.this);
            //再次检测移动距离
            if (getHostWindow() != null && XPopupUtils.getDecorViewInvisibleHeight(getHostWindow()) > 0 && !hasMoveUp) {
                XPopupUtils.moveUpToKeyboard(XPopupUtils.getDecorViewInvisibleHeight(getHostWindow()), BasePopupView.this);
            }
        }
    };

    private ShowSoftInputTask showSoftInputTask;
    public void focusAndProcessBackPress() {
        if (popupInfo != null && popupInfo.isRequestFocus) {
            setFocusableInTouchMode(true);
            setFocusable(true);
//            requestFocus();
            // 此处焦点可能被内部的EditText抢走，也需要给EditText也设置返回按下监听
            if (Build.VERSION.SDK_INT >= 28) {
                addOnUnhandledKeyListener(this);
            } else {
                setOnKeyListener(new BackPressListener());
            }
//            addOnUnhandledKeyListener(this);

            //let all EditText can process back pressed.
            ArrayList<EditText> list = new ArrayList<>();
            XPopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
            if (list.size() > 0) {
                preSoftMode = getHostWindow().getAttributes().softInputMode;
                if (popupInfo.isViewMode) {
                    getHostWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    hasModifySoftMode = true;
                }
                for (int i = 0; i < list.size(); i++) {
                    final EditText et = list.get(i);
//                    addOnUnhandledKeyListener(et);
                    if (Build.VERSION.SDK_INT >= 28) {
                        addOnUnhandledKeyListener(et);
                    }else {
                        boolean hasSetKeyListener = XPopupUtils.hasSetKeyListener(et);
                        if(!hasSetKeyListener) et.setOnKeyListener(new BackPressListener());
                    }
                    if (i == 0) {
                        if (popupInfo.autoFocusEditText) {
                            et.setFocusable(true);
                            et.setFocusableInTouchMode(true);
                            et.requestFocus();
                            if (popupInfo.autoOpenSoftInput) showSoftInput(et);
                        } else {
                            if (popupInfo.autoOpenSoftInput) showSoftInput(this);
                        }
                    }
                }
            } else {
                if (popupInfo.autoOpenSoftInput) showSoftInput(this);
            }
        }
    }

    @Override
    public boolean onUnhandledKeyEvent(View v, KeyEvent event) {
        return processKeyEvent(event.getKeyCode(), event);
    }

    protected void addOnUnhandledKeyListener(View view){
        ViewCompat.removeOnUnhandledKeyEventListener(view, this);
        ViewCompat.addOnUnhandledKeyEventListener(view, this);
    }

    protected void showSoftInput(View focusView) {
        if (popupInfo != null) {
            if (showSoftInputTask == null) {
                showSoftInputTask = new ShowSoftInputTask(focusView);
            } else {
                handler.removeCallbacks(showSoftInputTask);
            }
            handler.postDelayed(showSoftInputTask, 10);
        }
    }

    public void dismissOrHideSoftInput() {
        if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0) {
            dismiss();
        } else
            KeyboardUtils.hideSoftInput(BasePopupView.this);
    }

    static class ShowSoftInputTask implements Runnable {
        View focusView;

        public ShowSoftInputTask(View focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null) {
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    protected boolean processKeyEvent(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && popupInfo != null) {
            if (popupInfo.isDismissOnBackPressed &&
                    (popupInfo.xPopupCallback == null || !popupInfo.xPopupCallback.onBackPressed(BasePopupView.this))) {
                dismissOrHideSoftInput();
            }
            return true;
        }
        return false;
    }

    class BackPressListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return processKeyEvent(keyCode, event);
        }
    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     */
    protected PopupAnimator genAnimatorByPopupType() {
        if (popupInfo == null || popupInfo.popupAnimation == null) return null;
        switch (popupInfo.popupAnimation) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), popupInfo.popupAnimation);

            case NoAnimation:
                return new EmptyAnimator(getPopupContentView(), getAnimationDuration());
        }
        return null;
    }

    /**
     * 内部使用，自定义弹窗的时候不要重新这个方法
     * @return
     */
    protected abstract int getInnerLayoutId();

    /**
     * 如果你自己继承BasePopupView来做，这个不用实现
     *
     * @return
     */
    protected int getImplLayoutId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，用于每种类型的PopupView自定义自己的动画器
     *
     * @return
     */
    protected PopupAnimator getPopupAnimator() {
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，不要去重写。
     */
    protected void initPopupContent() { }

    /**
     * do init.
     */
    protected void onCreate() { }

    protected void applyDarkTheme() { }

    protected void applyLightTheme() { }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (popupInfo == null) return;
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && shadowBgAnimator!=null) {
            shadowBgAnimator.animateShow();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateShow();
        }
        if (popupContentAnimator != null)
            popupContentAnimator.animateShow();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (popupInfo == null) return;
        if (popupInfo.hasShadowBg && !popupInfo.hasBlurBg && shadowBgAnimator!=null) {
            shadowBgAnimator.animateDismiss();
        } else if (popupInfo.hasBlurBg && blurAnimator != null) {
            blurAnimator.animateDismiss();
        }

        if (popupContentAnimator != null)
            popupContentAnimator.animateDismiss();
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。
     * 而且我们对PopupView执行的动画，也是对它执行的动画
     *
     * @return
     */
    public View getPopupContentView() {
        return getChildAt(0);
    }

    public View getPopupImplView() {
        return ((ViewGroup) getPopupContentView()).getChildAt(0);
    }

    public int getAnimationDuration() {
        if (popupInfo == null) return 0;
        if (popupInfo.popupAnimation == NoAnimation) return 1;
        return popupInfo.animationDuration >= 0 ? popupInfo.animationDuration : XPopup.getAnimationDuration() + 1;
    }

    public int getShadowBgColor() {
        return popupInfo != null && popupInfo.shadowBgColor != 0 ? popupInfo.shadowBgColor : XPopup.getShadowBgColor();
    }

    public int getStatusBarBgColor() {
        return popupInfo != null && popupInfo.statusBarBgColor != 0 ? popupInfo.statusBarBgColor : XPopup.getStatusBarBgColor();
    }

    /**
     * 弹窗的最大宽度，用来限制弹窗的最大宽度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxWidth() {
        return popupInfo.maxWidth;
    }

    /**
     * 弹窗的最大高度，用来限制弹窗的最大高度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxHeight() {
        return popupInfo.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getPopupWidth() {
        return popupInfo.popupWidth;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getPopupHeight() {
        return popupInfo.popupHeight;
    }

    /**
     * 消失
     */
    public void dismiss() {
        handler.removeCallbacks(attachTask);
        handler.removeCallbacks(initTask);
        if (popupStatus == PopupStatus.Dismissing || popupStatus == PopupStatus.Dismiss) return;
        popupStatus = PopupStatus.Dismissing;
        clearFocus();
        if (popupInfo != null && popupInfo.xPopupCallback != null)
            popupInfo.xPopupCallback.beforeDismiss(this);
        beforeDismiss();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        doDismissAnimation();
        doAfterDismiss();
    }

    /**
     * 会等待弹窗show动画执行完毕再消失
     */
    public void smartDismiss() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delayDismiss(getAnimationDuration() + 50);
            }
        });
    }

    public void delayDismiss(long delay) {
        if (delay < 0) delay = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        // PartShadowPopupView要等到完全关闭再关闭输入法，不然有问题
        if (popupInfo != null && popupInfo.autoOpenSoftInput && !(this instanceof PartShadowPopupView))
            KeyboardUtils.hideSoftInput(this);
        handler.removeCallbacks(doAfterDismissTask);
        handler.postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    protected Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Dismiss;
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            if (popupInfo == null) return;
            if (popupInfo.autoOpenSoftInput && BasePopupView.this instanceof PartShadowPopupView)
                KeyboardUtils.hideSoftInput(BasePopupView.this);
            onDismiss();
            XPopup.longClickPoint = null;
            if (popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onDismiss(BasePopupView.this);
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null;//no cache, avoid some bad edge effect.
            }
            if (popupInfo.isRequestFocus && popupInfo.isViewMode) {
                // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                if (getWindowDecorView() != null) {
                    View needFocusView = getWindowDecorView().findViewById(android.R.id.content);
                    if (needFocusView != null) {
                        needFocusView.setFocusable(true);
                        needFocusView.setFocusableInTouchMode(true);
                    }
                }
            }
            // 移除弹窗，GameOver
            detachFromHost();
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }

    public boolean isShow() {
        return popupStatus != PopupStatus.Dismiss;
    }

    public boolean isDismiss() {
        return popupStatus == PopupStatus.Dismiss;
    }

    public void toggle() {
        if (isShow()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 尝试移除弹窗内的Fragment，如果提供了Fragment的名字
     */
    protected void tryRemoveFragments() {
        if (getContext() instanceof FragmentActivity) {
            FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            List<Fragment> fragments = manager.getFragments();
            List<String> internalFragmentNames = getInternalFragmentNames();
            if (fragments != null && fragments.size() > 0 && internalFragmentNames != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    String name = fragments.get(i).getClass().getSimpleName();
                    if (internalFragmentNames.contains(name)) {
                        manager.beginTransaction()
                                .remove(fragments.get(i))
                                .commitAllowingStateLoss();
                    }
                }
            }
        }
    }

    /**
     * 在弹窗内嵌入Fragment的场景中，当弹窗消失后，由于Fragment被Activity的FragmentManager缓存，
     * 会导致弹窗重新创建的时候，Fragment会命中缓存，生命周期不再执行。为了处理这种情况，只需重写：
     * getInternalFragmentNames() 方法，返回嵌入的Fragment名称，XPopup会自动移除Fragment缓存。
     * 名字是: Fragment.getClass().getSimpleName()
     *
     * @return
     */
    protected List<String> getInternalFragmentNames() {
        return null;
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * onDismiss之前执行一次
     */
    protected void beforeDismiss() { }

    /**
     * onCreated之后，onShow之前执行
     */
    protected void beforeShow() {}

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() { }

    protected void onKeyboardHeightChange(int height) { }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        onDetachedFromWindow();
        detachFromHost();
        destroy();
    }

    public void destroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        if (popupInfo != null) {
            popupInfo.atView = null;
            popupInfo.xPopupCallback = null;
            popupInfo.hostLifecycle = null;
            if (popupInfo.customAnimator != null && popupInfo.customAnimator.targetView != null) {
                popupInfo.customAnimator.targetView.animate().cancel();
            }
            if (popupInfo.isViewMode) tryRemoveFragments();
            if (popupInfo.isDestroyOnDismiss) popupInfo = null;
        }
        if (dialog != null) {
            if(dialog.isShowing()) dialog.dismiss();
            dialog.contentView = null;
            dialog = null;
        }
        if (shadowBgAnimator != null && shadowBgAnimator.targetView != null) {
            shadowBgAnimator.targetView.animate().cancel();
        }
        if (blurAnimator != null && blurAnimator.targetView != null) {
            blurAnimator.targetView.animate().cancel();
            if (blurAnimator.decorBitmap != null && !blurAnimator.decorBitmap.isRecycled()) {
                blurAnimator.decorBitmap.recycle();
                blurAnimator.decorBitmap = null;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
        if (popupInfo != null) {
            if (getWindowDecorView() != null)
                KeyboardUtils.removeLayoutChangeListener(getHostWindow(), BasePopupView.this);
            if (popupInfo.isViewMode && hasModifySoftMode) {
                //还原WindowSoftMode
                getHostWindow().setSoftInputMode(preSoftMode);
                hasModifySoftMode = false;
            }
            if (popupInfo.isDestroyOnDismiss) destroy();//如果开启isDestroyOnDismiss，强制释放资源
        }
        if(popupInfo!=null && popupInfo.hostLifecycle!=null){
            popupInfo.hostLifecycle.removeObserver(this);
        }else {
            if (getContext() != null && getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().removeObserver(this);
            }
        }
        popupStatus = PopupStatus.Dismiss;
        showSoftInputTask = null;
        hasMoveUp = false;
    }

    private void passTouchThrough(MotionEvent event) {
        if (popupInfo != null && (popupInfo.isClickThrough || popupInfo.isTouchThrough) ) {
            if (popupInfo.isViewMode) {
                //需要从DecorView分发，并且要排除自己，否则死循环
                ViewGroup decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
                for (int i = 0; i < decorView.getChildCount(); i++) {
                    View view = decorView.getChildAt(i);
                    //自己和兄弟弹窗都不互相分发，否则死循环
                    if (!(view instanceof BasePopupView)) view.dispatchTouchEvent(event);
                }
            } else {
                ((Activity) getContext()).dispatchTouchEvent(event);
            }
        }
    }

    private float x, y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        getPopupImplView().getGlobalVisibleRect(rect);
        if (!XPopupUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    if(popupInfo!=null && popupInfo.xPopupCallback!=null){
                        popupInfo.xPopupCallback.onClickOutside(this);
                    }
                    passTouchThrough(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(popupInfo != null){
                        if(popupInfo.isDismissOnTouchOutside) dismiss();
                        if(popupInfo.isTouchThrough)passTouchThrough(event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    passTouchThrough(event);
                    if (distance < touchSlop && popupInfo != null && popupInfo.isDismissOnTouchOutside) {
                        //查看是否在排除区域外
                        ArrayList<Rect> rects = popupInfo.notDismissWhenTouchInArea;
                        if(rects!=null && rects.size()>0){
                            boolean inRect = false;
                            for (Rect r : rects) {
                                if(XPopupUtils.isInRect(event.getX(), event.getY(), r)){
                                    inRect = true;
                                    break;
                                }
                            }
                            if(!inRect){
                                dismiss();
                            }
                        }else {
                            dismiss();
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        return true;
    }

}
