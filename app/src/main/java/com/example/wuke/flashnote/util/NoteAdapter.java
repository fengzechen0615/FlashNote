package com.example.wuke.flashnote.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;

import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements View.OnClickListener {

    private List<Note> mList;

//    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayout;

        TextView note_content;

        TextView note_time;

//        Button button;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.recycle_note);
            note_content = (TextView) view.findViewById(R.id.note_content);
            note_time = (TextView) view.findViewById(R.id.note_time);

            View button = view.findViewById(R.id.delete);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.delete:
                    Log.d("position", String.valueOf(view.getVerticalScrollbarPosition()));
                    Log.d("note_id", String.valueOf(mList.get(view.getVerticalScrollbarPosition()).getNoteID()));
                    DatabaseOperator databaseOperator = new DatabaseOperator(view.getContext());
                    databaseOperator.deleteNote(mList.get(view.getVerticalScrollbarPosition()).getNoteID());
                    mList.remove(view.getVerticalScrollbarPosition());
                    notifyItemRemoved(view.getVerticalScrollbarPosition());
                    Log.d("Delete", "Delete");
                    break;
            }
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

        // 超过一行隐藏 点击展开全部
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

    @Override
    public void onClick(View view) {

    }

}