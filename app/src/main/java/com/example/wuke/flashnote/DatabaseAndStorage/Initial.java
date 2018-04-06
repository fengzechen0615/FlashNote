package com.example.wuke.flashnote.DatabaseAndStorage;

/**
 * Created by recur on 2018/4/6.
 */

public class Initial {
    //five tables
    public static final String table_friend = "Friends";
    public static final String table_shared = "Shared";
    public static final String table_note = "Note";
    public static final String table_voice = "Voice";
    public static final String table_user = "User";
    //User table
    public static final String user_id="User_id";
    public static final String username="Username";
    public static final String user_password="Password";
    public static final String user_Lastlogin="LastLogin";
    public static final String user_capacity_words="Capacity_words";
    public static final String user_capacity_voice="Capacity_voice";
    public static final String user_list="List";
    //friends table
    public static final String friend_user_id = "User_id";
    public static final String friend_fid = "Friend_id";
    //note table
    public static final String note_id = "Note_id";
    public static final String note_user="User_id";
    public static final String note_words="Words";
    public static final String note_color="Note_olor";
    public static final String note_timestamp="Note_timestamp";
    public static final String note_priority="Note_priority";
    //voice table
    public static final String voice_id="Voice_id";
    public static final String voice_users="User_id";
    public static final String voice_url="URL";
    public static final String voice_color="Voice_color";
    public static final String voice_timestamp="Voice_timestamp";
    public static final String voice_priority="Voice_priority";

    // shared table
    public static final String shared_userid="User_id";
    public static final String shared_noteid="Note_id";
    public static final String shared_Voiceid="Voice_id";
}
