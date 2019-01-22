package com.lxj.xpopup.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.animation.MatrixEvaluator;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lxj.xpopup.R;
import com.lxj.xpopup.interfaces.OnDragChangeListener;
import com.lxj.xpopup.interfaces.OnLoadImageListener;
import com.lxj.xpopup.photoview.PhotoView;
import com.lxj.xpopup.util.MatrixUtils;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.HackyViewPager;
import com.lxj.xpopup.widget.PhotoViewContainer;

import java.util.ArrayList;

/**
 * Description: 图片预览的弹窗
 * Create by lxj, at 2019/1/22
 */
public class ImagePreviewPopupView extends BasePopupView implements OnDragChangeListener {
    FrameLayout photoViewContainer;
    HackyViewPager pager;
    IntEvaluator intEvaluator = new IntEvaluator();
    FloatEvaluator floatEvaluator = new FloatEvaluator();
    MatrixEvaluator matrixEvaluator = new MatrixEvaluator();
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
//        photoViewContainer.setOnDragChangeListener(this);
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PhotoViewAdapter());
        pager.setVisibility(INVISIBLE);
        addSnapshot();
    }

    ImageView startView, endView;
    Matrix startMatrix,endMatrix;
    private void addSnapshot() {
        //1. add startView
        startView = new ImageView(getContext());
        startView.setScaleType(srcView.getScaleType());
        startView.setImageBitmap(bitmap);
        photoViewContainer.addView(startView);
        startView.setTranslationX(rect.left);
        startView.setTranslationY(rect.top);
        XPopupUtils.setWidthHeight(startView, rect.width(), rect.height());

        //2. add endView
        endView = new ImageView(getContext());
        endView.setVisibility(INVISIBLE);
        endView.setImageBitmap(bitmap);
//        endView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoViewContainer.addView(endView);
        XPopupUtils.setWidthHeight(endView, XPopupUtils.getWindowWidth(getContext()), XPopupUtils.getWindowHeight(getContext()));
    }

    @Override
    public void doShowAnimation() {
        startView.setScaleType(ImageView.ScaleType.MATRIX);
        startMatrix = MatrixUtils.getImageMatrix(startView);
        endMatrix = MatrixUtils.getImageMatrix(endView);
        final float startX = startView.getTranslationX();
        final float startY = startView.getTranslationY();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                int w = intEvaluator.evaluate(fraction, srcView.getWidth(), pager.getWidth());
                int h = intEvaluator.evaluate(fraction, srcView.getHeight(), pager.getHeight());
                XPopupUtils.setWidthHeight(startView, w, h);
                startView.setTranslationX(floatEvaluator.evaluate(fraction, startX, 0f));
                startView.setTranslationY(floatEvaluator.evaluate(fraction, startY, 0f));
                startView.setImageMatrix(matrixEvaluator.evaluate(fraction, startMatrix, endMatrix));
//                photoViewContainer.applyBgAnimation(animation.getAnimatedFraction());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startView.setVisibility(INVISIBLE);
                pager.setVisibility(VISIBLE);
            }
        });
        animator.setDuration(shadowBgAnimator.animateDuration)
                .start();
    }

    @Override
    public void doDismissAnimation() {
        pager.setVisibility(INVISIBLE);
        startView.setVisibility(VISIBLE);

        final float startX = startView.getTranslationX();
        final float startY = startView.getTranslationY();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                int w = intEvaluator.evaluate(fraction, pager.getWidth(),srcView.getWidth());
                int h = intEvaluator.evaluate(fraction, pager.getHeight(), srcView.getHeight());
                XPopupUtils.setWidthHeight(startView, w, h);
                startView.setTranslationX(floatEvaluator.evaluate(fraction,startX,  rect.left));
                startView.setTranslationY(floatEvaluator.evaluate(fraction, startY, rect.top));
                startView.setImageMatrix(matrixEvaluator.evaluate(fraction, endMatrix, startMatrix));
//                photoViewContainer.applyBgAnimation(animation.getAnimatedFraction());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ImagePreviewPopupView.super.dismiss();
            }
        });
        animator.setDuration(shadowBgAnimator.animateDuration)
                .start();
    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
     *
     * @return
     */
    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
//        drawerLayout.close();
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
        srcView.getLocationOnScreen(locations);
        rect = new Rect(locations[0], locations[1], locations[0] + srcView.getWidth(), locations[1] + srcView.getHeight());
        Log.e("tag", rect.toShortString() + " left: "+srcView.getLeft());
        return this;
    }

    @Override
    public void onRelease() {

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
