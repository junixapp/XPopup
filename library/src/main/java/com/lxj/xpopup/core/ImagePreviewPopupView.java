package com.lxj.xpopup.core;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Slide;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lxj.xpopup.R;
import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.interfaces.OnDragChangeListener;
import com.lxj.xpopup.interfaces.OnLoadImageListener;
import com.lxj.xpopup.photoview.PhotoView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.HackyViewPager;
import com.lxj.xpopup.widget.PhotoViewContainer;

import java.util.ArrayList;

;

/**
 * Description: 图片预览的弹窗
 * Create by lxj, at 2019/1/22
 */
public class ImagePreviewPopupView extends BasePopupView implements OnDragChangeListener {
    PhotoViewContainer photoViewContainer;
    HackyViewPager pager;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    IntEvaluator intEvaluator = new IntEvaluator();
    FloatEvaluator floatEvaluator = new FloatEvaluator();

    public ImagePreviewPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout._xpopup_image_preview_popup_view;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        photoViewContainer = findViewById(R.id.photoViewContainer);
        photoViewContainer.setOnDragChangeListener(this);
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PhotoViewAdapter());
        pager.setVisibility(INVISIBLE);
        addSnapshot();
    }

    ImageView snapshotView;

    private void addSnapshot() {
        snapshotView = new ImageView(getContext());
        snapshotView.setScaleType(srcView.getScaleType());
        snapshotView.setImageBitmap(bitmap);
        photoViewContainer.addView(snapshotView);
//        snapshotView.setTranslationX(rect.left);
        snapshotView.setTranslationY(rect.top);
        XPopupUtils.setWidthHeight(snapshotView, rect.width(), rect.height());

    }

    @Override
    public void doShowAnimation() {
        snapshotView.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition((ViewGroup) snapshotView.getParent(), new TransitionSet()
                        .setDuration(shadowBgAnimator.animateDuration)
                        .addTransition(new Slide())
                        .addTransition(new ChangeBounds())
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeImageTransform())
                        .addListener(new TransitionListenerAdapter() {
                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                pager.setVisibility(VISIBLE);
                                snapshotView.setVisibility(INVISIBLE);
                            }
                        }));
                snapshotView.setTranslationY(0);
                snapshotView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                XPopupUtils.setWidthHeight(snapshotView, photoViewContainer.getWidth(), photoViewContainer.getHeight());

                // do shadow anim.
                animateShadowBg(Color.BLACK);
            }
        });

    }

    private void animateShadowBg(final int endColor) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                photoViewContainer.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(),
                        ((ColorDrawable) photoViewContainer.getBackground()).getColor(), endColor));
            }
        });
        animator.setDuration(shadowBgAnimator.animateDuration)
                .start();
    }

    @Override
    public void doDismissAnimation() {
        pager.setVisibility(INVISIBLE);
        snapshotView.setVisibility(VISIBLE);
        TransitionManager.beginDelayedTransition((ViewGroup) snapshotView.getParent(), new TransitionSet()
                .setDuration(shadowBgAnimator.animateDuration)
                .addTransition(new ChangeBounds())
                .addTransition(new Slide())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform())
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
                    }
                }));

        snapshotView.setTranslationY(rect.top);
        snapshotView.setTranslationX(0);
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

    private ArrayList<String> urls = new ArrayList<>();
    private OnLoadImageListener loadImageListener;

    public ImagePreviewPopupView setImageUrls(ArrayList<String> urls) {
        this.urls = urls;
        return this;
    }

    public ImagePreviewPopupView setLoadImageListener(OnLoadImageListener loadImageListener) {
        this.loadImageListener = loadImageListener;
        return this;
    }

    Rect rect = null;
    ImageView srcView;
    Bitmap bitmap;

    public ImagePreviewPopupView setSrcView(ImageView srcView, Bitmap bitmap) {
        this.srcView = srcView;
        this.bitmap = bitmap;
        int[] locations = new int[2];
        srcView.getLocationInWindow(locations);
        rect = new Rect(locations[0], locations[1], locations[0] + srcView.getWidth(), locations[1] + srcView.getHeight());
        Log.e("tag", rect.toShortString());
        return this;
    }

    @Override
    public void onRelease() {
        Log.e("tag", "onReleaseonRelease w: " + pager.getWidth() * pager.getScaleX());
        int w = (int) (pager.getWidth() * pager.getScaleX());
        int h = (int) (pager.getHeight() * pager.getScaleY());
        int x = (XPopupUtils.getWindowWidth(getContext()) - w) / 2;
        int y = (XPopupUtils.getWindowHeight(getContext()) - h) / 2;
        XPopupUtils.setWidthHeight(snapshotView, w, h);
        snapshotView.setTranslationX(x);
        snapshotView.setTranslationY(y);
//        dismiss();
    }

    @Override
    public void onDragChange(int dy, float scale) {

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
            PhotoView photoView = new PhotoView(container.getContext());
            // call LoadImageListener
            if (loadImageListener != null) {
                loadImageListener.loadImage(position, urls.get(position), photoView);
            }
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


}
