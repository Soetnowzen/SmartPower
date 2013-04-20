package com.sebbelebben.smartpower;

import java.util.ArrayList;
import org.json.*;

public class User {
	private String userName;
	private String password;
	private boolean loggedIn;
	
	public User(String userName, String password){
		this.loggedIn = false;
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public boolean loginStatus(){
		return this.loggedIn;
	}
	
	public void logIn(){
		String message = "{User:"+this.userName+",Password:"+password+"}";
		String result = Server.sendAndRecieve(message);
		try {
			JSONObject data = new JSONObject(result);
			if (data.getString("User") == userName){
				loggedIn = data.getBoolean("Login");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void logOut(){
		loggedIn = false;
	}
	
	public PowerStrip[] getPowerStrips(){
		ArrayList<PowerStrip> powerStripList = new ArrayList<PowerStrip>();
		String result = Server.sendAndRecieve("{User:"+userName+",Request:powerstrips}");
		try {
			JSONObject data = new JSONObject(result);
			if (data.getString("User") == userName){
				JSONArray powerStrips = data.getJSONArray("powerStrips");
				for(int i = 0; i < powerStrips.length(); i++){
					JSONObject JSONpowerStrip = powerStrips.getJSONObject(i);
					powerStripList.add(new PowerStrip(JSONpowerStrip.getInt("id"),JSONpowerStrip.getString("serialId"),1));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (PowerStrip[]) powerStripList.toArray();
	}
}
