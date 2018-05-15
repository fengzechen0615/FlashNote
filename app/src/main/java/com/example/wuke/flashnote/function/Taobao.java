package com.example.wuke.flashnote.function;

import android.content.Intent;
import android.net.Uri;

public class Taobao {

	private static final String Path = "https://s.m.taobao.com/h5?event_submit_do_new_search_auction=1&_input_charset=utf-8&topSearch=1&atype=b&searchfrom=1&action=home%3Aredirect_app_action&from=1&sst=1&n=20&buying=buyitnow&q=";

	public static String ObjecttoSearch(String link){

        int temp = link.indexOf("搜索");
        temp = temp + 2;
        String good = link.substring(temp);
        link = Path + good;
		return link;
	}

    public static String Object(String link){

        int temp = link.indexOf("搜索");
        temp = temp + 2;
        String good = link.substring(temp);
        return good;
    }
}
