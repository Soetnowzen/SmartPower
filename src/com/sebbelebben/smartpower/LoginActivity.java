package com.sebbelebben.smartpower;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button loginbutton = (Button) findViewById(R.id.loginbutton);
		
		loginbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				EditText UsernameBox = (EditText) findViewById(R.id.usernamebox);
				EditText PasswordBox = (EditText) findViewById(R.id.passwordbox);
				String Username = UsernameBox.getText().toString();
				String Password = PasswordBox.getText().toString();
				//Android#{"User":"<username","Password":"<password>"}:\n
				//Android#{"User":Username,
				//	"Password":Password}:\n
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
