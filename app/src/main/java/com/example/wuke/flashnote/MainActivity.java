package com.example.wuke.flashnote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuke.flashnote.database_storage.Storage;
import com.example.wuke.flashnote.database_storage.Voice;
import com.example.wuke.flashnote.download_upload.Deleting;
import com.example.wuke.flashnote.friends.Friend;
import com.example.wuke.flashnote.recyclerview.RecycleItemTouchHelper;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.database_storage.Sync;
import com.example.wuke.flashnote.login.Locallogin;
import com.example.wuke.flashnote.setting.Setting;
import com.example.wuke.flashnote.recyclerview.NoteAdapter;
import com.example.wuke.flashnote.util.JsonParser;
import com.example.wuke.flashnote.download_upload.Uploading;
import com.example.wuke.flashnote.download_upload.Downloading;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    private EditText mResultText;
    private EditText mResultVoice;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences pref;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private int dt=-1;

    int ret = 0;

    private RecyclerView mRecyclerView;
    private DatabaseOperator dbo;
    private DrawerLayout drawerLayout;
    private String time = null;
    private String username = null;
    private NoteAdapter mAdapter;
    private List list;

    private String time_record;
    private String time_stamp;

    private int voice = 0;

    //存储很多张话筒图片的数组
    private Drawable[] micImages;
    //话筒的图片
    private ImageView micImage;
    private RelativeLayout recordingContainer;
    private TextView recordingHint;

    private RelativeLayout recording;
    private ImageButton speak;

    private String topic = null;


    @SuppressLint({"ShowToast", "ClickableViewAccessibility"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);

        mSharedPreferences = getSharedPreferences(Setting.PREFER_NAME, Activity.MODE_PRIVATE);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = formatter.format(timestamp);
        Log.e("gtime1",time);

        // 初始化列表
        init_List();
        // 初始化布局
        init_View();
    }

    private void init_List() {
        if(list == null){
            list = new ArrayList<Storage>();
            dbo = new DatabaseOperator(this);
            list = dbo.getAllStorage();
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NoteAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        if (list.size() != 0) {
            mRecyclerView.smoothScrollToPosition(list.size() - 1);
        }

        ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);  //用Callback构造ItemtouchHelper
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void init_View() {
        // 侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 侧滑菜单功能
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestPermissions();

        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.username);

        speak = (ImageButton) findViewById(R.id.speak);
        recording = (RelativeLayout) findViewById(R.id.recording);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingContainer = (RelativeLayout) findViewById(R.id.recording_container);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14), };

        Locallogin locallogin = new Locallogin();

        if(locallogin.check() == true) {
            String[] user = locallogin.getaccount();
            username.setText(user[0]);
        } else {
            username.setText(getString(R.string.app_name));
        }

        final FloatingActionMenu add = (FloatingActionMenu) findViewById(R.id.add);

        FloatingActionButton add_text = (FloatingActionButton) findViewById(R.id.fab_text);
        FloatingActionButton add_voice = (FloatingActionButton) findViewById(R.id.fab_voice);

        add.setClosedOnTouchOutside(true);

        add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_text_note();
                recording.setVisibility(View.INVISIBLE);
                add.close(true);
            }
        });
        add_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_voice_note();
                add.close(true);
            }
        });
    }

    private void Add_text_note() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        Window dialogWindow = dialog.getWindow();

        View view = View.inflate(this, R.layout.add_text_note, null);
        dialog.setView(view, 0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        dialog.getWindow().setDimAmount(0);

        final ImageButton create = (ImageButton) view.findViewById(R.id.create);
        mResultText = ((EditText) view.findViewById(R.id.text));

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote(mResultText.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Add_voice_note() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.add_voice_note, null);

        mResultVoice = ((EditText) view.findViewById(R.id.voice));

        mResultVoice.setFocusable(false);
        mResultVoice.setFocusableInTouchMode(false);
        mResultVoice.setVisibility(View.GONE);

        recording.setVisibility(View.VISIBLE);

        speak.setOnTouchListener(new View.OnTouchListener() {
            float DownY;
            float MoveY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
                        SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        time_record = form.format(timestamp);
                        time_stamp = formatter.format(timestamp);
                        start_speak();
                        UpdateMicStatus();
                        DownY = event.getY();
                        Log.d("DownY", String.valueOf(DownY));
                        recordingContainer.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        float y = Math.abs(MoveY - DownY);
                        if (y > 300) {
                            stop_speak();
                            createVoice(mResultVoice.getText().toString(), 1);
                            Log.d("Delete_record", "you cancel this message");
                        } else {
                            Log.d("add_message", "you add this message");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stop_speak();
                                    createVoice(mResultVoice.getText().toString(), 0);
                                }
                            }, 1000);
                        }
                        dialog.dismiss();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        recording.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getY() < 0) {
                            recordingHint.setText(getString(R.string.loose));
                            recordingHint.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);
                            stop_speak();
                            MoveY = event.getY();
                            Log.d("MoveY", String.valueOf(MoveY));
                        } else {
                            recordingHint.setText(getString(R.string.slide_cancel));
                            recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void start_speak() {
        FlowerCollector.onEvent(MainActivity.this, "iat_recognize");
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
//                showTip("听写失败,错误码：" + ret);
            } else {
//                showTip(getString(R.string.text_begin));
            }
        }
    }

    private void stop_speak() {
        mIat.stopListening();
//        showTip("停止听写");
    }

    // create note
    private void createNote(String note) {
        DatabaseOperator dbo = new DatabaseOperator(MainActivity.this);
        String content = note;
        if (!"".equals(content)) {
            // 触发淘宝条件
            if (note.contains("淘宝") && note.contains("搜索")) {
                TaobaoDialog();
            }
            // 触发日历条件
            else if (note.contains("日历") && note.contains("创建")) {
                CalendarDialog();
            }
            else if (note.contains("打开微信")) {
            }
            else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                String time = form.format(timestamp);
                pref = getSharedPreferences("info", MODE_PRIVATE);
                int userid = pref.getInt("userid", 0);
                // 插入priority
                Note newnote = new Note(userid, note, 0, time, list.size(), 0);
                int i = dbo.InsertNote(newnote);
                Log.d("i", String.valueOf(newnote.getDataType()));
                newnote.setNoteID(i);
                list.add(newnote);
                mAdapter.notifyItemInserted(list.size() - 1);
                mRecyclerView.scrollToPosition(list.size() - 1);
            }
        }
    }

    private void createTopic() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("New Voice Note")
                .setView(editText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topic = editText.getText().toString();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav");
                        file.delete();
                    }
                }).show();
    }

    // create voice
    private void createVoice(String note, int i) {
        if (i == 0) {
            Log.d("i", "message");
            dbo  = new DatabaseOperator(MainActivity.this);
            String content = note;
            if (!"".equals(content)) {
//              触发淘宝条件
                if (note.contains("淘宝") && note.contains("搜索")) {
                    TaobaoDialog();
//                    new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav").delete();
                }
                // 触发日历条件
                else if (note.contains("日历") && note.contains("创建")) {
                    CalendarDialog();
//                    new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav").delete();
                } else if (note.contains("打开微信")) {
//                    new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav").delete();
                } else {
                    pref = getSharedPreferences("info", MODE_PRIVATE);
                    int user_id = pref.getInt("userid", 0);
//                    createTopic();
                    // 插入priority list.size
                    Voice voice = new Voice(user_id, new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav").getAbsolutePath(), 0, time_stamp, list.size(), 1, "topic");
                    list.add(voice);
                    dbo = new DatabaseOperator(MainActivity.this);
                    dbo.InsertVoice(voice);
                    mAdapter.notifyItemInserted(list.size() - 1);
                    mRecyclerView.scrollToPosition(list.size() - 1);

                }
            }
        }
        else if (i == 1) {
            File file = new File(Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav");
            file.delete();
            Log.d("delete", "Delete");
        }
    }

    private void TaobaoDialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Open Taobao to Search")
                .setView(editText)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input 为搜索的内容
                        String input = editText.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void CalendarDialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Open Calendar to Create a event")
                .setView(editText)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input 为创建的内容
                        String input = editText.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void WechatDialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Open Wechat to send a message")
                .setView(editText)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input 为发送的内容
                        String input = editText.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
//            showTip("Start speaking");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
//            showTip("End the talk");
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
//            showTip("Speaking, volume: " + volume);
            Log.d("volume", String.valueOf(volume));
            voice = volume;
//            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    // 声音大小监控
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what=msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            if(what>13){
                what=13;
            }
            micImage.setImageDrawable(micImages[what]);
        };
    };

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            UpdateMicStatus();
        }
    };

    private int SPACE = 200;

    private void UpdateMicStatus() {
        mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        mHandler.sendEmptyMessage(voice / 2);
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

        mResultVoice.setText(resultBuffer.toString());
        mResultVoice.setSelection(mResultVoice.length());
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
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/" + time_record + ".wav");
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER | Gravity.BOTTOM;
        getWindowManager().updateViewLayout(view, lp);
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
        } else if(item.getItemId()==R.id.friends) {
            Intent intent = new Intent(MainActivity.this, Friend.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.trash) {

        } else if (item.getItemId() == R.id.update) {
            if (time!=null) {
                pref=getSharedPreferences("info",MODE_PRIVATE);
                String newtime=pref.getString("time","0");
                Log.e("gtime2",newtime);
                HashMap map = Sync.CompareTimestamp(newtime, list);
                ArrayList before = (ArrayList<Note>) map.get("Before");//verify
                ArrayList After = (ArrayList<Note>) map.get("After");//new content,upload to server
                ArrayList Delete= (ArrayList) mAdapter.getDelete_List();
                Uploading uploading=new Uploading();
                Deleting d=new Deleting();
                uploading.uploadnote(After);
                d.deletenote(Delete);
                final Downloading dl=new Downloading();

                int userid=pref.getInt("userid",0);
                dl.downnote(String.valueOf(userid));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(dl.notes!=null) {
                            ArrayList<Note> test=dl.notes;
                        }
                        else {
                            mToast = Toast.makeText(MainActivity.this, "No", Toast.LENGTH_LONG);
                            mToast.show();
                        }
                    }
                },1000);
                Timestamp timestamp=new Timestamp(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String retime = formatter.format(timestamp);
                pref.edit().putString("time",retime);
                Log.e("gtime3",retime);
            }

            else
                Log.e("sync", "Empty");

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
