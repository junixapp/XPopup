package com.lxj.xpopupdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopupdemo.fragment.AllAnimatorDemo;
import com.lxj.xpopupdemo.fragment.BaseFragment;
import com.lxj.xpopupdemo.fragment.CustomAnimatorDemo;
import com.lxj.xpopupdemo.fragment.CustomPopupDemo;
import com.lxj.xpopupdemo.fragment.PartShadowDemo;
import com.lxj.xpopupdemo.fragment.QuickStartDemo;

public class MainActivity extends AppCompatActivity {

    PageInfo[] pageInfos = new PageInfo[]{
      new PageInfo("快速开始", new QuickStartDemo()),
      new PageInfo("局部阴影", new PartShadowDemo()),
      new PageInfo("尝试不同动画", new AllAnimatorDemo()),
      new PageInfo("自定义弹窗", new CustomPopupDemo()),
      new PageInfo("自定义动画", new CustomAnimatorDemo())
    };

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
               callFragmentInit(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                callFragmentInit(0);
            }
        },300);

        XPopup.get(this).setPrimaryColor(getResources().getColor(R.color.colorPrimary));

    }

    private void callFragmentInit(int i){
        BaseFragment fragment = (BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(i);
        fragment.init(fragment.getView());
    }

    class MainAdapter extends FragmentPagerAdapter{

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
}
