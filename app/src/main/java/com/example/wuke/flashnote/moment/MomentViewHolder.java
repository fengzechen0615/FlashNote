package com.example.wuke.flashnote.moment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class MomentViewHolder extends RecyclerView.ViewHolder{

    public TextView moment_username;
    public TextView moment_content;
    public TextView moment_time;

    public MomentViewHolder(View itemView) {
        super(itemView);
        moment_username = (TextView) itemView.findViewById(R.id.moment_user_name);
        moment_content = (TextView) itemView.findViewById(R.id.moment_content);
        moment_time = (TextView) itemView.findViewById(R.id.moment_time);
    }
}
