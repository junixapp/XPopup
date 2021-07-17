package com.lxj.xpopup.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import java.io.File;

/**
 * 能加载超长，超大的图片
 */
public class SmartGlideImageLoader implements XPopupImageLoader {
    int errorImg = 0;
    public SmartGlideImageLoader(){ }

    /**
     * @param errorImgRes 失败图片占位
     */
    public SmartGlideImageLoader(int errorImgRes){
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
                        int maxW = XPopupUtils.getWindowWidth(context)*3;
                        int maxH = XPopupUtils.getScreenHeight(context)*3;

                        int[] size = XPopupUtils.getImageSize(resource);
                        if(size[0] > maxW || size[1] > maxH){
                            //认为是大图
                            imageView.setImageBitmap(XPopupUtils.getBitmap(resource, maxW, maxH));
                        }else {
                            Glide.with(imageView).load(resource).apply(new RequestOptions().override(size[0], size[1])).into(imageView);
                        }
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