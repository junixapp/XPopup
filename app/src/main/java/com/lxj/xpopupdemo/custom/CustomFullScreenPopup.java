package com.lxj.xpopupdemo.custom;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopupdemo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Create by lxj, at 2019/3/12
 */
public class CustomFullScreenPopup extends FullScreenPopupView {
    public CustomFullScreenPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_fullscreen_popup;
    }

    RecyclerView recyclerView;
    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("text");
        }
        recyclerView.setAdapter(new EasyAdapter<String>(data, R.layout.adapter_custom_fullscreen_popup) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.text, s + "-"+position);
            }
        });
    }
}
