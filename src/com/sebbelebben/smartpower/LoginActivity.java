package com.sebbelebben.smartpower;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginActivity extends Activity {
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Finds the loginbutton on the login screen
		Button loginButton = (Button) findViewById(R.id.loginbutton);
		mUsernameBox = (EditText) findViewById(R.id.usernamebox);
		mPasswordBox = (EditText) findViewById(R.id.passwordbox);
		
		
		//Listens for when the button is pressed & takes the username & password...
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String username = mUsernameBox.getText().toString();
				String password = mPasswordBox.getText().toString();
				//Log.i("SmartPower",username +", "+password);
				PrintWriter output = null;
				BufferedReader input = null;
				try {
					Socket socket = new Socket("localhost", 39500);
					//Socket socket = new Socket("bregell.mine.nu", 39500);
					output = new PrintWriter(socket.getOutputStream(),true);
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output.println("Android#{User:"+username+", Password:"+password+"}:\n");
					String inputMessage = input.readLine();
					String tempUser = inputMessage.substring(14, username.length()+14);
					Log.i("SmartPower",tempUser);
					if(inputMessage.substring(14+username.length()).contains(",Login:true")) {
						//startActivity(new Intent(this, MainActivity.class));
					}
				} catch(IOException error) {
					error.toString();
					//error.printStackTrace();
				} finally {
					output.close();
					//input.close();
				}
				//Android#{User:<username>,Password:<password>}:\n
				//"Android#{User:"+Username+", Password:"+Password+"}:\n"
				
				//Android#{User:<username>,Login:<true/false>}
				//*,Login:true*
			}
			
		});
		/*aboutButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
        		Intent intent = new Intent(CounterActivity.this, AboutActivity.class);
        		startActivity(intent);
        	}
        });*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
