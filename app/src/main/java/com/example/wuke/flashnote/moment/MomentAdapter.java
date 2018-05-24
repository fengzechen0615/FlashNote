package com.example.wuke.flashnote.moment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.MomentDetail;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter {

    private List<MomentDetail> mList;
    private Context mContext;

    public MomentAdapter(Context mContext, List<MomentDetail> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.moment_item, parent, false);
        MomentViewHolder momentViewHolder = new MomentViewHolder(view);
        return momentViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        MomentViewHolder holder = (MomentViewHolder) viewholder;
        holder.moment_username.setText(mList.get(position).getMoment_username());
        holder.moment_content.setText(mList.get(position).getMoment_content());
        holder.moment_time.setText(mList.get(position).getMoment_time());
    }

    @Override
    public int getItemCount() {
        return null == mList ? 1 : mList.size();
    }
}
