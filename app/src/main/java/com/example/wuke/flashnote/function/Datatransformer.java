package com.example.wuke.flashnote.function;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.Calendar;

public class Datatransformer{

    public static void Datatransform(Context context, String command) {
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
                context.startActivity(Calendar_a.stratCalendar(messageVector));
            }

            else if (command.contains("创建")||command.contains("新建")||command.contains("Create")||command.contains("create")) {
                messageVector.set_value_2();
                messageVector.setItem(command);
                messageVector.printvector();

                long id = 1;

                ContentValues event = new ContentValues();
                Calendar mCalendar = Calendar.getInstance();
                StringRecognizer stringRecognizer = new StringRecognizer();
                stringRecognizer.Recognizer(command);

                Log.d("Calendar", "Datatransform: " + stringRecognizer.getTime());
                Log.d("Calendar", "Datatransform: " + stringRecognizer.getAlarm());
                Log.d("Calendar", "Datatransform: " + stringRecognizer.getTitle());
                Log.d("Calendar", "Datatransform: " + stringRecognizer.getDiscription());

                event.put("dtstart", stringRecognizer.getTime()-28800000);
                event.put("dtend", (stringRecognizer.getTime()-25200000));
                event.put("hasAlarm", stringRecognizer.getAlarm());
                event.put("calendar_id", id);
                event.put("title",stringRecognizer.getTitle());
                event.put("description",stringRecognizer.getDiscription());
                event.put("eventLocation", stringRecognizer.getLocation());
                event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
                Uri eventsUri = Uri.parse("content://com.android.calendar/events");
                Uri url = context.getContentResolver().insert(eventsUri, event);
                context.startActivity(Calendar_a.insertCalendar(messageVector));
            }
            else {
                messageVector.set_value_a();
                context.startActivity(Calendar_a.stratCalendar(messageVector));
//				Interface_calender.operation_calender(messageVector);
            }
        }

        //微信类
        else if (command.contains("微信")||command.contains("wechat")) {
            messageVector.set_value_b();
            context.startActivity(Wechat.startWechat(messageVector));
        }
    }

}
