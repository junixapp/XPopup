package com.lxj.xpopupdemo;

import com.lxj.xpopupdemo.fragment.BaseFragment;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public class PageInfo {
    public String title;
    public BaseFragment fragment;

    public PageInfo(String title, BaseFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }
}
