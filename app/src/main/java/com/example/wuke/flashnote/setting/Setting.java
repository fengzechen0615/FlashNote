package com.example.wuke.flashnote.setting;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Window;

import com.example.wuke.flashnote.R;
import com.example.wuke.flashnote.util.SettingTextWatcher;

/**
 * Created by francisfeng on 21/03/2018.
 */

public class Setting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String PREFER_NAME = "com.iflytek.setting";
//    private EditTextPreference mVadbosPreference;
//    private EditTextPreference mVadeosPreference;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.setting);

//        mVadbosPreference = (EditTextPreference)findPreference("iat_vadbos_preference");
//        mVadbosPreference.getEditText().addTextChangedListener(new SettingTextWatcher(Setting.this,mVadbosPreference,0,10000));
//
//        mVadeosPreference = (EditTextPreference)findPreference("iat_vadeos_preference");
//        mVadeosPreference.getEditText().addTextChangedListener(new SettingTextWatcher(Setting.this,mVadeosPreference,0,10000));
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
