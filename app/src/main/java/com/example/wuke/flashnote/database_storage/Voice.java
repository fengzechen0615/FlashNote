package com.example.wuke.flashnote.database_storage;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by recur on 2018/3/27.
 */

public class Voice extends Storage implements Serializable{
    private int voiceID;
    private int userID;
    private String url;//file position
    private int priority;
    private int color;
    private String timestamp;
    private int dataType;

    public Voice(int voiceID, int userID, String file, int color, String timestamp,int priority,int dataType)
    {
        this.voiceID=voiceID;
        this.userID=userID;
        this.url=file;
        this.color=color;
        this.timestamp=timestamp;
        this.priority=priority;
        this.dataType=dataType;
    }

    public Voice(int userID, String url, int color, String timestamp,int priority,int dataType)
    {
        this.userID=userID;
        this.url=url;
        this.color=color;
        this.timestamp=timestamp.toString();
        this.priority=priority;
        this.dataType=dataType;
    }

    public Voice(){

    }

    public String getFile() {
        return url;
    }

    public void setFile(String file) {
        this.url = file;
    }

    public String getURL() {
        return url;
    }

    public int getVoiceID()
    {
        return voiceID;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public int getUserID()
    {
        return userID;
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

    public void setVoiceID(int i) {
        voiceID = i;
    }

    public void setUserID(int i) {
        userID = i;
    }

    public void setTimestamp(String i) {
        timestamp = i;
    }

    public void setColor(int i) {
        color = i;
    }

    public void setPriority(int i) {
        priority = i;
    }

    public int getDataType() {
        return dataType;
    }
}
