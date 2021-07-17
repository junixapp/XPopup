package com.lxj.xpopupdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.util.XPopupUtils;
import java.io.File;

/**
 * 能加载超长，超大的图片
 */
public class SmartGlideImageLoader2 implements XPopupImageLoader {
    int errorImg = 0;
    public SmartGlideImageLoader2(){ }

    /**
     * @param errorImgRes 失败图片占位
     */
    public SmartGlideImageLoader2(int errorImgRes){
        errorImg = errorImgRes;
    }

    @Override
    public void loadImage(final int position, @NonNull final Object url, @NonNull final ImageView imageView,
                          @Nullable final ProgressBar progressBar) {
        if(progressBar!=null)progressBar.setVisibility(View.VISIBLE);
        //支持超大图片，超长图片的加载
        final Context context = imageView.getContext();
        Glide.with(imageView).downloadOnly().load(url)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if(progressBar!=null)progressBar.setVisibility(View.GONE);
                        imageView.setImageResource(errorImg);
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        if(progressBar!=null)progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(ImageUtils.getBitmap(resource, XPopupUtils.getWindowWidth(context)*2, XPopupUtils.getScreenHeight(context)*2));
                    }
                });
    }

    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}