package com.example.wuke.flashnote.download_upload;

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
 * Created by kumbaya on 2018/5/24.
 */

public class ChangeNote {
    private String url = "http://39.106.205.176:8080/artifacts/ChangeNote";


    public void getname(String noteID, String content){
        String s = url + "?noteID=" + noteID + "&content=" + content;
        new MyAsyncTask().execute(s);
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        Log log;

        String line;

        public AsyncResponse asyncResponse;
        public void setOnAsyncResponse(AsyncResponse asyncResponse){
            this.asyncResponse = asyncResponse;
        }

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
            return line;
        }
        protected void onPostExecute(String s){
            //Log.w("changeresult",s);
        }
    }
}
