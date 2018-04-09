package com.example.wuke.flashnote.function;

import android.content.Intent;
import android.net.Uri;

public class Taobao {
	public static Intent startTaobao(MessageVector messageVector){

		String frontpath = "https://s.taobao.com/search?q=";
		String backpath = "&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306";
		String tbPath = frontpath + messageVector.items + backpath;
		
		Intent intent = new Intent();
		intent.setAction("Android.intent.action.VIEW");
		Uri uri = Uri.parse(tbPath); // Good's Page
		intent.setData(uri);
		intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
		return intent;
	}
}
