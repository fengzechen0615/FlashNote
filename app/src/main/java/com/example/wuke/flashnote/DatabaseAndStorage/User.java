package com.example.wuke.flashnote.DatabaseAndStorage;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by recur on 2018/3/25.
 */

public class User {
    private int userID=-1;
    private String username;
    private String password;
    private String latestLogin;
    private int capacity_words;
    private int capacity_voice;

    public User(int userID, String username, String password, String latestLogin,int capacity_words,int capacity_voice)
    {
        this.userID=userID;
        this.username=username;
        this.password=password;
        this.latestLogin=latestLogin;
        this.capacity_words=capacity_words;
        this.capacity_voice=capacity_voice;
    }

    public User(String username, String password, Timestamp latestLogin,int capacity_words,int capacity_voice)
    {
        this.username=username;
        this.password=password;
        this.latestLogin=latestLogin.toString();
        this.capacity_words=capacity_words;
        this.capacity_voice=capacity_voice;
    }



    public int getUserID()
    {
        return userID;
    }

    public String getusername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getLatestLogin()
    {
        return latestLogin;
    }

    public int getCapacity_words()
    {
        return capacity_words;
    }

    public int getCapacity_voice()
    {
        return capacity_voice;
    }

    private String list; //好友列表，


}
