package com.lxj.xpopup.interfaces;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public interface XPopupImageLoader{

    void loadSnapshot(@NonNull Object uri, @NonNull ImageView snapshot);

    void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView, @NonNull ImageView snapshot,
                   @NonNull SubsamplingScaleImageView bigImageView,@NonNull ProgressBar progressBar);

    /**
     * 获取图片对应的文件
     * @param context
     * @param uri
     * @return
     */
    File getImageFile(@NonNull Context context, @NonNull Object uri);
}
