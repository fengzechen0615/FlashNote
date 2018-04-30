package com.example.wuke.flashnote;

import android.os.AsyncTask;
import android.util.Log;

import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Voice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kumbaya on 2018/4/30.
 */

public class deleting {

    private String note = "http://39.106.205.176:8080/artifacts/denote";
    private String voice = "http://39.106.205.176:8080/artifacts/devoice";

    public void deletenote(ArrayList<Note> a){
        int i = 0;
        while (i < a.size()){
            Note n = a.get(i);
            String userid = Integer.toString(n.getUserID());
            String noteid = Integer.toString(n.getNoteID());
            denote(userid,noteid);
        }
    }

    public void deletevoice(ArrayList<Voice> a){
        int i = 0;
        while (i < a.size()){
            Voice v = a.get(i);
            String userid = Integer.toString(v.getUserID());
            String voiceid = Integer.toString(v.getVoiceID());
            denote(userid,voiceid);
        }
    }

    public void denote(String userID,String noteID){
        String s = note + "?userID="+ userID + "&noteID=" + noteID;
        new MyAsyncTask().execute(s);
    }

    public void devoice(String userID,String noteID){
        String s = voice + "?userID="+ userID + "&voiceID=" + noteID;
        new MyAsyncTask().execute(s);
    }

    public class MyAsyncTask extends AsyncTask<String ,Integer, String > {

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
        protected void onPostExecute(String s){
            if(s.contains("resCode=100")){
                Log.d("123","yes");
            }
            else{
            }
        }
    }
}
