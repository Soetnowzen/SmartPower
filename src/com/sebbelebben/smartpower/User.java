package com.sebbelebben.smartpower;

import java.io.Serializable;
import java.util.*;
import org.json.*;
import com.sebbelebben.smartpower.Server.*;

public class User implements Serializable   {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5952542690960401843L;
	private String userName;
	private String password;
	private boolean loggedIn;
	private String apiKey;
	
	public User(String userName, String password){
		this.loggedIn = false;
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean loginStatus(){
		return this.loggedIn;
	}
	
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
	
	public void logOut(){
		loggedIn = false;
	}
	
	public void getPowerStrips(final OnPowerStripReceiveListener listener){
		Server.sendAndRecieve("{username:"+userName+",request:powerstrips,apikey:"+apiKey+"}", new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				ArrayList<PowerStrip> powerStripList = new ArrayList<PowerStrip>();
				try {
					JSONObject data = new JSONObject(result);
					if (data.getString("username").equals(userName)){
						JSONArray powerStrips = data.getJSONArray("powerstrips");
						for(int i = 0; i < powerStrips.length(); i++){
							JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
							powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialid"),1,apiKey));
						}
						listener.onPowerStripReceive(powerStripList.toArray(new PowerStrip[0]));
					} else {
						listener.failed();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
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
}
