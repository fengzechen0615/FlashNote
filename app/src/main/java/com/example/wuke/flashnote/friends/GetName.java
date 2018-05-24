package com.example.wuke.flashnote.friends;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wuke.flashnote.download_upload.AsyncResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kumbaya on 2018/5/24.
 */

public class GetName {
    private String url = "http://39.106.205.176:8080/artifacts/GetName";

    public static String getname;

    public void getname(String id){
        String s = url + "?ID="+ id;
        final GetName.MyAsyncTask connect = new GetName.MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                ArrayList a = (ArrayList) listData;
                getname = a.get(0).toString();
            }

            public void onDataReceivedFailed() {
            }

        });
    }

    private ArrayList<String> transfriend (ArrayList<String> a){
        ArrayList<String> friends = new ArrayList<>();
        int i = 0;
        if (a.get(0).contains("resCode")) {
            return null;
        }
        while (i < a.size()){
            Log.e("error",a.size()+"");
            String subs = a.get(i).substring(5);
            friends.add(subs);
            i++;
        }
        return friends;
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

                line = reader.readLine();
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
            ArrayList<String> a = new ArrayList<>();
            a.add(s);
            asyncResponse.onDataReceivedSuccess(a);
        }
    }
}
