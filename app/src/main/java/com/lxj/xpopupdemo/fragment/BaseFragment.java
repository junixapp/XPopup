package com.lxj.xpopupdemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lxj.xpopupdemo.XPopupApp;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public abstract class BaseFragment extends Fragment {
    View view;
    boolean isInit = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        safeInit();
    }

    private void safeInit() {
        if (getUserVisibleHint() && view!=null) {
            if (!isInit) {
                isInit = true;
                init(view);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        safeInit();
    }

    protected abstract int getLayoutId();
    public abstract void init(View view);

    public void toast(String msg) {
        Toast.makeText(XPopupApp.context, msg, Toast.LENGTH_SHORT).show();
    }
}
