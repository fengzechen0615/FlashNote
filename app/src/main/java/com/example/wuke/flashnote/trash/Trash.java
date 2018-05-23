package com.example.wuke.flashnote.trash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.NoteActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Friends;
import com.example.wuke.flashnote.database_storage.Garbage;
import com.example.wuke.flashnote.friends.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class Trash extends AppCompatActivity {

    private List mList;
    private TrashAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView done;
    private DatabaseOperator db;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        NoteActivity noteActivity = new NoteActivity();
        noteActivity.mNoteActivity.finish();

        init_list();

        done = (TextView) findViewById(R.id.trash_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    private void init_list() {
        if(mList == null){
            mList = new ArrayList<Garbage>();
            userid=getIntent().getIntExtra("userid",0);
            Log.e("g",userid+"");
            db = new DatabaseOperator(this);
            mList = db.getGarbage(userid);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.trash_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TrashAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}
