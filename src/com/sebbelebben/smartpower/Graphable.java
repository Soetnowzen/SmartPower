package com.sebbelebben.smartpower;

import java.util.Date;

import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;

public interface Graphable {
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener);
}
