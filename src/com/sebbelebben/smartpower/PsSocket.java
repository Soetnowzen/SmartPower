package com.sebbelebben.smartpower;

import java.util.*;

import org.json.*;

public class PsSocket {
	private int id;
	private String name;
	private String apiKey;
	
	public PsSocket(int id, String name, String apiKey){
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
	}
	
	public Consumption[] getConsumption(Date start, Date end){
		ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
		String result = Server.sendAndRecieve("{socketid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}");
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
	
	public void setName(String name){
		String result = Server.sendAndRecieve("{socketid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("socketid") == id){
				//check if it was successful
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void turnOn(){
		String result = Server.sendAndRecieve("{socketid:"+id+",request:turnoff,apikey:"+apiKey+"}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("socketid") == id){
				//check if it was successful
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void turnOff(){
		String result = Server.sendAndRecieve("{socketid:"+id+",request:turnon,apikey:"+apiKey+"}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("socketid") == id){
				//check if it was successful
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
