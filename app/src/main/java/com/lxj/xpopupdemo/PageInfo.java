package com.lxj.xpopupdemo;

import android.support.v4.app.Fragment;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class PageInfo {
    public String title;
    public Fragment fragment;

    public PageInfo(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }
}
