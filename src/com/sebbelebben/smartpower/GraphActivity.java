package com.sebbelebben.smartpower;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.sebbelebben.smartpower.GraphView.Point;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;
import com.sebbelebben.smartpower.PowerStrip;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GraphActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		final GraphView graphView = (GraphView) findViewById(R.id.graphview);
		Intent intent = getIntent();
		final PowerStrip mPowerStrip = (PowerStrip) intent.getSerializableExtra("PowerStrip");
		
		
		/*
		List<Point> data = new ArrayList<Point>();
		// TODO: Use real implementation - this is only test data
		data.add(new Point(-3.0f, 3.0f));
		data.add(new Point(0.0f, 0.0f));
		data.add(new Point(1.0f, 1.0f));
		data.add(new Point(2.0f, 0.0f));
		data.add(new Point(3.0f, 1.0f));
		data.add(new Point(4.0f, 0.0f));
		data.add(new Point(5.0f, 1.0f));
		data.add(new Point(6.0f, 0.0f));
		data.add(new Point(7.5f, 3.0f));
		*/
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View input = factory.inflate(R.layout.doubleinput_remote, null);
		final Context context = this;
		final List<Consumption> data = new ArrayList<Consumption>();
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ", Locale.ENGLISH);
				Calendar cal = Calendar.getInstance();								 
				Date start;
				Date end;
				EditText sta = (EditText) input.findViewById(R.id.startDate);
				EditText en = (EditText) input.findViewById(R.id.endDate);
				


				
				try {
					start = sdf.parse(sta.toString());
					end = sdf.parse(en.toString());
					Toast.makeText(context,"try nr 1", Toast.LENGTH_SHORT).show();
					mPowerStrip.getConsumption(start, end, new OnConsumptionReceiveListener() {
						
						@Override
						public void onConsumptionReceive(Consumption[] consumption) {
							Toast.makeText(context,"OnConsumptionReceiveListener", Toast.LENGTH_SHORT).show();
							Collections.addAll(data, consumption);
						}

						@Override
						public void failed() {
							// TODO Auto-generated method stub
							Toast.makeText(context, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (ParseException e) {
					try {
						//Set Default value
						end = sdf.parse(sdf.format(cal.getTime()));
						cal.setTimeInMillis(cal.getTimeInMillis()-1003600000); //3600 000 = 1 hour
						start = sdf.parse(sdf.format(cal.getTime()));
						Toast.makeText(context,"try nr 2", Toast.LENGTH_SHORT).show();
						mPowerStrip.getConsumption(start, end, new OnConsumptionReceiveListener() {

							@Override
							public void onConsumptionReceive(Consumption[] consumption) {
								Toast.makeText(context,"Default " + consumption.length, Toast.LENGTH_SHORT).show();
								Collections.addAll(data, consumption);
								List<Point> points = transform(data);
								graphView.SetDataPoints(points);
								graphView.setYAxisEnd(getMaxWatt(data));
								graphView.setXAxisEnd(points.get(points.size()-1).x);
								graphView.setXSegments((int)(points.get(points.size()-1).x - points.get(0).x)/4);
								graphView.setYSegments((int)(getMaxWatt(data)/4)); 
							}

							@Override
							public void failed() {
								// TODO Auto-generated method stub
								Toast.makeText(context, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
							}
						});
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}									
				}

				//							  String value = input.getText().toString();
				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}

		});
		alert.show();
		
		/*
		data.add(new Consumption("2010-05-23 12:05:13+02", 100));
		data.add(new Consumption("2010-05-24 12:05:13+02", 123));
		data.add(new Consumption("2010-05-25 12:05:13+02", 65));
		data.add(new Consumption("2010-05-26 12:05:13+02", 68));
		data.add(new Consumption("2010-05-27 12:05:13+02", 100));
		//Todo: sort data
		*/

	}
	
	private List<Point> transform(List<Consumption> data) {
		List<Point> points = new ArrayList<Point>();
		long firstTime = data.get(0).getDate().getTime();
		for(Consumption c : data) {
			long time = c.getDate().getTime() - firstTime;
			time /= 10000000;
			Point point = new Point(time, c.getWatt());
			points.add(point);
		}
		
		return points;
	}
	
	private int getMaxWatt(List<Consumption> data) {
		int maxWatt = 0;
		for(Consumption c : data) {
			if(c.getWatt() > maxWatt) {
				maxWatt = c.getWatt();
			}
		}
		
		return maxWatt;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
