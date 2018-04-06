package com.example.wuke.flashnote;

public class MessageVector {
	int value_a;//日历
	int value_b;//微信
	int value_c;//淘宝
	int value_1;//动作1
	int value_2;//动作2
	int value_3;//动作3
	String items;
	
	public MessageVector() {
		// TODO Auto-generated constructor stub
		this.value_a = 0;
		this.value_b = 0;
		this.value_c = 0;
		this.value_1 = 0;
		this.value_2 = 0;
		this.value_3 = 0;
		this.items = null;
	}
	
	/**
	 * 日历
	 */
	public void set_value_a(){
		this.value_a = 1;
	}

	/**
	 * 微信
	 */
	public void set_value_b(){
		this.value_b = 1;
	}

	/**
	 * 淘宝
	 */
	public void set_value_c(){
		this.value_c = 1;
	}

	/**
	 * 动作1
	 */
	public void set_value_1(){
		this.value_1 = 1;
	}

	/**
	 * 动作2
	 */
	public void set_value_2(){
		this.value_2 = 1;
	}

	/**
	 * 动作3
	 */
	public void set_value_3(){
		this.value_3 = 1;
	}

	/**
	 * 语音内容
	 */
	public void setItem(String s){
		this.items = s;
	}

	/**
	 * 测试用类0
	 */
	public void printvector() {System.out.println("(" + value_a + "," + value_b + "," +value_c + "," +value_1 + "," +value_2 + "," + value_3 + ")");}
}
