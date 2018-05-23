package com.example.wuke.flashnote.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;

public class Command extends AppCompatActivity {

    private TextView done;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);

        done = (TextView) findViewById(R.id.command_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Setting.Setting.finish();
                finish();
            }
        });

        back = (TextView) findViewById(R.id.command_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                NoteActivity.mNoteActivity.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Setting.Setting.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
