package com.example.wuke.flashnote.friends;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.download_upload.AsyncResponse;
import com.example.wuke.flashnote.download_upload.Downloading;

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
 * Created by kumbaya on 2018/5/23.
 */

public class GetFriends {
    public static ArrayList<String> list = new ArrayList<>();
    private String note = "http://39.106.205.176:8080/artifacts/GetFriends";
    ArrayList<String> friends = new ArrayList<>();


    public void getfriends(String user){
        String s = note + "?username="+ user;
        final GetFriends.MyAsyncTask connect = new GetFriends.MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                Log.e("friends","success");
                friends = (ArrayList) listData;
                list = transfriend(friends);
                Log.e("friends3",list.get(0)+"");
            }

            public void onDataReceivedFailed() {
            }
        });
    }

    private ArrayList<String> transfriend (ArrayList<String> a){
        ArrayList<String> friends = new ArrayList<>();
        int i = 0;
        while (i < a.size()){
            String subs = a.get(i).substring(5);
            friends.add(subs);
            i++;
        }
        return friends;
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, ArrayList<String>> {

        Log log;

        ArrayList<String> a = new ArrayList<>();

        public AsyncResponse asyncResponse;
        public void setOnAsyncResponse(AsyncResponse asyncResponse){
            this.asyncResponse = asyncResponse;
        }

        public MyAsyncTask() {
        }

        protected void onPreExecute() {
            log.w("1", "task onPreExecute()");
        }

        protected ArrayList<String> doInBackground(String... params) {
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
                    a.add(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return a;
        }
        protected void onPostExecute(ArrayList<String> s){
            if(s != null){
                List<String> listData = new ArrayList<>();
                listData = s;
                asyncResponse.onDataReceivedSuccess(listData);
            }
            else{
                Log.w("no","no values in the server");
            }
        }
    }
}
