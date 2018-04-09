package com.example.wuke.flashnote.function;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Calendar_a {
		@SuppressWarnings("deprecation")
		public static Intent stratCalendar(MessageVector messageVector){
			try {  
	            Intent i = new Intent();  
	            ComponentName cn = null;  
	            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {  
	                cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");  
	            } else {  
	                cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");  
	            }
	            i.setComponent(cn);  
	            return i; 
	        } catch (ActivityNotFoundException e) {  
	            // TODO: handle exception  
	            Log.e("No calendar", e.toString());
	            return null;
	        } 
		}
		
		@SuppressWarnings("deprecation")
		public static Intent insertCalendar(MessageVector messageVector){
			try {  
	            Intent i = new Intent();  
	            ComponentName cn = null;  
	            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {  
	                cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");  
	            } else {  
	                cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");  
	            }
//              Insert the event into the calendar(have uri bugs)
	            System.out.println("event");
	            i.setComponent(cn);  
	            return i; 
	        } catch (ActivityNotFoundException e) {  
	            // TODO: handle exception  
	            Log.e("No calendar", e.toString());
	            return null;
	        } 
		}
}
