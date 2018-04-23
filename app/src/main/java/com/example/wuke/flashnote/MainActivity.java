package com.example.wuke.flashnote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Sync;
import com.example.wuke.flashnote.login.Locallogin;
import com.example.wuke.flashnote.login.Login;
import com.example.wuke.flashnote.setting.Setting;
import com.example.wuke.flashnote.util.AddNote;
import com.example.wuke.flashnote.util.NoteAdapter;

import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by francisfeng on 21/03/2018.
 */

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private DatabaseOperator dbo;
    private DrawerLayout drawerLayout;
    private String time=null;
    private String username=null;
    private NoteAdapter myAdapter;
    private List<Note> list;

    @SuppressLint({"ShowToast", "ClickableViewAccessibility"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if(time!=null &&username!=null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("Bundle");
            time = bundle.getString("time");
            username = bundle.getString("username");
        }
        else
        {
            Timestamp nowTime=new Timestamp(System.currentTimeMillis());//Login Time
            SimpleDateFormat form=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time=form.format(nowTime);
            username="administrator";
        }
        // 侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 侧滑菜单功能
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestPermissions();

        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.username);

        Locallogin locallogin = new Locallogin();

        if(locallogin.check() == true) {
            String[] user = locallogin.getaccount();
            username.setText(user[0]);
        } else {
            username.setText("FlashNote");
        }

        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                startActivity(intent);
            }
        });


        //select test
        dbo = new DatabaseOperator(this);
        list = new ArrayList();
        list = dbo.getAllNote();
        // 事件气泡
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        myAdapter = new NoteAdapter(list);
        if (list.size() != 0) {
            mRecyclerView.smoothScrollToPosition(list.size() - 1);
        }
        mRecyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onResume() {

        super.onResume();

        dbo = new DatabaseOperator(this);
        list = dbo.getAllNote();
        // 事件气泡
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        myAdapter = new NoteAdapter(list);
        myAdapter.notifyItemInserted(list.size() - 1);
        mRecyclerView.scrollToPosition(list.size() - 1);
        mRecyclerView.setAdapter(myAdapter);
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intents = new Intent(MainActivity.this, Setting.class);
            startActivity(intents);
        } else if (item.getItemId() == R.id.log_out) {
            Locallogin in = new Locallogin();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            in.delete();
        } else if (item.getItemId() == R.id.log_in) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.update) {
            if (time!=null) {
                //Log.e("sync", time);
                HashMap map = Sync.CompareTimestamp(time, list);
                ArrayList before = (ArrayList<Note>) map.get("Before");//verify
                ArrayList After = (ArrayList<Note>) map.get("After");//new content
            }
            else
                Log.e("sync", "Empty");

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
