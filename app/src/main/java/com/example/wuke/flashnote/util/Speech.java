package com.example.wuke.flashnote.util;

import android.app.Application;

import com.example.wuke.flashnote.R;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by francisfeng on 21/03/2018.
 */

public class Speech extends Application {

    @Override
    public void onCreate() {

        SpeechUtility.createUtility(Speech.this, "appid=" + getString(R.string.app_id));

        Setting.setShowLog(true);
        super.onCreate();
    }
}
