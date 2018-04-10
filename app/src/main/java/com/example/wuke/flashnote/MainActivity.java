package com.example.wuke.flashnote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.function.Calendar_a;
import com.example.wuke.flashnote.function.MessageVector;
import com.example.wuke.flashnote.function.Taobao;
import com.example.wuke.flashnote.function.Wechat;
import com.example.wuke.flashnote.setting.Setting;
import com.example.wuke.flashnote.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Calendar;
import java.util.List;

/**
 * Created by francisfeng on 21/03/2018.
 */

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private ListView mListView;
    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private DatabaseOperator dbo;
    private DrawerLayout drawerLayout;

    int ret = 0;
    int count = 1;

    @SuppressLint({"ShowToast", "ClickableViewAccessibility"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 侧滑菜单功能
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestPermissions();

        // 初始化识别无UI识别对象
        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);

        mSharedPreferences = getSharedPreferences(Setting.PREFER_NAME,
                Activity.MODE_PRIVATE);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//      Log.e("mark","mark");
        mResultText = ((EditText) findViewById(R.id.text));

        final Button speak = (Button) findViewById(R.id.speak);
        final Button create = (Button) findViewById(R.id.create);
        speak.setVisibility(View.VISIBLE);
        create.setVisibility(View.GONE);
        mResultText.setFocusable(false);
        mResultText.setFocusableInTouchMode(false);

        final Button change = (Button) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                if (count % 2 != 0) {
                    speak.setVisibility(View.VISIBLE);
                    create.setVisibility(View.GONE);
                    mResultText.setFocusable(false);
                    mResultText.setFocusableInTouchMode(false);
                } else {
                    speak.setVisibility(View.GONE);
                    create.setVisibility(View.VISIBLE);
                    mResultText.setFocusable(true);
                    mResultText.setFocusableInTouchMode(true);
                    mResultText.requestFocus();
                }
            }
        });


        speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start_speak();
                        break;
                    case MotionEvent.ACTION_UP:
                        stop_speak();
                        alertDialog();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        //select test
        dbo = new DatabaseOperator(this);
        List list = new ArrayList();
        list = dbo.getAllNote();
//        Log.d("list",list.get(0).toString());
        mListView = findViewById(R.id.list);
        Adapter myAdapter = new NoteAdapter(this, list, R.layout.item);
        mListView.setAdapter((ListAdapter) myAdapter);
        //select finish

    }

    private void start_speak() {
        FlowerCollector.onEvent(MainActivity.this, "iat_recognize");
//        mResultText.setText(null);// 清空显示内容
//      Log.println(1,"mark","mark1");
        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), false);
        if (isShowDialog) {
            showTip(getString(R.string.text_begin));
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
                showTip(getString(R.string.text_begin));
            }
        }
    }

    private void stop_speak() {
        mIat.stopListening();
//        DatabaseOperator dbo=new DatabaseOperator(this);
//        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
//        Note newnote =new Note(1,mResultText.getText().toString(), Color.CYAN,timestamp,0);
//        dbo.InsertNote(newnote);
        showTip("停止听写");
    }

    private void alertDialog() {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Create Confirm")
            .setMessage("Are you confirm to create this?")
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Datatransform(mResultText.getText().toString());
                    DatabaseOperator dbo=new DatabaseOperator(MainActivity.this);
                    Timestamp timestamp=new Timestamp(System.currentTimeMillis());
                    Note newnote =new Note(1,mResultText.getText().toString(), Color.CYAN,timestamp,0);
                    dbo.InsertNote(newnote);
                    mResultText.setText(null);

                    List list = new ArrayList();
                    list = dbo.getAllNote();
//        Log.d("list",list.get(0).toString());
                    mListView = findViewById(R.id.list);
                    Adapter myAdapter = new NoteAdapter(MainActivity.this, list, R.layout.item);
                    mListView.setAdapter((ListAdapter) myAdapter);
//                    finish();
//                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                    startActivity(intent);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mResultText.setText(null);
                }
            }).show();
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

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
//            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
//            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
//            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    private void Datatransform(String command) {
        // TODO Auto-generated constructor stub
        MessageVector messageVector = new MessageVector();
//        System.out.println(command);
//		messageVector.printvector();

        //日历类
        if (command.contains("日历")||command.contains("calendar")||command.contains("Calendar")) {
            messageVector.set_value_a();

            if (command.contains("打开")||command.contains("Open")||command.contains("open")) {
                messageVector.set_value_1();
                messageVector.printvector();
                startActivity(Calendar_a.stratCalendar(messageVector));
            }

            else if (command.contains("创建")||command.contains("新建")||command.contains("Create")||command.contains("create")) {
                messageVector.set_value_2();
                messageVector.setItem(command);
                messageVector.printvector();

                long id = 1;

                ContentValues event = new ContentValues();
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.HOUR_OF_DAY, 11);
                mCalendar.set(Calendar.MINUTE, 45);
                long start = mCalendar.getTime().getTime();
                mCalendar.set(Calendar.HOUR_OF_DAY, 12);
                long end = mCalendar.getTime().getTime();

                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("hasAlarm", 1);
                event.put("calendar_id", id);
                event.put("title","New Event");
                event.put("description",command);
                event.put("eventLocation", "China");
                event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
                Uri eventsUri = Uri.parse("content://com.android.calendar/events");
                Uri url = getContentResolver().insert(eventsUri, event);
                startActivity(Calendar_a.insertCalendar(messageVector));
            }
            else {
                messageVector.set_value_a();
                startActivity(Calendar_a.stratCalendar(messageVector));
//				Interface_calender.operation_calender(messageVector);
            }
        }

        //微信类
        else if (command.contains("微信")||command.contains("wechat")) {
            messageVector.set_value_b();
            startActivity(Wechat.startWechat(messageVector));
        }

        //淘宝类
        else if (command.contains("淘宝")) {
            messageVector.set_value_c();
            startActivity(Taobao.startTaobao(messageVector));
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
//        System.out.println("mark3")

        String sn = null;
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mResultText.setText(resultBuffer.toString());
        mResultText.setSelection(mResultText.length());
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "5000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "10000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(MainActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(MainActivity.this);
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intents = new Intent(MainActivity.this, Setting.class);
            startActivity(intents);
        } else if (item.getItemId() == R.id.log_out) {
            Locallogin in = new Locallogin();
            in.delete();
            Intent intents = new Intent(MainActivity.this, Login.class);
            startActivity(intents);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
