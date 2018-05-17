package com.example.wuke.flashnote.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.setting.Setting;

public class Register extends AppCompatActivity {

    private EditText account;
    private EditText password;
    private EditText email;
    private Button sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);

        sign = (Button) findViewById(R.id.register_done);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup s = new Signup();
                String username = account.getText().toString();
                String pass = password.getText().toString();
                String e_mail = email.getText().toString();
                Log.w("1",username);
                s.signup(username, pass, e_mail);
                finish();
            }
        });
    }
}
