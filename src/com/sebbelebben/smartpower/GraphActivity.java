package com.sebbelebben.smartpower;

import java.util.ArrayList;
import java.util.List;

import com.sebbelebben.smartpower.GraphView.Point;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		GraphView graphView = (GraphView) findViewById(R.id.graphview);
		List<Point> data = new ArrayList<Point>();
		// TODO: Use real implementation - this is only test data
		data.add(new Point(-3.0f, 3.0f));
		data.add(new Point(0.0f, 0.0f));
		data.add(new Point(1.0f, 0.4f));
		data.add(new Point(2.0f, 4.3f));
		data.add(new Point(3.0f, 7.5f));
		data.add(new Point(4.0f, 1.2f));
		data.add(new Point(5.0f, 0.2f));
		data.add(new Point(11.0f, 30.0f));
		graphView.SetDataPoints(data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
