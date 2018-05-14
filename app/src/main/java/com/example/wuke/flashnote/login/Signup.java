package com.example.wuke.flashnote.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.wuke.flashnote.setting.Setting;

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

/**
 * Created by kumbaya on 2018/5/8.
 */

public class Signup {
    private String url = "http://39.106.205.176:8080/artifacts/Signup";

    private void signup(String a, String p,String e) {
        String s = url + "?account=" + a + "&pass=" + p +"&email=" + e;
        new Signup.MyAsyncTask().execute(s);
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        Log log;

        public MyAsyncTask() {

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
            Log.e("result",s);
        }
    }

}
