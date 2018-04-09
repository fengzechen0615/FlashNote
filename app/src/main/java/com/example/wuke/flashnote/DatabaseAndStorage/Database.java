package com.example.wuke.flashnote.DatabaseAndStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Database extends SQLiteOpenHelper{
    private static final String DBNAME = "flashnote.db";
    private static final String sql1="Create table if not exists "+Initial.table_user+" ( "
            +Initial.user_id+" integer primary key autoincrement, "
            +Initial.username+ " VARCHAR(255) UNIQUE, "
            +Initial.user_password+ " VARCHAR(255), "
            +Initial.user_capacity_words+" INTEGER, "
            +Initial.user_capacity_voice+" INTEGER, "
            +Initial.user_Lastlogin+" TEXT) ";
    //+Initial.user_list+" TEXT) ";


    private static final String sql2="Create table if not exists "+Initial.table_note+" ( "
            +Initial.note_id+" integer primary key autoincrement, "
            +Initial.note_user+ " VARCHAR(255), "
            +Initial.note_words+ " TEXT, "
            +Initial.note_color+" INTEGER, "
            +Initial.note_timestamp+" TEXT, "
            +Initial.note_priority+" INTEGER) ";


    private static final String sql3="Create table if not exists "+Initial.table_voice+" ( "
            +Initial.voice_id+" integer primary key autoincrement, "
            +Initial.voice_users+ " INTEGER, "
            +Initial.voice_url+ " VARCHAR(255), "
            +Initial.voice_color+" INTEGER, "
            +Initial.voice_timestamp+" TEXT, "
            +Initial.voice_priority+" INTEGER) ";


    private static final String sql4="Create table if not exists "+Initial.table_friend+" ( "
            +Initial.friend_fid+" INTEGER, "
            +Initial.friend_user_id+" INTEGER) ";


    private static final String sql5="Create table if not exists "+Initial.table_shared+" ( "
            +Initial.shared_userid+" INTEGER, "
            +Initial.shared_noteid+" INTEGER, "
            +Initial.shared_Voiceid+" INTEGER) ";


    public Database(Context context) {
        super(context, DBNAME, null, 1);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        sqLiteDatabase.execSQL(sql4);
        sqLiteDatabase.execSQL(sql5);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
