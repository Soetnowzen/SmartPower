package com.sebbelebben.smartpower;

import java.util.ArrayList;
import org.json.*;

public class PowerStrip {
	private int id;
	private String serialId;
	private int type;
	
	public PowerStrip(int id, String serialId, int type){
		this.id = id;
		this.serialId = serialId;
		this.type = type;
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
	
	public PsSocket[] getSockets(){
		ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
		String result = Server.sendAndRecieve("{powerStrip:"+id+",Request:sockets}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getInt("powerStrip") == id){
				JSONArray sockets = data.getJSONArray("sockets");
				for(int i = 0; i < sockets.length(); i++){
					JSONObject JSONsockets = sockets.getJSONObject(i);
					psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name")));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (PsSocket[]) psSocketList.toArray();
	}
}
