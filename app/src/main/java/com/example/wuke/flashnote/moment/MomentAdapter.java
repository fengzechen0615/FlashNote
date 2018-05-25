package com.example.wuke.flashnote.moment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.MomentDetail;
import com.example.wuke.flashnote.record.Record;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter {

    private List<MomentDetail> mList;
    private Context mContext;

    // 文本
    private static final int TYPE_TEXT = 0;
    // Record
    private static final int TYPE_RECORD = 1;

    public MomentAdapter(Context mContext, List<MomentDetail> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.moment_text, parent, false);
            MomentTextViewHolder momentTextViewHolder = new MomentTextViewHolder(view);
            return momentTextViewHolder;
        } else if (viewType == TYPE_RECORD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.moment_voice, parent, false);
            MomentVoiceViewHolder momentVoiceViewHolder = new MomentVoiceViewHolder(view);
            return momentVoiceViewHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == mList.get(position).getData_type()) {
            return TYPE_TEXT;
        } else if (1 == mList.get(position).getData_type()) {
            return TYPE_RECORD;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        if (viewholder instanceof MomentTextViewHolder) {
            MomentTextViewHolder holder = (MomentTextViewHolder) viewholder;
            holder.moment_username.setText(mList.get(position).getMoment_username());
            holder.moment_content.setText(mList.get(position).getMoment_content());
            holder.moment_time.setText(mList.get(position).getMoment_time());
        }
        else if (viewholder instanceof MomentVoiceViewHolder) {
            MomentVoiceViewHolder holder = (MomentVoiceViewHolder) viewholder;
            holder.moment_username_voice.setText(mList.get(position).getMoment_username());
            holder.moment_time_voice.setText(mList.get(position).getMoment_time());
            holder.moment_content_voice.setText(mList.get(position).getURL());
            holder.moment_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Record record = new Record(mContext);
                    record.startPlay(mList.get(position).getURL());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 1 : mList.size();
    }
}
