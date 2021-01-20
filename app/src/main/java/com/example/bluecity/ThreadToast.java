package com.example.bluecity;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ThreadToast {
    public static void toast(Context context,String msg) {
        Looper.prepare();
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
