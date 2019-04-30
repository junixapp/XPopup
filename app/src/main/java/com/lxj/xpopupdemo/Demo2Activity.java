package com.lxj.xpopupdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.IntStream;

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
