package com.example.administrator.imm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.imm.R;
import com.example.administrator.imm.common.AppConfig;
import com.example.administrator.imm.http.EventClick;
import com.example.administrator.imm.http.EventTypeChoose;
import com.example.administrator.imm.model.ListResp;
import com.example.administrator.imm.model.TypeResp;
import com.google.android.material.textview.MaterialTextView;
import com.hjq.demo.http.glide.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;

/**
 * 通过列表  实现的 tab
 */
public class RecyTabAdapter extends RecyclerView.Adapter<RecyTabAdapter.ViewHolder> {
    public RecyTabAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    public void setDataList(List<TypeResp.DataDTO> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    private List<TypeResp.DataDTO> dataList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_tab, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeResp.DataDTO item = dataList.get(position);
        MaterialTextView textView = holder.itemView.findViewById(R.id.tv_item_tab);
        textView.setText(item.getName());
        textView.setOnClickListener(view -> EventBus.getDefault().post(new EventTypeChoose(position)));
        if (item.isSelected()) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_type));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.indicator_uncheck));
        }
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
