package com.sebbelebben.smartpower;

import java.util.ArrayList;
import org.json.*;

public class PsSocket {
	private int id;
	private String name;
	
	public PsSocket(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public Consumption[] getConsumption(){
		ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
		String result = Server.sendAndRecieve("{powerStrip:"+id+",Request:sockets}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("powerStrip") == id){
				JSONArray sockets = data.getJSONArray("data");
				for(int i = 0; i < sockets.length(); i++){
					JSONObject JSONsockets = sockets.getJSONObject(i);
					consumptionList.add(new Consumption(JSONsockets.getString("time"),JSONsockets.getInt("power")));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (Consumption[]) consumptionList.toArray();
	}
	
	public void turnOn(){
		
	}
	
	public void tornOff(){
		
	}
}
