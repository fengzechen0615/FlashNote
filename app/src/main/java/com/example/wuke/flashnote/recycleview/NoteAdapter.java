package com.example.wuke.flashnote.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter{

    private List<Note> mList;
    private Context mContext;
    private List<Note> Delete_List=new ArrayList<>();
    Note deleteNote;
    class ViewHolder extends RecyclerView.ViewHolder {

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

    public NoteAdapter(Context mContext, List<Note> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view, parent, false);
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
//        return mList.size();
        return null == mList ? 1 : mList.size();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mList.size() && toPosition < mList.size()) {
            //交换数据位置
            Collections.swap(mList, fromPosition, toPosition);
            try {
                SaveObjectTool.writeObject(mList,"dataset");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);

    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();

        DatabaseOperator databaseOperator = new DatabaseOperator(mContext);
        databaseOperator.deleteNote(mList.get(position).getNoteID());
        Log.d("position", String.valueOf(position));
        Log.d("Note_id", String.valueOf(mList.get(position).getNoteID()));
        Log.d("Note_words", String.valueOf(mList.get(position).getWords()));
        deleteNote=mList.remove(position); //移除数据
        Delete_List.add(deleteNote);

        try {
            SaveObjectTool.writeObject(mList,"dataset");
        } catch (IOException e) {
            e.printStackTrace();
        }

        notifyItemRemoved(position);//刷新数据移除
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

    public List<Note> getDelete_List()
    {
        return Delete_List;
    }

    public void ClearList()
    {
        Delete_List.clear();
    }
}