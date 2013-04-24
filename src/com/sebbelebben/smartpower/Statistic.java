package com.sebbelebben.smartpower;

//import android.R.string;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Statistic extends AsyncTask<String, Integer, Integer[]>{

	//make sure param[0] == ip, param[1] = port;
	//all together its 7 params we need
	@Override
	protected Integer[] doInBackground(String... params) {
		// TODO Auto-generated method stub

		try{
			String msg = String.format("Android#{Starttime:%s" +
			",Endtime:%s" + 
			",SerialId:%s" + 
			",SocketNumber/%s" + 
			";SerialId:%s/GroupId:%s}", params[2],params[3],params[4], params[5],params[6],params[7]);
			String reply = null;
//			reply = user.sendandreceive(msg);
			//assume has received
			JSONObject js = new JSONObject(reply);
			String data = js.getString("Data");
			JSONArray ja = new JSONArray();
		}catch(JSONException e){
			
		}
		
		return null;
		
	}
	
	

}
