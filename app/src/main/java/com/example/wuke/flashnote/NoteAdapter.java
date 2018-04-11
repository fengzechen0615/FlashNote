package com.example.wuke.flashnote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;

        TextView note_content;

        TextView note_time;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.recycle_note);
            note_content = (TextView) view.findViewById(R.id.note_content);
            note_time = (TextView) view.findViewById(R.id.note_time);
        }
    }

    public NoteAdapter(List<Note> mDatas) {
        this.mList = mDatas;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        holder.note_content.setText(mList.get(position).getWords());
        holder.note_time.setText(mList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}