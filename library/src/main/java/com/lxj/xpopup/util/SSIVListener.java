package com.lxj.xpopup.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class SSIVListener implements SubsamplingScaleImageView.OnImageEventListener {

    private final ImageView snapshot;
    private final ProgressBar progressBar;
    public SSIVListener(ImageView snapshot, ProgressBar progressBar) {
        this.snapshot = snapshot;
        this.progressBar = progressBar;
    }

    @Override
    public void onReady() { }

    @Override
    public void onImageLoaded() {
        snapshot.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onPreviewLoadError(Exception e) { }

    @Override
    public void onImageLoadError(Exception e) {
        snapshot.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTileLoadError(Exception e) { }

    @Override
    public void onPreviewReleased() { }

}
