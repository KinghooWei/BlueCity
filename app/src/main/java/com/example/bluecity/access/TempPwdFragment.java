package com.example.bluecity.access;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.databinding.FragmentTempPwdBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TempPwdFragment extends Fragment {

    public TempPwdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentTempPwdBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_temp_pwd, container, false);

        //创建属于主线程的handler
        final Handler handler = new Handler();

        new Thread(new TempPwdThread("inquire", handler, binding.tvTempPwd, binding.btnTempPwd)).start();

        binding.btnTempPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String btnText = binding.btnTempPwd.getText().toString();
                if (btnText.equals("启用临时密码")) {
                    new Thread(new TempPwdThread("open", handler, binding.tvTempPwd, binding.btnTempPwd)).start();
                } else {
                    new Thread(new TempPwdThread("close", handler, binding.tvTempPwd, binding.btnTempPwd)).start();
                }
            }
        });
        return binding.getRoot();
    }

    private class TempPwdThread implements Runnable {
        private String type;
        private Handler handler;
        private TextView tvTempPwd;
        private Button btnTempPwd;
        private HttpUtils tempPwdHttp;
        String tempPwd = null;

        public TempPwdThread(String type, Handler handler, TextView tvTempPwd, Button btnTempPwd) {
            this.type = type;
            this.handler = handler;
            this.tvTempPwd = tvTempPwd;
            this.btnTempPwd = btnTempPwd;
        }

        @Override
        public void run() {
            JSONObject requestBody = new JSONObject();


            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), "userInfo");
            String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
            try {
                requestBody.put("service", "client.person.tempPsd");
                requestBody.put("phoneNum", phoneNum);
                requestBody.put("type", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = tempPwdHttp.sendJsonPost(requestBody.toString());

            try {
                JSONObject result = new JSONObject(response);
                tempPwd = result.optString("tempPwd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(tempPwd)) {
                handler.post(runnableUiOpen);
            } else {
                handler.post(runnableUiClose);
            }
        }

        // 构建Runnable对象，在runnable中更新界面
        Runnable runnableUiOpen = new Runnable() {
            @Override
            public void run() {
                //更新界面
                tvTempPwd.setText("未启用");
                btnTempPwd.setText("启用临时密码");
            }
        };

        Runnable runnableUiClose = new Runnable() {
            @Override
            public void run() {
                //更新界面
                tvTempPwd.setText(tempPwd);
                btnTempPwd.setText("关闭临时密码");
            }

        };
    }
}