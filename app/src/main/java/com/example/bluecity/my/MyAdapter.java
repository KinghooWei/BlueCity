package com.example.bluecity.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;

public class MyAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private OnItemClickListener mListener;
    private int mItemCount;

    public MyAdapter(Context context, int itemCount, OnItemClickListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mItemCount = itemCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.linear_item_my,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (position) {
            case 0:
                LinearViewHolder.textView.setText("使用说明");
                LinearViewHolder.imageView.setImageResource(R.drawable.ic_my_help);
                break;
            case 1:
                LinearViewHolder.textView.setText("推荐给邻居");
                LinearViewHolder.imageView.setImageResource(R.drawable.ic_my_share);
                break;
            case 2:
                LinearViewHolder.textView.setText("给APP评分");
                LinearViewHolder.imageView.setImageResource(R.drawable.ic_my_mark);
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

    static class LinearViewHolder extends RecyclerView.ViewHolder{

        private static TextView textView;
        private static ImageView imageView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_icon);
            imageView = itemView.findViewById(R.id.icon);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
