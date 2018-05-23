package com.example.wuke.flashnote.setting;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.login.LocalLogin;
import com.example.wuke.flashnote.login.Login;
import com.example.wuke.flashnote.login.LoginStatus;

public class WukeCloud extends AppCompatActivity {

    private TextView done;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wuke_cloud);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);

        LocalLogin localLogin = new LocalLogin();
        String user[] = localLogin.getaccount();
        if (user != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.id_content, new LoginStatus());
            fragmentTransaction.commit();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.id_content, new Login());
            fragmentTransaction.commit();
        }

        done = (TextView) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                NoteActivity.mNoteActivity.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Setting.class);
                startActivity(intent);
                finish();
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
