package com.example.bluecity.my.information.password;

import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.ThreadToast;

import org.json.JSONException;
import org.json.JSONObject;

public class SetPwdHttpThread implements Runnable {
    private String passwordType, phoneNum, pwd, confPwd;
    private Handler handler;
    private HttpUtils setPwdHttp;
    private AppCompatActivity mActivity;

    public SetPwdHttpThread(AppCompatActivity activity, Handler handler,String passwordType,
                            String phoneNum, String pwd, String confPwd) {
        this.mActivity = activity;
        this.handler = handler;
        this.passwordType = passwordType;
        this.phoneNum = phoneNum;
        this.pwd = pwd;
        this.confPwd = confPwd;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "client.person.passwordSet");
            jsonObject.put("phoneNum", phoneNum);
            jsonObject.put("passwordType", passwordType);
            jsonObject.put("password", pwd);
            jsonObject.put("confPwd", confPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = setPwdHttp.sendJsonPost(jsonObject.toString());
        JSONObject jsonObjectRp = null;
        try {
            jsonObjectRp = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int resultCode = jsonObjectRp.optInt("resultCode");
        switch (resultCode) {
            case 0:
                ThreadToast.toast(mActivity, "请检查网络");
                break;
            case -1:

                SharedPreferencesUtil sharedPreferencesUtil;
                Log.i("myLog","-1");
                switch (passwordType) {
                    case "communitySet":
                        sharedPreferencesUtil = new SharedPreferencesUtil(mActivity, "userInfo");
                        sharedPreferencesUtil.saveBoolean("hasCommunityPwd", true);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_communityPwdSetFragment_to_infoFragment);
                            }
                        });
                        break;
                    case "buildingSet":
                        sharedPreferencesUtil = new SharedPreferencesUtil(mActivity, "userInfo");
                        sharedPreferencesUtil.saveBoolean("hasBuildingPwd", true);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_communityPwdSetFragment_to_infoFragment);
                            }
                        });
                        Log.i("myLog","test");
                        break;
                    default:
                        Log.i("myLog","default");
                        break;
                }
                ThreadToast.toast(mActivity, "设置成功");
        }
    }
}
