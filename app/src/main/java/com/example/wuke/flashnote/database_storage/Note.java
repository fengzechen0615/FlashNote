package com.example.wuke.flashnote.database_storage;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

/*
 * Created by recur on 2018/3/25.
 */

public class Note extends Storage implements Serializable {
    private int noteID;//auto
    private int userID;
    private String words; //text context
    private int color; // qiqiu color
    private String Timestamp;
    private int priority;//0 is high,1 is loweer
    private int dataType;

    public Note(int noteID, int userID, String words, int color, String timestamp, int priority, int dataType)
    {
        this.noteID = noteID;
        this.userID = userID;
        this.words = words;
        this.color = color;
        this.Timestamp = timestamp;
        this.priority = priority;
        this.dataType = dataType;
    }

    public Note(int userID, String words, int color, String timestamp, int priority, int dataType)
    {
        this.userID = userID;
        this.words = words;
        this.color = color;
        this.Timestamp = timestamp;
        this.priority = priority;
        this.dataType = dataType;
    }


    public Note(){

    }

    public int getDataType() {
        return dataType;
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

    public void setNoteID(int i) {
        noteID = i;
    }

    public void setUserID(int i) {
        userID = i;
    }

    public void setWords(String i) {
        words = i;
    }

    public void setTimestamp(String i) {
        Timestamp = i;
    }

    public void setColor(int i) {
        color = i;
    }

    public void setPriority(int i) {
        priority = i;
    }
}
