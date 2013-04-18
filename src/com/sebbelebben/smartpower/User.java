package com.sebbelebben.smartpower;

import org.json.*;

public class User {
	private String userName;
	private String password;
	Boolean loggedIn = false;
	public String getUserName(){
		return this.userName;
	}
	
	public void logIn(){
		String message = "{User:"+this.userName+",Password:"+password+"}";
		String result = Server.sendAndRecieve(message);
		try {
			JSONObject data = new JSONObject(result);
			if (data.get("User") == userName){
				loggedIn = data.getBoolean("Login");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void logOut(){
		loggedIn = false;
	}
}
