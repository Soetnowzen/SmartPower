package com.sebbelebben.smartpower;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
	
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//Checks the old preferences
		loadPrefs();
		
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
				
				mUser = new User(username, password);
				mUser.logIn();
				if(mUser.loginStatus()) {
					JSONObject jUser = new JSONObject();
					try {
						jUser.put("Username", mUser.getUserName());
						jUser.put("Password", mUser.getPassword());
						jUser.put("Loggged in", mUser.loginStatus());
						savePrefs(jUser.toString());
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//Android#{User:<username>,Password:<password>}:\n
				//"Android#{User:"+Username+", Password:"+Password+"}:\n"
				
				//Android#{User:<username>,Login:<true/false>}
				//*,Login:true*
			}
			
		});
	}
	
	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String user = sp.getString("USER",null);
		try {
			JSONObject jUser;
			if(user == null) mUser = null;
			else {
				jUser = new JSONObject(user);
				mUser = new User(jUser.getString("Username"),jUser.getString("Password"));
				if(jUser.getBoolean("Logged in")) mUser.logIn();
			}
		} catch (JSONException error) {
			error.printStackTrace();
		}
		//If you're already logged in then skip this activity/screen
		if(mUser != null && mUser.loginStatus()) {
			startActivity(new Intent(this, MainActivity.class));
		}
	}

	private void savePrefs(String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString("USER", value);
		edit.commit();
	}
	
	/*@Override
	protected void onStop() {
		super.onStop();
		// We need an Editor object to make preference changes.
	    // All objects are from android.context.Context
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("silentMode", mSilentMode);

	    // Commit the edits!
	    editor.commit();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
