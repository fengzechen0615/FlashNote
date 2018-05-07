package com.example.wuke.flashnote.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layout;
    public EditText record_content;
    public TextView record_time;

    public RecordViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView.findViewById(R.id.recycle_record);
        record_content = (EditText) itemView.findViewById(R.id.record_content);
        record_time = (TextView) itemView.findViewById(R.id.record_time);
    }
}
