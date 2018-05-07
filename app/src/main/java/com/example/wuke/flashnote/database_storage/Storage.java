package com.example.wuke.flashnote.database_storage;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by recur on 2018/4/6.
 */

public class Storage implements Comparator{

    private int Datatype;
    private int userID;
    private String Timestamp;
    private int priority;
    private int Color;

    public int getUserID()
    {
        return userID;
    }
    public String getTimestamp()
    {
        return Timestamp;
    }
    public int getPriority()
    {
        return priority;
    }
    public int getColor() {return Color;}
    public int getDataType() {return Datatype;}

    @Override
    public int compare(Object o1, Object o2) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=form.parse((String)o1,new ParsePosition(0));
        Date d2=form.parse((String)o2,new ParsePosition(0));
        if(d1.after(d2)) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
