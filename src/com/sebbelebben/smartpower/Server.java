package com.sebbelebben.smartpower;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

/**
 * A class to send and receive messages to and from the server in a asynchronous task.
 * Also implements interfaces for the different types of requests that can be sent to the server.
 *
 * @author Johan Bregell
 */
public class Server {
	
	/**
	 * Sends a message to the server and calls the listener methods when received a response.
     *
	 * @param message The message to be sent to the server.
	 * @param listener A listener to notify when the message has completed.
	 */
	public static void sendAndRecieve(String message, OnReceiveListener listener){
		SendTask sendTask = new SendTask(listener);
		sendTask.execute(message);
	}
	
	/**
	 * Async task for sending a message to the server.
     *
	 * @author Johan Bregell
	 */
	private static class SendTask extends AsyncTask<String, Void, String> {
		private OnReceiveListener mListener;
		
		/**
		 * Creates an async task that can send and receive messages from the server.
         *
		 * @param listener The listener to be called when a response is received.
		 */
		public SendTask(OnReceiveListener listener) {
			mListener = listener;
		}
		
		/**
		 * Sends a message to the server and waits for the response.
		 */
		@Override
		protected String doInBackground(String... params) {
			String[] ret = new String[2];
			ret[0] = null;
			ret[1] = null;
			String message = params[0];
			try {
			    Socket socket = new Socket("bregell.mine.nu", 39500);
			    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			    out.println("Android#"+message);
			    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    ret = in.readLine().split("#");
			    socket.close();
			} catch (Exception e) {
                Log.i("SmartPower", "Error in server!");
				e.printStackTrace();
			}
			if(ret[0].equals("Android")){
				return ret[1];
			} else {
				return ret[0];
			}
		}
		
		/**
		 * Gives the listener the result from the server.
		 */
		@Override
		protected void onPostExecute(String result) {
            if(result != null) {
			    Log.i("SmartPower", result);
			    mListener.onReceiveSuccess(result);
            } else {
                mListener.onReceiveFailure();
            }
		}
	}

    // Interfaces for handling responses from the server

	public static interface OnSetNameReceiveListener{
		void onSetNameReceived(String name);
		void failed();
	}
	
	public static interface OnReceiveListener {
		void onReceiveSuccess(String result);
        void onReceiveFailure();
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
	
	public static interface GenericListener {
		void success();
		void failed();
	}
	
	public static interface OnUpdateListener {
		void onUpdateReceive(Boolean status);
		void failed();
	}
	
}

/*
import socket
HOST = 'bregell.mine.nu'
PORT = 39500
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))
s.sendall('Android#{socketid:17,request:turnoff,apikey:apikey1011}')
data = s.recv(1024)
print data
s.close()
*/
