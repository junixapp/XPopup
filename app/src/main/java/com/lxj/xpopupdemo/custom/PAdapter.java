package com.lxj.xpopupdemo.custom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public  class PAdapter extends FragmentPagerAdapter {
    String[] titles;
    public PAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public PAdapter(@NonNull FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.create("XPopup默认是Dialog实现，由于Android的限制，Dialog中默认无法使用Fragment。\n\n所以要想在弹窗中使用Fragment，要设置isViewMode(true).");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles!=null ? titles[position] : "xpopup";
    }
}