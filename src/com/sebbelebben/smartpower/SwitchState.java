package com.sebbelebben.smartpower;

import java.net.*;
import java.io.*;

public class SwitchState{

	public void switchOn(String powerStrip_serialId, Integer socket_id, Integer powerStrip_type){
		//Bregell Power Strip
		if(powerStrip_type == 1){
			PrintWriter out = null;
			Socket socket = null;
			try{
				socket = new Socket("bregell.mine.nu",39500);
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch(Exception e){
				System.out.println("Something went wrong");
			}
			String message = "Android#{\"SerialId\":\""+powerStrip_serialId+"\",\"SocketNumber\":"+socket_id+",\"Action\":true}";
			out.println(message);
			out.close();
			try {
				socket.close();
			} catch (IOException e) {

			}
		}
		//Swetzen Power Strip
		else if(powerStrip_type == 2){
		
		}
		
	}
	
	public void switchOff(String powerStrip_serialId, Integer socket_id, Integer powerStrip_type){
		//Bregell Power Strip
		if(powerStrip_type == 1){
			PrintWriter out = null;
			Socket socket = null;
			try{
				socket = new Socket("bregell.mine.nu",39500);
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch(Exception e){
				System.out.println("Something went wrong");
			}
			String message = "Android#{\"SerialId\":\""+powerStrip_serialId+"\",\"SocketNumber\":"+socket_id+",\"Action\":false}";
			out.println(message);
			out.close();
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Swetzen Power Strip
		else if(powerStrip_type == 2){
		
		}
	}

}