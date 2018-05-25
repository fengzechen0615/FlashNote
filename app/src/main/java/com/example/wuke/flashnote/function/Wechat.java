package com.example.wuke.flashnote.function;

import android.content.ComponentName;
import android.content.Intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String wordstoShare(String message){
        if (ifcommand(message)){
            if (isContainChinese(message)){
                return message.substring(message.indexOf("分享")+2);
            }else{
                message = message.toLowerCase();
                String[] words = new String[20];
                words = message.split(" ");
                String input = "";
                for (int i = 1;!words[i+2].equals("wechat");i++){
                    input = input + words[i];
                }
                return input;
            }
        }else {
            return message;
        }
    }

    public static boolean isContainChinese(String command) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            return true;
        }else{
            return false;
        }
    }

    public static boolean ifcommand(String message){
	    if (isContainChinese(message)){
	        if(message.contains("微信分享")){
	            return true;
            }else{
	            return false;
            }
        }else{
            message = message.toLowerCase();
            String[] words = new String[20];
            words = message.split(" ");
            if (words.length <3){
                return false;
            }else{
                if (words[0].equals("share") && words[words.length-1].equals("wechat")){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }
}
