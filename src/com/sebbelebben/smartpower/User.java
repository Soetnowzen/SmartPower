package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

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
	
	public String toJSON(){
		String PowerStripsJSON = "";
		for(int i = 0; i < powerStrips.length; i++){
			if(i != 1){
				PowerStripsJSON.concat(",");
			}
			PowerStripsJSON.concat(powerStrips[i].toJSON());
		}
		return "{" +
					"\"username\":\""+this.userName+"\"," +
					"\"password\":\""+this.password+"\"," +
					"\"apikey\":\""+this.apiKey+"\"," +
					"\"powerstrips\":["+PowerStripsJSON+"]," +
				"}";
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
	public void logIn(final GenericListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:login,password:"+password+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						loggedIn = data.getBoolean("login");
						if(loggedIn) {
							apiKey = data.getString("apikey");
							listener.sucess();
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
	 * Logs out the User
	 */
	public void logOut(){
		userName = "";
		password = "";
		loggedIn = false;
	}
	
	/**
	 * Returns the saved list of PowerStrips connected to the User with their Sockets set.
	 * @param update If set to true the list will be updated.
	 * @return
	 */
	public PowerStrip[] getPowerStrips(){
		return powerStrips;
	}
	
	/**
	 * Creates a listener that waits for the server to supply the User's connected PowerStrips with their PsSockets.
	 * @param listener
	 */
	public void updateUser(final GenericListener listener){
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
							User.this.powerStrips = powerStripList.toArray(new PowerStrip[0]);
							listener.sucess();
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
	
	/**
	 * @param ps is the PowerStrip that is to be added to favorites
	 * @param context
	 * Takes a PowerStrip that the method save in the SharedPreferences (so favorites won't be lost if the app crashes)
	 * it also saves it as a favorite.
	 */
    public void addFavorite(PowerStrip ps, Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try {
    		//Check if @param favorite is null then make an empty array
    		JSONArray jsArray = new JSONArray(favorite);
		    jsArray.put(ps);
		    Editor edit = sp.edit();
		    edit.putString("Favorite", jsArray.toString());
		    edit.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
	 * @param ps is the PowerStrip that will be removed if it exist.
	 * @param context
	 * Removes the given PowerStrip from favorites if it exists.
	 */
    public void removeFavorite(PowerStrip ps, Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try {
    		JSONArray jsArray = new JSONArray(favorite);
    		JSONArray newJsArray = new JSONArray();
    		for(int index = 0; index < jsArray.length(); index++) {
    			PowerStrip comparablePS = (PowerStrip) jsArray.get(index);
    			if(!ps.compareTo(comparablePS)) newJsArray.put(index, comparablePS);
			}
    		Editor edit = sp.edit();
    		edit.putString("Favorite", newJsArray.toString());
    		edit.commit();
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    }

    /*public boolean isFavorite() {
        return true;
    }*/

    /**
	 * @param context
	 * Returns all the favorite PowerStrips and return null if there are non.
	 */
    public ArrayList<PowerStrip> getFavorite(Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try{
    		JSONArray jsArray = new JSONArray(favorite);
    		ArrayList<PowerStrip> list = new ArrayList<PowerStrip>();
    		for(int index = 0; index < jsArray.length(); index++) {
    			PowerStrip addablePowerStrip = (PowerStrip) jsArray.get(index);
    			list.add(addablePowerStrip);
    		}
    		return list;
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
        return null;
    }
}
