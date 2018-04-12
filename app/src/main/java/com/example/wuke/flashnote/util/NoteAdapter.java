package com.example.wuke.flashnote.util;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.Note;

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
    public void onBindViewHolder(final NoteAdapter.ViewHolder holder, int position) {
        holder.note_content.setText(mList.get(position).getWords());
        holder.note_time.setText(mList.get(position).getTimestamp());

        holder.note_content.setEllipsize(TextUtils.TruncateAt.END);
        holder.note_content.setSingleLine(true);
        holder.note_content.setOnClickListener(new View.OnClickListener() {

            Boolean flag = true;

            @Override
            public void onClick(View view) {
                if (flag) {
                    flag = false;
                    holder.note_content.setEllipsize(null);
                    holder.note_content.setSingleLine(flag);
                }
                else {
                    flag = true;
                    holder.note_content.setEllipsize(TextUtils.TruncateAt.END);
                    holder.note_content.setSingleLine(flag);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}