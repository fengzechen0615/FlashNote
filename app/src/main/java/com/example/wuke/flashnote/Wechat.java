package com.example.wuke.flashnote;

import android.content.ComponentName;
import android.content.Intent;

public class Wechat {
	public static Intent startWechat(MessageVector messageVector){
		Intent intent = new Intent();  
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");// ��������activity  

        intent.setAction(Intent.ACTION_MAIN);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER);  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setComponent(cmp);  
        return intent;
	}
}
