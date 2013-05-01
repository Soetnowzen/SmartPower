package com.sebbelebben.smartpower;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.sebbelebben.smartpower.Server.*;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PowerStripActivity extends SherlockActivity {
	private PowerStrip powerStrip;
	private ListView psSocketList;
	private TextView powerStripSerialId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_powerstrip);
		
		//Get intents
		powerStrip = (PowerStrip) getIntent().getSerializableExtra("PowerStrip");
		
		//Set textview to the powerSrip serial id name
		powerStripSerialId = (TextView) findViewById(R.id.powerStripSerialId);
		powerStripSerialId.setText(powerStrip.toString());
		
		//Start to populate list
		psSocketList = (ListView) findViewById(R.id.listViewPsSocket);
		psSocketList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				PsSocket psSocket = (PsSocket) arg0.getItemAtPosition(arg2);
				Toast.makeText(getApplicationContext(), psSocket.getId(),
				          Toast.LENGTH_SHORT).show();
			}
		});
		powerStrip.getSockets(new OnSocketReceiveListener() {
			@Override
			public void onSocketReceive(PsSocket[] sockets) {
				ArrayList<PsSocket> psarray = new ArrayList<PsSocket>();
				for(int i=0; i < sockets.length; i++){
					psarray.add(sockets[i]);
				}
				ArrayAdapter<PsSocket> arrayAdapter = new ArrayAdapter<PsSocket>(PowerStripActivity.this, android.R.layout.simple_list_item_1, psarray);
				psSocketList.setAdapter(arrayAdapter);	
			}
			
			@Override
			public void failed() {
				// TODO Auto-generated method stub
				
			}
		});	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.main, menu);
			return true;
	}
}