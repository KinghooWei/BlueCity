package com.example.bluecity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpUtils {
//    private static String PATH = "http://192.168.1.220:8080/client/api";
    private static String PATH = "http://172.20.10.10:8080/client/api";
//    private static String PATH = "http://192.168.43.220:8080/client/api";

    private static URL url;

    public HttpUtils() {
        // TODO Auto-generated constructor stub
    }

    static {
        try {
            url = new URL(PATH);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
    向服务器发送请求，并接受响应
     */
    public static void sendJsonPost(final String Json, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // HttpClient 6.0被抛弃了
                String result = "";
                BufferedReader reader = null;
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();      //连接服务器
                    //设置连接属性
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Charset", "UTF-8");
                    // 设置文件类型:
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    // 设置接收类型否则返回415错误
                    //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
                    conn.setRequestProperty("accept", "application/json");
                    // 往服务器里面发送数据
                    if (!TextUtils.isEmpty(Json)) {
                        byte[] writebytes = Json.getBytes();        //json转为字节数组
                        // 设置文件长度
                        conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                        OutputStream outwritestream = conn.getOutputStream();   //从连接中得到一个输出流，通过输出流把数据写到服务器
                        outwritestream.write(Json.getBytes());
                        outwritestream.flush();
                        outwritestream.close();
                        Log.d("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
                    }
                    if (conn.getResponseCode() == 200) {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        result = reader.readLine();
                        listener.onFinish(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
//        return result;
            }
        }).start();
    }

    public static String sendJsonPost(final String Json) {
        // HttpClient 6.0被抛弃了
        String result = "";
        BufferedReader reader = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();      //连接服务器
            //设置连接属性
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (!TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();        //json转为字节数组
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();   //从连接中得到一个输出流，通过输出流把数据写到服务器
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                Log.d("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("result", result);
        return result;
    }

    interface HttpCallbackListener {
        void onFinish(String response) throws JSONException;

        void onError(Exception e);
    }

    public static String getSecondTimestamp() {
        //获取当前时间戳
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyddHHmmss");
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

}

