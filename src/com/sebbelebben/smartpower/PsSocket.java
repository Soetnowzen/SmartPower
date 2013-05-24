package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.util.*;
import org.json.*;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;
import com.sebbelebben.smartpower.Server.OnSetNameReceiveListener;


/**
 * 
 * @author Johan Bregell
 *
 */
public class PsSocket implements Serializable,Graphable {

	private static final long serialVersionUID = 2749123692313834401L;
	private int id;
	private String name;
	private String apiKey;
	
	/**
	 * Creates a PsSocket.
	 * @param id
	 * @param name
	 * @param apiKey
	 */
	public PsSocket(int id, String name, String apiKey){
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the PsSocket's consumption between the given dates.
	 */
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+start+",enddate:"+end+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
				try {			
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						if (data.has("result")){
							listener.failed();
						}
						else if (data.has("data")){
							JSONArray sockets = data.getJSONArray("data");
							for(int i = 0; i < sockets.length(); i++){
								JSONObject JSONsockets = sockets.getJSONObject(i);
								consumptionList.add(new Consumption(JSONsockets.getString("time"),JSONsockets.getInt("power")));
							}
							listener.onConsumptionReceive(consumptionList.toArray(new Consumption[0]));
						} else { //TODO
							listener.failed();
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
	
	/**
	 * Sets a new name on the PsSocket.
	 * @param name
	 * @param listener
	 */
	public void setName(String name, final OnSetNameReceiveListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						listener.onSetNameReceived(data.getString("newname"));
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		this.name = name;
	}
	
	/**
	 * Returns the name of the PsSocket.
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the id of the PsSocket.
	 * @return
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Sends a turn on packet to the server.
	 */
	public void turnOn(){
		Server.sendAndRecieve("{socketid:"+id+",request:turnon,apikey:"+apiKey+"}", new OnReceiveListener() {
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
	
	/**
	 * Sends a turn off packet to the server.
	 */
	public void turnOff(){
		Server.sendAndRecieve("{socketid:"+id+",request:turnoff,apikey:"+apiKey+"}", new OnReceiveListener() {
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
	
	/**
	 * Converts the PsSocket into a printable string.
	 */
	@Override
	public String toString(){
		if (name.equals("null")) {
				return Integer.toString(id);
		} else {
			return name;
		}
		
	}
}
