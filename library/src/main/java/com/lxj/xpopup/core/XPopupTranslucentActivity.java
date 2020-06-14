package com.lxj.xpopup.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lxj.xpopup.XPopup;

public class XPopupTranslucentActivity extends AppCompatActivity {
    static OnStartListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mListener!=null) mListener.onStart(this);
    }

    public static void start(Context context, OnStartListener listener){
        mListener = listener;
        context.startActivity(new Intent(context, XPopupTranslucentActivity.class));
    }
    public static interface OnStartListener{
        void onStart(Context context);
    }
}
