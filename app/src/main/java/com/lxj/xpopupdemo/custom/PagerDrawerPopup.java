package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopupdemo.R;


/**
 * Description: 自定义带有ViewPager的Drawer弹窗
 * Create by dance, at 2019/5/5
 */
public class PagerDrawerPopup extends DrawerPopupView {
    public PagerDrawerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_pager_drawer;
    }

    TabLayout tabLayout;
    ViewPager pager;
    String[] titles = new String[]{"首页", "娱乐", "汽车", "八卦", "搞笑", "互联网"};
    @Override
    protected void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PAdapter());
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag", "PagerDrawerPopup onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag", "PagerDrawerPopup onDismiss");
    }

    class PAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return titles.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setText(titles[position]);
            textView.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
            container.addView(textView);
            return textView;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
