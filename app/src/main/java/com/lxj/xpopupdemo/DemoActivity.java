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
    RecyclerView recycler_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            data.add(""+i);
        }
        recycler_view.setAdapter(new CommonAdapter<String>(android.R.layout.simple_list_item_1, data) {
            @Override
            protected void convert(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(android.R.id.text1, s);
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPopup.get(DemoActivity.this).asBottomList("我是标题", new String[]{"aaaa", "bbbb", "bbbb", "ddddd"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Toast.makeText(DemoActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });


        XPopup.get(DemoActivity.this).asLoading().show("c");

        XPopup.get(this).autoDismiss(false).asBottomList("haha", new String[]{"aaaa", "bbbb", "bbbb", "ddddd"}, new OnSelectListener() {
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
