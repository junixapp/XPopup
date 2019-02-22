package com.lxj.xpopupdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.easyadapter.CommonAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;

/**
 * Description:
 * Create by lxj, at 2019/2/2
 */
public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);


        XPopup.get(DemoActivity.this).asLoading().show("c");

        XPopup.get(this).autoDismiss(false).asBottomList("haha", new String[]{"点我显示弹窗", "点我显示弹窗", "点我显示弹窗", "点我显示弹窗"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Toast.makeText(DemoActivity.this, text, Toast.LENGTH_LONG).show();

                XPopup.get(DemoActivity.this).autoDismiss(false).asConfirm("测试", "aaaa", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        XPopup.get(DemoActivity.this).dismiss("c");
                        XPopup.get(DemoActivity.this).dismiss("a");
                        XPopup.get(DemoActivity.this).dismiss("b");
                    }
                }).show("b");
            }
        }).show("a");
    }
}
