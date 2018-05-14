package com.example.wuke.flashnote.function;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.annotation.SuppressLint;

public class StringRecognizer {
	private String title;
	private String location;
	private String discription;
	private long starttime;
	private boolean alarm;
	
	//numbers in english
	private static final String[] DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	private static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty"};
	private static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	//create a new hashmap
	final Map<String, String> map = new HashMap<String, String>();
	
	public StringRecognizer() {
		// TODO Auto-generated constructor stub
		this.title = "New Event";
		this.discription = "Default Description";
		this.starttime = 0;
		this.alarm = true;
	}
	
//	@SuppressLint("DefaultLocale")
	public void Recognizer(String command){
		
		//
		
		command = command.replaceAll("��","");
		command = command.replaceAll(",","");
		
		this.starttime = 0;
		//
		Calendar calendar = Calendar.getInstance();
		long currenttime = calendar.getTime().getTime();
		currenttime = (long)(Math.floor(currenttime / 86400000) * 86400000);
		this.starttime = currenttime;
		
		if (isContainChinese(command)) {
			String[] temp = command.split("");
			this.discription = command;
			
			if (command.contains("不要闹钟") || command.contains("不要提醒")) {
				this.alarm = false;
			}
			
			if (command.contains("见面")) {
				String title = "和";
				title = title + temp[command.indexOf("见面")-1] + temp[command.indexOf("见面")] + "见面";
				this.title = title;
			}else{
				this.title = "日程事件";
			}
			
			if (command.contains("明天")) {
				this.starttime += 86400000;
			}
			
			if (command.contains("上午")) {
				
				String aimtime = temp[command.indexOf("上午") + 3];
				this.starttime += TranstoHour(aimtime);
				
			}else if (command.contains("下午")) {
				String aimtime = temp[command.indexOf("下午") + 3];
				this.starttime += TranstoHour(aimtime);
				this.starttime += 43200000;
			}
			
			if (command.contains("点") && temp[command.indexOf("点")+2].equals("半")) {
				this.starttime += 1800000;
			}else if (command.contains(":")) {
				String min = temp[command.indexOf(":")+2] + temp[command.indexOf(":")+3];
				this.starttime += TranstoMinute(min);
			}
		}else{
			//the english version
			DictionaryLoader();
			command = command.toLowerCase();
			String[] temp = command.split(" ");
			command = " " + command;
			
			for(int i = 0;i<temp.length;i++){
				temp[i] = TransEngtoNum(temp[i]);
			}
			
			//word list
//			int[] blankSquence = new int[temp.length-1];
//			int ctr = 0;
//			while(command.indexOf(" ", (blankSquence[ctr]+1)) != -1){
//				ctr++;
//				blankSquence[ctr] = command.indexOf(" ", blankSquence[ctr-1]+1);
//			}
			
			if (command.contains("tomorrow")) {
				this.starttime += 86400000;
			}
			
			if (command.contains("afternoon") || command.contains("tonight") || command.contains("night") || command.contains("pm") || command.contains("evening")) {
				this.starttime += 43200000;
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].equals("at")) {
						if (i+2 < temp.length) {
							if (temp[i+2].equals("past")) {
								if (temp[i+1].equals("half")) {
									this.starttime += TranstoMinute("30");
								}
								else if (temp[i+1].equals("quater")) {
									this.starttime += TranstoMinute("15");
								}
								else {
									this.starttime += TranstoMinute(TransEngtoNum(temp[i+1]));
								}
								if (i+3 < temp.length) {
									this.starttime += TranstoHour(TransEngtoNum(temp[i+3]));
									continue;
								}else{
									continue;
								}
							}
						}
						int j = i+1;
						if (j < temp.length) {
							this.starttime += TranstoHour(temp[j]);
							if (j+1 < temp.length) {
								this.starttime += TranstoMinute(temp[j+1]);
								if (j+2 < temp.length) {
									this.starttime += TranstoMinute(temp[j+2]);
									continue;
								}else{continue;}
							}else{continue;}
						}else{continue;}
					}else{continue;}
				}
			}else{
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].equals("at")) {
						int j = i+1;
						if (j < temp.length) {
							this.starttime += TranstoHour(temp[j]);
							if (j+1 < temp.length) {
								this.starttime += TranstoMinute(temp[j+1]);
								if (j+2 < temp.length) {
									this.starttime += TranstoMinute(temp[j+2]);
									continue;
								}else{continue;}
							}else{continue;}
						}else{continue;}
					}else{continue;}
				}
			}		
		}
	}
	
	private void DictionaryLoader() {
		//load the hashmap
		//0(although useless)
		map.put("zero", "0");
		
		//DIGITS
		for(int i = 0; i < DIGITS.length;i++){
			map.put(DIGITS[i], TranstoString(i+1));
		}
		
		//TEENS
		for(int i = 0; i < TEENS.length;i++){
			map.put(TEENS[i], TranstoString(i+10));
		}
		
		//TENS
		for(int i = 0; i < TENS.length;i++){
			map.put(TENS[i], TranstoString((i+2)*10));
		}
		
		//21~59
		for (int i = 0; i < TENS.length; i++) {
			for(int j = 0; j < DIGITS.length; j++){
				map.put(TENS[i] + " " + DIGITS[j], TranstoString(((i+2)*10 + j+1)));
				map.put(TENS[i] + "-" + DIGITS[j], TranstoString(((i+2)*10 + j+1)));
			}
		}
	}

	private String TransEngtoNum(String command) {		
		if (map.containsKey(command)) {
			return map.get(command);
		}else {
			return command;
		}
	}

	
	public String getTitle(){
		return this.title;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public String getDiscription(){
		return this.discription;
	}
	
	public long getTime(){
		return this.starttime;
	}
	
	public boolean getAlarm(){
		return this.alarm;
	}
	
	private static boolean isContainChinese(String command) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(command);
		if (matcher.find()) {
			return true;
		}else{
			return false;
		}
	}
	
	private String TranstoString(int v) {
		String result = String.valueOf(v);
		return result;
	}
	
	private static long TranstoHour(String t) {
		
		long time = 0;
		
		if (t.equals("一")) {
			time = 3600000;
		}
		
		else if (t.equals("两")) {
			time = 7200000;
		}
		
		else{
			time = TranstoInt(t) * 3600000;
		}
		
		return time;
		
	}
	
	private static int TranstoInt(String t) {
		
		try {
			int v = Integer.parseInt(t);
			return v;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
	}
	
	private static long TranstoMinute(String t) {
		long min = 60000;
		
		try {
			int v = Integer.parseInt(t);
			return min*v;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
			
	}
}
