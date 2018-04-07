package com.example.wuke.flashnote.DatabaseAndStorage;

import java.sql.Date;

/**
 * Created by recur on 2018/3/25.
 */

public class Note {
    private int noteID;//auto
    private int userID;
    private String words; //text context
    private String color; // qiqiu color
    private Date Timestamp;
    private int priority;//0 is high,1 is loweer
}
