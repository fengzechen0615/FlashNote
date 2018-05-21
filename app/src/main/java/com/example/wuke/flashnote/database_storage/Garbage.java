package com.example.wuke.flashnote.database_storage;

/**
 * Created by recur on 2018/4/23.
 */

public class Garbage {
    private int  Litter_id;
    private int Content_id;
    private int  datatype;
    private int guser_id;
    private String keywords;
    private String previous_timestamp;
    private int previous_color;
    private int previous_priority;
    private int extra;

    public Garbage(int LID,int CID,int datatype,int guser_id,String key,String pt,int pc,int pp,int extra)
    {
        this.Litter_id=LID;
        this.Content_id=CID;
        this.datatype=datatype;
        this.guser_id=guser_id;
        this.keywords=key;
        this.previous_timestamp=pt;
        this.previous_color=pc;
        this.previous_priority=pp;
        this.extra=extra;
    }

    public Garbage(int CID,int datatype,int guser_id,String key,String pt,int pc,int pp,int extra)
    {
        this.Content_id=CID;
        this.datatype=datatype;
        this.guser_id=guser_id;
        this.keywords=key;
        this.previous_timestamp=pt;
        this.previous_color=pc;
        this.previous_priority=pp;
        this.extra = extra;
    }

    public int getLitter_id(){
        return Litter_id;
    }

    public int getContent_id(){
        return Content_id;
    }

    public int getDatatype(){
        return datatype;
    }

    public int getGuser_id(){
        return guser_id;
    }

    public String getKeywords(){
        return keywords;
    }

    public String getPrevious_timestamp(){
        return previous_timestamp;
    }

    public int getPrevious_color(){
        return previous_color;
    }

    public int getPrevious_priority(){
        return previous_priority;
    }

    public int getExtra() {return extra;}


}
