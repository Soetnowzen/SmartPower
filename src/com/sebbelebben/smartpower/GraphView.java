package com.sebbelebben.smartpower;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {
	private Context mContext;
	
	// Data for the graph lines
	private Paint mDataPaint;
	private Paint mSegmentPaint;
	private Paint mAxisPaint;
	
	// Attributes
	private int mXAxisStart;
	private int mXAxisEnd;
	private int mYAxisStart;
	private int mYAxisEnd;
	private int mXSegments;
	private int mYSegments;
	private int mSegmentColor;
	private int mDataColor;
	private int mAxisColor;
	
	public GraphView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.GraphView);
		mXAxisStart = attributes.getInteger(R.styleable.GraphView_xAxisStart, 10);
		mXAxisEnd = attributes.getInteger(R.styleable.GraphView_xAxisEnd, 10);
		mYAxisStart = attributes.getInteger(R.styleable.GraphView_yAxisStart, 10);
		mYAxisEnd = attributes.getInteger(R.styleable.GraphView_yAxisEnd, 10);
		mXSegments = attributes.getInteger(R.styleable.GraphView_xSegments, 3);
		mYSegments = attributes.getInteger(R.styleable.GraphView_ySegments, 3);
		mSegmentColor = attributes.getColor(R.styleable.GraphView_segmentColor, Color.BLACK);
		mDataColor = attributes.getColor(R.styleable.GraphView_dataColor, Color.BLACK);
		mAxisColor = attributes.getColor(R.styleable.GraphView_axisColor, Color.BLACK);
		
		init();
	}
	
	private void init() {
		mDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDataPaint.setColor(mDataColor);
		mDataPaint.setStyle(Paint.Style.STROKE);
		
		mSegmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSegmentPaint.setColor(mSegmentColor);
		
		mAxisPaint = new Paint();
		mAxisPaint.setColor(mAxisColor);
		mAxisPaint.setStrokeWidth(2f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawAxis(canvas);
		drawSegments(canvas);
		

		
		// TODO: Use real implementation - this is only test data
		ArrayList<Float> data = new ArrayList<Float>();
		data.add(0.0f);
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
		
		int startX = 0;
		int stopX = w;
		int startY = h;
		int stopY = h;
		
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
		
		startX = 0;
		stopX = 0;
		startY = h;
		stopY = 0;
		
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
	}
	
	private void drawSegments(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		float xSegmentInterval = w / mXSegments;
		for(int i = 1; i < mXSegments; i++) {
			canvas.drawLine(xSegmentInterval*i, 0, xSegmentInterval*i, h, mSegmentPaint);
		}
		
		float ySegmentInterval = h / mYSegments;
		for(int i = 1; i < mYSegments; i++) {
			canvas.drawLine(0, ySegmentInterval*i, w, ySegmentInterval*i, mSegmentPaint);
		}
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
		
		Path dataPath = new Path();
		dataPath.moveTo(0, getHeight() - (data.get(0) * scaleY));
		
		for(int i = 1; i < data.size(); i++) {	
			dataPath.lineTo(i * xDist, getHeight() - (data.get(i) * scaleY));
		}
		
		canvas.drawPath(dataPath, mDataPaint);
	}
}
