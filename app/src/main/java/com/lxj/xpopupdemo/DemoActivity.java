package com.lxj.xpopupdemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopupdemo.custom.QQMsgPopup;
import com.lxj.xpopupdemo.fragment.FragmentLifecycleDemo;
import com.lxj.xpopupdemo.fragment.ImageViewerDemo;

/**
 * Description:
 * Create by lxj, at 2019/2/2
 */
public class DemoActivity extends AppCompatActivity {
    EditText editText;
    RecyclerView recyclerView;
    BasePopupView attachPopup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        editText = findViewById(R.id.et);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.btnShowFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment();
            }
        });
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiPopup();
            }
        });
        showMultiPopup();

        attachPopup = new XPopup.Builder(this)
                .atView(editText)
                .dismissOnTouchOutside(false)
                .isViewMode(true)      //开启View实现
                .isRequestFocus(false) //不强制焦点
//                .isClickThrough(true)  //点击透传
                .isTouchThrough(true)
                .hasShadowBg(false)
                .positionByWindowCenter(true)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asAttachList(new String[]{"联想到的内容 - 1", "联想到的内容 - 2", "联想到的内容 - 333"}, null, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Toast.makeText(XPopupApp.context, text, Toast.LENGTH_LONG).show();
                    }
                });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    attachPopup.dismiss();
                    return;
                }
                if(attachPopup.isDismiss()){
                    attachPopup.show();
                }
            }
        });

        initData();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ImageViewerDemo.ImageAdapter());
        showFragment();
    }

    public void showMultiPopup(){
        final BasePopupView loadingPopup = new XPopup.Builder(this).asLoading();
        loadingPopup.show();
        new XPopup.Builder(DemoActivity.this)
                .autoDismiss(false)
                .asBottomList("haha", new String[]{"点我显示弹窗", "点我显示弹窗", "点我显示弹窗", "点我显示弹窗"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        new XPopup.Builder(DemoActivity.this).asConfirm("测试", "aaaa", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                loadingPopup.dismiss();
                            }
                        }).show();
                    }
                }).show();


    }

    FragmentLifecycleDemo fragmentLifecycleDemo;
    public void showFragment(){
        fragmentLifecycleDemo = new FragmentLifecycleDemo();
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fragmentLifecycleDemo)
        .commitNow();
    }
    public void delayDestroy(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().remove(fragmentLifecycleDemo)
                        .commitNow();
                fragmentLifecycleDemo = null;
            }
        }, 3000);
    }
}
