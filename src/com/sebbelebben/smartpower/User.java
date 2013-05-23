package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.*;

import com.sebbelebben.smartpower.Server.*;

/**
 * 
 * @author Johan Bregell
 *
 */
public class User implements Serializable, Graphable   {
	
	private static final long serialVersionUID = 5952542690960401843L;
	private String userName;
	private String password;
	private boolean loggedIn;
	private String apiKey;
	private PowerStrip[] powerStrips;
	
	/**
	 * Creates a User with the supplied name and password.
	 * @param userName
	 * @param password
	 */
	public User(String userName, String password){
		this.loggedIn = false;
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * Returns the username of the User.
	 * @return
	 */
	public String getUserName(){
		return this.userName;
	}
	
	/**
	 * Returns the password of the User.
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Returns the login status of the User.
	 * @return
	 */
	public boolean loginStatus(){
		return this.loggedIn;
	}
	
	/**
	 * Log in the User by supplying a listener to the server class and the string to send to the server.
	 * @param listener
	 */
	public void logIn(final OnLoginListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:login,password:"+password+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						loggedIn = data.getBoolean("login");
						if(loggedIn) {
							apiKey = data.getString("apikey");
							listener.onLoginSuccess();
						} else {
							listener.onLoginFailure();
						}
					} else {
						listener.onLoginFailure();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * Logs out the User
	 */
	public void logOut(){
		loggedIn = false;
	}
	
	/**
	 * Returns the saved list of PowerStrips connected to the User, or updates the list and then returns it.
	 * @param update If set to true the list will be updated.
	 * @return  
	 */
	public PowerStrip[] getPowerStrips(Boolean update){
		if(this.powerStrips.equals(null) || update){
			this.getPowerStrips(new OnPowerStripReceiveListener() {
				
				@Override
				public void onPowerStripReceive(PowerStrip[] powerStrips) {
					User.this.powerStrips  = powerStrips;
				}
				
				@Override
				public void failed() {
					User.this.powerStrips = null;
				}

			});
		}
		return powerStrips;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the User's connected PowerStrips.
	 * @param listener
	 */
	private void getPowerStrips(final OnPowerStripReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:powerstrips,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<PowerStrip> powerStripList = new ArrayList<PowerStrip>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						if(!data.get("powerstrips").equals(null)){
							JSONArray powerStrips = data.getJSONArray("powerstrips");
							for(int i = 0; i < powerStrips.length(); i++){
								JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
								powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialid"),apiKey,JSONpowerStrip.getString("name")));
							}
							listener.onPowerStripReceive(powerStripList.toArray(new PowerStrip[0]));
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
	 * Returns the saved list of PowerStrips connected to the User with their Sockets set, or updates the list and then returns it.
	 * @param update If set to true the list will be updated.
	 * @return
	 */
	public PowerStrip[] getPowerStripsAndSockets(Boolean update){
		if(this.powerStrips.equals(null) || update){
			this.getPowerStrips(new OnPowerStripReceiveListener() {
				
				@Override
				public void onPowerStripReceive(PowerStrip[] powerStrips) {
					User.this.powerStrips  = powerStrips;
				}
				
				@Override
				public void failed() {
					User.this.powerStrips = null;
				}

			});
		}
		return powerStrips;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the User's connected PowerStrips with their PsSockets.
	 * @param listener
	 */
	private void getPowerStripsAndSockets(final OnPowerStripAndSocketReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:powerstripsandsockets,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<PowerStrip> powerStripList = new ArrayList<PowerStrip>();
				ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						if(!data.get("powerstrips").equals(null)){
							JSONArray powerStrips = data.getJSONArray("powerstrips");
							for(int i = 0; i < powerStrips.length(); i++){
								JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
								JSONArray psSockets = JSONpowerStrip.getJSONArray("sockets");
								for(int j = 0; j < psSockets.length(); j++){
									JSONObject JSONsocket = psSockets.getJSONObject(j);
									psSocketList.add(new PsSocket(JSONsocket.getInt("socketid"), JSONsocket.getString("name"), apiKey));
								}
								powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialid"),apiKey,JSONpowerStrip.getString("name"),psSocketList.toArray(new PsSocket[0])));
							}
							listener.onPowerStripAndSocketReceive(powerStripList.toArray(new PowerStrip[0]));
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
	 * Creates a new group connected to the User on the server.
	 * @param name
	 * @param listener
	 */
	public void createNewGroup(String name, final OnNewGroupReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:newgroup,apikey:"+apiKey+",name:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						listener.onNewGroupReceive(new Group(data.getInt("id"), data.getString("name"), apiKey));
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
	 * Creates a listener that waits for the server to supply the User's groups.
	 * @param listener
	 */
	public void getGroups(final OnGroupsReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:groups,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Group> groupList = new ArrayList<Group>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						JSONArray groups = data.getJSONArray("groups");
						for(int i = 0; i < groups.length(); i++){
							JSONObject JSONgroupList = groups.getJSONObject(i);
							groupList.add(new Group(JSONgroupList.getInt("id"),JSONgroupList.getString("name"),apiKey));
						}
						listener.onGroupReceive(groupList.toArray(new Group[0]));
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
	 * Creates a listener that waits for the server to supply the User's consumption between the given dates.
	 * @param start
	 * @param end
	 * @param listener
	 */
	public void getConsumption(Date start, Date end, final OnConsumptionReceiveListener listener){
		DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSSZ", Locale.ENGLISH);
		Server.sendAndRecieve("{username:"+userName+",request:consumption,apikey:"+apiKey+",startdate:"+dd.format(start)+",enddate:"+dd.format(end)+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<Consumption> consumptionList = new ArrayList<Consumption>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
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
}
