package com.example.wuke.flashnote.DatabaseAndStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Database extends SQLiteOpenHelper{
    private static final String DBNAME = "flashnote.db";


    public Database(Context context) {
        super(context, DBNAME, null, 1);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    String sql1="Create table if not exists "+Initial.table_user+" ( "
            +Initial.user_id+" integer primary key autoincrement, "
            +Initial.username+ " VARCHAR(30), "
            +Initial.user_password+ " VARCHAR(20), "
            +Initial.user_capacity_words+" INTEGER, "
            +Initial.user_capacity_voice+" INTEGER, "
            +Initial.user_Lastlogin+" TEXT, "
            +Initial.user_list+" TEXT ) ";
    sqLiteDatabase.execSQL(sql1);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
