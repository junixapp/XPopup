package com.lxj.xpopup.interfaces;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.File;

public interface XPopupImageLoader{
    void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView);
    File getImageFile(int position, @NonNull Object uri, @NonNull ImageView imageView);
}
