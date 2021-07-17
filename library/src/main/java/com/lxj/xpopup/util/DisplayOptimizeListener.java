package com.lxj.xpopup.util;


import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * credit: https://github.com/Piasy/BigImageViewer/issues/2
 */

public class DisplayOptimizeListener implements SubsamplingScaleImageView.OnImageEventListener {

    private final SubsamplingScaleImageView mImageView;
    boolean isCenterCrop;
    public DisplayOptimizeListener(SubsamplingScaleImageView imageView) {
        mImageView = imageView;
    }
    public DisplayOptimizeListener(SubsamplingScaleImageView imageView, boolean isCenterCrop) {
        mImageView = imageView;
        this.isCenterCrop = isCenterCrop;
    }

    @Override
    public void onReady() {
        float result = 0.5f;
        int imageWidth = mImageView.getSWidth();
        int imageHeight = mImageView.getSHeight();
        int viewWidth = mImageView.getWidth();
        int viewHeight = mImageView.getHeight();

//        if(isCenterCrop){
            if(imageHeight*1f/ imageWidth  > viewHeight*1f/viewWidth   ){
                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                mImageView.setScaleAndCenter(1f, new PointF(imageWidth/2f, 0));
//                mImageView.animateScaleAndCenter(1f, new PointF(imageWidth/2f, 0));
            }else {
                mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                mImageView.setScaleAndCenter(1f, new PointF(imageWidth/2f, imageHeight/2f));
            }
            mImageView.setDoubleTapZoomScale(5f);
//            mImageView.animateScaleAndCenter(1f, new PointF())
//            mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//            mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
            return;
//        }

//        boolean hasZeroValue = false;
//        if (imageWidth == 0 || imageHeight == 0 || viewWidth == 0 || viewHeight == 0) {
//            result = 0.5f;
//            hasZeroValue = true;
//        }
//
//        if (!hasZeroValue) {
//            if (imageWidth <= imageHeight) {
//                result = (float) viewWidth / imageWidth;
//            } else {
//                result = (float) viewHeight / imageHeight;
//            }
//        }

//        mImageView.setMinScale(2f);
////        mImageView.setMaxScale(5f);
//        mImageView.setDoubleTapZoomScale(5f);
//        mImageView.setScaleAndCenter(1f, new PointF(imageWidth/2f, imageHeight/2f));
//
//        if (!hasZeroValue && (float) imageHeight / imageWidth > LONG_IMAGE_SIZE_RATIO) {
//            // scale at top
//
//            mImageView
//                    .animateScaleAndCenter(result, new PointF(imageWidth / 2, 0))
//                    .withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD)
//                    .start();
//        }
//
//        // `对结果进行放大裁定，防止计算结果跟双击放大结果过于相近`
//        if (Math.abs(result - 0.1) < 0.2f) {
//            result += 0.2f;
//        }
//
//        if (mInitScaleType == SubsamplingScaleImageView.SCALE_TYPE_CUSTOM) {
//            float maxScale = Math.max((float) viewWidth / imageWidth,
//                    (float) viewHeight / imageHeight);
//            if (maxScale > 1) {
//                // image is smaller than screen, it should be zoomed out to its origin size
//                mImageView.setMinScale(1);
//
//                // and it should be zoomed in to fill the screen
//                float defaultMaxScale = mImageView.getMaxScale();
//                mImageView.setMaxScale(Math.max(defaultMaxScale, maxScale * 1.2F));
//            } else {
//                // image is bigger than screen, it should be zoomed out to fit the screen
//                float minScale = Math.min((float) viewWidth / imageWidth,
//                        (float) viewHeight / imageHeight);
//                mImageView.setMinScale(minScale);
//                // but no need to set max scale
//            }
//            // scale to fit screen, and center
//            mImageView.setScaleAndCenter(maxScale, new PointF(imageWidth / 2, imageHeight / 2));
//        }
//
//        mImageView.setDoubleTapZoomScale(result);
    }

    @Override
    public void onImageLoaded() {

    }

    @Override
    public void onPreviewLoadError(Exception e) {

    }

    @Override
    public void onImageLoadError(Exception e) {

    }

    @Override
    public void onTileLoadError(Exception e) {

    }

    @Override
    public void onPreviewReleased() {

    }

}
