package com.sebbelebben.smartpower;

import java.util.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sebbelebben.smartpower.GraphView.Point;
import com.sebbelebben.smartpower.Server.OnConsumptionReceiveListener;

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

		final List<Consumption> data = new ArrayList<Consumption>();

        graphable.getConsumption(Duration.HOUR, 24, new OnConsumptionReceiveListener() {
            @Override
            public void onConsumptionReceive(Consumption[] consumption) {
                Collections.addAll(data, consumption);
                display(data, graphView);
                //pb.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void failed() {
                Toast.makeText(GraphActivity.this, "FAILED TO GET CONSUMPTION", Toast.LENGTH_SHORT).show();
            }
        });
	}

    /**
     * Displays a list of consumption points in the provided {@link GraphView}.
     *
     * @param data The consumption data.
     * @param graphView The graph view.
     */
	public static void display(List<Consumption> data, GraphView graphView){
        List<GraphView.Point> points = GraphActivity.transform(data);
        graphView.setDataPoints(points);
        graphView.setYAxisEnd(getMaxWatt(points) + getMaxWatt(points) / 5);
        graphView.setXAxisEnd(points.get(points.size() - 1).x);
        graphView.setXSegments((int) (points.get(points.size() - 1).x - points.get(0).x) / 4);
        graphView.setYSegments((int)(points.get(points.size()-1).y)/4);
	}
	/**
	 * Transforms the list of consumption to a list of data points.
     *
	 * @param data The list of datapoints to be transformed.
	 * @return List<Point> The format the graph can display.
	 */
    public static List<Point> transform(List<Consumption> data) {
        List<Point> graphViewData = new ArrayList<Point>();

        long firstTime = data.get(0).getDate().getTime();

        for (Consumption c : data) {
            long time = c.getDate().getTime();
            float hour = (time - firstTime) / (60 * 60 * 1000);
            Point point = new Point(hour, c.getWatt());
            point.label = String.valueOf(time);

            graphViewData.add(point);
        }

        return graphViewData;
    }
    /**
     *  Gets the maximum watt from the list of data points.
     *
     * @param data The list of data points.
     * @return The value of the consumption with the highest value.
     */
	public static int getMaxWatt(List<Point> data) {
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
