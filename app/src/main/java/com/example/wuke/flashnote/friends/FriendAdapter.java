package com.example.wuke.flashnote.friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Friends;
import com.example.wuke.flashnote.login.LocalLogin;
import com.example.wuke.flashnote.recyclerview.ItemTouchHelperAdapter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by FrancisFeng on 2018/5/1.
 */

public class FriendAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapterFriend {

    private List<String> mList;
    private Context mContext;
    private SharedPreferences pref;
    public  String userid;

    public FriendAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_item, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final FriendViewHolder holder = (FriendViewHolder) viewHolder;
        // 加载用户名字
        holder.Friend_id.setText(mList.get(position)+"");
    }

    @Override
    public int getItemCount() {
        return null == mList ? 1 : mList.size();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mList.size() && toPosition < mList.size()) {
            //交换数据位置
            Collections.swap(mList, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);

    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        // 删除数据库
        DeFriend deFriend=new DeFriend();
        deFriend.delete(userid,mList.get(source.getAdapterPosition()));
        mList.remove(source.getAdapterPosition());

        // 刷新数据
        notifyItemRemoved(source.getAdapterPosition());
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setScaleX(1.2f);
        viewHolder.itemView.setScaleY(1.2f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }
}
