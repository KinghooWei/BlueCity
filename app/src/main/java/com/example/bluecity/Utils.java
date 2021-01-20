package com.example.bluecity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Utils {
    /**
     * toast
     */
    public static void showToastInThread(String mag) {
        Looper.prepare();
        Toast.makeText(MyApplication.context,mag,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public static void showToast(String mag) {
        Toast.makeText(MyApplication.context,mag,Toast.LENGTH_SHORT).show();
    }

    public static void sendMessage(Handler handler,int what) {
        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);
    }

    /**
     * log
     */
    private static final int INFO = 3;
    private static final int level = INFO;
    public static void logV(String tag, String msg) {
        int VERBOSE = 1;
        if (level <= VERBOSE)
            Log.v(tag,msg);
    }
    public static void logD(String tag, String msg) {
        int DEBUG = 2;
        if (level <= DEBUG)
            Log.d(tag,msg);
    }
    public static void logI(String tag, String msg) {
        int INFO = 3;
        if (level <= INFO)
            Log.i(tag,msg);
    }
    public static void logW(String tag, String msg) {
        int WARN = 4;
        if (level <= WARN)
            Log.w(tag,msg);
    }
    public static void logE(String tag, String msg) {
        int ERROR = 5;
        if (level <= ERROR)
            Log.e(tag,msg);
    }
}
