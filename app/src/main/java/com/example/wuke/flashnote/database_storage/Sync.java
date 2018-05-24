package com.example.wuke.flashnote.database_storage;

import android.util.Log;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.download_upload.Downloading;

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
    DatabaseOperator dbo;

    public static HashMap<String,List> CompareTimestamp(String nowTime,List<Storage> data)
    {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=form.parse(nowTime,new ParsePosition(0));
        Log.e("sync_date",date.toString());
        Iterator iterator=data.iterator();
        List beforeList =new ArrayList<>();
        List afterList =new ArrayList<>();
        while(iterator.hasNext())
        {
            Storage storage= (Storage) iterator.next();
            Date d=form.parse(storage.getTimestamp(),new ParsePosition(0));
            SimpleDateFormat form1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat form2=new SimpleDateFormat("HH:mm:ss");
            Log.e("before1",date+""+d);
            if(date.before(d)==true)
            {
                storage.setTimestamp(form1.format(d)+"%20"+form2.format(d));
                afterList.add(storage);
                storage.setTimestamp(form.format(d));
                Log.e("after",storage.getDataType()+"");
            }
            else
            {
                beforeList.add(storage);
                Log.e("before",storage.getTimestamp());
            }
        }
        HashMap map=new HashMap();
        map.clear();
        map.put("Before",beforeList);
        map.put("After",afterList);
        return map;
    }

}
