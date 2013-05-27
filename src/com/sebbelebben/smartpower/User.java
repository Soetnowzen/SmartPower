package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.sebbelebben.smartpower.Server.GenericListener;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnGroupsReceiveListener;
import com.sebbelebben.smartpower.Server.OnNewGroupReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;


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
			public void onReceiveSuccess(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						loggedIn = data.getBoolean("login");
						if(loggedIn) {
							apiKey = data.getString("apikey");
							listener.success();
						} else {
							listener.failed();
						}
					} else {
						listener.failed();
					}
				} catch (Exception e) {
					e.printStackTrace();
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
	 * Logs out the User
	 */
	public void logOut(){
		userName = "";
		password = "";
		loggedIn = false;
	}
	
	/**
	 * Returns the saved list of PowerStrips connected to the User with their Sockets set.
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
			public void onReceiveSuccess(String result) {
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						if(!data.get("powerstrips").equals(null)){
							JSONArray powerStrips = data.getJSONArray("powerstrips");
                            ArrayList<PowerStrip> powerStripList = new ArrayList<PowerStrip>();
							for(int i = 0; i < powerStrips.length(); i++){
								JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
								JSONArray psSockets = JSONpowerStrip.getJSONArray("sockets");
                                ArrayList<PsSocket> psSocketList = new ArrayList<PsSocket>();
								for(int j = 0; j < psSockets.length(); j++){
									JSONObject JSONsocket = psSockets.getJSONObject(j);
									if(JSONsocket.getInt("status") == 1){
										psSocketList.add(new PsSocket(JSONsocket.getInt("socketid"),JSONsocket.getString("name"),apiKey,true));
									} else if (JSONsocket.getInt("status") == 0) {
										psSocketList.add(new PsSocket(JSONsocket.getInt("socketid"),JSONsocket.getString("name"),apiKey,false));
									} else {
										listener.failed();
									}
								}

								if(JSONpowerStrip.getInt("status") == 1){
									powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialid"),apiKey,JSONpowerStrip.getString("name"),psSocketList.toArray(new PsSocket[0]),true));
								} else if (JSONpowerStrip.getInt("status") == 0) {
									powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialid"),apiKey,JSONpowerStrip.getString("name"),psSocketList.toArray(new PsSocket[0]),false));
								} else {
									listener.failed();
								}
							}
							User.this.powerStrips = powerStripList.toArray(new PowerStrip[0]);
							listener.success();
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
	 * Creates a new group connected to the User on the server.
	 * @param name
	 * @param listener
	 */
	public void createNewGroup(String name, final OnNewGroupReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:newgroup,apikey:"+apiKey+",name:"+name+"}", new OnReceiveListener() {
			@Override
			public void onReceiveSuccess(String result) {
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

            @Override
            public void onReceiveFailure() {
                listener.failed();
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
			public void onReceiveSuccess(String result) {
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

            @Override
            public void onReceiveFailure() {
                listener.failed();
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
			public void onReceiveSuccess(String result) {
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

            @Override
            public void onReceiveFailure() {
                listener.failed();
            }
        });
	}
	
	/**
	 * @param ps is the PowerStrip that is to be added to favorites
	 * @param context
	 * Takes a PowerStrip that the method save in the SharedPreferences (so favorites won't be lost if the app crashes)
	 * it also saves it as a favorite.
	 */
    public void addFavorite(PsSocket ps, Context context) {
    	Toast.makeText(context, "Adding "+ps.toString(), Toast.LENGTH_SHORT).show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try {
    		//Check if @param favorite is null then make an empty array
    		JSONArray jsArray;
    		if(favorite == null) jsArray = new JSONArray();
    		else jsArray = new JSONArray(favorite);
		    jsArray.put(new JSONObject(ps.toJSON()));
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
    public void removeFavorite(PsSocket psSocket, Context context) {
    	Toast.makeText(context, "removing "+psSocket.toString(), Toast.LENGTH_SHORT).show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try {
    		JSONArray jsArray = new JSONArray();
    		JSONArray newJsArray = new JSONArray();
    		if(favorite != null) {
    			jsArray = new JSONArray(favorite);
	    		for(int index = 0; index < jsArray.length(); index++) {
	    			JSONObject comparablePS = (JSONObject) jsArray.get(index);
	    			if(comparablePS.getInt("id") != psSocket.getId()) newJsArray.put(comparablePS);
				}
    		}
    		Editor edit = sp.edit();
    		edit.putString("Favorite", newJsArray.toString());
    		edit.commit();
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    }

    /**
	 * @param context
	 * @return eturns all the favorite PowerStrips and return null if there are non.
	 */
    public boolean isFavorite(PsSocket psSocket, Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try {
    		JSONArray jsArray;
    		if(favorite == null) jsArray = new JSONArray();
    		else {
    			jsArray = new JSONArray(favorite);
    			for(int index = 0; index < jsArray.length(); index++) {
        			JSONObject comparablePS = (JSONObject) jsArray.get(index);
        			return comparablePS.getInt("id") == psSocket.getId();
    			}
    		}
    		
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
        return false;
    }

    /**
	 * @param context
	 * @return Returns all the favorite PowerStrips and return null if there are non.
	 */
    public ArrayList<PsSocket> getFavorite(Context context) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    	String favorite = sp.getString("Favorite", null);
    	try{
    		ArrayList<PsSocket> list = new ArrayList<PsSocket>();
    		if(favorite != null) {
	    		JSONArray jsArray = new JSONArray(favorite);
	    		for(int index = 0; index < jsArray.length(); index++) {
	    			JSONObject jAddablePsSocket = (JSONObject) jsArray.get(index);
	    			//PsSocket addablePsSocket = new PsSocket(jAddablePsSocket);
	    			PsSocket addablePsSocket = new PsSocket(jAddablePsSocket.getInt("id"), jAddablePsSocket.getString("name"),apiKey,jAddablePsSocket.getBoolean("status"));
	    			list.add(addablePsSocket);
	    		}
    		}
    		return list;
    		
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
        return new ArrayList<PsSocket>();
    }
}
