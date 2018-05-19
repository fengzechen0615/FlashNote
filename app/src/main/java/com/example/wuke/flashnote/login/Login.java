package com.example.wuke.flashnote.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wuke.flashnote.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Login extends Fragment implements OnClickListener{

    private String url = "http://39.106.205.176:8080/artifacts/Servlet";
    private EditText account;
    private EditText password;
    private TextView result;
    private Button login;
    private TextView register;
    private TextView cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        account = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        result = (TextView) view.findViewById(R.id.result);

        LocalLogin localLogin = new LocalLogin();
        if (localLogin.check() == true) {
            String[] x = localLogin.getaccount();
            account.setText(x[0]);
            password.setText(x[1]);
            access(x[0], x[1]);
        }

        login = (Button) view.findViewById(R.id.login_button);
        login.setOnClickListener(this);

        register = (TextView) view.findViewById(R.id.sign_button);
        register.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_button:
                Register Register = new Register();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                tx.replace(R.id.id_content, Register);
                tx.addToBackStack(null);
                tx.commit();
                break;
            case R.id.login_button:
                if (account.getText().toString() != null && password.getText().toString() != null) {
                    String a = account.getText().toString();
                    String p = password.getText().toString();
                    access(a, p);
                }
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.replace(R.id.id_content, new LoginStatus().newInstance(account.getText().toString()));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                break;
        }
    }

    private void access(String a, String p) {
        String s = url + "?account=" + a + "&password=" + p ;
        new Login.MyAsyncTask(result).execute(s);
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private TextView tv;

        Log log;

        public MyAsyncTask(TextView v) {
            tv = v;
        }

        protected void onPreExecute() {
            log.w("1", "task onPreExecute()");
        }

        protected String doInBackground(String... params) {
            log.w("1", "task DoInBackStage()");

            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();

            try {
                System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(100000);
                connection.setReadTimeout(100000);

                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        protected void onPostExecute(String s) {
            if (s.contains("resCode=201")) {
                LocalLogin l = new LocalLogin();
                String a = account.getText().toString();
                String p = password.getText().toString();
                int id = s.indexOf("resCode=201 Login in succeeduserID=");
                String userid = s.substring(id+35);
                l.save(a, p);
                Log.e("post1", a + " " + p);
                Timestamp nowTime = new Timestamp(System.currentTimeMillis());//Login Time
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = form.format(nowTime);
//                SharedPreferences pref=getSharedPreferences("info",MODE_PRIVATE);
//                SharedPreferences.Editor editor=pref.edit();
//                editor.putString("username",a);
//                editor.putInt("userid",Integer.parseInt(userid));

//                Intent intent = new Intent(Login.this, RecordSetting.class);
//                startActivity(intent);
//                finish();
            } else {
                tv.setText(s);
            }
        }
    }

//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
