package com.example.wuke.flashnote.login;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by kumbaya on 2018/5/8.
 */

public class Signup {
    private String url = "http://39.106.205.176:8080/artifacts/Signup";

    public void signup(String a, String p,String e) {
        String s = url + "?account=" + a + "&password=" + p +"&email=" + e;
        Log.e("result",s);
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
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
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
