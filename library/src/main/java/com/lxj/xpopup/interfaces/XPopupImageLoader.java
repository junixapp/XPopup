package com.lxj.xpopup.interfaces;

import android.support.annotation.NonNull;
import android.widget.ImageView;

public interface XPopupImageLoader{
    void loadImage(int position, @NonNull String url, @NonNull ImageView imageView);
}
