package com.example.wuke.flashnote.moment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class MomentVoiceViewHolder extends RecyclerView.ViewHolder {

    public TextView moment_username_voice;
    public TextView moment_content_voice;
    public TextView moment_time_voice;
    public ImageView moment_play;

    public MomentVoiceViewHolder(View itemView) {
        super(itemView);
        moment_username_voice = (TextView) itemView.findViewById(R.id.moment_user_name_voice);
        moment_content_voice = (TextView) itemView.findViewById(R.id.moment_voice);
        moment_time_voice = (TextView) itemView.findViewById(R.id.moment_time_voice);
        moment_play = (ImageView) itemView.findViewById(R.id.moment_play);
    }
}
