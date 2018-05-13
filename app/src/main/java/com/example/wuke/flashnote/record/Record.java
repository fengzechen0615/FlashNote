package com.example.wuke.flashnote.record;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuke.flashnote.setting.Setting;
import com.example.wuke.flashnote.util.FucUtil;
import com.example.wuke.flashnote.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Record {

    // 播放音频文件API
    private MediaPlayer mediaPlayer;
    //当前是否正在播放
    private volatile boolean isPlaying;

    private SpeechRecognizer mIat;
    private static String TAG = "Convert";
    private Toast mToast;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    public void startPlay(String url) {
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(url);
            Log.d("URL_3", url);
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }
    }

    public void playEndOrFail(boolean isEnd) {
        isPlaying = false;
        if (isEnd) {
            Log.d("record", String.valueOf(Constant.PLAY_COMPLETION));
        } else {
            Log.d("record", String.valueOf(Constant.PLAY_ERROR));
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    int ret = 0;

    private TextView textView;

    public void Convert(Context context, TextView textView, String filename) {
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);

        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        mIat.setParameter(SpeechConstant.VAD_BOS, "5000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "10000");
        mIat.setParameter(SpeechConstant.ASR_PTT,  "1");

        this.textView = textView;

        RecordText(context, filename);
    }

    public void RecordText(Context context, String filename) {

        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("识别失败,错误码：" + ret);
        } else {
            byte[] audioData = FucUtil.readAudioFile(context, Environment.getExternalStorageDirectory() + "/msc/" + filename + ".wav");
            Log.d("audioData", String.valueOf(audioData));

            if (null != audioData) {
                mIat.writeAudio(audioData, 0, audioData.length);
                mIat.stopListening();
            } else {
                mIat.cancel();
                textView.setText("Unable to convert");
//                showTip("读取音频流失败");
            }
        }

    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            textView.setText("Converting");
            showTip("Converting");
        }


        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean b) {
            printResult(results);
        }

        @Override
        public void onError(SpeechError speechError) {

        }


        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
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

        if (resultBuffer.toString() != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(resultBuffer.toString());
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
