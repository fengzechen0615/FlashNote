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
	final Map<String, String> map = new HashMap<>();
	
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
		
		command = command.replaceAll("，","");
		command = command.replaceAll(",","");
		
		//
		Calendar calendar = Calendar.getInstance();
		long currenttime = calendar.getTime().getTime();
		currenttime = ((currenttime / 86400000) * 86400000);
		this.starttime += currenttime;
		System.out.println(this.starttime);
		
		if (isContainChinese(command)) {
			String[] temp = command.split("");
			for(int i = 0;i< temp.length;i++){
				System.out.println(temp[i]);
			}
			this.discription = command;
			if (command.contains(":")) {
//				System.out.println("mark");
				String hour = temp[command.indexOf(":")-1];
				System.out.println(hour);
				System.out.println(command.indexOf(":")-1);
				String min = temp[command.indexOf(":")+1] + temp[command.indexOf(":")+2];
				System.out.println(min);
				System.out.println((command.indexOf(":")+1) + "|" + (command.indexOf(":")+2));
//				System.out.println(TranstoMinute(min) + "mark1");
				this.starttime += TranstoMinute(min);
//				System.out.println(this.starttime + "mark1");
//				System.out.println(TranstoHour(hour) + "mark2");
				this.starttime += TranstoHour(hour);
//				System.out.println(this.starttime + "mark2");

				if (command.contains("明天")) {
					this.starttime += 86400000;
					System.out.println(this.starttime + "mark3");
				}

				if (command.contains("下午")) {
					this.starttime += 43200000;
					System.out.println(this.starttime + "mark4");
				}

			}else{
				if (command.contains("不要闹钟") || command.contains("不要提醒")) {
					this.alarm = false;
				}

				if (command.contains("见面")) {
					String title = "和";
					title = title + temp[command.indexOf("见面")-2] + temp[command.indexOf("见面")-1] + "见面";
					this.title = title;
				}else{
					this.title = "日程事件";
				}

				if (command.contains("明天")) {
					this.starttime += 86400000;
				}

				if (command.contains("上午")) {

					String aimtime = temp[command.indexOf("上午") + 2];
					this.starttime += TranstoHour(aimtime);

				}else if (command.contains("下午")) {
					String aimtime = temp[command.indexOf("下午") + 2];
					this.starttime += TranstoHour(aimtime);
					this.starttime += 43200000;
				}

				if (command.contains("点") && temp[command.indexOf("点")+1] == "半") {
					this.starttime += 1800000;
				}
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
		
		switch (t) {
		// 1
		case "1":
			return (time+(3600000*1));
		case "һ":
			return (time+(3600000*1));
		case "one":
			return (time+(3600000*1));
		case "One":
			return (time+(3600000*1));
			
		//2
		case "2":
			return (time+(3600000*2));
		case "两":
			return (time+(3600000*2));
		case "two":
			return (time+(3600000*2));
		case "Two":
			return (time+(3600000*2));
			
		//3
		case "3":
			return (time+(3600000*3));
		case "three":
			return (time+(3600000*3));
		case "Three":
			return (time+(3600000*3));
		
		//4
		case "4":
			return (time+(3600000*4));
		case "four":
			return (time+(3600000*4));
		case "Four":
			return (time+(3600000*4));
			
		//5
		case "5":
			return (time+(3600000*5));
		case "five":
			return (time+(3600000*5));
		case "Five":
			return (time+(3600000*5));
			
		//6
		case "6":
			return (time+(3600000*6));
		case "six":
			return (time+(3600000*6));
		case "Six":
			return (time+(3600000*6));
			
		//7
		case "7":
			return (time+(3600000*7));
		case "seven":
			return (time+(3600000*7));
		case "Seven":
			return (time+(3600000*7));
			
		//8
		case "8":
			return (time+(3600000*8));
		case "eight":
			return (time+(3600000*8));
		case "Eight":
			return (time+(3600000*8));
		
		//9
		case "9":
			return (time+(3600000*9));
		case "nine":
			return (time+(3600000*9));
		case "Nine":
			return (time+(3600000*9));
			
		//10
		case "10":
			return (time+(3600000*10));
		case "ten":
			return (time+(3600000*10));
		case "Ten":
			return (time+(3600000*10));
		
		//11
		case "11":
			return (time+(3600000*11));
		case "eleven":
			return (time+(3600000*11));
		case "Eleven":
			return (time+(3600000*11));
			
		//12
		case "12":
			return (time+(3600000*12));
		case "twelve":
			return (time+(3600000*12));
		case "Twelve":
			return (time+(3600000*12));
			
		default:
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
