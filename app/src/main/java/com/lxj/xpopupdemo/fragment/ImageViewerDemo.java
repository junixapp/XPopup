package com.lxj.xpopupdemo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;

/**
 * Description:
 * Create by lxj, at 2019/1/22
 */
public class ImageViewerDemo extends BaseFragment {

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548777981087&di=0618a101655e57c675c7c21b4ef55f00&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1504%2F06%2Fc70%2F5014635_1428321310010_mthumb.jpg";
    String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549382334&di=332b0aa1ec4ccd293f176164d998e5ab&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D121ef3421a38534398c28f62fb7ada0b%2Ffaf2b2119313b07eedb4502606d7912397dd8c96.jpg";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_preview;
    }

    ArrayList<Object> list = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView image1, image2;

    @Override
    public void init(View view) {
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        list.clear();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548756837006&di=551df0dcf59d1d71673c3d46b33f0d93&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201308%2F04%2F20130804155912_wCRnE.thumb.700_0.jpeg");
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg");
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg");
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548764579122&di=e3a46d9075ee49ecefb552a447974ddc&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201112%2F03%2F20111203233836_3wu5E.thumb.700_0.jpg");
        list.add("https://user-gold-cdn.xitu.io/2019/1/25/168839e977414cc1?imageView2/2/w/800/q/100");

        recyclerView.setAdapter(new ImageAdapter());


        Glide.with(this).load(url1).apply(new RequestOptions().override(Target.SIZE_ORIGINAL).transform(new RoundedCorners(50))).into(image1);
        Glide.with(this).load(url2).into(image2);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPopup.get(getContext())
                        .asImageViewer(image1, url1, -1, -1, 50,new ImageLoader())
                        .show();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPopup.get(getContext())
                        .asImageViewer(image2, url2, new ImageLoader())
                        .show();
            }
        });

    }

    class ImageAdapter extends CommonAdapter<Object> {
        public ImageAdapter() {
            super(R.layout.adapter_image, list);
        }

        @Override
        protected void convert(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
            final ImageView imageView = holder.<ImageView>getView(R.id.image);
            //1. 加载图片, 由于ImageView是centerCrop，必须指定Target.SIZE_ORIGINAL，禁止Glide裁剪图片；
            // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
            Glide.with(imageView).load(s).apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                    .into(imageView);

            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XPopup.get(getContext()).asImageViewer(imageView, position, list, new OnSrcViewUpdateListener() {
                        @Override
                        public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                            popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
                        }
                    }, new ImageLoader())
                            .show();
                }
            });
        }
    }

    class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).into(imageView);
        }
    }
}

