package com.example.bluecity.access;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bluecity.R;
import com.example.bluecity.enity.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private RecordFragment fragment;
    private List<Record> recordList;

    public RecordAdapter(RecordFragment fragment, List<Record> recordList) {
        this.fragment = fragment;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.time.setText(recordList.get(position).getTime());
        holder.community.setText(recordList.get(position).getCommunity());
        holder.building.setText(recordList.get(position).getBuilding());
        Glide.with(fragment).load(recordList.get(position).getScene()).into(holder.scene);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onclick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView community;
        TextView building;
        ImageView scene;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            community = itemView.findViewById(R.id.community);
            building = itemView.findViewById(R.id.building);
            scene = itemView.findViewById(R.id.scene);
            layout = itemView.findViewById(R.id.lo_record);
        }
    }

    interface OnItemClickListener {
        void onclick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
