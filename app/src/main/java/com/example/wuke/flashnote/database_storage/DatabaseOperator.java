package com.example.wuke.flashnote.database_storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
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

    public boolean EditWord(int noteId, String words)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        Log.e("database",words);
        cValue.put(Initial.note_words, words);
        String sql = "Update "+Initial.table_note+" set "+Initial.note_words+"= '"+
        words+"' where "+Initial.note_id+"="+noteId;
        Log.d("db",sql);
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateNoteColor(int noteId, int color)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.note_color,color);
        String sql = "Update "+Initial.table_note+" set "+Initial.note_color+"= "+
                color+" where "+Initial.note_id+"="+noteId;
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateVoiceColor(int voiceid, int color) {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.voice_color,color);
        String sql = "Update "+Initial.table_voice+" set "+Initial.voice_color+"= "+
                color+" where "+Initial.voice_id+"="+voiceid;
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateNotePriority(int noteid, int priority) {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.note_priority,priority);
        String sql = "Update "+Initial.table_note+" set "+Initial.note_priority+"= "+
                priority+" where "+Initial.note_id+"="+noteid;
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateNoteUser(int noteid, int userID) {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.note_priority,userID);
        String sql = "Update "+Initial.table_note+" set "+Initial.note_user+"= "+
                userID+" where "+Initial.note_id+"="+noteid;
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateVoicePriority(int voiceid, int priority) {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.voice_priority,priority);
        String sql = "Update "+Initial.table_voice+" set "+Initial.voice_priority+"= "+
                priority+" where "+Initial.voice_id+"="+voiceid;
        wdb.execSQL(sql);
        return true;
    }

    public boolean UpdateVoiceUser(int voiceid, int userID) {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb = WriteDatabase;
        cValue.put(Initial.voice_priority,userID);
        String sql = "Update "+Initial.table_voice+" set "+Initial.voice_users+"= "+
                userID+" where "+Initial.voice_id+"="+voiceid;
        wdb.execSQL(sql);
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
        cValue.put(Initial.datatype,note.getDataType());
        long NID=wdb.insert(Initial.table_note,null,cValue);
        Log.d("database","Insert successfully");
        return (int)NID;
    }

    public int RevertNote(Note note)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.note_id,note.getNoteID());
        cValue.put(Initial.note_user,note.getUserID());
        cValue.put(Initial.note_words,note.getWords());
        cValue.put(Initial.note_color,note.getColor());
        cValue.put(Initial.note_timestamp,note.getTimestamp());
        cValue.put(Initial.note_priority,note.getPriority());
        cValue.put(Initial.datatype,note.getDataType());
        long NID=wdb.insert(Initial.table_note,null,cValue);
        Log.d("database","Insert successfully");
        return (int)NID;
    }

    public int InsertVoice(Voice voice)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.voice_file,voice.getURL());
        cValue.put(Initial.voice_users,voice.getUserID());
        cValue.put(Initial.voice_color,voice.getColor());
        cValue.put(Initial.voice_timestamp,voice.getTimestamp());
        cValue.put(Initial.voice_priority,voice.getPriority());
        cValue.put(Initial.datatype,voice.getDataType());
        cValue.put(Initial.voice_duration,voice.getDuration());
        long VID=wdb.insert(Initial.table_voice,null,cValue);
        return (int)VID;
    }

    public int RevertVoice(Voice voice)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.voice_id,voice.getVoiceID());
        cValue.put(Initial.voice_file,voice.getURL());
        cValue.put(Initial.voice_users,voice.getUserID());
        cValue.put(Initial.voice_color,voice.getColor());
        cValue.put(Initial.voice_timestamp,voice.getTimestamp());
        cValue.put(Initial.voice_priority,voice.getPriority());
        cValue.put(Initial.datatype,1);
        cValue.put(Initial.voice_duration,voice.getDuration());
        long VID=wdb.insert(Initial.table_voice,null,cValue);
        return (int)VID;
    }


    public int InsertFriend(Friends f)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.friend_fid,f.getFriendsID());
        cValue.put(Initial.friend_user_id,f.getUserID());
        long FID=wdb.insert(Initial.table_friend,null,cValue);
        return (int)FID;
    }


    public boolean deleteNote(int id){
        SQLiteDatabase wdb=WriteDatabase;
        wdb.delete(Initial.table_note,"note_id=?",new String[]{String.valueOf(id)});
        return false;
    }

    public boolean deleteAllNote(int userid){
        SQLiteDatabase wdb=WriteDatabase;
        wdb.delete(Initial.table_note,Initial.note_user+"=?",new String[]{String.valueOf(userid)});
        return false;
    }


    public boolean deleteVoice(int id){
        SQLiteDatabase wdb=WriteDatabase;
        wdb.delete(Initial.table_voice,"voice_id=?",new String[]{String.valueOf(id)});
        return false;
    }

    public boolean deleteGarbage(int id){
        SQLiteDatabase wdb=WriteDatabase;
        wdb.delete(Initial.Garbage_table,Initial.Litter_id+"=?",new String[]{String.valueOf(id)});
        return false;
    }



    public boolean addGarbage(Garbage garbage)
    {
        ContentValues cValue = new ContentValues();
        SQLiteDatabase wdb=WriteDatabase;
        cValue.put(Initial.content_id,garbage.getContent_id());
        cValue.put(Initial.garbage_datatype,garbage.getDatatype());
        cValue.put(Initial.guser_id,garbage.getGuser_id());
        cValue.put(Initial.keywords,garbage.getKeywords());
        cValue.put(Initial.previous_color,garbage.getPrevious_color());
        cValue.put(Initial.previous_timestamp,garbage.getPrevious_timestamp());
        cValue.put(Initial.previous_priority,garbage.getPrevious_priority());
        cValue.put(Initial.extra,garbage.getExtra());
        wdb.insert(Initial.Garbage_table,null,cValue);
        return false;
    }

    public List<Garbage> getGarbage(int userid)
    {
        ArrayList list=new ArrayList();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.Garbage_table+" WHERE "+Initial.guser_id+"=0 or "+Initial.guser_id+" =?",new String[]{String.valueOf(userid)});
        int lidindex = cursor.getColumnIndex(Initial.Litter_id);
        int contentindex = cursor.getColumnIndex(Initial.content_id);
        int dataindex = cursor.getColumnIndex(Initial.garbage_datatype);
        int userindex = cursor.getColumnIndex(Initial.guser_id);
        int keyindex = cursor.getColumnIndex(Initial.keywords);
        int ptdindex = cursor.getColumnIndex(Initial.previous_timestamp);
        int pcdindex = cursor.getColumnIndex(Initial.previous_color);
        int ppdindex = cursor.getColumnIndex(Initial.previous_priority);
        int eindex = cursor.getColumnIndex(Initial.extra);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            Garbage garbage = new Garbage(cursor.getInt(lidindex)
                    , cursor.getInt(contentindex)
                    , cursor.getInt(dataindex)
                    , cursor.getInt(userindex)
                    , cursor.getString(keyindex)
                    , cursor.getString(ptdindex)
                    , cursor.getInt(pcdindex)
                    , cursor.getInt(ppdindex)
                    , cursor.getInt(eindex));
            list.add(garbage);
        }
        cursor.close();
        return list;
    }

    public int finduser(String username) {
        int id=-1;
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT User_id FROM User WHERE Username=?", new String[]{String.valueOf(username)});
        int userIDindex=cursor.getColumnIndex("User_id");
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            id=cursor.getInt(userIDindex);
            Log.e("friend",id+"");
        }
        return id;
    }


    public List getAllFriends(int userID){
        ArrayList<Friends> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM Friends WHERE User_id =?",new String[]{String.valueOf(userID)});
        int FriendsIDindex=cursor.getColumnIndex(Initial.friend_fid);
        int userIDindex=cursor.getColumnIndex(Initial.friend_user_id);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            Friends friends = new Friends(cursor.getInt(FriendsIDindex)
                    , cursor.getInt(userIDindex));
            Log.e("db", friends.getUserID() + "");
            result.add(friends);
        }
        cursor.close();
        return result;
    }


    public List getAllNote(int userID) //Later,priority order
    {
        ArrayList<Note> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.table_note+" WHERE "+Initial.note_user+"=0 or "+Initial.note_user+" =?",new String[]{String.valueOf(userID)});
        int noteIDindex=cursor.getColumnIndex(Initial.note_id);
        int userIDindex=cursor.getColumnIndex(Initial.note_user);
        int wordsindex=cursor.getColumnIndex(Initial.note_words);
        int timeindex=cursor.getColumnIndex(Initial.note_timestamp);
        int colorindex=cursor.getColumnIndex(Initial.note_color);
        int pindex=cursor.getColumnIndex(Initial.note_priority);
        int dataindex=cursor.getColumnIndex(Initial.datatype);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            Note note=new Note(cursor.getInt(noteIDindex)
                    ,cursor.getInt(userIDindex)
                    ,cursor.getString(wordsindex)
                    ,cursor.getInt(colorindex)
                    ,cursor.getString(timeindex)
                    ,cursor.getInt(pindex),
                    cursor.getInt(dataindex));
            Log.e("db",note.getNoteID()+"");
            result.add(note);
        }
        cursor.close();
        return result;
    }

    public List getAllVoice(int userID) //Later,priority order
    {
        ArrayList<Voice> result = new ArrayList<>();
        SQLiteDatabase rdb=ReadDatabase;
        Log.e("get",userID+"");
        Cursor cursor=rdb.rawQuery("SELECT * FROM "+Initial.table_voice+" WHERE "+Initial.voice_users+"=0 or "+Initial.voice_users+"=?",new String[]{String.valueOf(userID)});
        int voiceIDindex=cursor.getColumnIndex(Initial.voice_id);
        int userIDindex=cursor.getColumnIndex(Initial.voice_users);
        int fileindex=cursor.getColumnIndex(Initial.voice_file);
        int timeindex=cursor.getColumnIndex(Initial.voice_timestamp);
        int colorindex=cursor.getColumnIndex(Initial.voice_color);
        int pindex=cursor.getColumnIndex(Initial.voice_priority);
        int dataindex=cursor.getColumnIndex(Initial.datatype);
        int topicindex=cursor.getColumnIndex(Initial.voice_duration);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext())
        {
            Voice voice=new Voice(cursor.getInt(voiceIDindex)
                    ,cursor.getInt(userIDindex)
                    ,cursor.getString(fileindex)
                    ,cursor.getInt(colorindex)
                    ,cursor.getString(timeindex)
                    ,cursor.getInt(pindex)
                    ,cursor.getInt(dataindex)
                    ,cursor.getInt(topicindex));
            result.add(voice);
        }
        cursor.close();
        return result;
    }

    public HashMap<String,List> getAll(int userID)
    {
        HashMap<String,List> map=new HashMap<>();
        map.put("NOTE",getAllNote(userID));
        map.put("VOICE",getAllVoice(userID));
        return map;
    }

    public List<Storage> getAllStorage(int userID)
    {
        List<Storage> AlList=new ArrayList<>();
        AlList.addAll(getAllNote(userID));
        AlList.addAll(getAllVoice(userID));
        Storage s=new Storage();
        Collections.sort(AlList,s);
        return AlList;
    }


}
