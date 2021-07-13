package com.lxj.xpopup.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import java.io.File;

/**
 * 能加载超长，超大的图片
 */
public class SmartGlideImageLoader implements XPopupImageLoader {
    int errorImg = 0;
    int maxMemorySize = 10 * 1024 * 1024; //以图片内存10M为限制进行采样，对超大超长图片有影响
    public SmartGlideImageLoader(){ }

    /**
     * @param errorImgRes 失败图片占位
     */
    public SmartGlideImageLoader(int errorImgRes){
        errorImg = errorImgRes;
    }

    /**
     * @param memorySizeLimit 限制图片最大的内存大小
     * @param errorImgRes 失败图片占位
     */
    public SmartGlideImageLoader(int memorySizeLimit,int errorImgRes){
        errorImg = errorImgRes;
        maxMemorySize = memorySizeLimit;
    }

    @Override
    public void loadImage(final int position, @NonNull final Object url, @NonNull final ImageView imageView,
                          @Nullable final ProgressBar progressBar) {
        //支持超大图片，超长图片的加载
        Glide.with(imageView).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if(progressBar!=null) progressBar.setVisibility(View.GONE);
                return false;
            }
            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if(progressBar!=null) progressBar.setVisibility(View.GONE);
                int r = resource.getByteCount() / maxMemorySize;
                if (r >= 1) {
                    imageView.setImageBitmap(XPopupUtils.compressBySampleSize(resource, r));
                    return true;
                }
                return false;
            }
        }).apply(new RequestOptions().error(errorImg).override(Target.SIZE_ORIGINAL)).into(imageView);
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