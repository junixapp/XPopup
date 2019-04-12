package com.lxj.xpopup.core;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpermission.PermissionConstants;
import com.lxj.xpermission.XPermission;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.interfaces.OnDragChangeListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.photoview.PhotoView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.BlankView;
import com.lxj.xpopup.widget.HackyViewPager;
import com.lxj.xpopup.widget.PhotoViewContainer;

import java.util.ArrayList;
import java.util.List;


/**
 * Description: 大图预览的弹窗，使用Transition实现
 * Create by lxj, at 2019/1/22
 */
public class ImageViewerPopupView extends BasePopupView implements OnDragChangeListener, View.OnClickListener {
    protected PhotoViewContainer photoViewContainer;
    protected BlankView placeholderView;
    protected TextView tv_pager_indicator, tv_save;
    protected HackyViewPager pager;
    protected ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private List<Object> urls = new ArrayList<>();
    private XPopupImageLoader imageLoader;
    private OnSrcViewUpdateListener srcViewUpdateListener;
    private int position;
    protected Rect rect = null;
    protected ImageView srcView;
    boolean isShowPlaceholder = true;
    int placeholderColor = -1; //占位View的颜色
    int placeholderStrokeColor = -1; // 占位View的边框色
    int placeholderRadius = -1; // 占位View的圆角
    boolean isShowSaveBtn = true; //是否显示保存按钮

    public ImageViewerPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_image_viewer_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_pager_indicator = findViewById(R.id.tv_pager_indicator);
        tv_save = findViewById(R.id.tv_save);
        placeholderView = findViewById(R.id.placeholderView);
        photoViewContainer = findViewById(R.id.photoViewContainer);
        photoViewContainer.setOnDragChangeListener(this);
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PhotoViewAdapter());
        pager.setOffscreenPageLimit(urls.size());
        pager.setCurrentItem(position);
        pager.setVisibility(INVISIBLE);
        addOrUpdateSnapshot();

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                position = i;
                showPagerIndicator();
                //更新srcView
                if (srcViewUpdateListener != null) {
                    srcViewUpdateListener.onSrcViewUpdate(ImageViewerPopupView.this , i);
                }
            }
        });
        if(isShowSaveBtn)tv_save.setOnClickListener(this);
    }

    private void setupPlaceholder(){
        placeholderView.setVisibility(isShowPlaceholder ? VISIBLE : INVISIBLE);
        if(isShowPlaceholder){
            if(placeholderColor!=-1){
                placeholderView.color = placeholderColor;
            }
            if(placeholderRadius!=-1) {
                placeholderView.radius = placeholderRadius;
            }
            if(placeholderStrokeColor!=-1) {
                placeholderView.strokeColor = placeholderStrokeColor;
            }
            XPopupUtils.setWidthHeight(placeholderView, rect.width(), rect.height());
            placeholderView.setTranslationX(rect.left);
            placeholderView.setTranslationY(rect.top);
            placeholderView.invalidate();
        }
    }

    private void showPagerIndicator() {
        if (urls.size() > 1) {
            tv_pager_indicator.setVisibility(VISIBLE);
            tv_pager_indicator.setText((position + 1) + "/" + urls.size());
        }
        if(isShowSaveBtn)tv_save.setVisibility(VISIBLE);
    }

    ImageView snapshotView;

    private void addOrUpdateSnapshot() {
        if (snapshotView == null) {
            snapshotView = new PhotoView(getContext());
            photoViewContainer.addView(snapshotView);
            snapshotView.setScaleType(srcView.getScaleType());
            snapshotView.setTranslationX(rect.left);
            snapshotView.setTranslationY(rect.top);
            XPopupUtils.setWidthHeight(snapshotView, rect.width(), rect.height());
        }
        setupPlaceholder();
        snapshotView.setImageDrawable(srcView.getDrawable());
    }

    @Override
    protected void doAfterShow() {
        //do nothing self.
    }

    @Override
    public void doShowAnimation() {
        photoViewContainer.isReleaseing = true;
        snapshotView.setVisibility(VISIBLE);
        snapshotView.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition((ViewGroup) snapshotView.getParent(), new TransitionSet()
                        .setDuration(XPopup.getAnimationDuration())
                        .addTransition(new ChangeBounds())
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeImageTransform())
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .addListener(new TransitionListenerAdapter() {
                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                pager.setVisibility(VISIBLE);
                                snapshotView.setVisibility(INVISIBLE);
                                showPagerIndicator();
                                photoViewContainer.isReleaseing = false;
                                ImageViewerPopupView.super.doAfterShow();
                            }
                        }));
                snapshotView.setTranslationY(0);
                snapshotView.setTranslationX(0);
                snapshotView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                XPopupUtils.setWidthHeight(snapshotView, photoViewContainer.getWidth(), photoViewContainer.getHeight());

                // do shadow anim.
                animateShadowBg(photoViewContainer.blackColor);
            }
        });

    }

    private void animateShadowBg(final int endColor) {
        final int start = ((ColorDrawable) photoViewContainer.getBackground()).getColor();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                photoViewContainer.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(),
                        start, endColor));
            }
        });
        animator.setDuration(XPopup.getAnimationDuration())
                .setInterpolator(new LinearInterpolator());
        animator.start();
    }

    @Override
    public void doDismissAnimation() {
        tv_pager_indicator.setVisibility(INVISIBLE);
        tv_save.setVisibility(INVISIBLE);
        pager.setVisibility(INVISIBLE);
        snapshotView.setVisibility(VISIBLE);
        photoViewContainer.isReleaseing = true;
        TransitionManager.beginDelayedTransition((ViewGroup) snapshotView.getParent(), new TransitionSet()
                .setDuration(XPopup.getAnimationDuration())
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform())
                .setInterpolator(new FastOutSlowInInterpolator())
                .addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        doAfterDismiss();
                        pager.setVisibility(INVISIBLE);
                        snapshotView.setVisibility(VISIBLE);
                        pager.setScaleX(1f);
                        pager.setScaleY(1f);
                        snapshotView.setScaleX(1f);
                        snapshotView.setScaleY(1f);
                        placeholderView.setVisibility(INVISIBLE);
                    }
                }));

        snapshotView.setTranslationY(rect.top);
        snapshotView.setTranslationX(rect.left);
        snapshotView.setScaleX(1f);
        snapshotView.setScaleY(1f);
        snapshotView.setScaleType(srcView.getScaleType());
        XPopupUtils.setWidthHeight(snapshotView, rect.width(), rect.height());

        // do shadow anim.
        animateShadowBg(Color.TRANSPARENT);
    }

    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        if (popupStatus != PopupStatus.Show) return;
        popupStatus = PopupStatus.Dismissing;
        doDismissAnimation();
    }

    public ImageViewerPopupView setImageUrls(List<Object> urls) {
        this.urls = urls;
        return this;
    }

    public ImageViewerPopupView setSrcViewUpdateListener(OnSrcViewUpdateListener srcViewUpdateListener) {
        this.srcViewUpdateListener = srcViewUpdateListener;
        return this;
    }

    public ImageViewerPopupView setXPopupImageLoader(XPopupImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public ImageViewerPopupView isShowPlaceholder(boolean isShow) {
        this.isShowPlaceholder = isShow;
        return this;
    }

    public ImageViewerPopupView isShowSaveButton(boolean isShowSaveBtn) {
        this.isShowSaveBtn = isShowSaveBtn;
        return this;
    }

    public ImageViewerPopupView setPlaceholderColor(int color){
        this.placeholderColor = color;
        return this;
    }
    public ImageViewerPopupView setPlaceholderRadius(int radius){
        this.placeholderRadius = radius;
        return this;
    }
    public ImageViewerPopupView setPlaceholderStrokeColor(int strokeColor){
        this.placeholderStrokeColor = strokeColor;
        return this;
    }

    /**
     * 设置单个使用的源View。单个使用的情况下，无需设置url集合和SrcViewUpdateListener
     *
     * @param srcView
     * @return
     */
    public ImageViewerPopupView setSingleSrcView(ImageView srcView, Object url) {
        if (this.urls == null) {
            urls = new ArrayList<>();
        }
        urls.clear();
        urls.add(url);
        setSrcView(srcView, 0);
        return this;
    }

    public ImageViewerPopupView setSrcView(ImageView srcView, int position) {
        this.srcView = srcView;
        this.position = position;
        int[] locations = new int[2];
        this.srcView.getLocationInWindow(locations);
        rect = new Rect(locations[0], locations[1], locations[0] + srcView.getWidth(), locations[1] + srcView.getHeight());
        return this;
    }

    public void updateSrcView(ImageView srcView) {
        setSrcView(srcView, position);
        addOrUpdateSnapshot();
    }

    @Override
    public void onRelease() {
        dismiss();
    }

    @Override
    public void onDragChange(int dy, float scale, float fraction) {
        tv_pager_indicator.setAlpha(1 - fraction);
        if(isShowSaveBtn)tv_save.setAlpha(1 - fraction);
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        srcView = null;
    }

    @Override
    public void onClick(View v) {
        if(v==tv_save){
            //check permission
            XPermission.create(getContext(), PermissionConstants.STORAGE)
                    .callback(new XPermission.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            //save bitmap to album.
                            XPopupUtils.saveBmpToAlbum(getContext(), imageLoader, urls.get(position));
                        }
                        @Override
                        public void onDenied() {
                            Toast.makeText(getContext(), "没有保存权限，保存功能无法使用！", Toast.LENGTH_SHORT).show();
                        }
                    }).request();
        }
    }

    public class PhotoViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return o == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            // call LoadImageListener
            if (imageLoader != null) {
                imageLoader.loadImage(position, urls.get(position), photoView);
            }
            container.addView(photoView);
            photoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoView.getScale() == 1.0f) {
                        dismiss();
                    }
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


}
