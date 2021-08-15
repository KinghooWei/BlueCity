package com.example.bluecity.my.information.network;

import android.util.Log;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.MyApplication;
import com.example.bluecity.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class ModifyFaceHttpThread implements Callable<Integer> {
    private String base64;

    public ModifyFaceHttpThread(String base64) {
        this.base64 = base64;
    }

    @Override
    public Integer call() throws Exception {
        Log.i("HttpTest", "start->");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "client.person.face");
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(MyApplication.context, "userInfo");
            String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
            jsonObject.put("phoneNum", phoneNum);
            jsonObject.put("faceBase64", base64);
            //jsonObject.put("place", "1");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        String result = HttpUtils.sendJsonPost(jsonObject.toString());  //向服务器发送请求，并从服务器返回结果
        JSONObject jsonObjectRp = null;
        try {
            jsonObjectRp = new JSONObject(result);  //结果string转json
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectRp.optInt("resultCode");
    }
}