package com.example.wuke.flashnote.trash;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class TrashVoiceViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout trash_layout;
    public TextView trash_record_content;
    public TextView trash_record_time;
    public ImageButton trash_back_voice;
    public LinearLayout trash_record_function;
    public ImageView trash_color_voice;
    public ImageView trash_play_record;
    public ImageButton trash_delete_record;

    public TrashVoiceViewHolder (View itemView) {
        super(itemView);
        trash_layout = (LinearLayout) itemView.findViewById(R.id.trash_recycle_record);
        trash_record_content = (TextView) itemView.findViewById(R.id.trash_record_content);
        trash_record_time = (TextView) itemView.findViewById(R.id.trash_record_time);
        trash_record_function = (LinearLayout) itemView.findViewById(R.id.trash_record_function);
        trash_back_voice = (ImageButton) itemView.findViewById(R.id.trash_back_voice);
        trash_color_voice = (ImageView) itemView.findViewById(R.id.trash_color_voice);
        trash_play_record = (ImageView) itemView.findViewById(R.id.trash_play);
        trash_delete_record = (ImageButton) itemView.findViewById(R.id.trash_delete_record);
    }
}
