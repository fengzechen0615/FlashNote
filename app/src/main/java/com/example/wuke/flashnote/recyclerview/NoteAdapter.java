package com.example.wuke.flashnote.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Storage;
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.record.Record;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by recur on 2018/4/9.
 */

public class NoteAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter{

    private List<Storage> mList;
    private Context mContext;
    private List<Note> Delete_List = new ArrayList<>();
    Note deleteNote;

    // 文本
    private static final int TYPE_TEXT = 0;
    // Record
    private static final int TYPE_RECORD = 1;

    public NoteAdapter(Context mContext, List<Storage> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
            TextViewHolder viewHolder = new TextViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_RECORD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.record_item, parent, false);
            RecordViewHolder viewHolder = new RecordViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        // text的逻辑
        if (viewHolder instanceof TextViewHolder) {
            final TextViewHolder holder = (TextViewHolder) viewHolder;
            if (mList.get(position) instanceof Note) {
                holder.note_content.setText(((Note) mList.get(position)).getWords());
                holder.note_time.setText(((Note) mList.get(position)).getTimestamp());


                holder.function.setVisibility(View.INVISIBLE);
                holder.function.setVisibility(View.GONE);

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
                            holder.function.setVisibility(View.VISIBLE);
                        } else {
                            flag = true;
                            holder.note_content.setEllipsize(TextUtils.TruncateAt.END);
                            holder.note_content.setSingleLine(flag);
                            holder.function.setVisibility(View.INVISIBLE);
                            holder.function.setVisibility(View.GONE);
                        }
                    }
                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("share", "click share");
                        Toast.makeText(mContext, "click" + position, Toast.LENGTH_SHORT).show();
                    }
                });

                // note颜色选择器
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] color = view.getResources().getStringArray(R.array.note_color);
//                Toast.makeText(mContext, color[position], Toast.LENGTH_LONG).show();
                        if (color[position].equals("Red")) {
                            holder.itemView.setBackgroundColor(Color.RED);
                        } else if (color[position].equals("White")) {
                            holder.itemView.setBackgroundColor(Color.WHITE);
                        } else if (color[position].equals("Blue")) {
                            holder.itemView.setBackgroundColor(Color.BLUE);
                        } else if (color[position].equals("Orange")) {
                            holder.itemView.setBackgroundColor(Color.YELLOW);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // edit text 状态 是否可输入
                // 需要在后期解决 光标跳转小键盘出现等问题
                holder.note_content.setFocusable(false);
                holder.note_content.setFocusableInTouchMode(false);
                holder.note_content.setCursorVisible(false);
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.edit.getText().toString().equals("Edit")) {
                            holder.edit.setText("Done");
                            holder.note_content.setFocusable(true);
                            holder.note_content.setFocusableInTouchMode(true);
                            holder.note_content.setCursorVisible(true);
//                    holder.note_content.setSelection(mList.get(position).getWords().length());
                        } else if (holder.edit.getText().toString().equals("Done")) {
                            holder.edit.setText("Edit");
                            holder.note_content.setFocusable(false);
                            holder.note_content.setFocusableInTouchMode(false);
                            holder.note_content.isCursorVisible();
                            holder.note_content.setCursorVisible(false);
                        }
                    }
                });
            }
        }

        if (viewHolder instanceof RecordViewHolder) {
            final RecordViewHolder holder = (RecordViewHolder) viewHolder;
            if (mList.get(position) instanceof Voice) {
                holder.record_content.setFocusable(false);
                holder.record_content.setFocusableInTouchMode(false);
                holder.record_content.setCursorVisible(false);
                holder.record_content.setText("录音地址" + ((Voice) mList.get(position)).getURL());
                holder.record_time.setText(((Voice) mList.get(position)).getTimestamp());
                holder.record_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record = new Record();
                        record.startPlay(((Voice) mList.get(position)).getURL());
                        Log.d("time_2", mList.get(position).getTimestamp());
                        Log.d("URL", ((Voice) mList.get(position)).getURL());
                        Toast.makeText(mContext, "play", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof Voice) {
            Log.d("data", String.valueOf(mList.get(position).getDataType()));
            if (0 == mList.get(position).getDataType()) {
                return TYPE_TEXT;
            } else if (1 == mList.get(position).getDataType()) {
                return TYPE_RECORD;
            } else {
                return 0;
            }
        } else if (mList.get(position) instanceof Note) {
            if (0 == mList.get(position).getDataType()) {
                return TYPE_TEXT;
            } else if (1 == mList.get(position).getDataType()) {
                return TYPE_RECORD;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
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
        onItemClear(source);
        int position = source.getAdapterPosition();

        DatabaseOperator databaseOperator = new DatabaseOperator(mContext);

        if (mList.get(position) instanceof Note) {
            databaseOperator.deleteNote(((Note) mList.get(position)).getNoteID());
//        Log.d("position", String.valueOf(position));
//        Log.d("Note_id", String.valueOf(mList.get(position).getNoteID()));
//        Log.d("Note_words", String.valueOf(mList.get(position).getWords()));

            deleteNote = (Note) mList.remove(position); //移除数据
            Delete_List.add(deleteNote);

            try {
                SaveObjectTool.writeObject(mList, "dataset");
            } catch (IOException e) {
                e.printStackTrace();
            }

            notifyItemRemoved(position);//刷新数据移除
        }
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