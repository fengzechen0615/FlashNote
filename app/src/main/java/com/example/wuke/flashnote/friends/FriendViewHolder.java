package com.example.wuke.flashnote.friends;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class FriendViewHolder extends RecyclerView.ViewHolder{

    public TextView Friend_id;

    public FriendViewHolder(View itemView) {
        super(itemView);
        Friend_id = (TextView) itemView.findViewById(R.id.friend_id);
    }
}
