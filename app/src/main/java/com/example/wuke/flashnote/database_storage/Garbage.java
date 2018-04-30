package com.example.wuke.flashnote.database_storage;

/**
 * Created by recur on 2018/4/23.
 */

public class Garbage {
    private int  GID;
    private int  UID;
    private int  NID;

    public Garbage(int GID,int UID,int NID)
    {
        this.GID=GID;
        this.UID=UID;
        this.NID=NID;
    }

    public int getGID()
    {
        return this.GID;
    }
    public int UID()
    {
        return this.UID;
    }
    public int NID(){return this.NID;}


}
