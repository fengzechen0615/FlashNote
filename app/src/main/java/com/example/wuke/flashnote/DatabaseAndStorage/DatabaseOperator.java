package com.example.wuke.flashnote.DatabaseAndStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by recur on 2018/4/7.
 */

public class DatabaseOperator {
    private static SQLiteDatabase WriteDatabase;
    private static SQLiteDatabase ReadDatabase;

    public DatabaseOperator(Context context)
    {
        Database db=new Database(context);
        WriteDatabase=db.getWritableDatabase();
        ReadDatabase=db.getReadableDatabase();

    }
}
