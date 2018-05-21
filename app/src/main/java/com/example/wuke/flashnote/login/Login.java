package com.example.wuke.flashnote.login;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.setting.WukeCloud;

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

import static android.content.Context.MODE_PRIVATE;

public class Login extends Fragment implements OnClickListener{

    private String url = "http://39.106.205.176:8080/artifacts/Servlet";
    private EditText account;
    private EditText password;
    private TextView result;
    private Button login;
    private TextView register;
    private TextView forget;

    private String rightCode;

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

        forget = (TextView) view.findViewById(R.id.forget);
        forget.setOnClickListener(this);

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
                if (account.length() != 0 && password.length() != 0) {
                    Log.d("login", "Login");
                    String a = account.getText().toString();
                    String p = password.getText().toString();
                    access(a, p);

                    login.setEnabled(false);

                    Toast.makeText(getActivity(), getActivity().getString(R.string.login_p), Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (rightCode.equals("resCode=201")) {
                                login.setEnabled(true);
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                                transaction.replace(R.id.id_content, new LoginStatus().newInstance(account.getText().toString()));
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                login.setEnabled(true);
                                wrongDialog();
                            }
                        }
                    }, 2000);
                } else {
                    wrongDialog();
                }

                break;
            case R.id.forget:
                forget();
                break;
            default:
                break;
        }
    }

    private void wrongDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.wrong))
                .setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void forget() {
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.e_mail))
                .setTitle("Enter your username to reset password")
                .setView(editText)
                .setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input 为输入内容
                        String input = editText.getText().toString();
                        ResetPass reset = new ResetPass();
                        reset.resetpass(input);
                        //Toast t = Toast.makeText(this,"Send email to your email address",Toast.LENGTH_LONG);
                        //t.show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
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
                rightCode = "resCode=201";
                LocalLogin l = new LocalLogin();
                String a = account.getText().toString();
                String p = password.getText().toString();
                int id=s.indexOf("userID=");
                String userid = s.substring(id+7);

                l.save(a, p);
                Log.e("post1", a + " " + p);
                Timestamp nowTime = new Timestamp(System.currentTimeMillis());//Login Time
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = form.format(nowTime);
                SharedPreferences pref=getActivity().getSharedPreferences("info",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("username",a);
                editor.putInt("userid",Integer.parseInt(userid));
                editor.commit();
                Log.e("fk",pref.getInt("userid",0)+"");
//                Intent intent = new Intent(Login.this, RecordSetting.class);
//            startActivity(intent);
//                finish();
            } else {
//                tv.setText(s);
                rightCode = "wrong";
            }
        }
    }

}
