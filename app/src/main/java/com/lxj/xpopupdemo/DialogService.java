package com.lxj.xpopupdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class DialogService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showDialog();
        return super.onStartCommand(intent, flags, startId);

    }

    private void showDialog() {
        show();
    }

    private void show(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                AlertDialog dialog = new AlertDialog.Builder(getApplicationContext()).setTitle("title")
//                        .setMessage("这是一个由service弹出的对话框")
//                        .setCancelable(false)
//                        .setPositiveButton("button confirm", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create();
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//                dialog.show();

//                new XPopup.Builder(getApplicationContext()).asConfirm("XPopup牛逼", "XPopup支持直接在后台弹出！", new OnConfirmListener() {
//                    @Override
//                    public void onConfirm() {
//                        startActivity(new Intent(DialogService.this, MainActivity.class));
//                        stopSelf();
//                    }
//                }).show();
            }
        }, 3000);
    }

}