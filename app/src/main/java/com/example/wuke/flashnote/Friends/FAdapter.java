package com.example.wuke.flashnote.Friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.Friends;

import java.util.List;

/**
 * Created by recur on 2018/5/1.
 */

public class FAdapter extends BaseAdapter {
    private List<Friends> list = null;
    private Context context = null;
    private LayoutInflater inflater = null;
    private Friends friends;
    public FAdapter(List<Friends> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.friend_view, null);
            viewHolder.Name=(TextView)convertView.findViewById(R.id.friend_name);
            convertView.setTag(viewHolder);
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        friends=list.get(i);
        viewHolder.Name.setText(friends.getFriendsID()+"");
        return convertView;
    }

    class ViewHolder {
        TextView Name;
    }
}
