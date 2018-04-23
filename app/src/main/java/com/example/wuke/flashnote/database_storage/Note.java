package com.example.wuke.flashnote.database_storage;

import java.sql.Timestamp;

/**
 * Created by recur on 2018/3/25.
 */

public class Note {
    private int noteID=-1;//auto
    private int userID;
    private String words; //text context
    private int color; // qiqiu color
    private String Timestamp;
    private int priority;//0 is high,1 is loweer

    public Note(int noteID, int userID, String words, int color, String timestamp,int priority)
    {
        this.noteID=noteID;
        this.userID=userID;
        this.words=words;
        this.color=color;
        this.Timestamp=timestamp;
        this.priority=priority;
    }

    public Note(int userID, String words, int color, String timestamp,int priority)
    {
        this.userID=userID;
        this.words=words;
        this.color=color;
        this.Timestamp=timestamp;
        this.priority=priority;
    }


    public int getNoteID()
    {
        return noteID;
    }

    public int getUserID()
    {
        return userID;
    }

    public String getWords()
    {
        return words;
    }

    public String getTimestamp()
    {
        return Timestamp;
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
