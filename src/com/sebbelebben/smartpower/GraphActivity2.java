package com.sebbelebben.smartpower;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.internal.widget.TabsLinearLayout;
import com.jjoe64.graphview.*;
import com.jjoe64.graphview.GraphView;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GraphActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph2);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.container);
        final BarGraphView graphView = new BarGraphView(GraphActivity2.this, "Test");
        graphView.setScalable(true);
        //graphView.setScrollable(true);
        layout.addView(graphView);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.loading_progress);
		Intent intent = getIntent();
		final Graphable graphable = (Graphable) intent.getSerializableExtra("Graphable");
		final Context context = this;
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
                            GraphView.GraphViewData[] data2 = transform2(data);
                            graphView.addSeries(new GraphViewSeries(data2));
                            // TODO: remove this test
                            for(GraphView.GraphViewData d : data2) {
                                Log.i("SmartPower", "X: " + d.valueX + "Y: " + d.valueY);
                            }

                            graphView.invalidate();
                            Toast.makeText(context, "GOT THE CONSUMPTION!", Toast.LENGTH_SHORT).show();
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

    private GraphView.GraphViewData[] transform2(List<Consumption> data) {
        List<GraphView.GraphViewData> graphViewData = new ArrayList<GraphView.GraphViewData>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data.get(0).getDate());

        int currentDay = cal.get(Calendar.DAY_OF_YEAR);
        int totalWatt = 0;
        int j = 0;

        for(int i = 0; i < data.size(); i++) {
            Consumption c = data.get(i);
            cal.setTime(c.getDate());

            Log.i("SmartPower", ""+cal.get(Calendar.DAY_OF_YEAR));

            if(currentDay == cal.get(Calendar.DAY_OF_YEAR)) {
                totalWatt += c.getWatt() * (10.0f/3600);
            } else {
                currentDay = cal.get(Calendar.DAY_OF_YEAR);
                graphViewData.add(new GraphView.GraphViewData(j++, totalWatt));
                totalWatt = 0;
            }
        }
        graphViewData.add(new GraphView.GraphViewData(j++, totalWatt));

        GraphView.GraphViewData[] d = new GraphView.GraphViewData[graphViewData.size()];
        for(int i = 0; i < d.length; i++) {
            d[i] = graphViewData.get(i);
        }
        return d;
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
