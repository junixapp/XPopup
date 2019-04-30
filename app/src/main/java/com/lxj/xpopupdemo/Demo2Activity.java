package com.lxj.xpopupdemo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Description:
 * Create by lxj, at 2019/2/2
 */
public class Demo2Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    BasePopupView popupView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }
        EasyAdapter<String> adapter = new EasyAdapter<String>(list, android.R.layout.simple_list_item_1) {
            @Override
            protected void bind(@NotNull ViewHolder viewHolder, String s, int i) {
                viewHolder.setText(android.R.id.text1, s);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(@NotNull View view, @NotNull RecyclerView.ViewHolder holder, int position) {
                super.onItemClick(view, holder, position);
                if (popupView == null) {
                    popupView = new XPopup.Builder(Demo2Activity.this)
                            .autoOpenSoftInput(true)
                            .asCustom(new CustomEditTextBottomPopup(Demo2Activity.this));
                }
                popupView.show();
            }
        });
        recyclerView.setAdapter(adapter);


    }


}
