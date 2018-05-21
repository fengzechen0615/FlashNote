package com.example.wuke.flashnote.friends;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Friends;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {

    private List mList;
    private static String TAG = FragmentActivity.class.getSimpleName();
    private DatabaseOperator dbo;
    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;

    private android.support.design.widget.FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        init_List();
    }

    private void init_List() {
        if(mList == null){
            mList = new ArrayList<Friends>();
            // 测试数据
            init_data();
//            dbo = new DatabaseOperator(this);
//            mList = dbo.getAllFriends();
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.friend_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FriendAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        floatingActionButton = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add_friends);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dage", "pi");
            }
        });

        ItemTouchHelper.Callback callback = new RecycleItemTouchHelperFriend(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void init_data() {
        Friends friends_1 = new Friends(1, 2);
        mList.add(friends_1);
        Friends friends_2 = new Friends(3, 4);
        mList.add(friends_2);
        Friends friends_3 = new Friends(5, 6);
        mList.add(friends_3);
        Friends friends_4 = new Friends(5, 6);
        mList.add(friends_4);
        Friends friends_5 = new Friends(5, 6);
        mList.add(friends_5);
        Friends friends_6 = new Friends(5, 6);
        mList.add(friends_6);
        Friends friends_7 = new Friends(5, 6);
        mList.add(friends_7);
        Friends friends_8 = new Friends(5, 6);
        mList.add(friends_8);
        Friends friends_9 = new Friends(5, 6);
        mList.add(friends_9);

    }

}