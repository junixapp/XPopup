package com.lxj.xpopupdemo.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lxj.statelayout.StateLayout;
import com.lxj.xpopupdemo.XPopupApp;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public abstract class BaseFragment extends Fragment {
    View view;
    boolean isInit = false;
    StateLayout stateLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            stateLayout = new StateLayout(getContext()).wrap(view).showLoading(false);
        }
        return stateLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        safeInit();
    }

    private void safeInit() {
        if (getUserVisibleHint() && view!=null) {
            if (!isInit) {
                isInit = true;
                init(view);
                stateLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stateLayout.showContent();
                    }
                },300);
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
