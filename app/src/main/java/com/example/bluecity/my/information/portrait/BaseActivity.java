//package com.example.bluecity.my.information.portrait;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Settings;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class BaseActivity extends AppCompatActivity {
//    private final int mRequestCode = 1024;
//    private RequestPermissionCallBack mRequestPermissionCallBack;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    /**
//     * 权限请求结果回调
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        boolean hasAllGranted = true;
//        StringBuilder permissionName = new StringBuilder();
//        for (String s : permissions) {
//            permissionName = permissionName.append(s + "\r\n");
//        }
//        switch (requestCode) {
//            case mRequestCode: {
//                for (int i = 0; i < grantResults.length; ++i) {
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        hasAllGranted = false;
//                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false,
//                        //则可以推断出用户选择了“不再提示”选项，在这种情况下需要引导用户至设置页手动授权
//                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
//                            new AlertDialog.Builder(BaseActivity.this).setTitle("PermissionTest")   //设置对话框标题
//                                    .setMessage("获取相关权限失败：" + permissionName +
//                                            "将导致部分功能无法正常使用，需要到设置页面手动授权")    //设置显示内容
//                                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {    //确定按钮的响应事件
//                                            //TODO Auto-generated method stub
//                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
//                                            intent.setData(uri);
//                                            startActivity(intent);
//                                            dialog.dismiss();
//                                        }
//                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //TODO Auto-generated method stub
//                                    dialog.dismiss();
//                                }
//                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
//                                @Override
//                                public void onCancel(DialogInterface dialog) {
//                                    mRequestPermissionCallBack.denied();
//                                }
//                            }).show();  //在按键响应事件中显示此对话框
//                        } else {
//                            //用户拒绝权限请求，但未选中“不再提示”选项
//                            mRequestPermissionCallBack.denied();
//                        }
//                        break;
//                    }
//                }
//                if (hasAllGranted) {
//                    mRequestPermissionCallBack.granted();
//                }
//            }
//        }
//    }
//
//    /**
//     * 发起权限请求
//     *
//     * @param context
//     * @param permissions
//     * @param callBack
//     */
//    public void requestPermissions(final Context context, final String[] permissions, RequestPermissionCallBack callBack) {
//        this.mRequestPermissionCallBack = callBack;
//        StringBuilder permissionNames = new StringBuilder();
//        for (String s : permissions) {
//            permissionNames = permissionNames.append(s + "\r\n");
//        }
//        //如果所有权限都已授权，则直接返回授权成功，只要有一项未授权，则发起权限请求
//        boolean isAllGranted = true;
//        for (final String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
//                isAllGranted = false;
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
//                    new AlertDialog.Builder(BaseActivity.this).setTitle("提示")
//                            .setMessage("需要如下权限：" + permissionNames + "请允许，否则将影响部分功能的正常使用。")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //TODO Auto-generated method stub
//                                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
//                                }
//                            }).show();
//                } else {
//                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
//                }
//                break;
//            }
//        }
//        if (isAllGranted) {
//            mRequestPermissionCallBack.granted();
//            return;
//        }
//    }
//
//    /**
//     * 权限请求结果回调接口
//     */
//    public interface RequestPermissionCallBack {
//        /**
//         * 同意授权
//         */
//        void granted();
//
//        /**
//         * 取消授权
//         */
//        void denied();
//    }
//}
