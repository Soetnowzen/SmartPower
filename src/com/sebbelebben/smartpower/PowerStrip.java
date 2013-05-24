package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import org.json.*;

import com.sebbelebben.smartpower.Server.*;

/**
 * 
 * @author Johan Bregell
 *
 */
public class PowerStrip implements Serializable, Graphable{
	
	private static final long serialVersionUID = 4087429125837326107L;
	private int id;
	private String apiKey;
	private String serialId;
	private String name;
	private String previousName; // The previous name, for rolling back failed name changes
	private PsSocket[] psSockets;
	
	/**
	 * Creates a PowerStrip with the list of PsSockets supplied for faster handling.
	 * @param id
	 * @param serialId
	 * @param apiKey
	 * @param name
	 * @param psSocekts
	 */
	public PowerStrip(int id, String serialId, String apiKey, String name, PsSocket[] psSockets){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
		this.name = name;
		this.psSockets = psSockets;
	}
	
	/**
	 * Creates a PowerStrip.
	 * @param id
	 * @param serialId
	 * @param apiKey
	 * @param name
	 */
	public PowerStrip(int id, String serialId, String apiKey, String name){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
		this.name = name;
		this.previousName = name;
	}
	
	/**
	 * Converts the PowerStrip into a printable string.
	 */
	@Override
	public String toString(){
		return serialId;
	}
	
	/**
	 * Sets a new name on the PowerStrip.
	 * @param name
	 * @param listener
	 */
	public void setName(String name, final OnSetNameReceiveListener listener){
		this.previousName = this.name;
		this.name = name;
		Server.sendAndRecieve("{powerstripid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						PowerStrip.this.name = data.getString("newname");
						listener.onSetNameReceived(data.getString("newname"));
					} else {
						PowerStrip.this.name = PowerStrip.this.previousName;
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Returns the name of the PowerStip.
	 * @return
	 */
	public String getName(){
		return this.name;
	}
		
	/**
	 * Creates a listener that waits for the server to supply the PowerStrip's consumption between the given dates.
	 * @param start
	 * @param end
	 * @param listener
	 */
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
							listener.failed();
						}
						else if (data.has("data")){
							JSONArray sockets = data.getJSONArray("data");
							for(int i = 0; i < sockets.length(); i++){
								JSONObject JSONsockets = sockets.getJSONObject(i);
								consumptionList.add(new Consumption(JSONsockets.getString("timestamp"),JSONsockets.getInt("activepower")));
							}
							listener.onConsumptionReceive(consumptionList.toArray(new Consumption[0]));
						} else {
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
	 * Returns the saved list of PsSockets  connected to the PowerStrip, or updates the list and then returns it.
	 * @param update If set to true the list will be updated
	 * @return
	 */
	public PsSocket[] getSockets(Boolean update){
		if(this.psSockets.equals(null) || update){
			this.getSockets(new OnSocketReceiveListener() {
				
				@Override
				public void onSocketReceive(PsSocket[] sockets) {
					PowerStrip.this.psSockets = sockets;
				}
				
				@Override
				public void failed() {
					PowerStrip.this.psSockets = null;
				}
			});
		}
		return this.psSockets;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the PowerStrips's connected PsSockets.
	 * @param listener
	 */
	private void getSockets(final OnSocketReceiveListener listener){
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
	
	/**
	 * Returns the id of the PowerStrip.
	 * @return
	 */
	public Integer getId() {
		return id;
	}
	
}
