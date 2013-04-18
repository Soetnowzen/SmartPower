package com.sebbelebben.smartpower;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;

public class Statistic extends AsyncTask<String, Integer, Integer[]>{

	//make sure param[0] == ip, param[1] = port;
	//all together its 7 params we need
	@Override
	protected Integer[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		InetSocketAddress ipep = new InetSocketAddress(params[0],Integer.parseInt(params[1]));
		try{
			Socket tcpSocket = new Socket(ipep.getAddress(), ipep.getPort());
			String msg = "Android#{Starttime:" + params[2] +
					",Endtime:" + params[3] + 
					",\nSerialId:" + params[4] +
					",SocketNumber/" + params[5] +
					";SerialId:" + params[6] +
					"/GroupId:" + params[7] + "}";
			byte[] buffer = msg.getBytes("US-ASCII");
			tcpSocket.getOutputStream().write(buffer);
			
		}catch(IOException e){
			
		}
		
		return null;
		
	}
	
	

}
