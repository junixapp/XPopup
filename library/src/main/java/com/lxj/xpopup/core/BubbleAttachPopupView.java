package com.lxj.xpopup.core;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.animator.ScaleAlphaAnimator;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BubbleLayout;

/**
 * Description: 带气泡背景的Attach弹窗
 */
public abstract class BubbleAttachPopupView extends BasePopupView {
    protected int defaultOffsetY = 0;
    protected int defaultOffsetX = 0;
    protected BubbleLayout bubbleContainer;

    public BubbleAttachPopupView(@NonNull Context context) {
        super(context);
        bubbleContainer = findViewById(R.id.bubbleContainer);
    }

    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), bubbleContainer, false);
        bubbleContainer.addView(contentView);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._xpopup_bubble_attach_popup_view;
    }

    public boolean isShowUp;
    public boolean isShowLeft;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        if (bubbleContainer.getChildCount() == 0) addInnerContent();
        if (popupInfo.atView == null && popupInfo.touchPoint == null)
            throw new IllegalArgumentException("atView() or watchView() must be called for BubbleAttachPopupView before show()！");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bubbleContainer.setElevation(XPopupUtils.dp2px(getContext(), 10));
        }
        bubbleContainer.setShadowRadius(XPopupUtils.dp2px(getContext(), 0f));
        defaultOffsetY = popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX;
        bubbleContainer.setTranslationX(popupInfo.offsetX);
        bubbleContainer.setTranslationY(popupInfo.offsetY);
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(),getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }


    /**
     * 执行倚靠逻辑
     */
    float translationX = 0, translationY = 0;
    // 弹窗显示的位置不能超越Window高度
    float maxY = XPopupUtils.getAppHeight(getContext());
    int overflow = XPopupUtils.dp2px(getContext(), 10);
    float centerY = 0;

    public void doAttach() {
        maxY = XPopupUtils.getAppHeight(getContext()) - overflow;
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if(XPopup.longClickPoint!=null) popupInfo.touchPoint = XPopup.longClickPoint;
            centerY = popupInfo.touchPoint.y;
            // 依附于指定点,尽量优先放在下方，当不够的时候在显示在上方
            //假设下方放不下，超出window高度
            boolean isTallerThanWindowHeight = (popupInfo.touchPoint.y + getPopupContentView().getMeasuredHeight()) > maxY;
            if (isTallerThanWindowHeight) {
                isShowUp = popupInfo.touchPoint.y > XPopupUtils.getScreenHeight(getContext()) / 2;
            } else {
                isShowUp = false;
            }
            isShowLeft = popupInfo.touchPoint.x < XPopupUtils.getAppWidth(getContext()) / 2;

            //限制最大宽高
            ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
            int maxHeight = (int) (isShowUpToTarget() ? (popupInfo.touchPoint.y - XPopupUtils.getStatusBarHeight() - overflow)
                    : (XPopupUtils.getScreenHeight(getContext()) - popupInfo.touchPoint.y - overflow));
            int maxWidth = (int) (isShowLeft ? (XPopupUtils.getAppWidth(getContext()) - popupInfo.touchPoint.x - overflow) : (popupInfo.touchPoint.x - overflow));
            if (getPopupContentView().getMeasuredHeight() > maxHeight) {
                params.height = maxHeight;
            }
            if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                params.width = maxWidth;
            }
            getPopupContentView().setLayoutParams(params);

            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if (isRTL) {
                        translationX = isShowLeft ? -(XPopupUtils.getAppWidth(getContext()) - popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth() - defaultOffsetX)
                                : -(XPopupUtils.getAppWidth(getContext()) - popupInfo.touchPoint.x + defaultOffsetX);
                    } else {
                        translationX = isShowLeft ? (popupInfo.touchPoint.x + defaultOffsetX) : (popupInfo.touchPoint.x - getPopupContentView().getMeasuredWidth() - defaultOffsetX);
                    }
                    if (popupInfo.isCenterHorizontal) {
                        //水平居中
                        if (isShowLeft) {
                            if (isRTL) {
                                translationX += getPopupContentView().getMeasuredWidth() / 2f;
                            } else {
                                translationX -= getPopupContentView().getMeasuredWidth() / 2f;
                            }
                        } else {
                            if (isRTL) {
                                translationX -= getPopupContentView().getMeasuredWidth() / 2f;
                            } else {
                                translationX += getPopupContentView().getMeasuredWidth() / 2f;
                            }
                        }
                    }
                    if (isShowUpToTarget()) {
                        // 应显示在point上方
                        // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                        translationY = popupInfo.touchPoint.y - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
                    } else {
                        translationY = popupInfo.touchPoint.y + defaultOffsetY;
                    }
                    //设置气泡相关
                    if(isShowUpToTarget()){
                        bubbleContainer.setLook(BubbleLayout.Look.BOTTOM);
                    }else {
                        bubbleContainer.setLook(BubbleLayout.Look.TOP);
                    }
                    if(popupInfo.isCenterHorizontal){
                        bubbleContainer.setLookPositionCenter(true);
                    }else {
                        if(isShowLeft){
                            //在目标左边，箭头在最右边
                            bubbleContainer.setLookPosition(XPopupUtils.dp2px(getContext(),1));
                        }else {
                            //在目标右边，箭头在最开始
                            bubbleContainer.setLookPosition(bubbleContainer.getMeasuredWidth()-XPopupUtils.dp2px(getContext(),1));
                        }
                    }
                    bubbleContainer.invalidate();
                    translationX -= getActivityContentLeft();
                    getPopupContentView().setTranslationX(translationX);
                    getPopupContentView().setTranslationY(translationY);
                    initAndStartAnimation();
                }
            });

        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            final Rect rect = popupInfo.getAtViewRect();
            final int centerX = (rect.left + rect.right) / 2;

            // 尽量优先放在下方，当不够的时候在显示在上方
            //假设下方放不下，超出window高度
            boolean isTallerThanWindowHeight = (rect.bottom + getPopupContentView().getMeasuredHeight()) > maxY;
            centerY = (rect.top + rect.bottom) / 2;
            if (isTallerThanWindowHeight) {
                //超出可用大小就显示在上方
                isShowUp = true;
//                isShowUp = centerY > XPopupUtils.getScreenHeight(getContext()) / 2;
            } else {
                isShowUp = false;
            }
            isShowLeft = centerX < XPopupUtils.getAppWidth(getContext()) / 2;

            //修正高度，弹窗的高有可能超出window区域
//            if (!isCreated) {
                ViewGroup.LayoutParams params = getPopupContentView().getLayoutParams();
                int maxHeight = isShowUpToTarget() ? (rect.top - XPopupUtils.getStatusBarHeight() - overflow)
                        : (XPopupUtils.getScreenHeight(getContext()) - rect.bottom - overflow);
                int maxWidth = isShowLeft ? (XPopupUtils.getAppWidth(getContext()) - rect.left - overflow) : (rect.right - overflow);
                if (getPopupContentView().getMeasuredHeight() > maxHeight) {
                    params.height = maxHeight;
                }
                if (getPopupContentView().getMeasuredWidth() > maxWidth) {
                    params.width = maxWidth;
                }
                getPopupContentView().setLayoutParams(params);
//            }

            getPopupContentView().post(new Runnable() {
                @Override
                public void run() {
                    if (isRTL) {
                        translationX = isShowLeft ? -(XPopupUtils.getAppWidth(getContext()) - rect.left - getPopupContentView().getMeasuredWidth() - defaultOffsetX)
                                : -(XPopupUtils.getAppWidth(getContext()) - rect.right + defaultOffsetX);
                    } else {
                        translationX = isShowLeft ? (rect.left + defaultOffsetX) : (rect.right - getPopupContentView().getMeasuredWidth() - defaultOffsetX);
                    }
                    if (popupInfo.isCenterHorizontal) {
                        //水平居中
                        if (isShowLeft)
                            if (isRTL) {
                                translationX -= (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            } else {
                                translationX += (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            }
                        else {
                            if (isRTL) {
                                translationX += (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            } else {
                                translationX -= (rect.width() - getPopupContentView().getMeasuredWidth()) / 2f;
                            }
                        }
                    }
                    if (isShowUpToTarget()) {
                        //说明上面的空间比较大，应显示在atView上方
                        // translationX: 在左边就和atView左边对齐，在右边就和其右边对齐
                        translationY = rect.top - getPopupContentView().getMeasuredHeight() - defaultOffsetY;
                    } else {
                        translationY = rect.bottom + defaultOffsetY;
                    }

                    //设置气泡相关
                    if(isShowUpToTarget()){
                        bubbleContainer.setLook(BubbleLayout.Look.BOTTOM);
                    }else {
                        bubbleContainer.setLook(BubbleLayout.Look.TOP);
                    }
                    //箭头对着目标View的中心
                    if(popupInfo.isCenterHorizontal){
                        bubbleContainer.setLookPositionCenter(true);
                    }else {
                        bubbleContainer.setLookPosition(rect.left + rect.width()/2 - (int)translationX);
                    }
                    bubbleContainer.invalidate();
                    translationX -= getActivityContentLeft();
                    getPopupContentView().setTranslationX(translationX);
                    getPopupContentView().setTranslationY(translationY);
                    initAndStartAnimation();
                }
            });
        }


    }

    protected void initAndStartAnimation(){
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    //是否显示在目标上方
    protected boolean isShowUpToTarget() {
        if(popupInfo.positionByWindowCenter){
            //目标在屏幕上半方，弹窗显示在下；反之，则在上
            return centerY > XPopupUtils.getAppHeight(getContext())/2;
        }
        //默认是根据Material规范定位，优先显示在目标下方，下方距离不足才显示在上方
        return (isShowUp || popupInfo.popupPosition == PopupPosition.Top)
                && popupInfo.popupPosition != PopupPosition.Bottom;
    }

    /**
     * 设置气泡箭头的偏移位置
     * @param offset
     * @return
     */
    public BubbleAttachPopupView setArrowOffset(int offset){
        bubbleContainer.arrowOffset = offset;
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡背景颜色
     * @param color
     * @return
     */
    public BubbleAttachPopupView setBubbleBgColor(int color){
        bubbleContainer.setBubbleColor(color);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡背景圆角
     * @param radius
     * @return
     */
    public BubbleAttachPopupView setBubbleRadius(int radius){
        bubbleContainer.setBubbleRadius(radius);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡箭头的宽度
     * @param width
     * @return
     */
    public BubbleAttachPopupView setArrowWidth(int width){
        bubbleContainer.setLookWidth(width);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡箭头的高度
     * @param height
     * @return
     */
    public BubbleAttachPopupView setArrowHeight(int height){
        bubbleContainer.setLookLength(height);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡阴影大小
     * @param size
     * @return
     */
    public BubbleAttachPopupView setBubbleShadowSize(int size){
        bubbleContainer.setShadowRadius(size);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡阴影颜色
     * @param color
     * @return
     */
    public BubbleAttachPopupView setBubbleShadowColor(int color){
        bubbleContainer.setShadowColor(color);
        bubbleContainer.invalidate();
        return this;
    }

    /**
     * 设置气泡箭头的圆角，默认是1dp
     * @param radius
     * @return
     */
    public BubbleAttachPopupView setArrowRadius(int radius){
        bubbleContainer.setArrowRadius(radius);
        bubbleContainer.invalidate();
        return this;
    }

    @Override
    protected PopupAnimator getPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(),PopupAnimation.ScaleAlphaFromCenter);
    }
}
