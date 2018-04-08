package com.example.wuke.flashnote;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Calendar {
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
//	            ContentValues event = new ContentValues();
//	            event.put(command.getText().toString(),"title");
//	            Uri eventsUri = Uri.parse("content://calendar/events");   
//	            Uri url = getContentResolver().insert(eventsUri, event);
//	            System.out.println(event);
	            i.setComponent(cn);  
	            return i; 
	        } catch (ActivityNotFoundException e) {  
	            // TODO: handle exception  
	            Log.e("No calendar", e.toString());
	            return null;
	        } 
		}
}
