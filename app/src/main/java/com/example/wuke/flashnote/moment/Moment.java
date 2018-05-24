package com.example.wuke.flashnote.moment;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.MomentDetail;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Storage;
import com.example.wuke.flashnote.download_upload.Downloading;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Moment extends AppCompatActivity {

    private List mList;
    private TextView done;
    private MomentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SharedPreferences pref;
    private int userid;
    private List test;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monent);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_list();
            }
        });

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
        pref=getSharedPreferences("info",MODE_PRIVATE);
        userid=pref.getInt("userid",0);
        final Downloading dl=new Downloading();
        dl.downsharednote(String.valueOf(userid));
        Log.e("share",userid+"");
        Log.e("share",dl.notes.size()+"");
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                test=dl.notes;
                Iterator iterator=test.iterator();
                while(iterator.hasNext()) {
                    Note note = (Note)iterator.next();
                    MomentDetail m=new MomentDetail(note.getNoteID()+"",note.getWords(),note.getTimestamp());
                    mList.add(m);
                }
                mAdapter = new MomentAdapter(Moment.this, mList);
                mRecyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        },1000);


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
                        final Downloading dl=new Downloading();
                        dl.downsharednote(String.valueOf(userid));
                        Log.e("share",userid+"");
                        Log.e("share",dl.notes.size()+"");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mList.clear();
                                test=dl.notes;
                                Iterator iterator=test.iterator();
                                while(iterator.hasNext()) {
                                    Note note = (Note)iterator.next();
                                    MomentDetail m=new MomentDetail(note.getNoteID()+"",note.getWords(),note.getTimestamp());
                                    mList.add(m);
                                }
                                mAdapter = new MomentAdapter(Moment.this, mList);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        },1000);
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
