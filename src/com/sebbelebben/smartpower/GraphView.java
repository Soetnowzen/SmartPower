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
	private List<Point> mDataPoints = new ArrayList<Point>();
	private Paint mDataPaint;
	private Paint mDataFillPaint;
	private Paint mSegmentPaint;
	private Paint mAxisPaint;
	private Paint mAxisBackgroundPaint;
	private Paint mDataBackgroundPaint;
	
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
	private boolean mFillData;
	private int mFillColor;
	private int mAxisBackgroundColor;
	private int mDataBackgroundColor;
	
	
	// Misc
	private int mXPadding = 20;
	private int mYPadding = 20;
	
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
		mFillData = attributes.getBoolean(R.styleable.GraphView_fillData, false);
		mFillColor = attributes.getColor(R.styleable.GraphView_fillColor, Color.BLACK);
		mAxisBackgroundColor = attributes.getColor(R.styleable.GraphView_axisBackgroundColor, Color.BLACK);
		mDataBackgroundColor = attributes.getColor(R.styleable.GraphView_dataBackgroundColor, Color.BLACK);
		
		init();
	}
	
	private void init() {
		mDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDataPaint.setColor(mDataColor);
		mDataPaint.setStyle(Paint.Style.STROKE);
		mDataPaint.setStrokeWidth(2f);
		
		mDataFillPaint = new Paint();
		mDataFillPaint.setColor(mFillColor);
		mDataFillPaint.setStyle(Paint.Style.FILL);
		
		mSegmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSegmentPaint.setColor(mSegmentColor);
		
		mAxisPaint = new Paint();
		mAxisPaint.setColor(mAxisColor);
		mAxisPaint.setStrokeWidth(2f);
		
		mAxisBackgroundPaint = new Paint();
		mAxisBackgroundPaint.setColor(mAxisBackgroundColor);
		
		mDataBackgroundPaint = new Paint();
		mDataBackgroundPaint.setColor(mDataBackgroundColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawBackground(canvas);
		drawSegments(canvas);
		
		if(mDataPoints.size() > 0) {
			drawData(canvas);
		}
		drawAxis(canvas);
	}
	
	public void SetDataPoints(List<Point> data) {
		mDataPoints = data;
	}
	
	public void AppendDataPoints(List<Point> data) {
		mDataPoints.addAll(data);
	}
	
	private void drawAxis(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		int startX = mXPadding;
		int stopX = w;
		int startY = h - mYPadding;
		int stopY = h - mYPadding;
		
		canvas.drawRect(0, 0, mXPadding, h, mAxisBackgroundPaint);
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
		
		startX = mXPadding;
		stopX = mXPadding;
		startY = h - mYPadding;
		stopY = 0;
		
		canvas.drawRect(0, h - mYPadding, w, h, mAxisBackgroundPaint);
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
	}
	
	private void drawBackground(Canvas canvas) {
		canvas.drawRect(0, 0, getWidth(), getHeight(), mDataBackgroundPaint);
	}
	
	private void drawSegments(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		float xSegmentInterval = (w - mXPadding)  / mXSegments + mXPadding;
		for(int i = 1; i < mXSegments; i++) {
			canvas.drawLine(xSegmentInterval*i, 0, xSegmentInterval*i, h - mYPadding, mSegmentPaint);
			canvas.drawText("Hello", xSegmentInterval*i, h, mSegmentPaint);
		}
		
		float ySegmentInterval = (h - mYPadding) / mYSegments + mYPadding;
		for(int i = 1; i < mYSegments; i++) {
			canvas.drawLine(mYPadding, ySegmentInterval*i, w, ySegmentInterval*i, mSegmentPaint);
		}
	}
	
	private void drawData(Canvas canvas) {		
		Path dataPath = new Path();
		Point firstPoint = dataToCanvas(mDataPoints.get(0));
		dataPath.moveTo(firstPoint.x, firstPoint.y);
		
		for(int i = 1; i < mDataPoints.size(); i++) {	
			Point currentPoint = mDataPoints.get(i);
			Point p = dataToCanvas(currentPoint);
			dataPath.lineTo(p.x, p.y);
		}
		
		/*
		if(mFillData) {
			Path fillPath = new Path();
			fillPath.setFillType(Path.FillType.WINDING);
			Point lastPoint = dataToCanvas(mDataPoints.get(mDataPoints.size() - 1));
			fillPath.moveTo(firstPoint.x, mYPadding);
			fillPath.addPath(dataPath);
			
			fillPath.lineTo(lastPoint.x, getHeight() - mYPadding);
			canvas.drawPath(fillPath, mDataFillPaint);
		}
		*/
		canvas.drawPath(dataPath, mDataPaint);
	}
	
	private Point dataToCanvas(Point point) {
		float xInterval = mXAxisEnd - mXAxisStart;
		float yInterval = mYAxisEnd - mYAxisStart;
		float x;
		float y;
		
		x = point.x * (getWidth() - mXPadding) / xInterval + mXPadding - mXAxisStart * (getWidth() - mXPadding) / xInterval;
		y = getHeight() - (point.y * ((getHeight() - mYPadding) / yInterval)) - mYPadding;
		
		return new Point(x, y);
	}
	
	private boolean isInCanvas(Point point) {
		if(point.x >= mXAxisStart && point.x <= mXAxisEnd) {
			if(point.y >= mYAxisStart && point.y <= mYAxisEnd) {
				return true;
			}
		}
		
		return false;
	}
	
	public static class Point {
		public float x;
		public float y;
		
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
