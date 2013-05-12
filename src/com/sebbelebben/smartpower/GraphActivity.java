package com.sebbelebben.smartpower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sebbelebben.smartpower.GraphView.Point;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		GraphView graphView = (GraphView) findViewById(R.id.graphview);
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
		
		List<Consumption> data = new ArrayList<Consumption>();
		data.add(new Consumption("2010-05-23 12:05:13+02", 100));
		data.add(new Consumption("2010-05-24 12:05:13+02", 123));
		data.add(new Consumption("2010-05-25 12:05:13+02", 65));
		data.add(new Consumption("2010-05-26 12:05:13+02", 68));
		data.add(new Consumption("2010-05-27 12:05:13+02", 100));
		//Todo: sort data
		
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
