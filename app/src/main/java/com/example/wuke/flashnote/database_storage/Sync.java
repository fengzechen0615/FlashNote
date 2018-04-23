package com.example.wuke.flashnote.database_storage;

import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by recur on 2018/4/23.
 */

public class Sync {

    public static HashMap<String,List> CompareTimestamp(String nowTime,List<Note> data)
    {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=form.parse(nowTime,new ParsePosition(0));
        Log.e("sync_date",date.toString());
        Iterator iterator=data.iterator();
        List beforeList =new ArrayList<>();
        List afterList =new ArrayList<>();
        while(iterator.hasNext())
        {
            Note note= (Note) iterator.next();
            Date d=form.parse(note.getTimestamp(),new ParsePosition(0));
            if(date.before(d)==true)
            {
                afterList.add(note);
                Log.e("after",note.getTimestamp());
            }
            else
            {
                beforeList.add(note);
                Log.e("before",note.getTimestamp());
            }
        }
        HashMap map=new HashMap();
        map.put("Before",beforeList);
        map.put("After",afterList);
        return map;
    }


    public boolean VerifyList()
    {
        return true;
    }
}
