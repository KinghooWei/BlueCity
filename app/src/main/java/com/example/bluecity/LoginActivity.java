package com.example.bluecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.bluecity.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private HttpUtils loginHttpUtil;
    private MyViewModel myViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);

        String autoPhoneNum = myViewModel.getInfo("phoneNum").getValue();
        String autoLoginPwd = myViewModel.getInfo("loginPwd").getValue();
//        Log.i("phoneNum",autoPhoneNum);
        if (autoPhoneNum != null && autoLoginPwd != null) {
            //自动登录
            new Thread(new AutoLoginThread(autoPhoneNum, autoLoginPwd)).start();
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = binding.etPhone.getText().toString();
                String loginPwd = binding.etPwd.getText().toString();
                new Thread(new LoginThread(phoneNum, loginPwd)).start();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    自动登录
     */
    class AutoLoginThread extends Thread {
        private String mPhoneNum, mLoginPwd;
        private int resultCode;

        private AutoLoginThread(String phoneNum, String loginPwd) {
            this.mPhoneNum = phoneNum;
            this.mLoginPwd = loginPwd;
        }

        @Override
        public void run() {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("service", "client.person.login");
                jsonObject.put("phoneNum", mPhoneNum);
                jsonObject.put("LoginPassword", mLoginPwd);
                jsonObject.put("isAuto", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = loginHttpUtil.sendJsonPost(jsonObject.toString());  //向服务器发送登录请求，并返回结果
            JSONObject jsonObjectRp = null;
            try {
                jsonObjectRp = new JSONObject(result);  //结果格式从string转json
                resultCode = jsonObjectRp.optInt("resultCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (resultCode) {
                case -1:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    break;
            }
        }
    }

    /*
    用户登录
     */
    class LoginThread extends Thread {

        private String mPhoneNum, mLoginPwd, name, portrait, community, building, roomId;
        private int resultCode;
        private boolean hasCommunityPwd, hasBuildingPwd, hasFace;

        private LoginThread(String phoneNum, String loginPwd) {
            this.mPhoneNum = phoneNum;
            this.mLoginPwd = loginPwd;
        }

        @Override
        public void run() {
//            super.run();
            if (TextUtils.isEmpty(mPhoneNum) || TextUtils.isEmpty(mLoginPwd)) {
                ThreadToast.toast(LoginActivity.this, "请输入完整信息再登录");
            } else {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("service", "client.person.login");
                    jsonObject.put("phoneNum", mPhoneNum);
                    jsonObject.put("LoginPassword", mLoginPwd);
                    jsonObject.put("isAuto", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String result = loginHttpUtil.sendJsonPost(jsonObject.toString());  //向服务器发送登录请求，并返回结果
                JSONObject jsonObjectRp = null;
                try {
                    jsonObjectRp = new JSONObject(result);  //结果格式从string转json
                    resultCode = jsonObjectRp.optInt("resultCode");
                    name = jsonObjectRp.optString("name");
                    portrait = jsonObjectRp.optString("portrait");
                    community = jsonObjectRp.optString("community");
                    building = jsonObjectRp.optString("building");
                    roomId = jsonObjectRp.optString("roomId");
                    hasCommunityPwd = jsonObjectRp.optBoolean("hasCommunityPwd");
                    hasBuildingPwd = jsonObjectRp.optBoolean("hasBuildingPwd");
                    hasFace = jsonObjectRp.optBoolean("hasFace");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("resultCode", String.valueOf(resultCode));
                switch (resultCode) {
                    case -1:
                        SharedPreferences shp = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shp.edit();
                        editor.putString("phoneNum", mPhoneNum).putString("loginPwd", mLoginPwd).putString("name", name).putString("portrait", portrait)
                                .putString("community", community).putString("building", building).putString("roomId", roomId)
                                .putBoolean("hasCommunityPwd", hasCommunityPwd).putBoolean("hasBuildingPwd", hasBuildingPwd)
                                .putBoolean("hasFace", hasFace);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 0:
                        ThreadToast.toast(LoginActivity.this, "账号或密码错误");
                        break;
                }
            }
        }
    }
}
