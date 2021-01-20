package com.example.bluecity.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;

public class HomeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private OnItemClickListener mListener;
    private int mItemCount;

    public HomeAdapter(Context context, int itemCount, OnItemClickListener listener){
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
                GridViewHolder.textView.setText("智能摄像头");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_home_camera);
                break;
            case 1:
                GridViewHolder.textView.setText("智能网关");
                GridViewHolder.imageView.setImageResource(R.drawable.ic_home_gateway);
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
