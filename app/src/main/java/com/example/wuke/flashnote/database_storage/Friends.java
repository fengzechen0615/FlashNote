package com.example.wuke.flashnote.database_storage;

import com.example.wuke.flashnote.friends.Friend;

/**
 * Created by recur on 2018/3/27.
 */

public class Friends {
    private int userID;
    private int friendsID;

    public Friends (int FID,int UID)
    {
        this.friendsID=FID;
        this.userID=UID;
    }

    public int getUserID(){
        return userID;
    }
    public int getFriendsID(){
        return friendsID;
    }
}
