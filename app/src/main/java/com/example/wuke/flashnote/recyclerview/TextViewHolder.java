package com.example.wuke.flashnote.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class TextViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layout;
    public EditText note_content;
    public TextView note_time;
    public ImageButton share;
    public View v_background;
    public Spinner spinner;
    public LinearLayout function;
    public ImageButton edit;
    public ImageView ColorImage;
    public ImageButton delete_text;

    public TextViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView.findViewById(R.id.recycle_note);
        note_content = (EditText) itemView.findViewById(R.id.note_content);
        note_time = (TextView) itemView.findViewById(R.id.note_time);
        share = (ImageButton) itemView.findViewById(R.id.share);
        v_background = (View) itemView.findViewById(R.id.v_background);
        spinner = (Spinner) itemView.findViewById(R.id.color);
        edit = (ImageButton) itemView.findViewById(R.id.edit);
        function= (LinearLayout) itemView.findViewById(R.id.function);
        ColorImage = (ImageView) itemView.findViewById(R.id.color_note);
        delete_text = (ImageButton) itemView.findViewById(R.id.delete_text);
    }
}
