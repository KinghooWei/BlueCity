package com.example.bluecity.my.information;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.HttpUtils;
import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;
import com.example.bluecity.ThreadToast;
import com.example.bluecity.databinding.FragmentInfoBinding;
import com.example.bluecity.my.information.portrait.BaseFragment;
import com.example.bluecity.my.information.portrait.PhotoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends BaseFragment {

    private FragmentInfoBinding binding;
    private PopupWindow popupWindow;
    private HttpUtils httpUtils;
    SharedPreferencesUtil sharedPreferencesUtil;
    private InfoAdapter adapter;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    private static final int CODE_FACE_CAMERA_REQUEST = 0xb0;
    private static final int CODE_FACE_CROP_REQUEST = 0xb1;

    //    private File portraitFile = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_oriPortrait.jpg");
//    private File portraitCropFile = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_portrait.jpg");
    private File faceFile = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_oriFace.jpg");
    private File faceCropFile = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_face.jpg");
    private File portraitFile, portraitCropFile;
    private Uri portraitUri, portraitCropUri, faceUri, faceCropUri;
    String[] infoArr = new String[] {"头像", "人脸采集", "家庭住址", "登录密码", "小区密码", "楼栋密码", "退出登录"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false);
        binding.setLifecycleOwner(getActivity());
        sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), "userInfo");
        boolean hasFace = sharedPreferencesUtil.loadBoolean("hasFace");
        String communityResult = sharedPreferencesUtil.loadString("community");
        String buildingResult = sharedPreferencesUtil.loadString("building");
        String[] resultArr = new String[infoArr.length];
        resultArr[1] = hasFace ? "已上传" : "未上传";
        resultArr[2] = communityResult + " " + buildingResult;
        adapter = new InfoAdapter(this, infoArr, resultArr);
        adapter.setOnItemClickListener(new InfoAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                NavController controller;

                switch (pos) {
                    case 0:     //头像
                        showPopup();
                        break;
                    case 1:     //人脸
                        takePhotoFace();
                        break;
                    case 2:     //住址
                        controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                        controller.navigate(R.id.action_infoFragment_to_addressFragment);
                        break;
                    case 3:     //登录密码
                        controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                        controller.navigate(R.id.action_infoFragment_to_loginPwdModifyFragment);
                        break;
                    case 4:     //小区密码

                        boolean hasCommunityPwd = sharedPreferencesUtil.loadBoolean("hasCommunityPwd");
                        if (hasCommunityPwd) {
                            controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                            controller.navigate(R.id.action_infoFragment_to_communityPwdModifyFragment);
                        } else {
                            controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                            controller.navigate(R.id.action_infoFragment_to_communityPwdSetFragment);
                        }
                        break;
                    case 5:     //楼栋密码
                        boolean hasBuildingPwd = sharedPreferencesUtil.loadBoolean("hasBuildingPwd");
                        if (hasBuildingPwd) {
                            controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                            controller.navigate(R.id.action_infoFragment_to_buildingPwdModifyFragment);
                        } else {
                            controller = Navigation.findNavController(getActivity(), R.id.rv_info);
                            controller.navigate(R.id.action_infoFragment_to_buildingPwdSetFragment);
                        }
                        break;
                    case 6:     //注销
                        new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("确定要退出当前账号吗")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sharedPreferencesUtil.clearAll();
                                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        getContext().startActivity(intent);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                                }).setNegativeButton("取消", null).show();

                        break;
                }
            }
        });
        binding.rvInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvInfo.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void initUI() {

    }

    //初始化对话框
    private void showPopup() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_portrait, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,才能使用动画效果
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.updownpopwindow_anim_style);
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置弹窗消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });


        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);

        //设置位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        getActivity().getWindow().setAttributes(lp);
    }

    //点击事件
    private void setOnPopupViewClick(View view) {
        TextView tvPhoto, tvAlbum, tvCancel;

        tvPhoto = view.findViewById(R.id.tv_photo);
        tvAlbum = view.findViewById(R.id.tv_album);
        tvCancel = view.findViewById(R.id.tv_cancel);

        tvPhoto.setOnClickListener(new View.OnClickListener() {//拍照
            @Override
            public void onClick(View v) {
                requestPermissions(getContext(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        new RequestPermissionCallBack() {
                            @Override
                            public void granted() {
                                if (hasSdcard()) {      //设备有SD卡
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                        portraitUri = PhotoUtils.createImageUri(getContext());
//                                    } else {
//                                        try {
//                                            portraitFile = PhotoUtils.createImageFile(getContext());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        if (portraitFile != null) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                                //                                portraitUri = getImageContentUri(getContext(), portraitFile);   //一直返回0用这个？
//                                                //通过FileProvider创建一个content类型的Uri
//                                                portraitUri = FileProvider.getUriForFile(getContext(), "com.example.bluecity.fileprovider", portraitFile);
//                                            } else {
//                                                portraitUri = Uri.fromFile(portraitFile);
//                                            }
//                                        }
//                                    }
                                    portraitUri = PhotoUtils.createImageUri(getContext());
                                    PhotoUtils.takePicture(InfoFragment.this, portraitUri, CODE_CAMERA_REQUEST);
                                } else {
                                    Toast.makeText(getContext(), "设备没有SD卡", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void denied() {
                                Toast.makeText(getContext(), "部分权限获取失败，正常功能受到影响", Toast.LENGTH_SHORT).show();
                            }
                        });
                popupWindow.dismiss();
            }
        });
        tvAlbum.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {
                requestPermissions(getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        PhotoUtils.openPic(InfoFragment.this, CODE_GALLERY_REQUEST);
                    }

                    @Override
                    public void denied() {
                        Toast.makeText(getContext(), "部分权限获取失败，正常功能受到影响", Toast.LENGTH_SHORT).show();
                    }
                });
                popupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }

    private void takePhotoFace() {
        requestPermissions(getContext(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        if (hasSdcard()) {      //设备有SD卡
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                                faceUri = Uri.fromFile(faceFile);
//                            } else {
////                                                faceUri = getImageContentUri(getContext(), faceFile);   //一直返回0用这个？
////                                                通过FileProvider创建一个content类型的Uri
////                                                faceUri = getImageContentUri(getContext(), faceFile);   //一直返回0用这个？
//                                faceUri = FileProvider.getUriForFile(getContext(), "com.example.bluecity.fileprovider", faceFile);
//
//                            }
                            faceUri = PhotoUtils.createImageUri(getContext());
                            PhotoUtils.takePicture(InfoFragment.this, faceUri, CODE_FACE_CAMERA_REQUEST);
                        } else {
                            Toast.makeText(getContext(), "设备没有SD卡", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void denied() {
                        Toast.makeText(getContext(), "部分权限获取失败，正常功能受到影响", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
    startActivityForResult执行后的回调方法，接收返回的图片
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        Log.e("requestCode", String.valueOf(requestCode));
        Log.e("resultCode", String.valueOf(resultCode));
//        Log.e("data",data.toString());
        if (resultCode == RESULT_OK) {
            Log.e("result", "OK");
            Bitmap bitmap;
            switch (requestCode) {
                case CODE_CAMERA_REQUEST:   //拍照完成回调
//                    try {
//                        portraitCropFile = PhotoUtils.createImageFile(getContext());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    portraitCropUri = Uri.fromFile(portraitCropFile);
                    portraitCropUri = PhotoUtils.createImageUri(getContext());
                    PhotoUtils.cropImageUri(InfoFragment.this, portraitUri, portraitCropUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST:  //访问相册完成回调
                    Log.e("result", "相册回调");
                    if (hasSdcard()) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            portraitCropUri = PhotoUtils.createImageUri(getContext());
//                        } else {
//                            try {
//                                portraitCropFile = PhotoUtils.createImageFile(getContext());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            portraitCropUri = Uri.fromFile(portraitCropFile);   //file转Uri
//                        }
                        Uri newUri = data.getData();
                        portraitCropUri = PhotoUtils.createImageUri(getContext());
//                        Uri newUri = Uri.parse(PhotoUtils.getPath(getContext(), data.getData()));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                            newUri = FileProvider.getUriForFile(getContext(), "com.example.bluecity.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(InfoFragment.this, newUri, portraitCropUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(getContext(), "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:   //裁剪回调
                    Log.e("result", "裁剪回调");
//                    bitmap = PhotoUtils.getBitmapFromUri(getContext(), portraitCropUri);
                    bitmap = PhotoUtils.getBitmapfromUri(portraitCropUri, getContext());
                    if (bitmap != null) {
                        //showImages(bitmap);
                        String base64 = bitmapToBase64(bitmap);
                        Log.e("base64", base64);
                        new Thread(new ModifyPortraitHttpThread(getContext(), base64)).start(); //上传头像
                    }
                    break;
                case CODE_FACE_CAMERA_REQUEST:   //拍照完成回调
//                    faceCropUri = Uri.fromFile(faceCropFile);
//                    Log.e("fileCropUri", faceCropFile.toString());
//                    Log.e("cropImageUri", faceCropUri.toString());
                    faceCropUri = PhotoUtils.createImageUri(getContext());
                    PhotoUtils.cropImageUri(InfoFragment.this, faceUri, faceCropUri, 1, 1, output_X, output_Y, CODE_FACE_CROP_REQUEST);
                    adapter.notifyDataSetChanged();
                    break;
                case CODE_FACE_CROP_REQUEST:   //裁剪回调
                    Log.e("result", "裁剪回调");
                    bitmap = PhotoUtils.getBitmapFromUri(getContext(), faceCropUri);
                    if (bitmap != null) {
                        //showImages(bitmap);
//                        byte[] rgba = getPixelsRGBA(bitmap);
//                        String base64 = Base64.encodeToString(rgba, Base64.NO_WRAP);
                        //加密
                        String base64 = bitmapToBase64(bitmap);
                        Log.e("base64", base64);
                        new Thread(new ModifyFaceHttpThread(getContext(), base64)).start(); //上传人脸
                    }
                    break;
            }
        }

    }

    private class ModifyPortraitHttpThread extends Thread {
        private int resultCode;
        private Context context;
        private String mBase64;

        ModifyPortraitHttpThread(Context context, String base64) {
            this.context = context;
            this.mBase64 = base64;
        }

        @Override
        public void run() {
            Log.i("HttpTest", "start->");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("service", "client.person.portraitModify");
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, "userInfo");
                String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
                jsonObject.put("phoneNum", phoneNum);
                jsonObject.put("portrait", mBase64);
                //jsonObject.put("place", "1");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            String result = httpUtils.sendJsonPost(jsonObject.toString());  //向服务器发送请求，并从服务器返回结果
//            Message msg = new Message();
//            msg.obj = "result->"+result;
//            handler.sendMessage(msg);
//            Log.i("HttpTest", "result->"+result);
            JSONObject jsonObjectRp = null;
            try {
                jsonObjectRp = new JSONObject(result);  //结果string转json
                resultCode = jsonObjectRp.optInt("resultCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (resultCode) {
                case -1:
//                    Log.i(TAG, "头像上传成功");
                    SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), "userInfo");
                    sharedPreferencesUtil.saveString("portrait", mBase64);
                    break;
                case 0:
                    ThreadToast.toast(getContext(), "上传失败");
                    break;
            }

        }
    }

    private class ModifyFaceHttpThread implements Runnable {
        private int resultCode;
        private Context context;
        private String faceBase64;

        ModifyFaceHttpThread(Context context, String base64) {
            this.context = context;
            this.faceBase64 = base64;
        }

        @Override
        public void run() {
            Log.i("HttpTest", "start->");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("service", "client.person.face");
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, "userInfo");
                String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
                jsonObject.put("phoneNum", phoneNum);
                jsonObject.put("faceBase64", faceBase64);
                //jsonObject.put("place", "1");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            String result = httpUtils.sendJsonPost(jsonObject.toString());  //向服务器发送请求，并从服务器返回结果
            JSONObject jsonObjectRp = null;
            try {
                jsonObjectRp = new JSONObject(result);  //结果string转json
                resultCode = jsonObjectRp.optInt("resultCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            switch (resultCode) {
                case -1:
                    sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), "userInfo");
                    sharedPreferencesUtil.saveBoolean("hasFace", true);
                    ThreadToast.toast(getContext(), "上传成功");
                    break;
                case 1:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("未检测到人脸，请重新拍照")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            takePhotoFace();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                    });
                    break;
                case 2:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("生成口罩和墨镜特征失败，将影响口罩和墨镜识别，建议重新拍照")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            takePhotoFace();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                    });
                    break;
                case 3:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("生成口罩特征失败，将影响口罩识别，建议重新拍照")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            takePhotoFace();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                    });
                    break;
                case 4:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("生成墨镜特征失败，将影响墨镜识别，建议重新拍照")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            takePhotoFace();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }
                    });
                    break;
                case 0:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("上传失败，请检查网络再重试")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                    });
                    break;
            }

        }
    }

    /**
     * 检查设备是否存在SD card
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * bitmap转base64
     */
    private static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();

                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}