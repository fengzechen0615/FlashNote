package com.example.wuke.flashnote.trash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Garbage;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.record.Record;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TrashAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<Garbage> mList;
    private Drawable[] ColorImages;
    private DatabaseOperator dbo;
    private int userid;
    private SharedPreferences pref;

    // 文本
    private static final int TYPE_TEXT = 0;
    // Record
    private static final int TYPE_RECORD = 1;

    public TrashAdapter(Context mContext, List<Garbage> mDatas) {
        this.mContext = mContext;
        this.mList = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.trash_text_item, parent, false);
            TrashTextViewHolder viewHolder = new TrashTextViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_RECORD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.trash_voice_item, parent, false);
            TrashVoiceViewHolder viewHolder = new TrashVoiceViewHolder(view);
            return viewHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == mList.get(position).getDatatype()) {
            return TYPE_TEXT;
        } else if (1 == mList.get(position).getDatatype()) {
            return TYPE_RECORD;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {

        ColorImages = new Drawable[] {
                mContext.getResources().getDrawable(R.drawable.light_green),
                mContext.getResources().getDrawable(R.drawable.blue),
                mContext.getResources().getDrawable(R.drawable.red),
                mContext.getResources().getDrawable(R.drawable.orange),
                mContext.getResources().getDrawable(R.drawable.green),
        };


        if (viewholder instanceof TrashTextViewHolder) {
            final TrashTextViewHolder holder = (TrashTextViewHolder) viewholder;

            holder.trash_note_content.setText(mList.get(position).getKeywords());
            holder.trash_note_time.setText(mList.get(position).getPrevious_timestamp());

            holder.trash_function.setVisibility(View.INVISIBLE);
            holder.trash_function.setVisibility(View.GONE);

            holder.trash_note_content.setEllipsize(TextUtils.TruncateAt.END);
            holder.trash_note_content.setSingleLine(true);
            holder.trash_note_content.setOnClickListener(new View.OnClickListener() {

                Boolean flag = true;

                @Override
                public void onClick(View view) {
                    if (flag) {
                        flag = false;
                        holder.trash_note_content.setEllipsize(null);
                        holder.trash_note_content.setSingleLine(flag);
                        holder.trash_function.setVisibility(View.VISIBLE);

                    } else {
                        flag = true;
                        holder.trash_note_content.setEllipsize(TextUtils.TruncateAt.END);
                        holder.trash_note_content.setSingleLine(flag);
                        holder.trash_function.setVisibility(View.INVISIBLE);
                        holder.trash_function.setVisibility(View.GONE);
                    }
                }
            });

            //颜色
            if ((mList.get(position)).getPrevious_color() == 0) {
                holder.trash_color_text.setImageDrawable(ColorImages[0]);
            } else if ((mList.get(position)).getPrevious_color() == 1) {
                holder.trash_color_text.setImageDrawable(ColorImages[1]);
            } else if ((mList.get(position)).getPrevious_color() == 2) {
                holder.trash_color_text.setImageDrawable(ColorImages[2]);
            } else if ((mList.get(position)).getPrevious_color() == 3) {
                holder.trash_color_text.setImageDrawable(ColorImages[3]);
            } else if ((mList.get(position)).getPrevious_color() == 4) {
                holder.trash_color_text.setImageDrawable(ColorImages[4]);
            }

            holder.trash_delete_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Garbage g=mList.remove(position);
                    Log.e("g",g.getKeywords()+"");
                    Log.e("g",g.getLitter_id()+"");
                    dbo=new DatabaseOperator(mContext);
                    dbo.deleteGarbage(g.getLitter_id());
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });

            holder.text_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Garbage g = mList.get(position);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = form.format(timestamp);
                    Note note = new Note(g.getContent_id(),g.getGuser_id(),g.getKeywords(),g.getPrevious_color(),time
                    , g.getPrevious_priority(),g.getDatatype());
                    dbo=new DatabaseOperator(mContext);
                    dbo.RevertNote(note);
                    if (g.getGuser_id() != 0) {
                        Uploading uploading=new Uploading();
                        ArrayList<Note> al=new ArrayList<>();
                        al.add(note);
                        uploading.uploadnote(al);
                        al.clear();
                    }
                    mList.remove(position);
                    dbo.deleteGarbage(g.getLitter_id());
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
        }

        if (viewholder instanceof TrashVoiceViewHolder) {
            final TrashVoiceViewHolder holder = (TrashVoiceViewHolder) viewholder;

            holder.trash_record_content.setText(mContext.getString(R.string.voice_trash));
            holder.trash_record_time.setText(mList.get(position).getPrevious_timestamp());

            holder.trash_record_function.setVisibility(View.INVISIBLE);
            holder.trash_record_function.setVisibility(View.GONE);

            holder.trash_record_content.setEllipsize(TextUtils.TruncateAt.END);
            holder.trash_record_content.setSingleLine(true);
            holder.trash_record_content.setOnClickListener(new View.OnClickListener() {
                Boolean flag = true;

                @Override
                public void onClick(View view) {
                    if (flag) {
                        flag = false;
                        holder.trash_record_content.setEllipsize(null);
                        holder.trash_record_content.setSingleLine(flag);
                        holder.trash_record_function.setVisibility(View.VISIBLE);

                    } else {
                        flag = true;
                        holder.trash_record_content.setEllipsize(TextUtils.TruncateAt.END);
                        holder.trash_record_content.setSingleLine(flag);
                        holder.trash_record_function.setVisibility(View.INVISIBLE);
                        holder.trash_record_function.setVisibility(View.GONE);
                    }
                }
            });

            //颜色
            if ((mList.get(position)).getPrevious_color() == 0) {
                holder.trash_color_voice.setImageDrawable(ColorImages[0]);
            } else if ((mList.get(position)).getPrevious_color() == 1) {
                holder.trash_color_voice.setImageDrawable(ColorImages[1]);
            } else if ((mList.get(position)).getPrevious_color() == 2) {
                holder.trash_color_voice.setImageDrawable(ColorImages[2]);
            } else if ((mList.get(position)).getPrevious_color() == 3) {
                holder.trash_color_voice.setImageDrawable(ColorImages[3]);
            } else if ((mList.get(position)).getPrevious_color() == 4) {
                holder.trash_color_voice.setImageDrawable(ColorImages[4]);
            }

            holder.trash_delete_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Garbage g=mList.remove(position);
                    Log.e("g",g.getKeywords()+"");
                    Log.e("g",g.getLitter_id()+"");
                    File f = new File(g.getKeywords());
                    f.delete();
                    dbo=new DatabaseOperator(mContext);
                    dbo.deleteGarbage(g.getLitter_id());
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });

            holder.trash_back_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Garbage g = mList.get(position);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = form.format(timestamp);
                    Voice voice = new Voice(g.getContent_id(),g.getGuser_id(),g.getKeywords(),g.getPrevious_color(),time
                            , g.getPrevious_priority(),g.getDatatype(),g.getExtra());
                    dbo=new DatabaseOperator(mContext);
                    dbo.RevertVoice(voice);//还原
                    if (g.getGuser_id() != 0) {
                        Uploading uploading=new Uploading();
                        ArrayList<Voice> al=new ArrayList<>();
                        al.add(voice);
                        uploading.uploadvoice(al);
                        al.clear();
                    }
                    mList.remove(position);
                    dbo.deleteGarbage(g.getLitter_id());

                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });

            holder.trash_play_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Record record = new Record(mContext);
                    record.startPlay(mList.get(position).getKeywords());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == mList ? 1 : mList.size();
    }
}
