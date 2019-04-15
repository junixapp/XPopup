package com.lxj.xpopup.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.animator.ScrollScaleAnimator;
import com.lxj.xpopup.animator.ShadowBgAnimator;
import com.lxj.xpopup.animator.TranslateAlphaAnimator;
import com.lxj.xpopup.animator.TranslateAnimator;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.util.KeyboardUtils;
import com.lxj.xpopup.util.XPopupUtils;
import java.util.ArrayList;
import static com.lxj.xpopup.enums.PopupAnimation.ScaleAlphaFromCenter;
import static com.lxj.xpopup.enums.PopupAnimation.ScrollAlphaFromLeftTop;
import static com.lxj.xpopup.enums.PopupAnimation.TranslateFromBottom;

/**
 * Description: 弹窗基类
 * Create by lxj, at 2018/12/7
 */
public abstract class BasePopupView extends FrameLayout {
    public PopupInfo popupInfo;
    protected PopupAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    private int touchSlop;
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    private boolean isCreated = false;

    public BasePopupView(@NonNull Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        shadowBgAnimator = new ShadowBgAnimator(this);
        //  添加Popup窗体内容View
        View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        contentView.setAlpha(0);
        addView(contentView);
    }

    public BasePopupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 执行初始化
     *
     */
    public void init() {
        if (popupStatus == PopupStatus.Showing) return;
        popupStatus = PopupStatus.Showing;
        //1. 初始化Popup
        if (!isCreated) {
            isCreated = true;
            onCreate();
        }
        applyOffset();//执行偏移
        initPopupContent();
        post(new Runnable() {
            @Override
            public void run() {
                // 如果有导航栏，则不能覆盖导航栏，
                boolean isLand = Configuration.ORIENTATION_LANDSCAPE == getContext().getResources().getConfiguration().orientation;
                if (XPopupUtils.isNavBarVisible(getContext()) && !isLand && !(BasePopupView.this instanceof FullScreenPopupView)) {
                    FrameLayout.LayoutParams params = (LayoutParams) getLayoutParams();
                    params.bottomMargin = XPopupUtils.getNavBarHeight();
                    setLayoutParams(params);
                }
                getPopupContentView().setAlpha(1f);

                //2. 收集动画执行器
                // 优先使用自定义的动画器
                if (popupInfo.customAnimator != null) {
                    popupContentAnimator = popupInfo.customAnimator;
                    popupContentAnimator.targetView = getPopupContentView();
                } else {
                    // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                    popupContentAnimator = genAnimatorByPopupType();
                    if (popupContentAnimator == null) {
                        // 使用默认的animator
                        popupContentAnimator = getPopupAnimator();
                    }
                }

                //3. 初始化动画执行器
                shadowBgAnimator.initAnimator();
                if (popupContentAnimator != null) {
                    popupContentAnimator.initAnimator();
                }

                //4. 执行动画
                doShowAnimation();

                // call init.
                doAfterShow();
            }
        });

    }

    public BasePopupView show() {
        if (getParent() != null) return this;
        final Activity activity = (Activity) getContext();
        popupInfo.decorView = (ViewGroup) activity.getWindow().getDecorView();
        KeyboardUtils.registerSoftInputChangedListener(activity, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) { // 说明对话框隐藏
                    XPopupUtils.moveDown(BasePopupView.this);
                } else {
                    //when show keyboard, move up
                    XPopupUtils.moveUpToKeyboard(height, BasePopupView.this);
                }
            }
        });
        // 1. add PopupView to its decorView after measured.
        popupInfo.decorView.post(new Runnable() {
            @Override
            public void run() {
                if (getParent() != null) {
                    ((ViewGroup) getParent()).removeView(BasePopupView.this);
                }
                popupInfo.decorView.addView(BasePopupView.this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));

                //2. do init，game start.
                init();
            }
        });
        return this;
    }

    protected void doAfterShow() {
        removeCallbacks(doAfterShowTask);
        postDelayed(doAfterShowTask, getAnimationDuration());
    }

    private Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            popupStatus = PopupStatus.Show;
            onShow();
            if (popupInfo != null && popupInfo.xPopupCallback != null)
                popupInfo.xPopupCallback.onShow();
            if (XPopupUtils.getDecorViewInvisibleHeight((Activity) getContext()) > 0) {
                XPopupUtils.moveUpToKeyboard(XPopupUtils.getDecorViewInvisibleHeight((Activity) getContext()), BasePopupView.this);
            }
            focusAndProcessBackPress();
        }
    };

    private ShowSoftInputTask showSoftInputTask;
    private void focusAndProcessBackPress() {
        // 处理返回按键
        if(popupInfo.isRequestFocus){
            setFocusableInTouchMode(true);
            requestFocus();
        }
        // 此处焦点可能被内容的EditText抢走，也需要给EditText也设置返回按下监听
        setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (popupInfo.isDismissOnBackPressed)
                        dismiss();
                    return true;
                }
                return false;
            }
        });

        //let all EditText can process back pressed.
        ArrayList<EditText> list = new ArrayList<>();
        XPopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
        for (int i = 0; i < list.size(); i++) {
            final View et = list.get(i);
            if (i == 0) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                if (popupInfo.autoOpenSoftInput) {
                    if(showSoftInputTask==null){
                        showSoftInputTask = new ShowSoftInputTask(et);
                    }else {
                        removeCallbacks(showSoftInputTask);
                    }
                    postDelayed(showSoftInputTask, 10);
                }
            }
            et.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (popupInfo.isDismissOnBackPressed)
                            dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    class ShowSoftInputTask implements Runnable{
        View focusView;
        boolean isDone = false;
        public ShowSoftInputTask(View focusView){
            this.focusView = focusView;
        }
        @Override
        public void run() {
            if(focusView!=null && !isDone){
                isDone = true;
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    /**
     * 进行偏移
     */
    protected void applyOffset(){}
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
                return new ScaleAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), popupInfo.popupAnimation);
        }
        return null;
    }

    protected abstract int getPopupLayoutId();

    /**
     * 如果你自己继承BasePopupView来做，这个不用实现
     *
     * @return
     */
    protected int getImplLayoutId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，每种类型的PopupView可以选择返回一个动画器，
     * 父类默认实现是根据popupType字段返回一个默认最佳的动画器
     *
     * @return
     */
    protected PopupAnimator getPopupAnimator() {
        if (popupInfo == null || popupInfo.popupType == null) return null;
        switch (popupInfo.popupType) {
            case Center:
                return new ScaleAlphaAnimator(getPopupContentView(), ScaleAlphaFromCenter);
            case Bottom:
                return new TranslateAnimator(getPopupContentView(), TranslateFromBottom);
            case AttachView:
                return new ScrollScaleAnimator(getPopupContentView(), ScrollAlphaFromLeftTop);
        }
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，未来会移除。
     */
    @Deprecated
    protected void initPopupContent() { }

    /**
     * do init.
     */
    protected void onCreate() { }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    public void doShowAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateShow();
        }
        if (popupContentAnimator != null)
            popupContentAnimator.animateShow();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    public void doDismissAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateDismiss();
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
        return XPopup.getAnimationDuration();
    }

    protected int getMaxWidth() {
        return 0;
    }

    protected int getMaxHeight() {
        return popupInfo.maxHeight;
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (popupStatus == PopupStatus.Dismissing) return;
        popupStatus = PopupStatus.Dismissing;
        clearFocus();
        doDismissAnimation();
        doAfterDismiss();
    }

    protected void doAfterDismiss() {
        if(popupInfo.isRequestFocus)
            KeyboardUtils.hideSoftInput(this);
        removeCallbacks(doAfterDismissTask);
        postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            onDismiss();
            if (popupInfo != null && popupInfo.xPopupCallback != null) {
                popupInfo.xPopupCallback.onDismiss();
            }
            if (dismissWithRunnable != null) dismissWithRunnable.run();
            popupStatus = PopupStatus.Dismiss;
            // 让根布局拿焦点，避免布局内RecyclerView获取焦点导致布局滚动
            if(popupInfo.isRequestFocus) {
                View contentView = ((Activity) getContext()).findViewById(android.R.id.content);
                contentView.setFocusable(true);
                contentView.setFocusableInTouchMode(true);
            }

            // 移除弹窗，GameOver
            popupInfo.decorView.removeView(BasePopupView.this);
            KeyboardUtils.removeLayoutChangeListener(popupInfo.decorView);
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

    public void toggle(){
        if(isShow()){
            dismiss();
        }else {
            show();
        }
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() {
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(doAfterShowTask);
        removeCallbacks(doAfterDismissTask);
        if(showSoftInputTask!=null)removeCallbacks(showSoftInputTask);
        popupStatus = PopupStatus.Dismiss;
        showSoftInputTask = null;
    }

    private float x, y;
    private long downTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件
        // 如果是，则dismiss
        Rect rect = new Rect();
        getPopupContentView().getGlobalVisibleRect(rect);
        if (!XPopupUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && (System.currentTimeMillis() - downTime) < 350) {
                        if (popupInfo.isDismissOnTouchOutside)
                            dismiss();
                    }
                    x = 0;
                    y = 0;
                    downTime = 0;
                    break;
            }
        }
        return true;
    }


}
