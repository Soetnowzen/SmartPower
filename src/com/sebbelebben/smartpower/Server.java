package com.sebbelebben.smartpower;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;
import android.util.Log;

/**
 * A class to send and receive messages to and from the server in a asynchronous task.
 * Also implements interfaces for the different types of requests that can be sent to the server.
 * @author Johan Bregell
 *
 */
public class Server {
	
	/**
	 * Main method of the server creates a send task.
	 * @param message
	 * @param listener
	 */
	public static void sendAndRecieve(String message, OnReceiveListener listener){
		SendTask sendTask = new SendTask(listener);
		sendTask.execute(message);
	}
	
	/**
	 * 
	 * @author Johan Bregell
	 *
	 */
	private static class SendTask extends AsyncTask<String, Void, String> {
		private OnReceiveListener mListener;
		
		/**
		 * Creates a sendtask that can send and receive messages from the server.
		 * @param listener
		 */
		public SendTask(OnReceiveListener listener) {
			mListener = listener;
		}
		
		/**
		 * Sends a message to the server and watis for the response.
		 */
		@Override
		protected String doInBackground(String... params) {
			String[] ret = new String[2];
			String message = params[0];
			try{
			     Socket socket = new Socket("bregell.mine.nu", 39500);
			     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			     out.println("Android#"+message);
			     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			     ret = in.readLine().split("#");
			     socket.close();
			   } catch (UnknownHostException e) {
				   e.printStackTrace();
			   } catch  (IOException e) {
				   e.printStackTrace();
			   }
			
			return ret[1];
		}
		
		/**
		 * Gives the listener the result from the server.
		 */
		@Override
		protected void onPostExecute(String result) {
			Log.i("onPostExecute", result);
			mListener.onReceive(result);
		}
	}
	
	public static interface OnSetNameReceiveListener{
		void onSetNameReceived(String name);
		void failed();
	}
	
	public static interface OnReceiveListener {
		void onReceive(String result);
	}
	
	public static interface OnSocketReceiveListener {
		void onSocketReceive(PsSocket[] sockets);
		void failed();
	}
	
	public static interface OnConsumptionReceiveListener {
		void onConsumptionReceive(Consumption[] consumption);
		void failed();
	}
	
	public static interface OnNewGroupReceiveListener {
		void onNewGroupReceive(Group group);
		void failed();
	}
	
	public static interface OnGroupsReceiveListener {
		void onGroupReceive(Group[] groups);
		void failed();
	}
	
	public static interface OnPowerStripReceiveListener {
		void onPowerStripReceive(PowerStrip[] powerStrips);
		void failed();
	}
	
	public static interface OnPowerStripAndSocketReceiveListener {
		void onPowerStripAndSocketReceive(PowerStrip[] powerStrips);
		void failed();
	}
	
	public static interface OnLoginListener {
		void onLoginSuccess();
		void onLoginFailure();
	}
}
