package com.example.wuke.flashnote.function;

import android.content.Intent;
import android.net.Uri;

public class Taobao {
	public static Intent startTaobao(MessageVector messageVector){

//		String frontpath = "https://s.taobao.contentm/search?q=";
//		String backpath = "&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306";
		String tbPath = "https://item.taobao.com/item.htm?spm=a219r.Im872.14.7.7f265101rMMpLF&id=532813558165&ns=1&abbucket=19#detail";
		
		Intent intent = new Intent();
		intent.setAction("Android.intent.action.VIEW");
		Uri uri = Uri.parse(tbPath); // Good's Page
		intent.setData(uri);
		intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
		return intent;
	}
}
