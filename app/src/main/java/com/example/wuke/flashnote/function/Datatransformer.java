package com.example.wuke.flashnote.function;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;

public class Datatransformer extends Activity{

    public void Datatransform(String command) {
        MessageVector messageVector = new MessageVector();
//        System.out.println(command);
//		  messageVector.printvector();
        Log.d("command", command);

        //日历类
        if (command.contains("日历")||command.contains("calendar")||command.contains("Calendar")) {
            messageVector.set_value_a();

            if (command.contains("打开")||command.contains("Open")||command.contains("open")) {
                messageVector.set_value_1();
                messageVector.printvector();
                startActivity(Calendar_a.stratCalendar(messageVector));
            }

            else if (command.contains("创建")||command.contains("新建")||command.contains("Create")||command.contains("create")) {
                messageVector.set_value_2();
                messageVector.setItem(command);
                messageVector.printvector();

                long id = 1;

                ContentValues event = new ContentValues();
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.HOUR_OF_DAY, 11);
                mCalendar.set(Calendar.MINUTE, 45);
                long start = mCalendar.getTime().getTime();
                mCalendar.set(Calendar.HOUR_OF_DAY, 12);
                long end = mCalendar.getTime().getTime();

                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("hasAlarm", 1);
                event.put("calendar_id", id);
                event.put("title","New Event");
                event.put("description",command);
                event.put("eventLocation", "China");
                event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
                Uri eventsUri = Uri.parse("content://com.android.calendar/events");
                Uri url = getContentResolver().insert(eventsUri, event);
                startActivity(Calendar_a.insertCalendar(messageVector));
            }
            else {
                messageVector.set_value_a();
                startActivity(Calendar_a.stratCalendar(messageVector));
//				Interface_calender.operation_calender(messageVector);
            }
        }

        //微信类
        else if (command.contains("微信")||command.contains("wechat")) {
            messageVector.set_value_b();
            startActivity(Wechat.startWechat(messageVector));
        }
    }

}
