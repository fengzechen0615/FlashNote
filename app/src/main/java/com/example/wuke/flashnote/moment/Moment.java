package com.example.wuke.flashnote.moment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.MomentDetail;

import java.util.ArrayList;
import java.util.List;

public class Moment extends AppCompatActivity {

    private List mList;
    private TextView done;
    private MomentAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monent);

        done = (TextView) findViewById(R.id.moment_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        init_list();
    }

    private void init_list() {
        mList = new ArrayList<Moment>();
//        init_data();

        mRecyclerView = (RecyclerView) findViewById(R.id.moment_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MomentAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        if (mList.size() != 0) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    private void init_data() {
        MomentDetail momentDetail_0 = new MomentDetail("Feng Zechen", "Have launch with WeiYuan", "2015-4-27 12:00:00");
        mList.add(momentDetail_0);
        MomentDetail momentDetail_1 = new MomentDetail("Wang Yikai", "I Love WeiYuan", "2015-4-27 13:00:00");
        mList.add(momentDetail_1);
        MomentDetail momentDetail_2 = new MomentDetail("Gong Linghua", "I fall in Love with WeiYuan", "2015-4-27 13:00:00");
        mList.add(momentDetail_2);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
