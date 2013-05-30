package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sebbelebben.smartpower.Server.GenericStringListener;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnSocketReceiveListener;

public class Group implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6987559953747933724L;
	private int id;
	private String name;
	private String apiKey;
	
	public Group(int id, String name, String apiKey) {
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
	}
	
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		Server.sendAndRecieve("{groupid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("groupid") == id){
						JSONArray sockets = data.getJSONArray("data");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							consumptionList.add(new Consumption(JSONsockets.getString("time"),JSONsockets.getInt("power")));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onConsumptionReceive(consumptionList.toArray(new Consumption[0]));
			}

            @Override
            public void failed() {
                listener.failed();
            }
        });
	}
	
	public void getSockets(final OnSocketReceiveListener listener){
		Server.sendAndRecieve("{groupid:"+id+",request:sockets,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("groupid") == id){
						JSONArray sockets = data.getJSONArray("sockets");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							if(JSONsockets.getInt("status") == 1){
								psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey,true));	
							} else if(JSONsockets.getInt("status") == 0){
								psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey,false));	
							} else {
								listener.failed();
							}
						}
					}
					else if(data.getString("status").equals("failed")) {
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onSocketReceive(psSocketList.toArray(new PsSocket[0]));
			}

            @Override
            public void failed() {
                listener.failed();
            }
        });
	}
	
	public void addSocket(PsSocket psSocket, final OnSocketReceiveListener listener){
		Server.sendAndRecieve("{groupid:"+id+",request:addsocket,apikey:"+apiKey+",socket:"+Integer.toString(psSocket.getId())+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("groupid") == id){
						JSONArray sockets = data.getJSONArray("sockets");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							if(JSONsockets.getInt("status") == 1){
								psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey,true));	
							} else if(JSONsockets.getInt("status") == 0){
								psSocketList.add(new PsSocket(JSONsockets.getInt("id"),JSONsockets.getString("name"),apiKey,false));	
							} else {
								listener.failed();
							}
						}
					}
					else if(data.getString("status").equals("failed")) {
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onSocketReceive(psSocketList.toArray(new PsSocket[0]));
			}

            @Override
            public void failed() {
                listener.failed();
            }
        });
	}
	
	public void turnOn(){
		Server.sendAndRecieve("{groupid:"+id+",request:turnon,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("groupid") == id){
						//check if it was successful
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void failed() {
                
            }
        });
		
	}
	
	public void turnOff(){
		Server.sendAndRecieve("{groupid:"+id+",request:turnoff,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("groupid") == id){
						//check if it was successful
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void failed() {
            	
            }
        });
	}
	
	public String getName() {
		return name;
	}
}
