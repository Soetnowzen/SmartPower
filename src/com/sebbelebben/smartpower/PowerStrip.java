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
public class PowerStrip implements Serializable, Graphable, PsPart{
	
	private static final long serialVersionUID = 4087429125837326107L;
	private int id;
	private String apiKey;
	private String serialId;
	private String name;
	private PsSocket[] psSockets;
	private Boolean status;
	
	/**
	 * Creates a PowerStrip with the list of PsSockets supplied for faster handling.
	 * @param id the numeric id of the powerstrip
	 * @param serialId The serialId of the powerstrip
	 * @param apiKey The users apiKey
	 * @param name Name of the powerstrip
	 * @param psSockets list of all sockets the powerstrip holds
	 */
	public PowerStrip(int id, String serialId, String apiKey, String name, PsSocket[] psSockets, Boolean status){
		this.id = id;
		this.apiKey = apiKey;
		this.serialId = serialId;
		this.name = name;
		this.psSockets = psSockets;
		this.status = status;
	}
	
	public String toJSON(){
		String PsSocketsJSON = "";
		for(int i = 0; i < psSockets.length; i++){
			if(i != 1){
				PsSocketsJSON.concat(",");
			}
			PsSocketsJSON.concat(psSockets[i].toJSON());
		}
		return "{" +
					"\"id\":"+Integer.toString(this.id)+"," +
					"\"serialId\":\""+this.serialId+"\"," +
					"\"name\":\""+this.name+"\"," +
					"\"pssockets\":["+PsSocketsJSON+"]" +
				"}";
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
	 * @param name the new name of the socket
	 * @param listener The callback function
	 */
	public void setName(String name, final OnSetNameReceiveListener listener){
		final String previousName = this.name;
		this.name = name;
		Server.sendAndRecieve("{powerstripid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						PowerStrip.this.name = data.getString("newname");
						listener.onSetNameReceived(data.getString("newname"));
					} else {
						PowerStrip.this.name = previousName;
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void onReceiveFailure() {
                listener.failed();
            }
        });
	}
	
	/**
	 * 
	 * @return Returns the name of the PowerStip.
	 */
	public String getName(){
		return this.name;
	}
		
	/**
	 * Creates a listener that waits for the server to supply the PowerStrip's consumption between the given dates.
	 * @param start Start date
	 * @param end end Date
	 * @param listener
	 */
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSZ", Locale.ENGLISH);
		Server.sendAndRecieve("{powerstripid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+dd.format(start)+",enddate:"+dd.format(end)+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
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

            @Override
            public void onReceiveFailure() {
                listener.failed();
            }
        });
	}

	/**
	 * 
	 * @return Returns the saved list of PsSockets  connected to the PowerStrip, or updates the list and then returns it.
	 */
	public PsSocket[] getSockets(){
		return this.psSockets;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the PowerStrips's connected PsSockets.
	 * @param listener
	 */
	public void updatePowerStrip(final GenericListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:sockets,apikey:"+apiKey+"}", new Server.OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						JSONArray sockets = data.getJSONArray("sockets");
						for(int i = 0; i < sockets.length(); i++){
							JSONObject JSONsockets = sockets.getJSONObject(i);
							if(JSONsockets.getInt("status") == 1){
								psSocketList.add(new PsSocket(JSONsockets.getInt("socketid"),JSONsockets.getString("name"),apiKey,true));
							} else if (JSONsockets.getInt("status") == 0) {
								psSocketList.add(new PsSocket(JSONsockets.getInt("socketid"),JSONsockets.getString("name"),apiKey,false));
							} else {
								listener.failed();
							}	
						}
						PowerStrip.this.psSockets = psSocketList.toArray(new PsSocket[0]);
						listener.success();
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

            @Override
            public void onReceiveFailure() {
                listener.failed();
            }
        });
	}
	
	/**
	 * 
	 * @return Returns the id of the PowerStrip.
	 */
	public Integer getId() {
		return id;
	}
	
	public boolean compareTo(PowerStrip ps) {
		return this.getId() == ps.getId();
	}
	
	public Boolean getStatus(){
		return status;	
	}
	
	public void updateStatus(final OnUpdateListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:status,apikey:"+apiKey+"}", new OnReceiveListener() {
			
			@Override
			public void onReceiveSuccess(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id){
						int status_int = data.getInt("status");
						Boolean status;
						if (status_int == 1){
							status = true;
							PowerStrip.this.status = status;
							listener.onUpdateReceive(status);
						} else if (status_int == 0) {
							status = false;
							PowerStrip.this.status = status;
							listener.onUpdateReceive(status);
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
			
			@Override
			public void onReceiveFailure() {
				listener.failed();
			}
		});
	}
}
