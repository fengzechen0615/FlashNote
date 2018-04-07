package com.example.wuke.flashnote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Login extends AppCompatActivity {

    private String url = "http://39.106.205.176:8080/artifacts/Servlet";
    private EditText account;
    private EditText password;
    private TextView result;
    private Button login;
    private Button sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        result = (TextView) findViewById(R.id.result);

        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString() != null &&
                        password.getText().toString() != null) {
                    String a = account.getText().toString();
                    String p = password.getText().toString();
                    access(a, p, "login");

                }
            }
        });

        sign = (Button) findViewById(R.id.sign_button);
        sign.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(account.getText().toString() != null &&
                        password.getText().toString() != null) {
                    String a = account.getText().toString();
                    String p = password.getText().toString();
                    access(a, p, "sign");
                }
            }
        });

        }

    private void access(String a, String p, String l){
        String s = url+"?account="+ a + "&password=" + p +"&login="+l;
        new MyAsyncTask(result).execute(s);
    }

    public class MyAsyncTask extends AsyncTask<String ,Integer, String > {
        private TextView tv;

        Log log;

        public MyAsyncTask(TextView v){
            tv = v;
        }

        protected void onPreExecute(){
            log.w("1", "task onPreExecute()");
        }

        protected String doInBackground (String... params){
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
                while ((line = reader.readLine()) != null){
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

        protected void onPostExecute(String s){
            if(s.contains("resCode=201")){
                Intent intent= new Intent (Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                tv.setText(s);
            }
        }
    }
}
