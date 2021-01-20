package com.example.bluecity.my.information.password;

import android.content.Intent;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.R;
import com.example.bluecity.ThreadToast;

import org.json.JSONException;
import org.json.JSONObject;

class ModifyPwdHttpThread implements Runnable {
    private String passwordType, phoneNum, oldPwd, newPwd, confPwd;
    private Handler handler;
    private HttpUtils modifyPwdHttp;
    private AppCompatActivity mActivity;

    public ModifyPwdHttpThread(AppCompatActivity activity, Handler handler, String passwordType,
                               String phoneNum, String oldPwd, String newPwd, String confPwd) {
        this.mActivity = activity;
        this.passwordType = passwordType;
        this.phoneNum = phoneNum;
        this.newPwd = newPwd;
        this.oldPwd = oldPwd;
        this.confPwd = confPwd;
        this.handler = handler;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service", "client.person.passwordModify");
            jsonObject.put("phoneNum", phoneNum);
            jsonObject.put("passwordType", passwordType);
            jsonObject.put("oldPwd", oldPwd);
            jsonObject.put("newPwd", newPwd);
            jsonObject.put("confPwd", confPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = modifyPwdHttp.sendJsonPost(jsonObject.toString());
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
            case 1:
                ThreadToast.toast(mActivity, "原密码不正确");
                break;
            case 2:
                ThreadToast.toast(mActivity, "新密码不符合要求");
                break;
            case 3:
                ThreadToast.toast(mActivity, "两次填写的密码不一致");
                break;
            case -1:
                switch (passwordType) {
                    case "login":
                        //修改登录密码后重启应用
                        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                    case "communityModify":
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_communityPwdModifyFragment_to_infoFragment);
                            }
                        });
                        break;
                    case "communitySet":
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_communityPwdSetFragment_to_infoFragment);
                            }
                        });
                        break;
                    case "buildingModify":
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_buildingPwdModifyFragment_to_infoFragment);
                            }
                        });
                        break;
                    case "buildingSet":
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                NavController controller = Navigation.findNavController(mActivity, R.id.btn_finish);
                                controller.navigate(R.id.action_buildingPwdSetFragment_to_infoFragment);
                            }
                        });
                        break;
                }
                ThreadToast.toast(mActivity, "修改成功");
                break;
        }
    }
}
