package com.example.wuke.flashnote.download_upload;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import com.example.wuke.flashnote.database_storage.*;

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
 * Created by kumbaya on 2018/4/28.
 */

public class Downloading {

    private String note = "http://39.106.205.176:8080/artifacts/downnote";
    private String voice = "http://39.106.205.176:8080/artifacts/downvoice";
    private String sharedvoice = "http://39.106.205.176:8080/artifacts/getsharedvoice";
    private String sharednote = "http://39.106.205.176:8080/artifacts/getsharednote";

    private TextView result;

    public static ArrayList<String> subsn = new ArrayList<>();
    public static ArrayList<String> subsv = new ArrayList<>();
    public static ArrayList<Note> notes = new ArrayList<>();
    public static ArrayList<Voice> voices = new ArrayList<>();

    public void downnote(String user){
        Log.e("down",user);
        String s = note + "?user="+ user;
        final MyAsyncTask connect = new MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                subsn = (ArrayList<String>) listData;
                notes = transnote(subsn);
                Log.e("down",notes.size()+"");
            }

            public void onDataReceivedFailed() {
                notes = null;
            }
        });
    }

    public void downsharednote(String id){
        String s = sharednote + "?id="+ id;
        final MyAsyncTask connect = new MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                subsn = (ArrayList<String>) listData;
                notes = transnote(subsn);
            }
            public void onDataReceivedFailed() {
                notes = null;
            }
        });
    }

    public void downnvoice(String user){
        String s = voice + "?user="+ user;
        final MyAsyncTask connect = new MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                subsv = (ArrayList<String>) listData;
                voices = transvoice(subsv);
            }

            public void onDataReceivedFailed() {
                notes = null;
            }
        });
    }

    public void downsharedvoice(String id){
        String s = sharedvoice + "?id="+ id;
        final MyAsyncTask connect = new MyAsyncTask();
        connect.execute(s);
        connect.setOnAsyncResponse(new AsyncResponse() {

            public void onDataReceivedSuccess(List<String> listData) {
                subsv = (ArrayList<String>) listData;
                voices = transvoice(subsv);
            }

            public void onDataReceivedFailed() {
                notes = null;
            }
        });
    }

    private ArrayList<Note> transnote (ArrayList<String> a){
        ArrayList<Note> notes = new ArrayList<>();
        int i = 0;
        while (i < a.size()){
            int m = 0;
            Note note = new Note();
            while(m < 6) {
                String sub = a.get(i);
                String content = null;
                if (sub.contains("userID=") == true) {
                    content = sub.substring(7);
                    int id = Integer.parseInt(content);
                    note.setUserID(id);
                } else if (sub.contains("content=") == true) {
                    content = sub.substring(8);
                    note.setWords(content);
                } else if (sub.contains("noteID=") == true) {
                    content = sub.substring(7);
                    int id = Integer.parseInt(content);
                    note.setNoteID(id);
                } else if (sub.contains("timeStamp=") == true) {
                    content = sub.substring(10);
                    note.setTimestamp(content);
                } else if (sub.contains("color=") == true) {
                    content = sub.substring(6);
                    int id = Integer.parseInt(content);
                    note.setColor(id);
                } else if (sub.contains("priority=") == true) {
                    content = sub.substring(9);
                    int id = Integer.parseInt(content);
                    note.setPriority(id);
                    notes.add(note);
                }
                i++;
                m++;
            }
        }
        return notes;
    }

    private ArrayList<Voice> transvoice (ArrayList<String> a){
        ArrayList<Voice> voices = new ArrayList<>();
        int i = 0;
        while (i < a.size()){
            int m = 0;
            Voice voice = new Voice();
            while(m < 6) {
                String sub = a.get(i);
                String content = null;
                if (sub.contains("userID=") == true) {
                    content = sub.substring(7);
                    int id = Integer.parseInt(content);
                    voice.setUserID(id);
                } else if (sub.contains("content=") == true) {
                    content = sub.substring(8);
                    voice.setURL(content);
                } else if (sub.contains("voiceID=") == true) {
                    content = sub.substring(8);
                    int id = Integer.parseInt(content);
                    voice.setVoiceID(id);
                } else if (sub.contains("timeStamp=") == true) {
                    content = sub.substring(10);
                    voice.setTimestamp(content);
                } else if (sub.contains("color=") == true) {
                    content = sub.substring(6);
                    int id = Integer.parseInt(content);
                    voice.setColor(id);
                } else if (sub.contains("priority=") == true) {
                    content = sub.substring(9);
                    int id = Integer.parseInt(content);
                    voice.setPriority(id);
                    voices.add(voice);
                }
                i++;
                m++;
            }
        }
        return voices;
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
