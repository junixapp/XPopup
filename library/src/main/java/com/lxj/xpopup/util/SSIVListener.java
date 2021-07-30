package com.lxj.xpopup.util;

import android.view.View;
import android.widget.ProgressBar;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class SSIVListener implements SubsamplingScaleImageView.OnImageEventListener {

    private final SubsamplingScaleImageView ssiv;
    private final ProgressBar progressBar;
    private final int errorImage;
    public SSIVListener(SubsamplingScaleImageView ssiv, ProgressBar progressBar, int errorImage) {
        this.ssiv = ssiv;
        this.progressBar = progressBar;
        this.errorImage = errorImage;
    }

    @Override
    public void onReady() { }

    @Override
    public void onImageLoaded() {
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onPreviewLoadError(Exception e) { }

    @Override
    public void onImageLoadError(Exception e) {
//        ssiv.animate().alpha(1f).setDuration(500).start();
        ssiv.setImage(ImageSource.resource(errorImage));
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTileLoadError(Exception e) { }

    @Override
    public void onPreviewReleased() { }

}
