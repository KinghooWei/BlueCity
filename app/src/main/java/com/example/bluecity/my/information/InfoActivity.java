//package com.example.bluecity.my.information;
//
////android:authorities="com.studio.cameraalbumtest.fileprovider"
//
//import android.Manifest;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.content.FileProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.bluecity.HttpUtils;
//import com.example.bluecity.R;
//import com.example.bluecity.SharedPreferencesUtil;
//import com.example.bluecity.ThreadToast;
//import com.example.bluecity.my.information.address.AddressActivity;
//import com.example.bluecity.my.information.password.AccountPwdActivity;
//import com.example.bluecity.my.information.password.BuildingPwdActivity;
//import com.example.bluecity.my.information.password.CommunityPwdActivity;
//import com.example.bluecity.my.information.password.SetBuildingPwdActivity;
//import com.example.bluecity.my.information.password.SetCommunityPwdActivity;
//import com.example.bluecity.my.information.portrait.BaseActivity;
//import com.example.bluecity.my.information.portrait.PhotoUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//
//public class InfoActivity extends BaseActivity implements View.OnClickListener {
//
//    private RecyclerView rvInfo;
//    //    private ImageView ivReturn;
//    private AlertDialog dialog;
//    private HttpUtils modifyPortraitHttp;
//    private String TAG = "info:";
//
//    private static final int CODE_GALLERY_REQUEST = 0xa0;
//    private static final int CODE_CAMERA_REQUEST = 0xa1;
//    private static final int CODE_RESULT_REQUEST = 0xa2;
//    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
//    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/blueCity_portrait.jpg");
//    private Uri imageUri;
//    private Uri cropImageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_info);
//
//
//        Log.e("pos", "create");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e("pos", "resume");
//
//        rvInfo = findViewById(R.id.rv_info);
////        ivReturn = findViewById(R.id.iv_return);
//        rvInfo.setLayoutManager(new LinearLayoutManager(this));
//        rvInfo.setAdapter(new InfoAdapter("个人信息", null, this, new InfoAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                Intent intent = null;
//                SharedPreferencesUtil sharedPreferencesUtil;
//                switch (pos) {
//
//                    case 0://头像
//                        Initialize();
//                        break;
//                    case 1:
//                        break;
//                    case 2://住址
//                        intent = new Intent(InfoActivity.this, AddressActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 3://账户密码
//                        intent = new Intent(InfoActivity.this, AccountPwdActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 4://社区密码
//                        sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(), "userInfo");
//                        boolean hasCommunityPwd = sharedPreferencesUtil.loadBoolean("hasCommunityPwd");
//                        if (hasCommunityPwd) {
//                            intent = new Intent(InfoActivity.this, CommunityPwdActivity.class);
//                        } else
//                            intent = new Intent(InfoActivity.this, SetCommunityPwdActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 5://楼栋密码
//                        sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(), "userInfo");
//                        boolean hasBuildingPwd = sharedPreferencesUtil.loadBoolean("hasBuildingPwd");
//                        if (hasBuildingPwd) {
//                            intent = new Intent(InfoActivity.this, BuildingPwdActivity.class);
//                        } else
//                            intent = new Intent(InfoActivity.this, SetBuildingPwdActivity.class);
//                        startActivity(intent);
//                        break;
//
//                }
//            }
//        }));
//    }
//
//    /*
//    startActivityForResult执行后的回调方法，接收返回的图片
//    */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        int output_X = 480, output_Y = 480;
//        Log.e("requestCode", String.valueOf(requestCode));
//        Log.e("resultCode", String.valueOf(resultCode));
////        Log.e("data",data.toString());
//        if (resultCode == RESULT_OK) {
//            Log.e("result", "OK");
//            switch (requestCode) {
//                case CODE_CAMERA_REQUEST:   //拍照完成回调
//                    cropImageUri = Uri.fromFile(fileCropUri);
//                    Log.e("fileCropUri", fileCropUri.toString());
//                    Log.e("cropImageUri", cropImageUri.toString());
//                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
//                    break;
//                case CODE_GALLERY_REQUEST:  //访问相册完成回调
//                    Log.e("result", "相册回调");
//                    if (hasSdcard()) {
//                        cropImageUri = Uri.fromFile(fileCropUri);   //file转Uri
////                        Log.e("fileCropUri",fileCropUri.toString());    //  /storage/emulated/0/crop_photo.jpg
////                        Log.e("crpImageUri",cropImageUri.toString());   //  file:///storage/emulated/0/crop_photo.jpg
//                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
//                        Log.e("data", data.getDataString());   //  content://com.android.providers.media.documents/document/image%3A29
//                        Log.e("newUri1", newUri.toString());    //  file:////storage/emulated/0/photo.jpg
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                            newUri = FileProvider.getUriForFile(this, "com.example.bluecity.fileprovider", new File(newUri.getPath()));
//                        Log.e("newUri2", newUri.toString());  //  content://com.example.bluecity.fileprovider/camera_photos/photo.jpg
//                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
//                    } else {
//                        Toast.makeText(InfoActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case CODE_RESULT_REQUEST:   //裁剪回调
//                    Log.e("result", "裁剪回调");
//                    Bitmap bitmap = PhotoUtils.getBitmapfromUri(cropImageUri, this);
//                    if (bitmap != null) {
//                        //showImages(bitmap);
//                        String base64 = bitmapToBase64(bitmap);
//                        Log.e("base64", base64);
//                        new Thread(new ModifyPortraitHttpThread(getApplicationContext(), base64)).start();
//                    }
//                    break;
//            }
//        }
//
//    }
//
//    private class ModifyPortraitHttpThread extends Thread {
//        private int resultCode;
//        private Context context;
//        private String mBase64;
//
//        ModifyPortraitHttpThread(Context context, String base64) {
//            this.context = context;
//            this.mBase64 = base64;
//        }
//
//        @Override
//        public void run() {
//            Log.i("HttpTest", "start->");
//
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("service", "client.person.portraitModify");
//                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(context, "userInfo");
//                String phoneNum = sharedPreferencesUtil.loadString("phoneNum");
//                jsonObject.put("phoneNum", phoneNum);
//                jsonObject.put("portrait", mBase64);
//                //jsonObject.put("place", "1");
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//            String result = modifyPortraitHttp.sendJsonPost(jsonObject.toString());  //向服务器发送请求，并从服务器返回结果
////            Message msg = new Message();
////            msg.obj = "result->"+result;
////            handler.sendMessage(msg);
////            Log.i("HttpTest", "result->"+result);
//            JSONObject jsonObjectRp = null;
//            try {
//                jsonObjectRp = new JSONObject(result);  //结果string转json
//                resultCode = jsonObjectRp.optInt("resultCode");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            switch (resultCode) {
//                case -1:
//                    Log.i(TAG, "头像上传成功");
//                    SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(), "userInfo");
//                    sharedPreferencesUtil.saveString("portrait", mBase64);
//                    break;
//                case 0:
//                    ThreadToast.toast(InfoActivity.this, "上传失败");
//                    break;
//            }
//
//        }
//    }
//
//    //初始化对话框
//    private void Initialize() {
//
//        TextView tvPhoto, tvAlbum, tvCancel;
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);//创建对话框
//        View layout = getLayoutInflater().inflate(R.layout.popup_portrait, null);//获取自定义布局
//        (dialog = builder.setView(layout).create()).show();//显示对话框
//
//        tvPhoto = layout.findViewById(R.id.tv_photo);
//        tvAlbum = layout.findViewById(R.id.tv_album);
//        tvCancel = layout.findViewById(R.id.tv_cancel);
//        //设置监听
//        tvPhoto.setOnClickListener(this);
//        tvAlbum.setOnClickListener(this);
//        tvCancel.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_photo:
//                requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
//                    @Override
//                    public void granted() {
//                        if (hasSdcard()) {      //设备有SD卡
////                            Date date = new Date();
////                            String time = date.toLocaleString();
////                            File fileUri = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/blueCity/" + time + ".jpg");
////                            if (!fileUri.exists())
////                                fileUri.getParentFile().mkdirs();
//                            Log.e("fileUri", fileUri.toString());    //  /storage/emulated/0/photo.jpg
//                            imageUri = Uri.fromFile(fileUri);
//                            Log.e("imageUri1", imageUri.toString()); //  file:///storage/emulated/0/photo.jpg
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                                imageUri = getImageContentUri(InfoActivity.this, fileUri);
//                            //通过FileProvider创建一个content类型的Uri
////                                imageUri = FileProvider.getUriForFile(InfoActivity.this,"com.example.bluecity.fileprovider",fileUri);
////                            Log.e("imageUri2",imageUri.toString()); //  拍照后保存的路径content://com.example.bluecity.fileprovider/camera_photos/photo.jpg
////                            imageUri = getImageContentUri(InfoActivity.this,fileUri);
//                            PhotoUtils.takePicture(InfoActivity.this, imageUri, CODE_CAMERA_REQUEST);
//                        } else {
//                            Toast.makeText(InfoActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void denied() {
//                        Toast.makeText(InfoActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialog.dismiss();
//                break;
//            case R.id.tv_album:
//                requestPermissions(InfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
//                    @Override
//                    public void granted() {
//                        PhotoUtils.openPic(InfoActivity.this, CODE_GALLERY_REQUEST);
//                    }
//
//                    @Override
//                    public void denied() {
//                        Toast.makeText(InfoActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialog.dismiss();
//                break;
//            case R.id.tv_cancel:
//                dialog.dismiss();
//                break;
//        }
//    }
//
//    /**
//     * 检查设备是否存在SD card
//     */
//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        return state.equals(Environment.MEDIA_MOUNTED);
//    }
//
//    public static Uri getImageContentUri(Context context, File imageFile) {
//        String filePath = imageFile.getAbsolutePath();
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Images.Media._ID},
//                MediaStore.Images.Media.DATA + "=? ",
//                new String[]{filePath}, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor
//                    .getColumnIndex(MediaStore.MediaColumns._ID));
//            Uri baseUri = Uri.parse("content://media/external/images/media");
//            return Uri.withAppendedPath(baseUri, "" + id);
//        } else {
//            if (imageFile.exists()) {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DATA, filePath);
//                return context.getContentResolver().insert(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                return null;
//            }
//        }
//    }
//
//    /**
//     * bitmap转base64
//     *
//     * @param bitmap
//     * @return
//     */
//    private static String bitmapToBase64(Bitmap bitmap) {
//        String result = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            if (bitmap != null) {
//                baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//                baos.flush();
//                baos.close();
//
//                byte[] bitmapBytes = baos.toByteArray();
//                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (baos != null) {
//                    baos.flush();
//                    baos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//}
//
