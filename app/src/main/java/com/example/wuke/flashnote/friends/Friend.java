package com.example.wuke.flashnote.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.wuke.flashnote.download_upload.Downloading;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.function.Datatransformer;
import com.example.wuke.flashnote.function.StringRecognizer;
import com.example.wuke.flashnote.login.LocalLogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Friend extends AppCompatActivity {

    private List mList;
    private static String TAG = FragmentActivity.class.getSimpleName();
    public static boolean exist=false;
    private DatabaseOperator dbo;
    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;
    private SharedPreferences pref;
    private android.support.design.widget.FloatingActionButton floatingActionButton;
    private TextView done;
    private String username;
    private String input;
    public static boolean repeat=false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        pref=getSharedPreferences("info",MODE_PRIVATE);
        userid=pref.getInt("userid",0);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_f_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        init_List();
    }


    private void refresh_list() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final GetFriends getf=new GetFriends();
                        getf.getfriends(username);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mList.clear();
                                if (getf.list.size()!=0) {
                                    mList = getf.list;
                                    mAdapter = new FriendAdapter(Friend.this, mList);
                                    mAdapter.userid=String.valueOf(userid);
                                    mRecyclerView.setAdapter(mAdapter);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                else {
                                    Log.e("friends","no");
                                }
                            }
                        },100);
                    }
                });
            }
        }).start();
    }



    private void init_List() {
        mList = new ArrayList<Friends>();

        final GetFriends gf=new GetFriends();
        pref=getSharedPreferences("info",MODE_PRIVATE);
        userid=pref.getInt("userid",0);
        LocalLogin localLogin=new LocalLogin();
        if(localLogin.check() == true) {
            String[] user = localLogin.getaccount();
            username = user[0];
        } else {
            username = "Flashnote";
        }
        gf.getfriends(username);
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gf.list.size()!=0) {
                    mList = gf.list;
                    mRecyclerView = (RecyclerView) findViewById(R.id.friend_recycler_view);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Friend.this);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mAdapter = new FriendAdapter(Friend.this, mList);
                    mRecyclerView.setAdapter(mAdapter);
                    ItemTouchHelper.Callback callback = new RecycleItemTouchHelperFriend(mAdapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(mRecyclerView);
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    Log.e("friends","no");
                }
            }
        },1000);

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
                LocalLogin localLogin = new LocalLogin();

                if(localLogin.check() == true) {
                    String[] user = localLogin.getaccount();
                    username=user[0];
                } else {
                    username="Wuke";
                }
            }
        });

        done = (TextView) findViewById(R.id.friend_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                        input = editText.getText().toString();
                        Log.e("Friends",username+"  "+input);
                        Addfriend af=new Addfriend();
                        af.addfriend(username,input);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("ffff",String.valueOf(exist));
                                if (exist && !repeat){
                                    mList.add(input);
                                    mAdapter.notifyItemInserted(mList.size() - 1);
                                    mRecyclerView.scrollToPosition(mList.size() - 1);
                                }
                                else if(exist && repeat){
                                    Toast.makeText(getBaseContext(),"已添加",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getBaseContext(),"No this friends",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },100);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

}