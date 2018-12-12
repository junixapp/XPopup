package com.lxj.xpopupdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;

import com.lxj.xpopupdemo.fragment.AttachPopupDemo;
import com.lxj.xpopupdemo.fragment.BaseFragment;
import com.lxj.xpopupdemo.fragment.BottomPopupDemo;
import com.lxj.xpopupdemo.fragment.CenterPopupDemo;
import com.lxj.xpopupdemo.fragment.QuickStartDemo;

public class MainActivity extends AppCompatActivity {

    PageInfo[] pageInfos = new PageInfo[]{
      new PageInfo("快速开始", new QuickStartDemo()),
      new PageInfo("Center类型", new CenterPopupDemo()),
      new PageInfo("Bottom类型", new BottomPopupDemo()),
      new PageInfo("依附某View", new AttachPopupDemo()),
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


        PopupMenu popupMenu;
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
