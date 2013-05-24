package com.sebbelebben.smartpower;

import java.text.*;
import java.util.*;

/**
 * 
 * @author Johan Bregell
 *
 */
public class Consumption {
	private int watt;
	private Date date;
	
	/**
	 * Creates a consumption.
	 * @param date
	 * @param watt
	 */
	public Consumption(String date, int watt){
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ", Locale.ENGLISH).parse(date);
			this.watt = watt;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the watt of the Consumption.
	 * @return 
	 */
	public int getWatt() {
		return watt;
	}

	/**
	 * Returns the date of the Consumption.
	 * @return
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Converts the Consumption into a printable string.
	 */
	@Override
	public String toString(){
		return ("Date: "+date.toString()+" Watt: "+Integer.toString(watt));
	}
}
