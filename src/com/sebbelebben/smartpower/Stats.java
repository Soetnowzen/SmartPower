package com.sebbelebben.smartpower;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.Date;

public class Stats{
	int power;
	SimpleDateFormat time;
	public Stats(int power, String time){
		this.power = power;
		this.time = new SimpleDateFormat(time, Locale.ENGLISH );
	}
	public int getPower(){
		return power;
	}
	public SimpleDateFormat getSimpleDateFormat(){
		return time;
	}
}
