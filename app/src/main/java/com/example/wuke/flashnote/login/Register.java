package com.example.wuke.flashnote.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wuke.flashnote.R;

public class Register extends Fragment implements View.OnClickListener {

    private EditText account;
    private EditText password;
    private EditText email;
    private Button sign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.register, container, false);

        account = (EditText) view.findViewById(R.id.register_username);
        password = (EditText) view.findViewById(R.id.register_password);
        email = (EditText) view.findViewById(R.id.register_email);

        sign = (Button) view.findViewById(R.id.register_done);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup s = new Signup();
                String username = account.getText().toString();
                String pass = password.getText().toString();
                String e_mail = email.getText().toString();
                Log.w("1",username);
//                s.signup(username, pass, e_mail);
                Login login = new Login();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.replace(R.id.id_content, login, "Login");
                tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_done:
                Signup s = new Signup();
                String username = account.getText().toString();
                String pass = password.getText().toString();
                String e_mail = email.getText().toString();
                Log.w("1",username);
                s.signup(username, pass, e_mail);

        }
    }
}
