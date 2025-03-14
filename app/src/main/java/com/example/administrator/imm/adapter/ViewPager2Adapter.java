package com.example.administrator.imm.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrator.imm.R;
import com.example.administrator.imm.http.EventClick;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {
    public void setDataList(List<Fragment> dataList) {
        this.dataList = dataList;
    }

    private List<Fragment> dataList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.frag_exp, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p = position;
//          holder.itemView.tra


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
