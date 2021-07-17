package com.lxj.xpopup.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import java.io.File;

/**
 * 支持加载超长，超大的图片，你能OOM就算我输！！！
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
                          @NonNull final ImageView snapshot,
                          @NonNull final SubsamplingScaleImageView bigImageView,
                          @NonNull final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        //支持超大图片，超长图片的加载
        final Context context = imageView.getContext();
        Glide.with(imageView).downloadOnly().load(url)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        bigImageView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(errorImg);
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        int maxW = (XPopupUtils.getWindowWidth(context)*2);
                        int maxH = (XPopupUtils.getScreenHeight(context)*2);

                        int[] size = XPopupUtils.getImageSize(resource);
                        if(size[0] > maxW || size[1] > maxH){
                            //认为是大图，大图则使用SubsamplingScaleImageView加载
                            imageView.setVisibility(View.GONE);
                            snapshot.setVisibility(View.VISIBLE);
                            bigImageView.setVisibility(View.VISIBLE);
                            bigImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
//                                bigImageView.setScaleAndCenter(1f, new PointF(size[0]/2f, size[1]/));
                            if(snapshot.getDrawable()!=null && snapshot.getDrawable() instanceof BitmapDrawable){
                                BitmapDrawable preview = (BitmapDrawable)snapshot.getDrawable();
                                bigImageView.setImage(ImageSource.uri(Uri.fromFile(resource)).dimensions(size[0], size[1]),
                                        ImageSource.cachedBitmap(preview.getBitmap()));
                            }else {
                                bigImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                            }
                        }else {
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            bigImageView.setVisibility(View.GONE);
                            Glide.with(imageView).load(resource).apply(new RequestOptions().override(size[0], size[1])).into(imageView);
                        }
                    }
                });
    }

    @Override
    public void loadSnapshot(@NonNull Object uri, @NonNull final ImageView snapshot) {
        Glide.with(snapshot).downloadOnly().load(uri)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        snapshot.setImageResource(errorImg);
                    }
                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        int maxW = (XPopupUtils.getWindowWidth(snapshot.getContext()));
                        int maxH = (XPopupUtils.getScreenHeight(snapshot.getContext()));
                        int[] size = XPopupUtils.getImageSize(resource);
                        if(size[0] > maxW || size[1] > maxH){
                            //缩放加载
                            snapshot.setImageBitmap(XPopupUtils.getBitmap(resource, maxW, maxH));
                        }else {
                            Glide.with(snapshot).load(resource).apply(new RequestOptions().override(size[0], size[1])).into(snapshot);
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