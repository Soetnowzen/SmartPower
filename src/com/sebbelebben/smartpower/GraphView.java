package com.sebbelebben.smartpower;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {
	private Context mContext;
	
	// Data for the graph line
	private Paint mLinePaint;
	private int mLineColor = Color.RED;
	
	public GraphView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	private void init() {
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(mLineColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawAxis(canvas);
		
		// TODO: Use real implementation - this is only test data
		ArrayList<Float> data = new ArrayList<Float>();
		data.add(0.4f);
		data.add(4.3f);
		data.add(7.5f);
		data.add(1.2f);
		data.add(0.2f);
		drawData(canvas, data);
	}
	
	private void drawAxis(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		int startX = 10;
		int stopX = w - 10;
		int startY = h - 10;
		int stopY = h - 10;
		
		canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
		
		startX = 10;
		stopX = 10;
		startY = h - 10;
		stopY = 10;
		
		canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
	}
	
	private void drawData(Canvas canvas, List<Float> data) {
		// Calculate the X distance between each pair of points
		float xDist = (float)getWidth() / data.size();
		
		// Get the y-coordinate of the heighest point
		float heighestY = 0;
		for(Float point : data) {
			if(heighestY < point) {
				heighestY = point;
			}
		}
		
		float scaleY = getHeight() / heighestY;
		
		for(int i = 0; i < data.size() - 1; i++) {
			Float p1 = data.get(i);
			Float p2 = data.get(i+1);
			
			canvas.drawLine(i * xDist, p1 * scaleY, i * xDist + xDist, p2 * scaleY, mLinePaint);
		}
	}
}
