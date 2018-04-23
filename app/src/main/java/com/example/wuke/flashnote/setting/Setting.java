package com.example.wuke.flashnote.setting;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.wuke.flashnote.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ContactManager;

/**
 * Created by francisfeng on 21/03/2018.
 */

public class Setting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String PREFER_NAME = "com.iflytek.setting";

    private Preference preference;

    private Context mContext;

    private SpeechRecognizer mIat;

    private Toast mToast;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.setting);

        mIat = SpeechRecognizer.createRecognizer(Setting.this, mInitListener);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mContext=getApplicationContext();
        preference = findPreference("contact");
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue == true) {
//                    Toast.makeText(mContext, String.format("Preference的值为%s", newValue),Toast.LENGTH_LONG).show();
                    ContactManager mgr = ContactManager.createManager(Setting.this, mContactListener);
                    mgr.asyncQueryAllContactsName();
                } else if ((Boolean)newValue == false) {
                    ContactManager.destroy();
//                    Toast.makeText(mContext, String.format("Preference的值为%s", newValue),Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    int ret = 0;

    private ContactManager.ContactListener mContactListener = new ContactManager.ContactListener() {

        @Override
        public void onContactQueryFinish(final String contactInfos, boolean changeFlag) {
            // 注：实际应用中除第一次上传之外，之后应该通过changeFlag判断是否需要上传，否则会造成不必要的流量.
            // 每当联系人发生变化，该接口都将会被回调，可通过ContactManager.destroy()销毁对象，解除回调。
             if(changeFlag == false) {
                 // 指定引擎类型
                 Log.d("changeFloag", String.valueOf(changeFlag));
                 runOnUiThread(new Runnable() {
                     public void run() {
                         Log.d("contactinfos", contactInfos);
                     }
                 });
             }

            mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            ret = mIat.updateLexicon("contact", contactInfos, mLexiconListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("上传联系人失败：" + ret);
            }
        }
    };

    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                showTip(error.toString());
            } else {
                showTip("上传成功");
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("TAG", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
