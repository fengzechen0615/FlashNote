package com.example.wuke.flashnote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class NoteAdapter extends BaseAdapter{
    private Context mContext;
    private List<Note> mList;
    private int mItemLayoutId;

    public NoteAdapter(Context mContext, List<Note> mDatas, int mItemLayoutid) {
        super();
        this.mContext = mContext;
        this.mList = mDatas;
        this.mItemLayoutId = mItemLayoutid;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mItemContent1 = (TextView) view.findViewById(R.id.userID);
            viewHolder.mItemContent2 = (TextView) view.findViewById(R.id.content);
            viewHolder.mItemContent3 = (TextView) view.findViewById(R.id.timestamp);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mItemContent1.setText(String.valueOf(mList.get(position).getUserID()));
        viewHolder.mItemContent2.setText(mList.get(position).getWords());
        viewHolder.mItemContent3.setText(mList.get(position).getTimestamp());

        return view;
    }

    class ViewHolder{
        TextView mItemContent1;
        TextView mItemContent2;
        TextView mItemContent3;
    }



}
