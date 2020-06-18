package com.lxj.xpopupdemo.fragment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomImageViewerPopup;

import java.io.File;
import java.util.ArrayList;

import static com.lxj.xpopupdemo.Constants.list;

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

    static {
        list.clear();
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg");
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg");
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg");
        list.add("https://user-gold-cdn.xitu.io/2019/1/25/168839e977414cc1?imageView2/2/w/800/q/100");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551692956639&di=8ee41e070c6a42addfc07522fda3b6c8&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160413%2F75659e9b05b04eb8adf5b52669394897.jpg");
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg");
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=851052518,4050485518&fm=26&gp=0.jpg");
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=174904559,2874238085&fm=26&gp=0.jpg");
        list.add("https://user-gold-cdn.xitu.io/2019/1/25/168839e977414cc1?imageView2/2/w/800/q/100");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1551692956639&di=8ee41e070c6a42addfc07522fda3b6c8&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160413%2F75659e9b05b04eb8adf5b52669394897.jpg");
    }

    RecyclerView recyclerView;
    ImageView image1, image2;
    ViewPager pager;
    ViewPager2 pager2;
    Button btn_custom;
    @Override
    public void init(final View view) {
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        pager = view.findViewById(R.id.pager);
        pager2 = view.findViewById(R.id.pager2);
        btn_custom = view.findViewById(R.id.btn_custom);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(new ImageAdapter());


        Glide.with(this).load(url1).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL).transform(new RoundedCorners(50))).into(image1);
        Glide.with(this).load(url2).into(image2);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asImageViewer(image1, url1, true, -1, -1, 50, false,new ImageLoader())
                        .show();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asImageViewer(image2, url2, new ImageLoader())
                        .show();
            }
        });

        //ViewPager bind data
        pager.setOffscreenPageLimit(list.size());
        pager.setAdapter(new ImagePagerAdapter());

        btn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义的弹窗需要用asCustom来显示，之前的asImageViewer这些方法当然不能用了。
                CustomImageViewerPopup viewerPopup = new CustomImageViewerPopup(getContext());
                //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
                viewerPopup.setSingleSrcView(image2, url2);
//                viewerPopup.isInfinite(true);
                viewerPopup.setXPopupImageLoader(new ImageLoader());
//                viewerPopup.isShowIndicator(false);//是否显示页码指示器
//                viewerPopup.isShowPlaceholder(false);//是否显示白色占位块
//                viewerPopup.isShowSaveButton(false);//是否显示保存按钮
                new XPopup.Builder(getContext())
                        .asCustom(viewerPopup)
                        .show();
            }
        });

        pager2.setAdapter(new ImageAdapter2());
    }

    public static class ImageAdapter extends EasyAdapter<Object> {
        public ImageAdapter() {
            super(list, R.layout.adapter_image);
        }

        @Override
        protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
            final ImageView imageView = holder.<ImageView>getView(R.id.image);
            //1. 加载图片, 由于ImageView是centerCrop，必须指定Target.SIZE_ORIGINAL，禁止Glide裁剪图片；
            // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
            Glide.with(imageView).load(s).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)
                    .override(Target.SIZE_ORIGINAL))
                    .into(imageView);

            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(holder.itemView.getContext()).asImageViewer(imageView, position, list,
                            new OnSrcViewUpdateListener() {
                        @Override
                        public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                            RecyclerView rv = (RecyclerView) holder.itemView.getParent();
                            popupView.updateSrcView((ImageView)rv.getChildAt(position));
                        }
                    }, new ImageLoader())
                            .show();
                }
            });
        }
    }

    //ViewPager2的adapter
    public class ImageAdapter2 extends EasyAdapter<Object> {
        public ImageAdapter2() {
            super(list, R.layout.adapter_image2);
        }

        @Override
        protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
            final ImageView imageView = holder.<ImageView>getView(R.id.image);
            //1. 加载图片, 由于ImageView是centerCrop，必须指定Target.SIZE_ORIGINAL，禁止Glide裁剪图片；
            // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
            Glide.with(imageView).load(s).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)
                    .override(Target.SIZE_ORIGINAL))
                    .into(imageView);

            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(holder.itemView.getContext()).asImageViewer(imageView, position, list,
                            new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(final ImageViewerPopupView popupView, final int position) {
                                    pager2.setCurrentItem(position, false);
                                    //一定要post，因为setCurrentItem内部实现是RecyclerView.scrollTo()，这个是异步的
                                    pager2.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //由于ViewPager2内部是包裹了一个RecyclerView，而RecyclerView始终维护一个子View
                                            RecyclerView rv = (RecyclerView) pager2.getChildAt(0);
                                            //再拿子View，就是ImageView
                                            popupView.updateSrcView((ImageView)rv.getChildAt(0));
                                        }
                                    });
                                }
                            }, new ImageLoader())
                            .show();
                }
            });
        }
    }

    class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);

            //1. 加载图片
            Glide.with(imageView).load(list.get(position)).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL))
                    .into(imageView);
            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(getContext())
                            .asImageViewer(imageView, position, list, true,false, -1, -1, -1, true, new OnSrcViewUpdateListener() {
                        @Override
                        public void onSrcViewUpdate(final ImageViewerPopupView popupView, final int position) {
                            //1.pager更新当前显示的图片
                            //当启用isInfinite时，position会无限增大，需要映射为当前ViewPager中的页
                            int realPosi = position%list.size();
//                            Log.e("tag", "position: "+realPosi + " list size: "+list.size());
                            pager.setCurrentItem(realPosi, false);
                            //2.更新弹窗的srcView，注意这里的position是list中的position，上面ViewPager设置了pageLimit数量，
                            //保证能拿到child，如果不设置pageLimit，ViewPager默认最多维护3个page，会导致拿不到child
                            popupView.updateSrcView((ImageView) pager.getChildAt(realPosi));
                        }
                    }, new ImageLoader())
                            .show();
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public static class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL)).into(imageView);
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
}

