package com.example.wuke.flashnote.util;

import android.content.Context;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 功能性函数扩展类
 */

//有用
public class FucUtil {

	public static byte[] readAudioFile(Context context, String filename) {
		try {
			InputStream ins = new FileInputStream(filename);
			byte[] data = new byte[ins.available()];

			ins.read(data);
			ins.close();
			
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
