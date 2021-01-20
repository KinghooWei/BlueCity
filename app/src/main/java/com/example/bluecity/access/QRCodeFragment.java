package com.example.bluecity.access;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.databinding.FragmentQRCodeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.bluecity.HttpUtils.sendJsonPost;
import static com.yzq.zxinglibrary.encode.CodeCreator.createQRCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRCodeFragment extends Fragment {

    private FragmentQRCodeBinding binding;
    private Context mContext;
    private String phoneNum;

    public QRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_r_code, container, false);
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext, "userInfo");
        phoneNum = sharedPreferencesUtil.loadString("phoneNum");
        new RequestQRCodeThread().start();
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        new DestroyQRCodeThread().start();
    }

    // 定义请求二维码的线程
    private class RequestQRCodeThread extends Thread {
        @Override
        public void run() {
            super.run();
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("service", "client.person.requestQRCode");
                requestBody.put("phoneNum", phoneNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = sendJsonPost(requestBody.toString());
            Log.d("response", response);
            try {
                JSONObject result = new JSONObject(response);
                String pwd = result.optString("QRCode");
                Message message = Message.obtain();
                message.obj = pwd;
                mHandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // 定义销毁二维码的线程
    private class DestroyQRCodeThread extends Thread {
        @Override
        public void run() {
            super.run();
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("service", "client.person.destroyQRCode");
                requestBody.put("phoneNum", phoneNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = sendJsonPost(requestBody.toString());
            try {
                JSONObject result = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            JSONObject requestBody = new JSONObject();
            Bitmap QRCode = createQRCode("blueCity-" + phoneNum + "-" + msg.obj.toString(), 700, 700, null);
            binding.ivQRCode.setImageBitmap(QRCode);
        }
    };
}
