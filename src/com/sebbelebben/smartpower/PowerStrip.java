package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import org.json.*;

import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;
import com.sebbelebben.smartpower.Server.OnSocketReceiveListener;

public class PowerStrip implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4087429125837326107L;
	private int id;
	private String apiKey;
	private String serialId;
	
	public PowerStrip(int id, String serialId, int type, String apiKey){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
	}
	
	@Override
	public String toString(){
		return serialId;
	}
	
	public void setName(String name){
		Server.sendAndRecieve("{powerstripid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
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
		
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSZ", Locale.ENGLISH);
		Server.sendAndRecieve("{powerstripid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+dd.format(start)+",enddate:"+dd.format(end)+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						if (data.has("result")){
							//This means no data was delivered
							//Some info code here 
						}
						else if (data.has("data")){
							JSONArray sockets = data.getJSONArray("data");
							for(int i = 0; i < sockets.length(); i++){
								JSONObject JSONsockets = sockets.getJSONObject(i);
								consumptionList.add(new Consumption(JSONsockets.getString("timestamp"),JSONsockets.getInt("activepower")));
							}
							listener.onConsumptionReceive(consumptionList.toArray(new Consumption[0]));
						} else {
							//There was something else wrong
						}	
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
					if (data.getInt("powerstripid") == id){
						JSONArray sockets = data.getJSONArray("sockets");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							psSocketList.add(new PsSocket(JSONsockets.getInt("socketid"),JSONsockets.getString("name"),apiKey));
						}
						listener.onSocketReceive(psSocketList.toArray(new PsSocket[0]));
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	public Integer getId() {
		return id;
	}
	
}
