package com.lxj.xpopupdemo.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.core.DrawerPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.R;
import com.lxj.xpopupdemo.vm.DemoVM;

import java.util.ArrayList;
import java.util.Random;

/**
 * Description: 自定义带列表的Drawer弹窗
 * Create by dance, at 2019/1/9
 */
public class ListDrawerPopupView extends DrawerPopupView {
    RecyclerView recyclerView;
    public ListDrawerPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_list_drawer;
    }
    final ArrayList<String> data = new ArrayList<>();

    DemoVM demoVM;
    @Override
    protected void onCreate() {
        demoVM = new ViewModelProvider(((FragmentActivity)getContext())).get(DemoVM.class);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        for (int i = 0; i < 50; i++) {
            data.add(""+i);
        }

        final Button button = findViewById(R.id.btn);
        final EasyAdapter<String> commonAdapter = new EasyAdapter<String>(data, android.R.layout.simple_list_item_1) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(android.R.id.text1, s);
            }
        };
        demoVM.liveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                button.setText(s);
                Toast.makeText(getContext(), "弹窗onResume时才收到数据更新", Toast.LENGTH_SHORT).show();
                Log.e("tag", "liveData onChange: "+ s);
            }
        });
        recyclerView.setAdapter(commonAdapter);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(data.size()==0)return;
//                data.remove(0);
//                commonAdapter.notifyDataSetChanged();
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        demoVM.liveData.postValue(new Random().nextInt(10000)+"");
                    }
                },1000);

            }
        });

    }

    @Override
    protected int getMaxWidth() {
        return XPopupUtils.getScreenWidth(getContext()) - 100;
    }
}
