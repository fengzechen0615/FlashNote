package com.example.wuke.flashnote.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

public class LoginStatus extends Fragment{

    private TextView username;
    private Button logout;
    private String name;
    static private boolean Situation;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_status, container, false);

        username = (TextView) view.findViewById(R.id.usename_login);
        final LocalLogin localLogin = new LocalLogin();
        String user[] = localLogin.getaccount();
        if (!localLogin.check()) {
            username.setText(getArguments().getString("name"));
            Situation=false;
        } else {
            username.setText(user[0]);
            Situation=true;
        }

        logout = (Button) view.findViewById(R.id.sign_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.replace(R.id.id_content, new Login());
                transaction.addToBackStack(null);
                transaction.commit();
                Situation=false;
                localLogin.delete();
            }
        });

        return view;
    }

    public static  LoginStatus newInstance(String text){
        LoginStatus loginStatus = new LoginStatus();
        Bundle bundle = new Bundle();
        bundle.putString("name", text);
        //fragment保存参数，传入一个Bundle对象
        loginStatus.setArguments(bundle);
        return loginStatus;
    }
}
