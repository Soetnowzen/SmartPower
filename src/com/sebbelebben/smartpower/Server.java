package com.sebbelebben.smartpower;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;
import android.util.Log;

public class Server {
	public static void sendAndRecieve(String message, OnReceiveListener listener){
		SendTask sendTask = new SendTask(listener);
		sendTask.execute(message);
	}
	
	private static class SendTask extends AsyncTask<String, Void, String> {
		private OnReceiveListener mListener;
		
		public SendTask(OnReceiveListener listener) {
			mListener = listener;
		}
		
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
			   } catch (UnknownHostException e) {
				   e.printStackTrace();
			   } catch  (IOException e) {
				   e.printStackTrace();
			   }
			
			return ret[1];
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.i("onPostExecute", result);
			mListener.onReceive(result);
		}
	}
	
	public static interface OnReceiveListener {
		void onReceive(String result);
	}
}
