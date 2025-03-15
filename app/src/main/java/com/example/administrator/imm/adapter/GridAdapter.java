package com.example.administrator.imm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.imm.R;
import com.example.administrator.imm.common.AppConfig;
import com.example.administrator.imm.http.EventClick;
import com.example.administrator.imm.model.ListResp;
import com.hjq.demo.http.glide.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    public GridAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    public void setDataList(List<ListResp.DataDTO> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private List<ListResp.DataDTO> dataList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_img, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView iv = holder.itemView.findViewById(R.id.iv);
        String relPath = dataList.get(position).getIcon();
        if (!relPath.startsWith("http")) {
            relPath = AppConfig.Companion.getHostUrl() + relPath;
        }

        GlideApp.with(context).load(relPath).apply(new RequestOptions().transform(new RoundedCorners(10))).into(iv);
        iv.setOnClickListener(view -> EventBus.getDefault().post(new EventClick(position)));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
