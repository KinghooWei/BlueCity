//package com.example.bluecity.my.information.password;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bluecity.R;
//import com.example.bluecity.SharedPreferencesUtil;
//
//public class CommunityPwdActivity extends AppCompatActivity {
//    private EditText etOldPwd, etNewPwd, etConfPwd;
//    private Button btnFinish;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.modify_access_pwd);
//
//        etOldPwd = findViewById(R.id.et_old_pwd);
//        etNewPwd = findViewById(R.id.et_new_pwd);
//        etConfPwd = findViewById(R.id.et_conf_pwd);
//        btnFinish = findViewById(R.id.btn_finish);
//
//        btnFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(CommunityPwdActivity.this, "userInfo");
//                String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
//                new Thread(new ModifyPwdHttpThread(CommunityPwdActivity.this, "community", phoneNum, etOldPwd.getText().toString(), etNewPwd.getText().toString(), etConfPwd.getText().toString())).start();
//            }
//        });
//    }
//}
