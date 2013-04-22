package com.sebbelebben.smartpower;

import java.util.ArrayList;
import org.json.*;

import com.sebbelebben.smartpower.Server.OnReceiveListener;

public class PsSocket {
	private int id;
	private String name;
	
	public PsSocket(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void getConsumption(final OnConsumptionReceiveListener listener){
		Server.sendAndRecieve("{powerStrip:"+id+",Request:sockets}", new OnReceiveListener() {
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
	
	public static interface OnConsumptionReceiveListener {
		void onConsumptionReceive(Consumption[] consumption);
	}
	
	public void turnOn(){
		
	}
	
	public void tornOff(){
		
	}
}
