package com.sebbelebben.smartpower;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.sebbelebben.smartpower.Server.*;

public class UserActivity extends SherlockActivity {
	private User user;
	private ListView powerStripList;
	private TextView userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//Get user from intent
		user = (User) getIntent().getSerializableExtra("User");
		
		//Set textview to the users name
		userName = (TextView) findViewById(R.id.userName);
		userName.setText(user.getUserName());
		//Start to populate list
		powerStripList = (ListView) findViewById(R.id.listViewPowerStrip);
		powerStripList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				PowerStrip powerStrip = (PowerStrip) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(UserActivity.this, PowerStripActivity.class);
				intent.putExtra("PowerStrip", powerStrip);
				startActivity(intent);
			}
		});
		user.getPowerStrips(new OnPowerStripReceiveListener() {
			
			@Override
			public void onPowerStripReceive(PowerStrip[] powerStrips) {
				ArrayList<PowerStrip> psarray = new ArrayList<PowerStrip>();
				for(int i=0; i < powerStrips.length; i++){
					psarray.add(powerStrips[i]);
				}
				ArrayAdapter<PowerStrip> arrayAdapter = new ArrayAdapter<PowerStrip>(UserActivity.this, android.R.layout.simple_list_item_1, psarray);
				powerStripList.setAdapter(arrayAdapter);
			}
			
			@Override
			public void failed() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
