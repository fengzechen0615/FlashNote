package com.example.wuke.flashnote.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Garbage;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Storage;
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.download_upload.ChangeColor;
import com.example.wuke.flashnote.download_upload.ChangeNote;
import com.example.wuke.flashnote.download_upload.Deleting;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.function.Wechat;
import com.example.wuke.flashnote.function.wechatShare;
import com.example.wuke.flashnote.login.LocalLogin;
import com.example.wuke.flashnote.record.Record;
import com.iflytek.cloud.SpeechRecognizer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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
    Voice deletevoice;
    private LocalLogin localLogin=new LocalLogin();

    private boolean edit_state = true;

    private DatabaseOperator databaseOperator;

    // 文本
    private static final int TYPE_TEXT = 0;
    // Record
    private static final int TYPE_RECORD = 1;

    private Drawable[] ColorImages;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        ColorImages = new Drawable[] {
                mContext.getResources().getDrawable(R.drawable.light_green),
                mContext.getResources().getDrawable(R.drawable.blue),
                mContext.getResources().getDrawable(R.drawable.red),
                mContext.getResources().getDrawable(R.drawable.orange),
                mContext.getResources().getDrawable(R.drawable.green),
        };

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
                        if (edit_state) {
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
                    }
                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("share", "click share");
                    }
                });

                //改变颜色的初始内容
//                holder.itemView.setBackgroundColor((mList.get(position)).getColor());
                if ((mList.get(position)).getColor()==0) {
                    holder.spinner.setSelection(0);
                    holder.color_text.setImageDrawable(ColorImages[0]);
                }
                else if (( mList.get(position)).getColor()==1) {
                    holder.spinner.setSelection(1);
                    holder.color_text.setImageDrawable(ColorImages[1]);
                }
                else if (( mList.get(position)).getColor()==2) {
                    holder.spinner.setSelection(2);
                    holder.color_text.setImageDrawable(ColorImages[2]);
                }
                else if (( mList.get(position)).getColor()==3) {
                    holder.spinner.setSelection(3);
                    holder.color_text.setImageDrawable(ColorImages[3]);
                }
                else if (( mList.get(position)).getColor()==4) {
                    holder.spinner.setSelection(4);
                    holder.color_text.setImageDrawable(ColorImages[4]);
                }

                // note颜色选择器
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int colorPosition, long id) {
                        String[] color = view.getResources().getStringArray(R.array.note_color);
                        ChangeColor cc = new ChangeColor();
                        databaseOperator = new DatabaseOperator(mContext);
                        if (color[colorPosition].equals("White")) {
                            holder.color_text.setImageDrawable(ColorImages[0]);
                            cc.getname("note",String.valueOf(((Note) mList.get(position)).getNoteID()),String.valueOf(0));
                            databaseOperator.UpdateNoteColor(((Note) mList.get(position)).getNoteID(), 0);
                            ((Note) mList.get(position)).setColor(0);
                        } else if (color[colorPosition].equals("Blue")) {
                            holder.color_text.setImageDrawable(ColorImages[1]);
                            cc.getname("note",String.valueOf(((Note) mList.get(position)).getNoteID()),String.valueOf(1));
                            databaseOperator.UpdateNoteColor(((Note) mList.get(position)).getNoteID(), 1);
                            ((Note) mList.get(position)).setColor(1);
                        } else if (color[colorPosition].equals("Red")) {
                            holder.color_text.setImageDrawable(ColorImages[2]);
                            cc.getname("note",String.valueOf(((Note) mList.get(position)).getNoteID()),String.valueOf(2));
                            databaseOperator.UpdateNoteColor(((Note) mList.get(position)).getNoteID(), 2);
                            ((Note) mList.get(position)).setColor(2);
                        } else if (color[colorPosition].equals("Orange")) {
                            holder.color_text.setImageDrawable(ColorImages[3]);
                            cc.getname("note",String.valueOf(((Note) mList.get(position)).getNoteID()),String.valueOf(3));
                            databaseOperator.UpdateNoteColor(((Note) mList.get(position)).getNoteID(), 3);
                            ((Note) mList.get(position)).setColor(3);
                        } else if (color[colorPosition].equals("Green")) {
                            holder.color_text.setImageDrawable(ColorImages[4]);
                            cc.getname("note",String.valueOf(((Note) mList.get(position)).getNoteID()),String.valueOf(4));
                            databaseOperator.UpdateNoteColor(((Note) mList.get(position)).getNoteID(), 4);
                            ((Note) mList.get(position)).setColor(4);
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

                    boolean edit_down = true;
                    @Override
                    public void onClick(View v) {
                        if (edit_down == true) {
                            holder.edit.setBackgroundResource(R.drawable.done);
                            holder.note_content.setFocusable(true);
                            holder.note_content.setFocusableInTouchMode(true);
                            holder.note_content.setCursorVisible(true);
                            holder.note_content.setSelection(((Note) mList.get(position)).getWords().length());
                            holder.note_content.setSingleLine(false);
                            holder.delete_text.setEnabled(false);
                            edit_down = false;
                            edit_state = edit_down;
                        } else if (edit_down == false) {
                            databaseOperator = new DatabaseOperator(mContext);
                            ChangeNote editor=new ChangeNote();
                            databaseOperator.EditWord(((Note) mList.get(position)).getNoteID(), holder.note_content.getText().toString());
                            ((Note) mList.get(position)).setWords(holder.note_content.getText().toString());
                            editor.getname(String.valueOf(((Note) mList.get(position)).getNoteID()),holder.note_content.getText().toString());
                            holder.edit.setBackgroundResource(R.drawable.edit);
                            holder.note_content.setFocusable(false);
                            holder.note_content.setFocusableInTouchMode(false);
                            holder.note_content.isCursorVisible();
                            holder.note_content.setCursorVisible(false);
                            holder.delete_text.setEnabled(true);
                            edit_down = true;
                            edit_state = edit_down;
                        }
                    }
                });

                holder.delete_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(viewHolder.getAdapterPosition());
                    }
                });

                LocalLogin localLogin = new LocalLogin();

                if(localLogin.check() == true) {
                    holder.share.setVisibility(View.VISIBLE);
                } else {
                    holder.share.setVisibility(View.GONE);
                    holder.share.setVisibility(View.INVISIBLE);
                }


                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("share","note on click");
                        LocalLogin l=new LocalLogin();
                        if (l.check()==true) {
                            Uploading uploading = new Uploading();
                            uploading.upsharednote(String.valueOf(((Note) mList.get(position)).getUserID()), String.valueOf(((Note) mList.get(position)).getNoteID()));
                            Log.e("share", ((Note) mList.get(position)).getUserID() + " " + ((Note) mList.get(position)).getNoteID());
//                            Toast.makeText(mContext, "share" + ((Note) mList.get(position)).getWords()+((Note) mList.get(position)).getNoteID(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
//                            Toast.makeText(mContext, "click without login" + position, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.wechat_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wechatShare wechatShare = new wechatShare();
                        wechatShare.shareInwechat(mContext, Wechat.wordstoShare(((Note)mList.get(position)).getWords()));
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
                holder.record_content.setText(mContext.getString(R.string.length) + " " + ((Voice) mList.get(position)).getDuration() + "s");
                holder.record_time.setText(((Voice) mList.get(position)).getTimestamp());

                holder.play_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record = new Record(mContext);
                        record.startPlay(((Voice) mList.get(position)).getURL());
                        Toast.makeText(mContext, mContext.getString(R.string.play_record), Toast.LENGTH_SHORT).show();
                    }
                });

                //改变颜色的初始内容
                if ((mList.get(position)).getColor()==0) {
                    holder.spinner.setSelection(0);
                    holder.color_voice.setImageDrawable(ColorImages[0]);
                }
                else if (( mList.get(position)).getColor()==1) {
                    holder.spinner.setSelection(1);
                    holder.color_voice.setImageDrawable(ColorImages[1]);
                }
                else if (( mList.get(position)).getColor()==2) {
                    holder.spinner.setSelection(2);
                    holder.color_voice.setImageDrawable(ColorImages[2]);
                }
                else if (( mList.get(position)).getColor()==3) {
                    holder.spinner.setSelection(3);
                    holder.color_voice.setImageDrawable(ColorImages[3]);
                }
                else if (( mList.get(position)).getColor()==4) {
                    holder.spinner.setSelection(4);
                    holder.color_voice.setImageDrawable(ColorImages[4]);
                }

                // record颜色选择器
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int colorPosition, long id) {
                        String[] color = view.getResources().getStringArray(R.array.note_color);
                        ChangeColor cc=new ChangeColor();
                        databaseOperator = new DatabaseOperator(mContext);
                        if (color[colorPosition].equals("White")) {
                            holder.color_voice.setImageDrawable(ColorImages[0]);
                            cc.getname("voice",String.valueOf(((Voice) mList.get(position)).getVoiceID()),"0");
                            databaseOperator.UpdateVoiceColor(((Voice) mList.get(position)).getVoiceID(), 0);
                            ((Voice) mList.get(position)).setColor(0);;
                        } else if (color[colorPosition].equals("Blue")) {
                            holder.color_voice.setImageDrawable(ColorImages[1]);
                            cc.getname("voice",String.valueOf(((Voice) mList.get(position)).getVoiceID()),"1");
                            databaseOperator.UpdateVoiceColor(((Voice) mList.get(position)).getVoiceID(), 1);
                            ((Voice) mList.get(position)).setColor(1);
                        } else if (color[colorPosition].equals("Red")) {
                            holder.color_voice.setImageDrawable(ColorImages[2]);
                            cc.getname("voice",String.valueOf(((Voice) mList.get(position)).getVoiceID()),"2");
                            databaseOperator.UpdateVoiceColor(((Voice) mList.get(position)).getVoiceID(), 2);
                            ((Voice) mList.get(position)).setColor(2);
                        } else if (color[colorPosition].equals("Orange")) {
                            holder.color_voice.setImageDrawable(ColorImages[3]);
                            cc.getname("voice",String.valueOf(((Voice) mList.get(position)).getVoiceID()),"3");
                            databaseOperator.UpdateVoiceColor(((Voice) mList.get(position)).getVoiceID(), 3);
                            ((Voice) mList.get(position)).setColor(3);
                        } else if (color[colorPosition].equals("Green")) {
                            holder.color_voice.setImageDrawable(ColorImages[4]);
                            cc.getname("voice",String.valueOf(((Voice) mList.get(position)).getVoiceID()),"4");
                            databaseOperator.UpdateVoiceColor(((Voice) mList.get(position)).getVoiceID(), 4);
                            ((Voice) mList.get(position)).setColor(4);
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
                            Log.e("click",mList.get(position).getPriority()+"");
                            Record record = new Record(mContext);
                            record.destory();
                        } else {
                            flag = true;
                            holder.function.setVisibility(View.INVISIBLE);
                            holder.function.setVisibility(View.GONE);
                            Record record = new Record(mContext);
                            record.destory();
                        }
                    }
                });

                holder.record_text.setVisibility(View.INVISIBLE);
                holder.record_text.setVisibility(View.GONE);

                // 语音转文字
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

                        Record record = new Record(mContext);
                        record.Convert(mContext, holder.record_text, fileName);
                    }
                });

                holder.delete_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(viewHolder.getAdapterPosition());
                    }
                });

                LocalLogin localLogin = new LocalLogin();

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

    private void delete(int position) {
        DatabaseOperator databaseOperator = new DatabaseOperator(mContext);

        if (mList.get(position) instanceof Note) {
            Note note = (Note)mList.get(position);
            databaseOperator.deleteNote(((Note) mList.get(position)).getNoteID());
            Garbage garbage = new Garbage(note.getNoteID(),note.getDataType(),note.getUserID(),note.getWords(),
                    note.getTimestamp(), note.getColor(), note.getPriority(), -1);
            databaseOperator.addGarbage(garbage);
            deleteNote = (Note) mList.remove(position); //移除数据
            if (localLogin.check()==true && deleteNote.getUserID()!=0){//判定登陆状态
                Deleting deleting=new Deleting();
                deleting.denote(String.valueOf(deleteNote.getUserID()),String.valueOf(deleteNote.getNoteID()));
            }
            notifyItemRemoved(position);
            notifyDataSetChanged();
            // remove后更改priority
            for (int i = position; i < mList.size(); i++) {
                if (mList.get(i) instanceof Note) {
                    int id = ((Note) mList.get(i)).getNoteID();
                    ((Note) mList.get(i)).setPriority(i);
                    databaseOperator.UpdateNotePriority(id, i);
//                    notifyItemRemoved(i);
                }
                else if (mList.get(i) instanceof Voice) {
                    int id = ((Voice) mList.get(i)).getVoiceID();
                    ((Voice) mList.get(i)).setPriority(i);
                    databaseOperator.UpdateVoicePriority(id, i);
//                    notifyItemRemoved(i);
                }
            }
        }

        else if (mList.get(position) instanceof Voice) {
            //File f = new File(((Voice) mList.get(position)).getURL());
            //f.delete();
            Voice voice = (Voice)mList.get(position);
            databaseOperator.deleteVoice(((Voice)mList.get(position)).getVoiceID());
            Garbage garbage = new Garbage(voice.getVoiceID(), voice.getDataType(), voice.getUserID(),voice.getURL(),
                    voice.getTimestamp(), voice.getColor(), voice.getPriority(), voice.getDuration());
            databaseOperator.addGarbage(garbage);
            deletevoice = (Voice)mList.remove(position); //移除数据
            if (localLogin.check()==true && deletevoice.getUserID()!=0){//判定登陆状态及内容讯息
                Deleting deleting=new Deleting();
                deleting.devoice(String.valueOf(deletevoice.getUserID()),String.valueOf(deletevoice.getVoiceID()));
            }
            notifyItemRemoved(position);
            notifyDataSetChanged();
            for (int i = position; i < mList.size(); i++) {
                if (mList.get(i) instanceof Note) {
                    int id = ((Note) mList.get(i)).getNoteID();
                    ((Note) mList.get(i)).setPriority(i);
                    databaseOperator.UpdateNotePriority(id, i);
//                    notifyItemRemoved(i);
                }
                else if (mList.get(i) instanceof Voice) {
                    int id = ((Voice) mList.get(i)).getVoiceID();
                    ((Voice) mList.get(i)).setPriority(i);
                    databaseOperator.UpdateVoicePriority(id, i);
//                    notifyItemRemoved(i);
                }
            }
        }
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
//        DatabaseOperator databaseOperator = new DatabaseOperator(mContext);
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mList.size() && toPosition < mList.size()) {
            //交换数据位置
            Collections.swap(mList, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
            Log.d("fromPosition", String.valueOf(fromPosition));
            Log.d("toPosition", String.valueOf(toPosition));
        }
        //移动过程中移除view的放大效果
        onItemClear(source);

    }

    @Override
    public void onItemDissmiss(int position_swipe) {

    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setScaleX(1.2f);
        viewHolder.itemView.setScaleY(1.2f);
        Log.d("Position_hhh", String.valueOf(viewHolder.getAdapterPosition()));
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }

    @Override
    public void onUpdate() {
//        Log.d("target", target.getAdapterPosition()+"");//
        DatabaseOperator databaseOperator = new DatabaseOperator(mContext);
        for(int i = 0; i < mList.size(); i++) {
            if (mList.get(i) instanceof Note) {
                int id = ((Note) mList.get(i)).getNoteID();
                ((Note) mList.get(i)).setPriority(i);
                databaseOperator.UpdateNotePriority(id, i);
                notifyDataSetChanged();
            }
            else if (mList.get(i) instanceof Voice) {
                int id = ((Voice) mList.get(i)).getVoiceID();
                ((Voice) mList.get(i)).setPriority(i);
                databaseOperator.UpdateVoicePriority(id, i);
                notifyDataSetChanged();
            }
        }

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