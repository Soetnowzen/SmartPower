package com.sebbelebben.smartpower;

import java.util.Date;

import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;

/**
 * 
 * @author Johan Bregell
 *
 */
public interface Graphable {
	/**
	 * Gets the consumption between the specified dates.
	 * @param start
	 * @param end
	 * @param listener
	 */
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener);
}
