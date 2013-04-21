package com.sebbelebben.smartpower;

import java.text.*;
import java.util.*;

public class Consumption {
	private int watt;
	private Date date;
	
	public Consumption(String date, int watt){
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ENGLISH).parse(date);
			this.watt = watt;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getWatt() {
		return watt;
	}

	public Date getDate() {
		return date;
	}
}
