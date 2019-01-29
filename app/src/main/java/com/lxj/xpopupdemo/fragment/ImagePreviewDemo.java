package com.lxj.xpopupdemo.fragment;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImagePreviewPopupView;
import com.lxj.xpopup.interfaces.OnLoadImageListener;
import com.lxj.xpopup.photoview.PhotoView;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;

/**
 * Description:
 * Create by lxj, at 2019/1/22
 */
public class ImagePreviewDemo extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_preview;
    }
    ArrayList<String> list = new ArrayList<>();
    ImagePreviewPopupView popupView;
    @Override
    public void init(View view) {
        final ImageView imageView = view.findViewById(R.id.iv_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPopup.get(getContext())
                        .asCustom(popupView)
                        .show();
//                imageView.setImageAlpha(50);
            }
        });

        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548756837006&di=551df0dcf59d1d71673c3d46b33f0d93&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201308%2F04%2F20130804155912_wCRnE.thumb.700_0.jpeg");
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg");
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548764579122&di=e3a46d9075ee49ecefb552a447974ddc&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201112%2F03%2F20111203233836_3wu5E.thumb.700_0.jpg");
        Glide.with(this).asBitmap().load(list.get(0)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
                popupView = new ImagePreviewPopupView(getContext())
                        .setSrcView(imageView, resource)
                        .setImageUrls(list)
                        .setLoadImageListener(new OnLoadImageListener() {
                            @Override
                            public void loadImage(int position, String url, ImageView imageView) {
                                Glide.with(getContext()).load(url).into(imageView);
                            }
                        });
            }
        });
//        imageView.setImageResource(R.drawable.t);
    }
}
