package com.example.bluecity.my.information;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;

public class InfoAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private OnItemClickListener mListener;

    InfoAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(mContext, "userInfo");
        switch (position) {
            case 0:
                LinearViewHolder.tvInfo.setText("头像");
                String portraitBase64 = sharedPreferencesUtil.loadString("portrait");
                if (!TextUtils.isEmpty(portraitBase64)) {
                    byte[] bytes = Base64.decode(portraitBase64, Base64.NO_WRAP);
                    Bitmap portraitBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    LinearViewHolder.ivResult.setImageBitmap(portraitBitmap);
                }
                break;
            case 1:
                LinearViewHolder.tvInfo.setText("人脸采集");
                boolean hasFace = sharedPreferencesUtil.loadBoolean("hasFace");
                if (hasFace) {
                    LinearViewHolder.tvResult.setText("已上传");
                } else {
                    LinearViewHolder.tvResult.setText("未上传");
                }
                break;
            case 2:
                LinearViewHolder.tvInfo.setText("家庭住址");
                String communityResult = sharedPreferencesUtil.loadString("community");
                String buildingResult = sharedPreferencesUtil.loadString("building");
                if (communityResult != null && buildingResult != null) {
                    LinearViewHolder.tvResult.setText(communityResult + "  " + buildingResult);
                }
                break;
            case 3:
                LinearViewHolder.tvInfo.setText("登录密码");
                break;
            case 4:
                LinearViewHolder.tvInfo.setText("小区大门密码");
                break;
            case 5:
                LinearViewHolder.tvInfo.setText("楼门密码");
                break;
            case 6:
                LinearViewHolder.tvInfo.setText("退出登录");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder {

        private static TextView tvInfo, tvResult;
        private static ImageView ivResult;

        LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tv_info);
            tvResult = itemView.findViewById(R.id.tv_result);
            ivResult = itemView.findViewById(R.id.iv_result);
        }
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }
}
