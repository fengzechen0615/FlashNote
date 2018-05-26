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
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.download_upload.Downloading;
import com.example.wuke.flashnote.record.Record;
import com.example.wuke.flashnote.friends.GetName;

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
        dl.downsharedvoice(String.valueOf(userid));
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("moment",dl.notes.size()+""+dl.voices.size());
                test=dl.notes;
                test.addAll(dl.voices);
                Iterator iterator=test.iterator();
                while(iterator.hasNext()) {
                    Storage s = (Storage) iterator.next();
                    final GetName getname=new GetName();
                    if (s instanceof Note){
                        String str = String.valueOf(((Note)s).getUserID());
                        getname.getname(str);
                        Log.e("moment",((Note)s).getWords());
                        MomentDetail m=new MomentDetail(getname.getname,
                                ((Note)s).getWords(),
                                ((Note)s).getTimestamp(),0);
                        Log.e("moment",m.getMoment_content());
                        mList.add(m);
                    }
                    else if (s instanceof Voice){
                        String str = String.valueOf(((Voice)s).getUserID());
                        getname.getname(str);
                        Log.e("moment",((Voice)s).getURL());
                        MomentDetail m=new MomentDetail(getname.getname,
                                "语音信息，点击收听",
                                ((Voice)s).getTimestamp(),1);
                        Log.e("moment",m.getMoment_content());
                        mList.add(m);
                    }

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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mList.clear();
                                test=dl.notes;
                                test.addAll(dl.voices);
                                Iterator iterator=test.iterator();
                                while(iterator.hasNext()) {
                                    Storage s = (Storage) iterator.next();
                                    GetName getname=new GetName();
                                    if (s instanceof Note){
                                        String str = String.valueOf(((Note)s).getUserID());
                                        getname.getname(str);
                                        MomentDetail m=new MomentDetail(getname.getname,
                                                ((Note)s).getWords(),
                                                ((Note)s).getTimestamp(),0);
                                        mList.add(m);
                                    }
                                    else if (s instanceof Voice){
                                        String str = String.valueOf(((Voice)s).getUserID());
                                        getname.getname(str);
                                        MomentDetail m=new MomentDetail(getname.getname,
                                                "语音信息，点击收听",
                                                ((Voice)s).getTimestamp(),1);
                                        mList.add(m);
                                    }
                                }
                                mAdapter = new MomentAdapter(Moment.this, mList);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        },500);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Record record = new Record(Moment.this);
        record.destory();
    }
}
