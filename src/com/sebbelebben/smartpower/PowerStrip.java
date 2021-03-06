package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.GenericStringListener;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnUpdateListener;

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
	 * @param status the state of the powerstrip (on/off)
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
	public void setName(String name, final GenericStringListener listener){
		final String previousName = this.name;
		final String newName = name.replaceAll("[^\\w\\s^-]", "");
        this.name = newName; 
		Server.sendAndRecieve("{powerstripid:"+id+",request:setname,apikey:"+apiKey+",newname:"+newName+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("powerstripid") == id && data.getBoolean("result") == true){
						PowerStrip.this.name = data.getString("newname");
						listener.success(data.getString("newname"));
					} else {
						PowerStrip.this.name = previousName;
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

            @Override
            public void failed() {
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
	public void getConsumption(Duration duration, final int amount, final OnConsumptionReceiveListener listener){
		String durationstring = null;
		if(duration.equals(Duration.YEAR)){
			durationstring = "year";
		} else if(duration.equals(Duration.MONTH)){
			durationstring = "month";
		} else if(duration.equals(Duration.DAY)){
			durationstring = "day";
		} else if(duration.equals(Duration.HOUR)){
			durationstring = "hour";
		} else if(duration.equals(Duration.MINUTE)){
			durationstring = "minute";
		}
		Server.sendAndRecieve("{powerstripid:"+id+",request:consumption,apikey:"+apiKey+",duration:"+durationstring+",amount:"+Integer.toString(amount)+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>(amount);
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
            public void failed() {
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
		Server.sendAndRecieve("{powerstripid:"+id+",request:sockets,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>(4);
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
            public void failed() {
                listener.failed();
            }
        });
	}
	
	/**
	 * 
	 * @return Returns the id of the PowerStrip.
	 */
	public int getId() {
		return id;
	}
	
	public boolean compareTo(PowerStrip ps) {
		return this.getId() == ps.getId();
	}
	
	public Boolean getStatus(){
		return status;	
	}
	
	public void updateStatus(final OnUpdateListener listener){
		Server.sendAndRecieve("{powerstripid:"+id+",request:status,apikey:"+apiKey+"}", new GenericStringListener() {
			
			@Override
			public void success(String result) {
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
			public void failed() {
				listener.failed();
			}
		});
	}
}
