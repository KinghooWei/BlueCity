package com.example.bluecity.my.information.face;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.ThreadPool;
import com.example.bluecity.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FaceProcessActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivFace;
    Button btnEncrypt;
    Button btnUpload;
    Bitmap origin;
    String base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_process);
        base64 = getIntent().getExtras().getString("origin");
        origin = Utils.base64ToBitmap(base64);
        initUI(origin);
    }

    private void initUI(Bitmap origin) {
        ivFace = findViewById(R.id.iv_face);
        btnEncrypt = findViewById(R.id.btn_encrypt);
        btnUpload = findViewById(R.id.btn_upload);

        ivFace.setImageBitmap(origin);
        btnEncrypt.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encrypt:
                btnEncrypt.setEnabled(false);
                Bitmap encrypt = Utils.encrypt(origin);
                ivFace.setImageBitmap(encrypt);
                btnUpload.setEnabled(true);
                break;
            case R.id.btn_upload:
                Future<Integer> future = ThreadPool.executorService.submit(new ModifyFaceHttpThread());
                try {
                    alertResult(future);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    private void alertResult(Future<Integer> future) throws ExecutionException, InterruptedException {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(), "userInfo");
        switch (future.get()) {
            case -1:
                sharedPreferencesUtil.saveBoolean("hasFace", true);
                Utils.showToast("上传成功");
                finish();
                break;
            case 1:
                alert("未检测到人脸，请重新拍照");
                break;
            case 2:
                sharedPreferencesUtil.saveBoolean("hasFace", true);
                alert("生成口罩和墨镜特征失败，将影响口罩和墨镜识别，建议重新拍照");
                break;
            case 3:
                sharedPreferencesUtil.saveBoolean("hasFace", true);
                alert("生成口罩特征失败，将影响口罩识别，建议重新拍照");
                break;
            case 4:
                sharedPreferencesUtil.saveBoolean("hasFace", true);
                alert("生成墨镜特征失败，将影响墨镜识别，建议重新拍照");
                break;
            case 0:
                alert("上传失败，请检查网络再重试");
                break;
        }
    }

    private class ModifyFaceHttpThread implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Log.i("HttpTest", "start->");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("service", "client.person.face");
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(), "userInfo");
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

    private void alert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext()).setTitle("提示").setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        builder.create().show();
    }
}