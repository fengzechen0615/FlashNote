package com.example.wuke.flashnote.trash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        init_list();

        done = (TextView) findViewById(R.id.trash_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init_list() {
        if(mList == null){
            mList = new ArrayList<Garbage>();
            db = new DatabaseOperator(this);
            mList = db.getGarbage();
            // 测试数据
//            init_data();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.trash_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TrashAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

    }

}
