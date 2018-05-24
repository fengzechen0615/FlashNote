package com.example.wuke.flashnote.database_storage;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class MomentDetail implements Comparator{

    private String moment_username;
    private String moment_content;
    private String moment_time;

    public MomentDetail(String moment_username, String moment_content, String moment_time) {
        this.moment_username = moment_username;
        this.moment_content = moment_content;
        this.moment_time = moment_time;
    }

    public String getMoment_content() {
        return moment_content;
    }

    public String getMoment_time() {
        return moment_time;
    }

    public String getMoment_username() {
        return moment_username;
    }

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
