package com.example.wuke.flashnote;

public class Datatransformer {
	public static void Datatransform(String command) {
		// TODO Auto-generated constructor stub
		MessageVector messageVector = new MessageVector();
		System.out.println(command);
//		messageVector.printvector();

		//日历类
		if (command.contains("日历")||command.contains("calendar")||command.contains("Calendar")) {
			messageVector.set_value_a();
			
			if (command.contains("打开")||command.contains("Open")||command.contains("open")) {
				messageVector.set_value_1();
				messageVector.printvector();
				Interface_calender.operation_calender(messageVector);
			}
			
			else if (command.contains("创建")||command.contains("新建")||command.contains("Create")||command.contains("create")) {
				messageVector.set_value_2();
				messageVector.setItem(command);
				messageVector.printvector();
				Interface_calender.operation_calender(messageVector);
			}
			else {
				messageVector.set_value_a();
				Interface_calender.operation_calender(messageVector);
			}
		}

		//微信类
		else if (command.contains("΢��")||command.contains("wechat")) {
			messageVector.set_value_b();
		}

		//淘宝类
		else if (command.contains("�Ա�")) {
			messageVector.set_value_c();
		}
	}
}
