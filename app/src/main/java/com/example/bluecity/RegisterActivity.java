package com.example.bluecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.bluecity.HttpUtils.getSecondTimestamp;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private HttpUtils mhttpUtil;
    private EditText etName, etPhone, etPwd, etConfPwd;
    private Button btnRegister;
    private static final String TAG = "registerActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_pwd);
        etConfPwd = findViewById(R.id.et_confirm_pwd);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);
    }

    private boolean isPhoneNum(String num) {
        String patt = "^[1]([3-9])[0-9]{9}$";
        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    private boolean isPwdValid(String pwd) {
        return ((pwd.trim().length() > 3) && (pwd.trim().length() < 21));
    }

    void register() {

        int resultCode;


        boolean isMsgValid = false;
        String name = etName.getText().toString();
        String phoneNum = etPhone.getText().toString();
        String pwd = etPwd.getText().toString();
        String confPwd = etConfPwd.getText().toString();

        if (TextUtils.isEmpty(etName.getText()) || TextUtils.isEmpty(etPhone.getText()) || TextUtils.isEmpty(etPwd.getText()) || TextUtils.isEmpty(etConfPwd.getText()))
            Utils.showToast("信息不完整");
        else if (!isPhoneNum(phoneNum))
            Utils.showToast("手机号码不正确");
        else if (!isPwdValid(pwd))
            Utils.showToast("密码不符合要求");
        else if (pwd.compareTo(confPwd) != 0)
            Utils.showToast("两次填写的密码不一致");
        else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("service", "client.person.register");
                jsonObject.put("name", name);
                jsonObject.put("phoneNum", phoneNum);
                jsonObject.put("loginPassword", pwd);
                jsonObject.put("timestamp", getSecondTimestamp());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            mhttpUtil.sendJsonPost(jsonObject.toString(), new HttpUtils.HttpCallbackListener() {
                @Override
                public void onFinish(String response) throws JSONException {
                    Utils.logI(TAG, response);
                    JSONObject result;
                    result = new JSONObject(response);
                    int resultCode = result.optInt("resultCode");
                    switch (resultCode) {
                        case -1:
                            Message msg = new Message();
                            msg.what = SUCCEED;
                            handler.sendMessage(msg);
                            break;
                        case 1:
                            Utils.showToastInThread("手机号已被注册");
                            break;
                        case 0:
                            Utils.showToastInThread("请检查网络连接");
                            break;
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    final static int SUCCEED = 0;
    final static int FAIL = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCEED:
                    Toast.makeText(MyApplication.context,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                case FAIL:
                    Toast.makeText(MyApplication.context,"手机号已被注册",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
        }
    }
}
