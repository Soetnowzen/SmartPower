package com.sebbelebben.smartpower;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.sebbelebben.smartpower.Server.OnLoginListener;

public class LoginActivity extends Activity {
	
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
		window.setFlags(WindowManager.LayoutParams.FLAG_DITHER, WindowManager.LayoutParams.FLAG_DITHER);
		setContentView(R.layout.activity_login);
		
		//Finds the loginbutton on the login screen
		Button loginButton = (Button) findViewById(R.id.loginbutton);
		mUsernameBox = (EditText) findViewById(R.id.usernamebox);
		mPasswordBox = (EditText) findViewById(R.id.passwordbox);
		
		Typeface robotoThin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
		TextView loginLabel = (TextView) findViewById(R.id.login_label);
		loginLabel.setTypeface(robotoThin);
		mUsernameBox.setTypeface(robotoThin);
		mPasswordBox.setTypeface(robotoThin);

		//Checks the old preferences
		loadPrefs();

		
		//Listens for when the button is pressed & takes the username & password...
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String username = mUsernameBox.getText().toString();
				String password = mPasswordBox.getText().toString();
				mUser = new User(username, password);
				logIn();
			}
		});
	}
	
	private void logIn() {
		//Initiates the User with the new username & password
		mUser.logIn(new OnLoginListener() {
			@Override
			public void onLoginSuccess() {
				//Creates a JSONObject to save the user so the username & password will be needed to be 
				//input every time
				JSONObject jUser = new JSONObject();
				try {
					jUser.put("Username", mUser.getUserName());
					jUser.put("Password", mUser.getPassword());
					jUser.put("Loggged in", mUser.loginStatus());
					savePrefs(jUser.toString());
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("User", mUser);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onLoginFailure() {
				Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
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
				if(jUser.getBoolean("Logged in")) {
					logIn();
				}
			}
		} catch (JSONException error) {
			error.printStackTrace();
		}
		//If you're already logged in then skip this activity/screen
		if(mUser != null && mUser.loginStatus()) {
			startActivity(new Intent(this, GraphActivity.class));
		} else if(mUser != null){ //If the user ain't logged in then the password & username is entered into the boxes for them
			mUsernameBox.setText(mUser.getUserName());
			mPasswordBox.setText(mUser.getPassword());
		}
	}

	private void savePrefs(String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString("USER", value);
		edit.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
