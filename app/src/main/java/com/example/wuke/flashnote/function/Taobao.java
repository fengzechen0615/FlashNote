package com.example.wuke.flashnote.function;

import android.content.Intent;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Taobao {

	private static final String Path = "https://s.m.taobao.com/h5?event_submit_do_new_search_auction=1&_input_charset=utf-8&topSearch=1&atype=b&searchfrom=1&action=home%3Aredirect_app_action&from=1&sst=1&n=20&buying=buyitnow&q=";

	public static String ObjecttoSearch(String link){
        if (isContainChinese(link)){
            int temp = link.indexOf("搜索");
            temp = temp + 2;
            String good = link.substring(temp);
            link = Path + good;
            return link;
        }else{
            link = link.toLowerCase();
            String[] words = new String[20];
            words = link.split(" ");
            String good = words[1];
            link = Path + good;
            return link;
        }

	}

    public static String Object(String link){
        if (isContainChinese(link)){
            int temp = link.indexOf("搜索");
            temp = temp + 2;
            String good = link.substring(temp);
            return good;
        }else{
            link = link.toLowerCase();
            String[] words = new String[20];
            words = link.split(" ");
            String good = words[1];
            return good;
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
}
