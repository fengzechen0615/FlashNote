package com.example.wuke.flashnote.database_storage;

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
    public static final String note_color="Note_color";
    public static final String note_timestamp="Note_timestamp";
    public static final String note_priority="Note_priority";
    public static final String datatype="Note_datatype";
    public static final String voice_id="Voice_id";
    public static final String voice_users="User_id";
    public static final String voice_duration="Voice_Duration";
    public static final String voice_color="Voice_color";
    public static final String voice_timestamp="Voice_timestamp";
    public static final String voice_file="Voice_file";
    public static final String voice_priority="Voice_priority";

    // shared table
    public static final String shared_userid="User_id";
    public static final String shared_noteid="Note_id";
    public static final String shared_Voiceid="Voice_id";

    //Garbage
    public static final String Garbage_table ="Garbage";
    public static final String Litter_id ="Litter_id";
    public static final String garbage_datatype="datatype";
    public static final String content_id = "content_id";
    public static final String guser_id="User_id";
    public static final String keywords="keywords";
    public static final String previous_color="color";
    public static final String previous_timestamp="timestamp";
    public static final String previous_priority="priority";
    public static final String extra="extra";


}
