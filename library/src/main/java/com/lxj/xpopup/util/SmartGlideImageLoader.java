package com.lxj.xpopup.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.photoview.PhotoView;

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
    public void loadImage(final int position, @NonNull final Object url, final View imageView) {
//        if(progressBar!=null)progressBar.setVisibility(View.VISIBLE);
        //支持超大图片，超长图片的加载
        final Context context = imageView.getContext();

        Glide.with(imageView).downloadOnly().load(url)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
//                        if(progressBar!=null)progressBar.setVisibility(View.GONE);
                        if(imageView instanceof PhotoView){
                            ((PhotoView)imageView).setImageResource(errorImg);
                        }else {
                            ((SubsamplingScaleImageView)imageView).setImage(ImageSource.resource(errorImg));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        if(imageView instanceof PhotoView){
                            //宽度超过屏幕宽的2倍，或者高度超过屏幕高2倍，则认为是大图
                            int maxW = XPopupUtils.getWindowWidth(context)*2;  //屏幕宽的2倍
                            int maxH = XPopupUtils.getScreenHeight(context)*2; //屏幕高的2倍
                            int[] bitmapSize = XPopupUtils.getBitmapSize(resource);
                            if(bitmapSize[0] > maxW || bitmapSize[1] > maxH){
                                //认为是大图或长图
                                ((PhotoView)imageView).setImageBitmap(XPopupUtils.getBitmap(resource, maxW, maxH));
                            }else {
                                //不是大图，则原样加载
                                Glide.with(imageView).load(resource).error(errorImg).apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).into((PhotoView)imageView);
                            }
                        }else {
                            ((SubsamplingScaleImageView)imageView).setImage(ImageSource.uri(Uri.fromFile(resource)));
                        }
//                        if(progressBar!=null)progressBar.setVisibility(View.GONE);

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