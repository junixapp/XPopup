package com.lxj.xpopupdemo.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnImageViewerLongPressListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.util.SmartGlideImageLoader;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.custom.CustomImageViewerPopup;

import static com.lxj.xpopupdemo.Constants.list;

/**
 * Description:
 * Create by lxj, at 2019/1/22
 */
public class ImageViewerDemo extends BaseFragment {

    String url1 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2279952540,2544282724&fm=26&gp=0.jpg";
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
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/4B4E81902BF3B6285DFAC5EAD2C3A9F3.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/B40CF2CA54715E64CF4AA3632FD4F70E.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/C2A333BA3CCCBE8290E2F9549385E0C1.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/3F8B1BFDCBA2559EB69BA1670915E912.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/5C50B56D6FC9C30562FE15716B02AA3E.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/E211145D8BA5CC519E9ED56D1AC57D2A.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/92FA62C554C0A4B61251A5A2FCDD400B.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/7ECFF80AEDFF9D2771DAFB979D13513E.jpg");
        list.add("https://word.7english.cn/user/publicNoteImage/4e44a8706ee94016a4d40ad0693e9f41/C12F6B62FF052BAB4844AB9A5A333F3C.jpg");
        list.add("https://test.yujoy.com.cn:59010/file/postImage/2021/03/03/7c9114bb-bc4a-40c4-94ab-01833228f26f.png");
    }

    RecyclerView recyclerView;
    ImageView image1, image2;
    ViewPager pager;
    ViewPager2 pager2;
    Button btn_custom;

    @Override
    public void init(final View view) {
        view.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThreadUtils.executeByCached(new ThreadUtils.Task<Object>() {
                    @Override
                    public Object doInBackground() throws Throwable {
                        Glide.get(requireContext()).clearDiskCache();
                        Glide.get(requireContext()).clearMemory();
                        return true;
                    }
                    @Override
                    public void onSuccess(Object result) {
                        ToastUtils.showShort("清理完毕");
                    }
                    @Override
                    public void onCancel() { }
                    @Override
                    public void onFail(Throwable t) { }
                });
            }
        });
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        pager = view.findViewById(R.id.pager);
        pager2 = view.findViewById(R.id.pager2);
        btn_custom = view.findViewById(R.id.btn_custom);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(new ImageAdapter());


        Glide.with(this).load(url1).into(image1);
        Glide.with(this).load(url2).into(image2);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true)
                        .asImageViewer(image1, url1, true, Color.parseColor("#f1f1f1"), -1, 0
                                , false, Color.BLACK, new SmartGlideImageLoader(), new OnImageViewerLongPressListener() {
                                    @Override
                                    public void onLongPressed(BasePopupView popupView, int position) {
                                        ToastUtils.showShort("长按了第" + position +"个图片");
                                    }
                                })
                        .show();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asImageViewer(image2, url2, new SmartGlideImageLoader())
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
                viewerPopup.setXPopupImageLoader(new SmartGlideImageLoader());
//                viewerPopup.isShowIndicator(false);//是否显示页码指示器
//                viewerPopup.isShowPlaceholder(false);//是否显示白色占位块
//                viewerPopup.isShowSaveButton(false);//是否显示保存按钮
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true)
                        .asCustom(viewerPopup)
                        .show();
            }
        });

        pager2.setAdapter(new ViewPager2Adapter());
    }

    public static class ImageAdapter extends EasyAdapter<Object> {
        public ImageAdapter() {
            super(list, R.layout.adapter_image);
        }

        @Override
        protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
            final ImageView imageView = holder.<ImageView>getView(R.id.image);
            //1. 加载图片
            Glide.with(imageView).load(s).into(imageView);

            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(holder.itemView.getContext()).asImageViewer(imageView, position, list,
                            true, true, -1, -1, -1, true,
                            Color.rgb(32, 36, 46),
                            new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                                    RecyclerView rv = (RecyclerView) holder.itemView.getParent();
                                    popupView.updateSrcView((ImageView) rv.getChildAt(position));
                                }
                            }, new SmartGlideImageLoader(), null)
                            .show();
                }
            });
        }
    }

    //ViewPager2的adapter
    public class ViewPager2Adapter extends EasyAdapter<Object> {
        public ViewPager2Adapter() {
            super(list, R.layout.adapter_image2);
        }

        @Override
        protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
            final ImageView imageView = holder.<ImageView>getView(R.id.image);
            //1. 加载图片
            Glide.with(imageView).load(s).into(imageView);

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
                                            popupView.updateSrcView((ImageView) rv.getChildAt(0));
                                        }
                                    });
                                }
                            }, new SmartGlideImageLoader())
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
            Glide.with(imageView).load(list.get(position)).into(imageView);

            //2. 设置点击
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(getContext())
                            .asImageViewer(imageView, position, list, true, false, -1, -1, -1, true, Color.BLACK, new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(final ImageViewerPopupView popupView, final int position) {
                                    //1.pager更新当前显示的图片
                                    //当启用isInfinite时，position会无限增大，需要映射为当前ViewPager中的页
                                    int realPosi = position % list.size();
//                            Log.e("tag", "position: "+realPosi + " list size: "+list.size());
                                    pager.setCurrentItem(realPosi, false);
                                    //2.更新弹窗的srcView，注意这里的position是list中的position，上面ViewPager设置了pageLimit数量，
                                    //保证能拿到child，如果不设置pageLimit，ViewPager默认最多维护3个page，会导致拿不到child
                                    popupView.updateSrcView((ImageView) pager.getChildAt(realPosi));
                                }
                            }, new SmartGlideImageLoader(), null)
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

}

