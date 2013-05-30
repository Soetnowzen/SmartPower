package com.sebbelebben.smartpower;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	 * @param date the timestamp
	 * @param watt the consumption
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
	 * 
	 * @return Returns the watt of the Consumption. 
	 */
	public int getWatt() {
		return watt;
	}

	/**
	 * 
	 * @return Returns the date of the Consumption.
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
