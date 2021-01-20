package com.example.bluecity.my.information.password;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuildingPwdModifyFragment extends Fragment {

    private EditText etOldPwd, etNewPwd, etConfPwd;

    public BuildingPwdModifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_login_pwd,container,false);
        etOldPwd = view.findViewById(R.id.et_old_pwd);
        etNewPwd = view.findViewById(R.id.et_new_pwd);
        etConfPwd = view.findViewById(R.id.et_conf_pwd);
        final Button btnFinish = view.findViewById(R.id.btn_finish);
        final Handler handler = new Handler();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getActivity(), "userInfo");
                String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
                new Thread(new ModifyPwdHttpThread((AppCompatActivity) getActivity(), handler,
                        "buildingModify", phoneNum, etOldPwd.getText().toString(),
                        etNewPwd.getText().toString(), etConfPwd.getText().toString())).start();
            }
        });
        return view;
    }
}
