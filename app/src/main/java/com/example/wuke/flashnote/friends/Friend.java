package com.example.wuke.flashnote.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Friends;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.function.Datatransformer;
import com.example.wuke.flashnote.function.StringRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {

    private List mList;
    private static String TAG = FragmentActivity.class.getSimpleName();
    private DatabaseOperator dbo;
    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;
    private SharedPreferences pref;
    private android.support.design.widget.FloatingActionButton floatingActionButton;
    private TextView done;

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
                RotateAnimation ra = new RotateAnimation(0,90, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                ra.setDuration(500);
                view.startAnimation(ra);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Add_friend();
                    }
                }, 500);
            }
        });

        done = (TextView) findViewById(R.id.friend_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ItemTouchHelper.Callback callback = new RecycleItemTouchHelperFriend(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void Add_friend() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(Friend.this)
                .setTitle(getString(R.string.add_friend))
                .setView(editText)
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input 为创建的内容
                        String input = editText.getText().toString();
                        dbo = new DatabaseOperator(Friend.this);
                        int fid = dbo.finduser(input);
                        if(fid == -1){
                            Toast.makeText(getApplication(), getString(R.string.no_friend), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            pref=getSharedPreferences("info",MODE_PRIVATE);
                            int userid=pref.getInt("userid",0);
                            Log.e("friend",userid+" "+fid);
                            Uploading up=new Uploading();
                            up.upfriend(String.valueOf(userid),String.valueOf(fid));
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
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