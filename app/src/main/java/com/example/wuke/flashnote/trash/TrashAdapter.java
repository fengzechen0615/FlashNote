package com.example.wuke.flashnote.trash;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.Garbage;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<Garbage> mList;
    private Drawable[] ColorImages;

    // 文本
    private static final int TYPE_TEXT = 0;
    // Record
    private static final int TYPE_RECORD = 1;

    public TrashAdapter(Context mContext, List<Garbage> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
            TrashTextViewHolder viewHolder = new TrashTextViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_RECORD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false);
            TrashVoiceViewHolder viewHolder = new TrashVoiceViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColorImages = new Drawable[] {
                mContext.getResources().getDrawable(R.drawable.light_green),
                mContext.getResources().getDrawable(R.drawable.blue),
                mContext.getResources().getDrawable(R.drawable.red),
                mContext.getResources().getDrawable(R.drawable.orange),
                mContext.getResources().getDrawable(R.drawable.green),
        };

    }

    @Override
    public int getItemCount() {
        return null == mList ? 1 : mList.size();
    }
}
