package com.lxj.xpopupdemo.custom;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;
import java.util.List;


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
        pager.setOffscreenPageLimit(titles.length);
        FragmentActivity activity = (FragmentActivity) getContext();
        pager.setAdapter(new PAdapter(activity.getSupportFragmentManager(), titles));
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    protected List<String> getInternalFragmentNames() {
        ArrayList<String> list = new ArrayList<>();
        list.add(TestFragment.class.getSimpleName());
        return list;
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

}
