package com.example.administrator.imm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.imm.R;
import com.example.administrator.imm.http.EventClick;
import com.google.android.material.textview.MaterialTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    public EmojiAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private List<String> dataList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = dataList.get(position);
        holder.tv_emoji.setText(item);
        holder.tv_emoji.setOnClickListener(view -> EventBus.getDefault().post(new EventClick(position, null)));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tv_emoji;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_emoji = itemView.findViewById(R.id.tv_emoji);
        }
    }
}
