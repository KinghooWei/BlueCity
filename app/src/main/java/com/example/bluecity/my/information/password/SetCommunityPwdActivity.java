//package com.example.bluecity.my.information.password;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bluecity.R;
//import com.example.bluecity.SharedPreferencesUtil;
//
//public class SetCommunityPwdActivity extends AppCompatActivity {
//    private EditText etPwd, etConfPwd;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.set_access_pwd);
//
//        etPwd = findViewById(R.id.et_pwd);
//        etConfPwd = findViewById(R.id.et_conf_pwd);
//        Button btnFinish = findViewById(R.id.btn_finish);
//
//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (etPwd.getText().toString().length() == 6) {
//                    if (etPwd.getText().toString().equals(etConfPwd.getText().toString())) {
//                        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(SetCommunityPwdActivity.this, "userInfo");
//                        String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
//                        new Thread(new SetPwdHttpThread(SetCommunityPwdActivity.this, "community", phoneNum, etPwd.getText().toString(), etConfPwd.getText().toString())).start();
//                    } else {
//                        Toast.makeText(SetCommunityPwdActivity.this, "两次填写的密码不一致", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(SetCommunityPwdActivity.this, "密码不符合要求", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
