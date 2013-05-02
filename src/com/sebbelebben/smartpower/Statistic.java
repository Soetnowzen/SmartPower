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

import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.Server.OnReceiveListener;

public class Statistic {

	//make sure param[0] == ip, param[1] = port;
	public static void getStatistics(final OnConsumptionReceiveListener listener, String... params) {
		// TODO Auto-generated method stub

		String msg = String.format("Android#{Starttime:%s" +
				",Endtime:%s" + 
				",SerialId:%s" + 
				",SocketNumber/%s" + 
				";SerialId:%s/GroupId:%s}", params[2],params[3],params[4], params[5],params[6],params[7]);
		Server.sendAndRecieve(msg, new OnReceiveListener() {
			@Override
			public void onReceive(String result) {
				JSONObject js;
				try {
					js = new JSONObject(result);

					JSONArray dataArray = js.getJSONArray("data");
					Consumption[] reply = new Consumption[dataArray.length()];
					for(int i = 0; i < dataArray.length(); i++){
						JSONObject jo = dataArray.getJSONObject(i);
						int power = jo.getInt("power");
						String time = jo.getString("time");
						reply[i] = new Consumption(time,power);
					}
					listener.onConsumptionReceive(reply);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.failed();
				}

			}
		});

	}



}
