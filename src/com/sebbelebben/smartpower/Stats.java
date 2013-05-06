package com.sebbelebben.smartpower;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class Stats{
	int power;
	Date time;
	public Stats(int power, String time)throws ParseException{
		this.power = power;
		this.time = new SimpleDateFormat("yyyy-MM-dd hh:mm:sszz", Locale.ENGLISH ).parse(time);
	}
	public int getPower(){
		return power;
	}
	public Date getSimpleDateFormat(){
		return time;
	}
}
