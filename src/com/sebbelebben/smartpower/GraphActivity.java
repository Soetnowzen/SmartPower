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
import android.os.Debug;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		final GraphView graphView = (GraphView) findViewById(R.id.graphview);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.loading_progress);
		Intent intent = getIntent();
		final PowerStrip mPowerStrip = (PowerStrip) intent.getSerializableExtra("PowerStrip");
		final Context context = this;
		Calendar cal = Calendar.getInstance();
		
		final List<Consumption> data = new ArrayList<Consumption>();
		
		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ", Locale.ENGLISH);
				String start = String.format("%s-%s-%s 00:00:00+00",year,monthOfYear,dayOfMonth);
				try{
					mPowerStrip.getConsumption(sdf.parse(start), new Date(System.currentTimeMillis()), new OnConsumptionReceiveListener() {

						@Override
						public void onConsumptionReceive(Consumption[] consumption) {
							Collections.addAll(data, consumption);
							display(data, graphView);
							pb.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void failed() {
							Toast.makeText(context, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
						}
					});
				}catch(ParseException e){
					Log.d("bug", "Error with parsing: " + e.getStackTrace());
				}
				
			}
		};
		DatePickerDialog dp = new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		dp.show();

	}
	private void display(List<Consumption> data, GraphView graphView){
		List<Point> points = transform(data);
		graphView.SetDataPoints(points);
		graphView.setYAxisEnd(getMaxWatt(data));
		graphView.setXAxisEnd(points.get(points.size()-1).x);
		graphView.setXSegments((int)(points.get(points.size()-1).x - points.get(0).x)/4);
		graphView.setYSegments((int)(getMaxWatt(data)/4)); 
		
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
