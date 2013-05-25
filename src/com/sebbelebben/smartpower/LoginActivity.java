package com.sebbelebben.smartpower;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity for logging in to the SmartPower service.
 */
public class LoginActivity extends Activity {
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	private ProgressBar mProgressBar;
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Finds the views on the login screen
		ImageButton loginButton = (ImageButton) findViewById(R.id.loginbutton);
		mUsernameBox = (EditText) findViewById(R.id.usernamebox);
		mPasswordBox = (EditText) findViewById(R.id.passwordbox);
        TextView loginLabel = (TextView) findViewById(R.id.login_label);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);

        // Sets the typeface for the views so that they match the look of the app.
		Typeface robotoThin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
		loginLabel.setTypeface(robotoThin);
		mUsernameBox.setTypeface(robotoThin);
		mPasswordBox.setTypeface(robotoThin);

		// Loads the preferences if available.
		loadPrefs();
		
		//Listens for when the login button is pressed.
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String username = mUsernameBox.getText().toString();
				String password = mPasswordBox.getText().toString();
				mUser = new User(username, password);
				logIn();
			}
		});
	}

    /**
     * Logs in by using the input provided via the EditViews
     */
	private void logIn() {
		// Show the progress bar
		mProgressBar.setVisibility(View.VISIBLE);
		
		//Initiates the User with the new username & password
		mUser.logIn(new Server.GenericListener() {
			@Override
			public void sucess() {
				// Hide the progress bar
				mProgressBar.setVisibility(View.GONE);
				
				saveUserToPrefs();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("User", mUser);
                startActivity(intent);
                finish();
			}
			
			@Override
			public void failed() {
				// Hide the progress bar
				mProgressBar.setVisibility(View.GONE);
				Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
			}
		});
	}

    /**
     * Handles the data stored in the preferences.
     */
	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String user = sp.getString("USER",null);
		try {
			JSONObject jUser;
			if(user == null) mUser = null;
			else {
				jUser = new JSONObject(user);
				mUser = new User(jUser.getString("Username"), jUser.getString("Password"));
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
		} else if(mUser != null){
		    //If the user ain't logged in then the password & username is entered into the boxes for them
			mUsernameBox.setText(mUser.getUserName());
			mPasswordBox.setText(mUser.getPassword());
		}
	}

    /**
     * Saves the user to SharedPreferences.
     */
    private void saveUserToPrefs() {
        JSONObject jUser = new JSONObject();
        try {
            jUser.put("Username", mUser.getUserName());
            jUser.put("Password", mUser.getPassword());
            jUser.put("Logged in", mUser.loginStatus());
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            Editor edit = sp.edit();
            edit.putString("USER", jUser.toString());
            edit.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
