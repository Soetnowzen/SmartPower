package com.sebbelebben.smartpower;

import java.util.*;

import org.json.*;

import com.sebbelebben.smartpower.PsSocket.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;

public class PowerStrip {
	private int id;
	private String apiKey;
	
	public PowerStrip(int id, String serialId, int type, String apiKey){
		this.id = id;
		this.apiKey = apiKey;
	}
		
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
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
				listener.onConsumptionReceive((Consumption[]) consumptionList.toArray());
			}
		});
	}
	
	public void getSockets(final OnSocketReceiveListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:sockets,apikey:"+apiKey+"}", new Server.OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstrip") == id){
						JSONArray sockets = data.getJSONArray("sockets");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onSocketReceive((PsSocket[]) psSocketList.toArray());
			}
		});
	}
	
	public static interface OnSocketReceiveListener {
		void onSocketReceive(PsSocket[] sockets);
	}
}
