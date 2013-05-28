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

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Class for displaying a graph of consumption.
 *
 * @author Andreas Arvidsson
 */
public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);

        // Find the views.
		final GraphView graphView = (GraphView) findViewById(R.id.graphview);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.loading_progress);

        // Retrieve the graphable which data is going to be displayed.
		Intent intent = getIntent();
		final Graphable graphable = (Graphable) intent.getSerializableExtra("Graphable");
        if (graphable == null) {
            throw new IllegalArgumentException("GraphActivity needs to be provided a Graphable object - otherwise " +
                    "there's no point, dummy!");
        }

		Calendar cal = Calendar.getInstance();
		
		final List<Consumption> data = new ArrayList<Consumption>();
		
		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ", Locale.ENGLISH);
				String start = String.format("%s-%s-%s 00:00:00+00",year,monthOfYear,dayOfMonth);
				try{

                    graphable.getConsumption(sdf.parse(start), new Date(System.currentTimeMillis()), new OnConsumptionReceiveListener() {

						@Override
						public void onConsumptionReceive(Consumption[] consumption) {
							Collections.addAll(data, consumption);
							display(data, graphView);
							pb.setVisibility(ProgressBar.GONE);
						}

						@Override
						public void failed() {
							Toast.makeText(GraphActivity.this, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
						}
					});
				}catch(ParseException e){
					Log.d("bug", "Error with parsing.");
				}
				
			}
		};
		DatePickerDialog dp = new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		dp.show();

	}

    /**
     * Displays a list of consumption points in the provided {@link GraphView}.
     *
     * @param data The consumption data.
     * @param graphView The graph view.
     */
	private void display(List<Consumption> data, GraphView graphView){
		List<Point> points = transform2(data);
		graphView.setDataPoints(points);
		graphView.setYAxisEnd(getMaxWatt(points));
		graphView.setXAxisEnd(points.get(points.size()-1).x);
		graphView.setXSegments((int)(points.get(points.size()-1).x - points.get(0).x)/4);
		//graphView.setYSegments((int)(getMaxWatt(points)/4));
        graphView.setYSegments(500);
		
	}
	/**
	 * Transforms the list of consumption to a list of data points.
     *
	 * @param data The list of datapoints to be transformed.
	 * @return List<Point> The format the graph can display.
	 */
    private List<Point> transform2(List<Consumption> data) {
        List<Point> graphViewData = new ArrayList<Point>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data.get(0).getDate());

        int currentDay = cal.get(Calendar.DAY_OF_YEAR);
        int totalWatt = 0;
        int j = 0;

        for(Consumption c : data) {
            cal.setTime(c.getDate());

            Log.i("SmartPower", ""+cal.get(Calendar.DAY_OF_YEAR));

            if(currentDay == cal.get(Calendar.DAY_OF_YEAR)) {
                totalWatt += c.getWatt() * (10.0f/3600);
            } else {
                currentDay = cal.get(Calendar.DAY_OF_YEAR);
                graphViewData.add(new Point(j++, totalWatt));
                totalWatt = 0;
            }
        }
        graphViewData.add(new Point(j++, totalWatt));

        return graphViewData;
    }
    /**
     *  Gets the maximum watt from the list of data points.
     *
     * @param data The list of data points.
     * @return The value of the consumption with the highest value.
     */
	private int getMaxWatt(List<Point> data) {
		int maxWatt = 0;
		for(Point p : data) {
			if(p.y > maxWatt) {
				maxWatt = (int) p.y;
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
