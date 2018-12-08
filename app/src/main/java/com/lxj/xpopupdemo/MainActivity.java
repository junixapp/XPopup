package com.lxj.xpopupdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lxj.xpopup.XPopup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPopup.get(MainActivity.this).show();
            }
        });
    }
}
