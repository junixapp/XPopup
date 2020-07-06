package com.lxj.xpopupdemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopupdemo.fragment.AllAnimatorDemo;
import com.lxj.xpopupdemo.fragment.CustomAnimatorDemo;
import com.lxj.xpopupdemo.fragment.CustomPopupDemo;
import com.lxj.xpopupdemo.fragment.ImageViewerDemo;
import com.lxj.xpopupdemo.fragment.PartShadowDemo;
import com.lxj.xpopupdemo.fragment.QuickStartDemo;

public class MainActivity extends AppCompatActivity {

    PageInfo[] pageInfos = new PageInfo[]{
            new PageInfo("快速开始", new QuickStartDemo()),
            new PageInfo("局部阴影", new PartShadowDemo()),
            new PageInfo("图片浏览", new ImageViewerDemo()),
            new PageInfo("尝试不同动画", new AllAnimatorDemo()),
            new PageInfo("自定义弹窗", new CustomPopupDemo()),
            new PageInfo("自定义动画", new CustomAnimatorDemo())
    };

    TabLayout tabLayout;
    public ViewPager viewPager;

    LoadingPopupView loadingPopupView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BarUtils.setStatusBarLightMode(this, true);
//        BarUtils.setNavBarColor(this, Color.RED);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(actionBar.getTitle() + "-" + BuildConfig.VERSION_NAME);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
//        XPopup.setAnimationDuration(1000);
//        XPopup.setPrimaryColor(Color.RED);
//        ScreenUtils.setLandscape(this);
        loadingPopupView = new XPopup.Builder(this).asLoading("嘻嘻嘻嘻嘻");
        loadingPopupView.show();
        loadingPopupView.delayDismiss(1000);

//        BarUtils.setStatusBarVisibility(this, false);
//        BarUtils.setNavBarVisibility(this, false);
    }

    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return pageInfos[i].fragment;
        }

        @Override
        public int getCount() {
            return pageInfos.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pageInfos[position].title;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeAllViews();
        viewPager = null;
        pageInfos = null;
    }

}
