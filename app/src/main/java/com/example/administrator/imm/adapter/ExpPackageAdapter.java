package com.example.administrator.imm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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
import com.example.administrator.imm.model.TypeResp;
import com.google.android.material.textview.MaterialTextView;
import com.hjq.demo.http.glide.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 表情包列表
 */
public class ExpPackageAdapter extends RecyclerView.Adapter<ExpPackageAdapter.ViewHolder> {
    public void setDataList(List<TypeResp.DataDTO> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public ExpPackageAdapter(Context context) {
        this.context = context;
    }

    private Context context;
    private List<TypeResp.DataDTO> dataList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = View.inflate(parent.getContext(), R.layout.item_package, parent, false);

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p = position;
        holder.tv_name.setText(dataList.get(position).getName());
        holder.tv_num.setText(dataList.get(position).getCount() + " Emojis");

        String relPath = dataList.get(position).getIcon();
        if (!relPath.startsWith("http")) {
            relPath = AppConfig.Companion.getHostUrl() + relPath;
        }
        GlideApp.with(context).asBitmap().load(relPath).apply(new RequestOptions().transform(new RoundedCorners(4)))
                .error(R.mipmap.qidai)
                .into(holder.iv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventClick(p));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        MaterialTextView tv_name;
        MaterialTextView tv_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num = itemView.findViewById(R.id.tv_num);


        }
    }
}
