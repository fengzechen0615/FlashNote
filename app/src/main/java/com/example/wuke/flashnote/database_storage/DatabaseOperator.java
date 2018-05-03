package com.example.wuke.flashnote.database_storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.RenderScript;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class DatabaseOperator {
    private static SQLiteDatabase WriteDatabase;
    private static SQLiteDatabase ReadDatabase;

    public DatabaseOperator(Context context)
    {
        Database db=new Database(context);
        WriteDatabase=db.getWritableDatabase();
        ReadDatabase=db.getReadableDatabase();

    }

    public int Register(User user)//insert user
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.user_id,user.getUserID());
        cValue.put(Initial.username,user.getusername());
        cValue.put(Initial.user_password,user.getPassword());
        cValue.put(Initial.user_Lastlogin,user.getLatestLogin());
        cValue.put(Initial.user_capacity_words,user.getCapacity_words());
        cValue.put(Initial.user_capacity_voice,user.getCapacity_voice());
        long UID=wdb.insert(Initial.table_user,null,cValue);
        wdb.close();
        return (int)UID;
    }

    public boolean Login(String username,String timestamp)//update user login info
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.user_Lastlogin,timestamp);

        wdb.update(Initial.table_user,cValue,Initial.username+"=?",
                new String[]{String.valueOf(username)});
        wdb.close();
        return true;
    }

    public boolean EditWord(int noteId,String words)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_words,words);
        wdb.update(Initial.table_note,cValue,Initial.note_id+"=?",
                new String[]{String.valueOf(noteId)});
        wdb.close();
        return true;
    }

    public boolean EditColor(int noteId,int color)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_color,color);
        wdb.update(Initial.table_note,cValue,Initial.note_id+"=?",
                new String[]{String.valueOf(noteId)});
        wdb.close();
        return true;
    }


    public int FindUserId(String username)
    {
        int result=0;
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.query(Initial.table_user,new String[]{Initial.user_id},"Username=?",
                new String[]{String.valueOf(username)},null,null,null,null);
        int userIdIndex=cursor.getColumnIndex(Initial.user_id);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            result=cursor.getInt(userIdIndex);
        }
        cursor.close();
        return result;
    }

    public String FindUser(int id)
    {
        String result=null;
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.query(Initial.table_user,new String[]{Initial.username},Initial.user_id+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            result=cursor.getString(cursor.getColumnIndex(Initial.username));
        }
        cursor.close();
        return result;
    }

    public int InsertNote(Note note)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_user,note.getUserID());
        cValue.put(Initial.note_words,note.getWords());
        cValue.put(Initial.note_color,note.getColor());
        cValue.put(Initial.note_timestamp,note.getTimestamp());
        cValue.put(Initial.note_priority,note.getPriority());
        long NID=wdb.insert(Initial.table_note,null,cValue);
        Log.d("database","Insert successfully");
        return (int)NID;
    }

    public int InsertVoice(Voice voice)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.voice_users,voice.getUserID());
        cValue.put(Initial.voice_url,voice.getURL());
        cValue.put(Initial.voice_color,voice.getColor());
        cValue.put(Initial.voice_timestamp,voice.getTimestamp());
        cValue.put(Initial.voice_priority,voice.getPriority());
        long VID=wdb.insert(Initial.table_voice,null,cValue);
        return (int)VID;
    }

    public boolean updatePriority(int id,int Priority)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_priority,Priority);

        wdb.update(Initial.table_user,cValue,Initial.note_id+"=?",
                new String[]{String.valueOf(id)});
        wdb.close();
        return true;
    }

    public boolean deleteNote(int id){
        SQLiteDatabase wdb=WriteDatabase;
        wdb.delete(Initial.table_note,"note_id=?",new String[]{String.valueOf(id)});
        wdb.close();
        return false;
    }

    private boolean addGarbage(Garbage garbage)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_id,garbage.NID());
        cValue.put(Initial.note_user,garbage.UID());
        wdb.insert(Initial.Garbage_table,null,cValue);
        wdb.close();
        return false;
    }

    public List getAllFriends(){
        ArrayList<Friends> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.table_friend,null);
        int FriendsIDindex=cursor.getColumnIndex(Initial.friend_fid);
        int userIDindex=cursor.getColumnIndex(Initial.friend_user_id);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            Friends friends = new Friends(cursor.getInt(FriendsIDindex)
                    , cursor.getInt(userIDindex));
            Log.e("db", friends.getUserID() + "");
            result.add(friends);
        }
        return result;
    }


    public List getAllNote() //Later,priority order
    {
        ArrayList<Note> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.table_note,null);
        int noteIDindex=cursor.getColumnIndex(Initial.note_id);
        int userIDindex=cursor.getColumnIndex(Initial.note_user);
        int wordsindex=cursor.getColumnIndex(Initial.note_words);
        int timeindex=cursor.getColumnIndex(Initial.note_timestamp);
        int colorindex=cursor.getColumnIndex(Initial.note_color);
        int pindex=cursor.getColumnIndex(Initial.note_priority);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            Note note=new Note(cursor.getInt(noteIDindex)
                    ,cursor.getInt(userIDindex)
                    ,cursor.getString(wordsindex)
                    ,cursor.getInt(colorindex)
                    ,cursor.getString(timeindex)
                    ,cursor.getInt(pindex));
            Log.e("db",note.getNoteID()+"");
            result.add(note);
        }
        cursor.close();
        rdb.close();
        return result;
    }

    public List getAllVoice() //Later,priority order
    {
        ArrayList<Voice> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.table_voice,null);
        int voiceIDindex=cursor.getColumnIndex(Initial.voice_id);
        int userIDindex=cursor.getColumnIndex(Initial.voice_users);
        int urlindex=cursor.getColumnIndex(Initial.voice_url);
        int timeindex=cursor.getColumnIndex(Initial.voice_timestamp);
        int colorindex=cursor.getColumnIndex(Initial.voice_color);
        int pindex=cursor.getColumnIndex(Initial.voice_priority);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            Voice voice=new Voice(cursor.getInt(voiceIDindex)
                    ,cursor.getInt(userIDindex)
                    ,cursor.getString(urlindex)
                    ,cursor.getInt(colorindex)
                    ,cursor.getString(timeindex)
                    ,cursor.getInt(pindex));
            result.add(voice);
        }
        cursor.close();
        rdb.close();
        return result;
    }

    public HashMap<String,List> getAll()
    {
        HashMap<String,List> map=new HashMap<>();
        map.put("NOTE",getAllNote());
        map.put("VOICE",getAllVoice());
        return map;
    }


}
