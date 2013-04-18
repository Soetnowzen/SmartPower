package com.sebbelebben.smartpower;

import java.io.*;
import java.net.*;

public class Server {
	public static String sendAndRecieve(String message){
		String[] ret = new String[10];
		try{
		     Socket socket = new Socket("bregell.mine.nu", 39500);
		     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		     out.println("Android#"+message);
		     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		     ret = in.readLine().split("#");
		   } catch (UnknownHostException e) {
			   e.printStackTrace();
		   } catch  (IOException e) {
			   e.printStackTrace();
		   }
		
		return ret[1];
	}
}
