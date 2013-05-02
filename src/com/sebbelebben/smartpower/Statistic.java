package com.sebbelebben.smartpower;

//import android.R.string;
import android.os.AsyncTask;
import android.text.format.DateFormat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.sql.Date;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Statistic extends AsyncTask<String, Integer, Consumption[]>{

	//make sure param[0] == ip, param[1] = port;
	//all together its 7 params we need
	@Override
	protected Consumption[] doInBackground(String... params) {
		// TODO Auto-generated method stub

		try{
			String msg = String.format("Android#{Starttime:%s" +
			",Endtime:%s" + 
			",SerialId:%s" + 
			",SocketNumber/%s" + 
			";SerialId:%s/GroupId:%s}", params[2],params[3],params[4], params[5],params[6],params[7]);
			String reply = Server.sendAndRecieve(msg);

			JSONObject js = new JSONObject(reply);
			JSONArray dataArray = js.getJSONArray("data");
			Consumption[] result = new Consumption[dataArray.length()];
			for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = dataArray.getJSONObject(i);
				int power = jo.getInt("power");
				String time = jo.getString("time");
				result[i] = new Consumption(time,power);
			}
			return result;			
			
		}catch(JSONException e){
			return null;
		}		
	}
	
	

}
