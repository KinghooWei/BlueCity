package com.example.bluecity.access;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluecity.R;
import com.example.bluecity.my.information.address.AddressAdapter;

import java.util.List;
import java.util.zip.Inflater;

public class AccessAdapter extends RecyclerView.Adapter<AccessAdapter.ViewHolder> {
    private AccessFragment fragment;
    private List<Integer> iconSrcs;
    private List<String> iconInfos;

    public AccessAdapter(AccessFragment fragment, List<Integer> iconSrcs, List<String> iconInfos) {
        this.fragment = fragment;
        this.iconSrcs = iconSrcs;
        this.iconInfos = iconInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(iconInfos.get(position));
        holder.imageView.setImageResource(iconSrcs.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconInfos.size();
    }


    public interface OnItemClickListener {
        void onClick(int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            textView = itemView.findViewById(R.id.name_icon);
            layout = itemView.findViewById(R.id.lo_access_fun);
        }
    }
}
