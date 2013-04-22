package com.sebbelebben.smartpower;

import java.util.ArrayList;
import org.json.*;

import com.sebbelebben.smartpower.Server.OnReceiveListener;

public class User {
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
					if (data.getString("username") == userName){
						loggedIn = data.getBoolean("login");
						if(loggedIn) {
							listener.onLoginSuccess();
						} else {
							listener.onLoginFailure();
						}
						apiKey = data.getString("apikey");
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
					if (data.getString("username") == userName){
						JSONArray powerStrips = data.getJSONArray("powerStrips");
						for(int i = 0; i < powerStrips.length(); i++){
							JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
							powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialId"),1,apiKey));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onPowerStripReceive((PowerStrip[]) powerStripList.toArray());
			}
		});
	}
	
	public static interface OnPowerStripReceiveListener {
		void onPowerStripReceive(PowerStrip[] powerStrips);
	}
	
	public static interface OnLoginListener {
		void onLoginSuccess();
		void onLoginFailure();
	}
}
