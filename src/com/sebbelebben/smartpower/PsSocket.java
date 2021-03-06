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
public class PsSocket implements Serializable,Graphable, PsPart {

	private static final long serialVersionUID = 2749123692313834401L;
	private int id;
	private String name;
	private String apiKey;
	private Boolean status;
	
	/**
	 * Creates a PsSocket.
	 * @param id the numeric Id of the PsSocket
	 * @param name the name of the socket
	 * @param apiKey the users apiKey
	 */
	public PsSocket(int id, String name, String apiKey, Boolean status){
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
		this.status = status;
	}
	
	/**
	 * Creates a psSocket from a JSONObject.
	 * @param psSocket A psSocket in JSONObject format.
	 */
	public PsSocket(JSONObject psSocket){
		try {
			this.id = psSocket.getInt("id");
			this.name = psSocket.getString("name");
			this.apiKey = psSocket.getString("apikey");
			this.status = psSocket.getBoolean("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toJSON(){
		return "{\"id\":"+Integer.toString(this.id)+",\"name\":\""+this.name+"\",\"apikey\":\""+this.apiKey+"\",\"status\":"+this.status.toString()+"}";
		
	}
	/**
	 * Creates a listener that waits for the server to supply the PsSocket's consumption between the given dates.
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
        Server.sendAndRecieve("{socketid:"+id+",request:consumption,apikey:"+apiKey+",duration:"+durationstring+",amount:"+Integer.toString(amount)+"}", new GenericStringListener() {
            @Override
            public void success(String result) {
                ArrayList<Consumption> consumptionList = new ArrayList<Consumption>(amount);
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
	public void setStatus(boolean status){
		this.status = status;
	}
	
	/**
	 * Sets a new name on the PsSocket.
	 * @param name the new name of the PsSocket
	 * @param listener 
	 */
	public void setName(String name, final GenericStringListener listener){
        final String previousName = this.name;
        final String newName = name.replaceAll("[^\\w\\s^-]", "");
        this.name = newName; 		
		Server.sendAndRecieve("{socketid:"+id+",request:setname,apikey:"+apiKey+",newname:"+newName+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
                        PsSocket.this.name = data.getString("newname");
						listener.success(data.getString("newname"));
					} else {
                        PsSocket.this.name = previousName;
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
	 * @return Returns the name of the PsSocket
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return Returns the id of the PsSocket.
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Sends a turn on packet to the server.
	 */
	public void turnOn(final GenericListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:turnon,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				if(result.equals("switchRequestTrue")){
					PsSocket.this.status = true;
					listener.success();
				} else {
					listener.failed();
				}
			}

            @Override
            public void failed() {
                listener.failed();
            }
        });
		
	}
	
	/**
	 * Sends a turn off packet to the server.
	 */
	public void turnOff(final GenericListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:turnoff,apikey:"+apiKey+"}", new GenericStringListener() {
			@Override
			public void success(String result) {
				if(result.equals("switchRequestTrue")){
					PsSocket.this.status = false;
					listener.success();
				} else {
					listener.failed();
				}
			}

            @Override
            public void failed() {
                listener.failed();
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
	
	public boolean compareTo(PsSocket psSocket) {
		return this.getId() == psSocket.getId();
	}
	
	public Boolean getStatus(){
		return status;	
	}
	
	public void updateStatus(final OnUpdateListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:status,apikey:"+apiKey+"}", new GenericStringListener() {
			
			@Override
			public void success(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
						if (data.getInt("status") == 1){
							PsSocket.this.status = true;
							listener.onUpdateReceive(true);
						} else if (data.getInt("status") == 0) {
							PsSocket.this.status = false;
							listener.onUpdateReceive(false);
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
