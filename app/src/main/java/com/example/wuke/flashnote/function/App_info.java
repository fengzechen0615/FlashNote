package com.example.wuke.flashnote.function;

import android.graphics.drawable.Drawable;

public class App_info {
	
	private String APP_Name;
	private String APP_Packagename;
	private String APP_Versionname;
	private int APP_Versioncode;
	private Drawable APP_Icon = null;
	
	public App_info() {
		this.APP_Name = "Default Name";
		this.APP_Packagename = "Default Name";
		this.APP_Versionname = "Default Name";
		this.APP_Versioncode = 0;
		this.APP_Icon = null;
	}
	
	public App_info(String APP_Name,String APP_Packagename,String APP_Versionname,int APP_Versioncode,Drawable APP_Icon) {
		this.APP_Name = APP_Name;
		this.APP_Packagename = APP_Packagename;
		this.APP_Versionname = APP_Versionname;
		this.APP_Versioncode = APP_Versioncode;
		this.APP_Icon = APP_Icon;
	}
	
	public void set_APP_Name(String APP_Name){
		this.APP_Name = APP_Name;
	}
	
	public void set_APP_Packagename(String APP_Packagename){
		this.APP_Packagename = APP_Packagename;
	}
	
	public void set_APP_Versionname(String APP_Versionname){
		this.APP_Versionname = APP_Versionname;
	}
	
	public void set_APP_Versioncode(int APP_Versioncode){
		this.APP_Versioncode = APP_Versioncode;
	}
	
	public void set_APP_Icon(Drawable APP_Icon) {
		this.APP_Icon = APP_Icon;
	}
	
	public String get_APP_Name(){
		return this.APP_Name;
	}
	
	public String get_APP_Packagename(){
		return this.APP_Packagename;
	}
	
	public String get_APP_Versionname(){
		return this.APP_Versionname;
	}

	public int get_APP_Versioncode(){
		return this.APP_Versioncode;
	}
	
	public Drawable get_APP_Icon(){
		return this.APP_Icon;
	}
}
