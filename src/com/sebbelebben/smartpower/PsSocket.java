package com.sebbelebben.smartpower;

import java.util.*;
import org.json.*;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;

public class PsSocket {
	private int id;
	private String name;
	private String apiKey;
	
	public PsSocket(int id, String name, String apiKey){
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
	}
	
	public void getConsumption(final OnConsumptionReceiveListener listener, Date start, Date end){
		Server.sendAndRecieve("{socketid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
				try {			
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						JSONArray sockets = data.getJSONArray("data");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							consumptionList.add(new Consumption(JSONsockets.getString("time"),JSONsockets.getInt("power")));
						}
						listener.onConsumptionReceive((Consumption[]) consumptionList.toArray());
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setName(String name){
		Server.sendAndRecieve("{socketid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						//check if it was successful
					} else {
						//report unsuccessful 
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String getName(){
		return name;
	}
	
	public void turnOn(){
		Server.sendAndRecieve("{socketid:"+id+",request:turnoff,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						//check if it was successful
					}
					else {
						//report unsuccessful
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void turnOff(){
		Server.sendAndRecieve("{socketid:"+id+",request:turnon,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						//check if it was successful
					} else {
						//report unsuccessful
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String toString(){
		return name;
	}
}
