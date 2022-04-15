package com.lxj.xpopupdemo.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.lxj.xpopupdemo.R;

import java.util.ArrayList;

/**
 * Description: 自定义抽屉弹窗
 * Create by dance, at 2018/12/20
 */
public class CustomDrawerPopupView extends DrawerPopupView {
    TextView text;

    public CustomDrawerPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_drawer_popup2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
//        CustomDrawerPopup2Binding.bind(getPopupImplView());
        Log.e("tag", "CustomDrawerPopupView onCreate");
//        text = findViewById(R.id.text);
//        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.topMargin = 450;

        VerticalRecyclerView rv = findViewById(R.id.rv);
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 200; i++) {
            list.add(i + "");
        }
        rv.setAdapter(new EasyAdapter(list, R.layout.temp) {
            @Override
            protected void bind(ViewHolder viewHolder, Object o, int i) {
                if (i % 2 == 0) {
                    viewHolder.<TextView>getView(R.id.text).setText("aa - " + i);
                    viewHolder.<TextView>getView(R.id.text).setBackgroundColor(Color.WHITE);
                } else {
                    viewHolder.<TextView>getView(R.id.text).setText("aa - " + i + "大萨达所撒多" +
                            "\n大萨达所撒多大萨达所撒多");
                    viewHolder.<TextView>getView(R.id.text).setBackgroundColor(Color.RED);

                }
            }
        });
        findViewById(R.id.btnMe).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext()).isDestroyOnDismiss(true)
                        .asConfirm("提示", "确定要退出吗？", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                ((Activity)getContext()).finish();
                                dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
//        text.setText(new Random().nextInt()+"");
        Log.e("tag", "CustomDrawerPopupView onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag", "CustomDrawerPopupView onDismiss");
    }
}