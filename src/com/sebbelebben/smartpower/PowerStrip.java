package com.sebbelebben.smartpower;

import java.util.*;

import org.json.*;

public class PowerStrip {
	private int id;
	private String apiKey;
	
	public PowerStrip(int id, String serialId, int type, String apiKey){
		this.id = id;
		this.apiKey = apiKey;
	}
		
	public Consumption[] getConsumption(Date start, Date end){
		ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
		String result = Server.sendAndRecieve("{powerstripid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}");
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
	
	public PsSocket[] getSockets(){
		ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
		String result = Server.sendAndRecieve("{powerstripid:"+id+",request:sockets,apikey:"+apiKey+"}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("powerStrip") == id){
				JSONArray sockets = data.getJSONArray("sockets");
				for(int i = 0; i < sockets.length(); i++){
					JSONObject JSONsockets = sockets.getJSONObject(i);
					psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (PsSocket[]) psSocketList.toArray();
	}
}
