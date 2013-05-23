package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import org.json.*;

import com.sebbelebben.smartpower.Server.*;

public class PowerStrip implements Serializable, Graphable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4087429125837326107L;
	private int id;
	private String apiKey;
	private String serialId;
	private String name;
	private PsSocket[] psSockets;
	
	public PowerStrip(int id, String serialId, String apiKey, String name, PsSocket[] psSocekts){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
		this.name = name;
		this.psSockets = psSocekts;
	}
	
	public PowerStrip(int id, String serialId, String apiKey, String name){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
		this.name = name;
	}
	
	@Override
	public String toString(){
		return serialId;
	}
	
	public void setName(String name, final OnSetNameReceiveListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						listener.onSetNameReceived(data.getString("newname"));
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String getName(){
		return this.name;
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
						if (data.has("result")){ //TODO
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
						} else { //TODO
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
	
	public PsSocket[] getSockets(Boolean update){
		if(this.psSockets.equals(null) || update){
			this.getSockets(new OnSocketReceiveListener() {
				
				@Override
				public void onSocketReceive(PsSocket[] sockets) {
					PowerStrip.this.psSockets = sockets;
				}
				
				@Override
				public void failed() {
					
				}
			});
		}
		return this.psSockets;
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
