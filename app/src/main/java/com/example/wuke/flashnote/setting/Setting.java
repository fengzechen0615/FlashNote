package com.example.wuke.flashnote.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class Setting extends AppCompatActivity{

    private LinearLayout record;
    private LinearLayout command;
    private LinearLayout cloud;
    private LinearLayout about;

    private TextView done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        record = (LinearLayout) findViewById(R.id.record_setting_button);
        command = (LinearLayout) findViewById(R.id.command_button);
        cloud = (LinearLayout) findViewById(R.id.cloud_button);
        about = (LinearLayout) findViewById(R.id.about_button);

        done = (TextView) findViewById(R.id.setting_done);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(getBaseContext(), RecordSetting.class);
                startActivity(setting);
            }
        });

        command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Command = new Intent(getBaseContext(), Command.class);
                startActivity(Command);
            }
        });

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cloud = new Intent(getBaseContext(), WukeCloud.class);
                startActivity(cloud);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent About = new Intent(getBaseContext(), About.class);
                startActivity(About);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
