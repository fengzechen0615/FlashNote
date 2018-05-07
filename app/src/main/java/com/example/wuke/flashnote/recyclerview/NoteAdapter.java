package com.example.wuke.flashnote.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Storage;
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.record.Record;

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

    private DatabaseOperator databaseOperator;

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
        // TEXT的逻辑
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
                        Uploading uploading=new Uploading();
                        if (mList.get(position) instanceof Note){
                            uploading.upsharednote(String.valueOf(((Note) mList.get(position)).getUserID()),String.valueOf(((Note) mList.get(position)).getNoteID()));
                        }
                        else {
                            uploading.upsharevoice(String.valueOf(((Voice) mList.get(position)).getUserID()),String.valueOf(((Voice) mList.get(position)).getVoiceID()));
                        }

                        Toast.makeText(mContext, "click" + position, Toast.LENGTH_SHORT).show();
                    }
                });

                // note颜色选择器
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] color = view.getResources().getStringArray(R.array.note_color);
                        databaseOperator = new DatabaseOperator(mContext);
//                Toast.makeText(mContext, color[position], Toast.LENGTH_LONG).show();
                        if (color[position].equals("Red")) {
                            holder.itemView.setBackgroundColor(Color.RED);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.RED);
//                            ((Note) mList.get(position)).setColor(Color.RED);
//                            update_file();
                        } else if (color[position].equals("White")) {
                            holder.itemView.setBackgroundColor(Color.WHITE);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.WHITE);
                        } else if (color[position].equals("Blue")) {
                            holder.itemView.setBackgroundColor(Color.BLUE);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.BLUE);
                        } else if (color[position].equals("Orange")) {
                            holder.itemView.setBackgroundColor(Color.YELLOW);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.YELLOW);
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
                        } else if (holder.edit.getText().toString().equals("Done")) {
//                            Log.d("id and word", ((Note) mList.get(position)).getNoteID() + holder.note_content.getText().toString());
                            databaseOperator = new DatabaseOperator(mContext);
                            databaseOperator.EditWord(((Note) mList.get(position)).getNoteID(), holder.note_content.getText().toString());
                            ((Note) mList.get(position)).setWords(holder.note_content.getText().toString());
                            update_file();
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

        // RECORD的逻辑
        if (viewHolder instanceof RecordViewHolder) {
            final RecordViewHolder holder = (RecordViewHolder) viewHolder;
            if (mList.get(position) instanceof Voice) {
                holder.function.setVisibility(View.INVISIBLE);
                holder.function.setVisibility(View.GONE);

                holder.record_content.setFocusable(false);
                holder.record_content.setFocusableInTouchMode(false);
                holder.record_content.setCursorVisible(false);
                holder.record_content.setText("Record Time:");
                holder.record_time.setText(((Voice) mList.get(position)).getTimestamp());

                holder.record_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record = new Record();
                        record.startPlay(((Voice) mList.get(position)).getURL());
                        Toast.makeText(mContext, "play record", Toast.LENGTH_SHORT).show();
                    }
                });

                // record颜色选择器
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] color = view.getResources().getStringArray(R.array.note_color);
                        databaseOperator = new DatabaseOperator(mContext);
//                Toast.makeText(mContext, color[position], Toast.LENGTH_LONG).show();
                        if (color[position].equals("Red")) {
                            holder.itemView.setBackgroundColor(Color.RED);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.RED);
//                            ((Note) mList.get(position)).setColor(Color.RED);
//                            update_file();
                        } else if (color[position].equals("White")) {
                            holder.itemView.setBackgroundColor(Color.WHITE);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.WHITE);
                        } else if (color[position].equals("Blue")) {
                            holder.itemView.setBackgroundColor(Color.BLUE);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.BLUE);
                        } else if (color[position].equals("Orange")) {
                            holder.itemView.setBackgroundColor(Color.YELLOW);
//                            databaseOperator.EditColor(((Note) mList.get(position)).getNoteID(), Color.YELLOW);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                holder.record_content.setOnClickListener(new View.OnClickListener() {

                    Boolean flag = true;

                    @Override
                    public void onClick(View view) {
                        if (flag) {
                            flag = false;
                            holder.function.setVisibility(View.VISIBLE);
                        } else {
                            flag = true;
                            holder.function.setVisibility(View.INVISIBLE);
                            holder.function.setVisibility(View.GONE);
                        }
                    }
                });

                holder.record_text.setVisibility(View.INVISIBLE);
                holder.record_text.setVisibility(View.GONE);

                holder.convert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = null;

                        String pathName = ((Voice) mList.get(position)).getURL();
                        int start = pathName.lastIndexOf("/");
                        int end = pathName.lastIndexOf(".");
                        if(start != -1 && end !=- 1){
                            fileName = pathName.substring(start + 1,end);
                        }
                        holder.record_text.setVisibility(View.VISIBLE);
                        holder.record_text.setText(fileName);
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
            Log.d("fromPosition", String.valueOf(fromPosition));
            Log.d("toPosition", String.valueOf(toPosition));
            update_file();
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

            deleteNote = (Note) mList.remove(position); //移除数据
            Delete_List.add(deleteNote);
            update_file();
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

    public void update_file() {
        try {
            SaveObjectTool.writeObject(mList, "dataset");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}