package com.example.wuke.flashnote.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wuke.flashnote.MainActivity;
import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.database_storage.DatabaseOperator;
import com.example.wuke.flashnote.database_storage.Note;
import com.example.wuke.flashnote.function.Datatransformer;
import com.example.wuke.flashnote.setting.Setting;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddNote extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    int ret = 0;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mIat = SpeechRecognizer.createRecognizer(AddNote.this, mInitListener);

        mSharedPreferences = getSharedPreferences(Setting.PREFER_NAME, Activity.MODE_PRIVATE);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

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
    }

    private void start_speak() {
        FlowerCollector.onEvent(AddNote.this, "iat_recognize");
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
        showTip("停止听写");
    }

    private void alertDialog() {
        new AlertDialog.Builder(AddNote.this)
                .setTitle("Create Confirm")
                .setMessage("Are you confirm to create this?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Datatransformer datatransformer = new Datatransformer();
                        datatransformer.Datatransform(mResultText.getText().toString());
                        DatabaseOperator dbo=new DatabaseOperator(AddNote.this);
                        finish();
                        String content = mResultText.getText().toString();
                        if (!"".equals(content)) {
                            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
                            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                            String time = formatter.format(timestamp);
                            Note newnote =new Note(1, mResultText.getText().toString(), Color.CYAN, time,0);
                            dbo.InsertNote(newnote);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mResultText.setText(null);
                    }
                }).show();
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
            showTip("Start speaking");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            showTip("End the talk");
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
            showTip("Speaking, volume: " + volume);
//            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

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
        FlowerCollector.onResume(AddNote.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(AddNote.this);
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
}
