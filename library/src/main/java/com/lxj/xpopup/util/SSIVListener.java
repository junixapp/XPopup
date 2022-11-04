package com.lxj.xpopup.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class SSIVListener implements SubsamplingScaleImageView.OnImageEventListener {

    private final SubsamplingScaleImageView ssiv;
    private final ProgressBar progressBar;
    private final File resource;
    private final int errorImage;
    private final boolean longImage;
    public SSIVListener(SubsamplingScaleImageView ssiv, ProgressBar progressBar, int errorImage, boolean longImage, File resource) {
        this.ssiv = ssiv;
        this.progressBar = progressBar;
        this.errorImage = errorImage;
        this.longImage = longImage;
        this.resource = resource;
    }

    @Override
    public void onReady() { }

    @Override
    public void onImageLoaded() {
        progressBar.setVisibility(View.INVISIBLE);
        if (longImage) {
            ssiv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
        } else {
            ssiv.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        }

    }
    @Override
    public void onPreviewLoadError(Exception e) { }

    @Override
    public void onImageLoadError(Exception e) {
//        ssiv.animate().alpha(1f).setDuration(500).start();
//        e.printStackTrace();
        Bitmap bitmap = XPopupUtils.getBitmap(resource, ssiv.getMeasuredWidth(), ssiv.getMeasuredHeight());
        ssiv.setImage(bitmap==null ? ImageSource.resource(errorImage) : ImageSource.bitmap(bitmap));
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTileLoadError(Exception e) { }

    @Override
    public void onPreviewReleased() { }

}
