package com.example.bluecity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.ViewHolder> {
    private List<String> mList;
    public LinearAdapter(List<String> list) {
        mList = list;
    }

    //创建子项item的布局
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_item,parent,false);
        return new ViewHolder(view);
    }

    //给控件设置数据
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvInfo.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //初始化子项控件
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tv_info);
        }
    }
}
