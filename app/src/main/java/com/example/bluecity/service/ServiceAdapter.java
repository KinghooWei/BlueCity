package com.example.bluecity.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;

public class ServiceAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private OnItemClickListener mListener;
    private int mItemCount;

    public ServiceAdapter(Context context, int itemCount, OnItemClickListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mItemCount = itemCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (position) {
            case 0:
                GridViewHolder.textView.setText("报修");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_repair);
                break;
            case 1:
                GridViewHolder.textView.setText("物业缴费");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_payment);
                break;
            case 2:
                GridViewHolder.textView.setText("社区公告");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_access_notice);
                break;
            case 3:
                GridViewHolder.textView.setText("车辆管理");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_car);
                break;
            case 4:
                GridViewHolder.textView.setText("呼叫物管");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_call);
                break;
            case 5:
                GridViewHolder.textView.setText("社区资讯");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_news);
                break;
            case 6:
                GridViewHolder.textView.setText("智慧商城");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_service_mall);
                break;
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
        return mItemCount;
    }

    static class GridViewHolder extends RecyclerView.ViewHolder{

        private static TextView textView;
        private static ImageView imageView;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_icon);
            imageView = itemView.findViewById(R.id.icon);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
