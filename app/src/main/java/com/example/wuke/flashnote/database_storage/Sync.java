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
            SimpleDateFormat form1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat form2=new SimpleDateFormat("HH:mm:ss");
            Log.e("before1",date+""+d);
            if(date.before(d)==true)
            {
                note.setTimestamp(form1.format(d)+"%20"+form2.format(d));
                afterList.add(note);
                note.setTimestamp(form.format(d));
                Log.e("after",note.getNoteID()+"");
            }
            else
            {
                beforeList.add(note);
                Log.e("before",note.getTimestamp());
            }
        }
        HashMap map=new HashMap();
        map.clear();
        map.put("Before",beforeList);
        map.put("After",afterList);
        return map;
    }


    public List<Note> VerifyList(List LocalList,List ServerList)
    {
        List Verified =new ArrayList<>();
        Iterator itl=LocalList.iterator();
        Iterator its=LocalList.iterator();
        while(itl.hasNext())
        {
            int i=((Note)itl.next()).getNoteID();
            while(its.hasNext())
            {
                if(((Note)its.next()).getNoteID()!=i)
                {
                    its.remove();
                }
            }
        }
        return Verified;
    }
}
