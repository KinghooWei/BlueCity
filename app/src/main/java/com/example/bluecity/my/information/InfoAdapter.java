package com.example.bluecity.my.information;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;
import com.example.bluecity.SharedPreferencesUtil;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private Fragment fragment;
    private String[] infoArr;
    private String[] resultArr;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;
        TextView tvResult;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tv_info);
            tvResult = itemView.findViewById(R.id.tv_result);
            layout = itemView.findViewById(R.id.lo_linear_item);
        }
    }

    InfoAdapter(Fragment fragment, String[] infoArr, String[] resultArr) {
        this.fragment = fragment;
        this.infoArr = infoArr;
        this.resultArr = resultArr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(fragment.getContext(), "userInfo");
        holder.tvInfo.setText(infoArr[position]);
        holder.tvResult.setText(resultArr[position]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoArr.length;
    }

    public interface OnItemClickListener {
        void onClick(int pos);
    }

    private InfoAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(InfoAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
