package com.example.wuke.flashnote.recyclerview;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layout;
    public EditText record_content;
    public TextView record_time;
    public ImageButton convert;
    public Spinner spinner;
    public LinearLayout function;
    public TextView record_text;
    public ImageView color_voice;
    public ImageView play_record;
    public ImageButton delete_record;
    public ImageButton record_share;

    public RecordViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView.findViewById(R.id.recycle_record);
        record_content = (EditText) itemView.findViewById(R.id.record_content);
        record_time = (TextView) itemView.findViewById(R.id.record_time);
        convert = (ImageButton) itemView.findViewById(R.id.convert);
        function = (LinearLayout) itemView.findViewById(R.id.record_function);
        spinner = (Spinner) itemView.findViewById(R.id.record_color);
        record_text = (TextView) itemView.findViewById(R.id.record_text);
        color_voice = (ImageView) itemView.findViewById(R.id.color_voice);
        play_record = (ImageView) itemView.findViewById(R.id.play);
        delete_record = (ImageButton) itemView.findViewById(R.id.delete_record);
        record_share = (ImageButton) itemView.findViewById(R.id.record_share);
    }
}
