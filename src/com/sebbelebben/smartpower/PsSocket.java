package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.*;

import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;
import com.sebbelebben.smartpower.Server.OnSetNameReceiveListener;


/**
 * 
 * @author Johan Bregell
 *
 */
public class PsSocket implements Serializable,Graphable, PsPart {

	private static final long serialVersionUID = 2749123692313834401L;
	private int id;
	private String name;
    private String previousName; // The previous name, for rolling back failed name changes
	private String apiKey;
	
	/**
	 * Creates a PsSocket.
	 * @param id the numeric Id of the PsSocket
	 * @param name the name of the socket
	 * @param apiKey the users apiKey
	 */
	public PsSocket(int id, String name, String apiKey){
		this.id = id;
		this.name = name;
		this.apiKey = apiKey;
	}
	
	public String toJSON(){
		return "{\"id\":"+Integer.toString(this.id)+",\"name\":\""+this.name+"\",\"apikey\":\""+this.apiKey+"\"}";
		
	}
	/**
	 * Creates a listener that waits for the server to supply the PsSocket's consumption between the given dates.
	 */
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
        DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSZ", Locale.ENGLISH);
        Server.sendAndRecieve("{socketid:"+id+",request:consumption,apikey:"+apiKey+",startdate:"+dd.format(start)+",enddate:"+dd.format(end)+"}", new OnReceiveListener() {
            @Override
            public void onReceiveSuccess(String result) {
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
	 * Sets a new name on the PsSocket.
	 * @param name the new name of the PsSocket
	 * @param listener 
	 */
	public void setName(String name, final OnSetNameReceiveListener listener){
        this.previousName = this.name;
        this.name = name;
		Server.sendAndRecieve("{socketid:"+id+",request:setname,apikey:"+apiKey+",newname:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getInt("socketid") == id){
                        PsSocket.this.name = data.getString("newname");
						listener.onSetNameReceived(data.getString("newname"));
					} else {
                        PsSocket.this.name = PsSocket.this.previousName;
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
		Server.sendAndRecieve("{socketid:"+id+",request:turnon,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
				if(result.equals("switchRequestTrue")){
					listener.success();
				} else {
					listener.failed();
				}
			}

            @Override
            public void onReceiveFailure() {
                listener.failed();
            }
        });
		
	}
	
	/**
	 * Sends a turn off packet to the server.
	 */
	public void turnOff(final GenericListener listener){
		Server.sendAndRecieve("{socketid:"+id+",request:turnoff,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
				if(result.equals("switchRequestTrue")){
					listener.success();
				} else {
					listener.failed();
				}
			}

            @Override
            public void onReceiveFailure() {
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
}
