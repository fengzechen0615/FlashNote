package com.example.wuke.flashnote.friends;

import android.content.SharedPreferences;
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
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kumbaya on 2018/5/22.
 */

public class Addfriend {
    private String url = "http://39.106.205.176:8080/artifacts/AddFriend";
    private boolean Exist=true;
    public void addfriend(String username, String friendname){
        String s = url + "?username=" + username + "&friendname=" + friendname;
        new Addfriend.MyAsyncTask().execute(s);
    }

    public boolean is_Exist()
    {
        return Exist;
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
            if (s.contains("resCode=102") || s.contains("resCode=201")) {
                Exist = false;
            }
            else if (s.contains("resCode=100")){
                Exist = true;
            }
        }
    }
}
