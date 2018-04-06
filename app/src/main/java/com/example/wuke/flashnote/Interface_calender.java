package com.example.wuke.flashnote;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Interface_calender {
	
	@SuppressWarnings("deprecation")
	public static void operation_calender(MessageVector messageVector) {
		// TODO Auto-generated constructor stub
		if (messageVector.value_1 == 1) {
			try {  
	            Intent i = new Intent();  
	            ComponentName cn = null;
				System.out.println("mark1");
	            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {  
	                cn = new ComponentName("com.android.calendar",  
	                        "com.android.calendar.LaunchActivity");
					System.out.println("mark2");
	  
	            } else {  
	                cn = new ComponentName("com.google.android.calendar",  
	                        "com.android.calendar.LaunchActivity");
					System.out.println("mark3");
	            }  
	            i.setComponent(cn);  
//	            startActivity(i);
	        } catch (ActivityNotFoundException e) {  
	            // TODO: handle exception  
//	            Log.e("ActivityNotFoundException", e.toString());
	        }  ;
		}
		
		else if(messageVector.value_2 == 1){
			ContentValues event = new ContentValues();
			event.put("calendar_id", System.currentTimeMillis());
			event.put("title", "New Event");   
		    event.put("description", messageVector.items);
//		    System.out.println("event finished");
		}
	}
	
	
}
