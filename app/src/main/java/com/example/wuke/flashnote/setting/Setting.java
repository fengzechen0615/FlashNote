package com.example.wuke.flashnote.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class Setting extends AppCompatActivity{

    private TextView record;
    private TextView command;
    private TextView cloud;
    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        record = (TextView) findViewById(R.id.record_setting_button);
        command = (TextView) findViewById(R.id.command_button);
        cloud = (TextView) findViewById(R.id.cloud_button);
        about = (TextView) findViewById(R.id.about_button);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setting = new Intent(getBaseContext(), RecordSetting.class);
                startActivity(setting);
            }
        });

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cloud = new Intent(getBaseContext(), WukeCloud.class);
                startActivity(cloud);
            }
        });
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.record_setting_button:
//                Intent setting = new Intent(getBaseContext(), RecordSetting.class);
//                startActivity(setting);
//                break;
//            case R.id.command_button:
////                Intent command = new Intent(getBaseContext(), RecordSetting.class);
////                startActivity(command);
//                break;
//            case R.id.cloud_button:
//                Intent cloud = new Intent(getBaseContext(), WukeCloud.class);
//                startActivity(cloud);
//                break;
//            case R.id.about_button:
////                Intent about = new Intent(getBaseContext(), RecordSetting.class);
////                startActivity(about);
//                break;
//            default:
//                break;
//        }
//    }
}
