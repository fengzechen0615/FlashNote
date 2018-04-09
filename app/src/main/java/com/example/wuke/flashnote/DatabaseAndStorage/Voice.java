package com.example.wuke.flashnote.DatabaseAndStorage;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by recur on 2018/3/27.
 */

public class Voice {
    private int voiceID;
    private int userID;
    private String url;//file position
    private int priority;
    private int color;
    private String timestamp;

    public Voice(int voiceID, int userID, String url, int color, String timestamp,int priority)
    {
        this.voiceID=voiceID;
        this.userID=userID;
        this.url=url;
        this.color=color;
        this.timestamp=timestamp;
        this.priority=priority;
    }

    public Voice(int userID, String url, int color, Timestamp timestamp,int priority)
    {
        this.userID=userID;
        this.url=url;
        this.color=color;
        this.timestamp=timestamp.toString();
        this.priority=priority;
    }


    public int getVoiceID()
    {
        return voiceID;
    }
    public int getUserID()
    {
        return userID;
    }

    public String getURL()
    {
        return url;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public int getColor()
    {
        return color;
    }

    public int getPriority()
    {
        return priority;
    }
}
