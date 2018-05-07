package com.example.wuke.flashnote.download_upload;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.wuke.flashnote.database_storage.Friends;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Sharednote;
import com.example.wuke.flashnote.database_storage.Sharedvoice;
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
 * Created by kumbaya on 2018/4/22.
 */

public class Uploading {

    private String note = "http://39.106.205.176:8080/artifacts/upnote";
    private String voice = "http://39.106.205.176:8080/artifacts/upvoice";
    private String friend = "http://39.106.205.176:8080/artifacts/upfriends";
    private String sharenote = "http://39.106.205.176:8080/artifacts/upsharednote";
    private String sharevoice = "http://39.106.205.176:8080/artifacts/upsharedvoice";

    private TextView result;

    private void upnote(String id, String content, String user, String time, String color, String priority){
        String s = note + "?ID="+ id + "&content=" + content +"&user=" + user + "&time=" + time + "&color="
                + color + "&priority=" + priority;
        new Uploading.MyAsyncTask().execute(s);
    }//单条上传的方法

    private void upvoice(String id, String content, String user, String time, String color, String priority){
        String s = voice + "?ID="+ id + "&content=" + content +"&user=" + user + "&time=" + time + "&color="
                + color + "&priority=" + priority;
        new Uploading.MyAsyncTask().execute(s);
    }//单条上传的方法

    public void upfriend(String userid, String friendid){
        String s = friend + "?ID=" + userid + "&friend=" + friendid;
        new Uploading.MyAsyncTask().execute(s);
    }

    public void upsharednote(String userid, String noteid){
        String s = sharenote + "?ID=" + userid + "&sharednote=" + noteid;
        new Uploading.MyAsyncTask().execute(s);
    }

    public void upsharevoice(String userid, String voiceid){
        String s = sharevoice + "?ID=" + userid + "&sharedvoice=" + voiceid;
        new Uploading.MyAsyncTask().execute(s);
    }

    public void uploadnote(ArrayList<Note> a){
        int i = 0;
        Note note;
        while(i < a.size()){
            note = a.get(i);

            String id = Integer.toString(note.getNoteID());
            String content = note.getWords();
            String userid = Integer.toString(note.getUserID());
            String time = note.getTimestamp();
            String color = Integer.toString(note.getColor());
            String priority = Integer.toString(note.getPriority());

            upnote(id,content,userid,time,color,priority);  //upload one piece
            i++;
        }
    }

    public void uploadvoice(ArrayList<Voice> a){
        int i = 0;
        Voice voice;
        while(i < a.size()){
            voice = a.get(i);

            String id = Integer.toString(voice.getVoiceID());
            String content = voice.getURL();
            String userid = Integer.toString(voice.getUserID());
            String time = voice.getTimestamp();
            String color = Integer.toString(voice.getColor());
            String priority = Integer.toString(voice.getPriority());

            upvoice(id,content,userid,time,color,priority);  //upload one piece
            i++;
        }
    }

    public void uploadfriend(ArrayList<Friends> a){
        int i = 0;
        Friends friend;
        while(i < a.size()){
            friend = a.get(i);

            String userid = Integer.toString(friend.getUserID());
            String frienid = Integer.toString(friend.getFriendsID());

            upfriend(userid,frienid);  //upload one piece
            i++;
        }
    }

    public void uploadsharednote(ArrayList<Sharednote> a){
        int i = 0;
        Sharednote note;
        while(i < a.size()){
            note = a.get(i);

            String userid = Integer.toString(note.getUserID());
            String noteid = Integer.toString(note.getShared_noteID());

            upsharednote(userid,noteid);  //upload one piece
            i++;
        }
    }

    public void uploadsharedvoice(ArrayList<Sharedvoice> a){
        int i = 0;
        Sharedvoice voice;
        while(i < a.size()){
            voice = a.get(i);

            String userid = Integer.toString(voice.getUserID());
            String voiceid = Integer.toString(voice.getShared_noteID());

            upfriend(userid,voiceid);  //upload one piece
            i++;
        }
    }

    private class MyAsyncTask extends AsyncTask<String ,Integer, String > {

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

            }
            else{
            }
        }
    }
}
