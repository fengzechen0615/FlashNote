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

import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class TestAdapter extends BaseAdapter{
    private Context mContext;
    private List<Note> mList;
    private int mItemLayoutId;

    public TestAdapter(Context mContext, List<Note> mDatas, int mItemLayoutid) {
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
        ViewHolder viewholder = null;
        if(convertView == null) {
            //73开
            viewholder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
            viewholder.mItemContent1 = (TextView) convertView.findViewById(R.id.userID);
            viewholder.mItemContent2 = (TextView) convertView.findViewById(R.id.content);
            viewholder.mItemContent3 = (TextView) convertView.findViewById(R.id.timestamp);
        }

        viewholder.mItemContent1.setText(String.valueOf(mList.get(position).getUserID()));
        viewholder.mItemContent2.setText(mList.get(position).getWords());
        viewholder.mItemContent3.setText(mList.get(position).getTimestamp());
        return convertView;
    }

    class ViewHolder{
        private TextView mItemContent1;
        private TextView mItemContent2;
        private TextView mItemContent3;
    }


}
