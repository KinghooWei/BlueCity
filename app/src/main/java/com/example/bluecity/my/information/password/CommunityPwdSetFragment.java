package com.example.bluecity.my.information.password;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityPwdSetFragment extends Fragment {

    private EditText etPwd, etConfPwd;

    public CommunityPwdSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_access_pwd,container,false);

        etPwd = view.findViewById(R.id.et_pwd);
        etConfPwd = view.findViewById(R.id.et_conf_pwd);
        Button btnFinish = view.findViewById(R.id.btn_finish);
        final Handler handler = new Handler();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPwd.getText().toString().length() == 6) {
                    if (etPwd.getText().toString().equals(etConfPwd.getText().toString())) {
                        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity(), "userInfo");
                        String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
                        new Thread(new SetPwdHttpThread((AppCompatActivity) getContext(), handler,
                                "community", phoneNum, etPwd.getText().toString(),
                                etConfPwd.getText().toString())).start();
                    } else {
                        Toast.makeText(getContext(), "两次填写的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "密码不符合要求", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
