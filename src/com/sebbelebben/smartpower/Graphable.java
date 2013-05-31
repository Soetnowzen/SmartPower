package com.sebbelebben.smartpower;

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
	public void getConsumption(Duration duration, int amount, final OnConsumptionReceiveListener listener);
}
