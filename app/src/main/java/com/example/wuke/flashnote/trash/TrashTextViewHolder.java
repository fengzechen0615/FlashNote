package com.example.wuke.flashnote.trash;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class TrashTextViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout trash_layout;
    public EditText trash_note_content;
    public TextView trash_note_time;
    public ImageButton text_back;
    public LinearLayout trash_function;
    public ImageView trash_color_image;
    public ImageButton trash_delete_text;

    public TrashTextViewHolder(View itemView) {
        super(itemView);
        trash_layout = (LinearLayout) itemView.findViewById(R.id.trash_recycle_note);
        trash_note_content = (EditText) itemView.findViewById(R.id.trash_note_content);
        trash_note_time = (TextView) itemView.findViewById(R.id.note_time);
        text_back = (ImageButton) itemView.findViewById(R.id.trash_back_text);
        trash_function= (LinearLayout) itemView.findViewById(R.id.trash_function);
        trash_color_image = (ImageView) itemView.findViewById(R.id.trash_color_note);
        trash_delete_text = (ImageButton) itemView.findViewById(R.id.trash_delete_text);
    }
}
