package com.example.bluecity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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

    /**
     * bitmap转base64
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();

                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
     * bitmap转base64
     */
    public static Bitmap base64ToBitmap(String base64String){
        byte[] bytes = Base64.decode(base64String, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 根据位图上随机某个pixel的值求密钥值key
      */
    protected static double getKey(Bitmap bitmap) {
        int h = bitmap.getHeight();                            // 位图高度
        int w = bitmap.getWidth();                            // 位图宽度
        int y = new Random().nextInt(h);        // 获得一个[0, h]区间内的随机整数
        int x = new Random().nextInt(w);        // 获得一个[0, w]区间内的随机整数
        int p = Math.abs(bitmap.getPixel(x, y));
        return (double) p / Math.pow(10, String.valueOf(p).length());
    }

    /**
     * 加密
      */
    public static Bitmap encrypt(Bitmap bitmap) {
        double key = getKey(bitmap);
        return processBitmap(bitmap, key);
    }

    /**
     * 解密
     */
    public static Bitmap decrypt(Bitmap bitmap, double key) {
        return processBitmap(bitmap, key);
    }

    /**
     * 像素置换
     * @param bitmap
     * @param key
     * @return
     */
    public static Bitmap processBitmap(Bitmap bitmap, double key) {
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int h = bitmap.getHeight();                            // 位图高度
        int w = bitmap.getWidth();                            // 位图宽度
        int mArrayColorLength = h * w;
        int[] s = sequenceGenerator(key, mArrayColorLength);    // 设置Logistic混沌系统初始值和迭代次数
        int[] mArrayColor = new int[mArrayColorLength];
        int[] bArray = new int[mArrayColorLength];
        bitmap.getPixels(bArray, 0, w, 0, 0, w, h);
        // 遍历位图
        for (int i = 0; i < mArrayColorLength; i++) {
            mArrayColor[i] = bArray[i] ^ s[i];                           // 位图像素值与混沌序列值作异或
        }
        newBitmap.setPixels(mArrayColor, 0, w, 0, 0, w, h);    // 为新位图赋值
        return newBitmap;
    }

    /**
     * 产生logistic混沌序列
     * @param x0
     * @param timeStep
     * @return
     */
    private static int[] sequenceGenerator(double x0, int timeStep) {
        final double u = 3.9;                        // 控制参数u
        double[] x = new double[timeStep + 1000];

        x[0] = x0;
        // 迭代产生混沌序列，长度为 “timeStep+1000”
        for (int i = 0; i < timeStep + 999; i++) {
            x[i + 1] = u * x[i] * (1 - x[i]);       // 一维Logistic混沌系统
        }

        double[] new_x = Arrays.copyOfRange(x, 1000, timeStep + 1000);    // 去除前1000个混沌值，去除暂态效应
        int[] seq = new int[timeStep];
        // 处理混沌序列值
        for (int i = 0; i < timeStep; i++) {
            new_x[i] = new_x[i] * Math.pow(10, 4) - Math.floor(new_x[i] * Math.pow(10, 4));
            seq[i] = (int) Math.floor(Math.pow(10, 9) * new_x[i]);
        }
        return seq;
    }
}
