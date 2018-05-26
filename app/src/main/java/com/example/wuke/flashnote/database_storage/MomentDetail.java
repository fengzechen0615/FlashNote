package com.example.wuke.flashnote.database_storage;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class MomentDetail implements Comparator{

    private String moment_username;
    private String moment_content;
    private String moment_time;
    private int data_type;
//    private String URL;

    // text
    public MomentDetail(String moment_username, String moment_content, String moment_time, int data_type) {
        this.moment_username = moment_username;
        this.moment_content = moment_content;
        this.moment_time = moment_time;
        this.data_type = data_type;
    }

    // voice
//    public MomentDetail(String moment_username, String URL, int data_type, String moment_time) {
//        this.moment_username = moment_username;
//        this.URL = URL;
//        this.moment_time = moment_time;
//        this.data_type = data_type;
//    }

    public String getMoment_content() {
        return moment_content;
    }

    public String getMoment_time() {
        return moment_time;
    }

    public String getMoment_username() {
        return moment_username;
    }

    public int getData_type() {
        return data_type;
    }

//    public String getURL() {
//        return URL;
//    }

    @Override
    public int compare(Object o1, Object o2) {
        String u1 = ((MomentDetail) o1).getMoment_time();
        String u2 = ((MomentDetail) o2).getMoment_time();

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=form.parse(u1,new ParsePosition(0));
        Date d2=form.parse(u2,new ParsePosition(0));
        if(d1.before(d2)==true) {
            return 1;
        } else {
            return -1;
        }
    }
}
