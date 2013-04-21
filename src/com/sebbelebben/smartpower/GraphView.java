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
import android.view.GestureDetector;
import android.view.MotionEvent;
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
	private Paint mTextPaint;
	
	// Attributes
	private float mXAxisStart;
	private float mXAxisEnd;
	private float mYAxisStart;
	private float mYAxisEnd;
	private int mXSegments;
	private int mYSegments;
	private int mSegmentColor;
	private int mDataColor;
	private int mAxisColor;
	private boolean mFillData;
	private int mFillColor;
	private int mAxisBackgroundColor;
	private int mDataBackgroundColor;
	private int mTextColor;
	
	
	// Misc
	private int mXPadding = 20;
	private int mYPadding = 20;
	private GestureDetector mDetector = new GestureDetector(GraphView.this.getContext(), new mListener());
	
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
		mTextColor = attributes.getColor(R.styleable.GraphView_textColor, Color.BLACK);
		attributes.recycle();
		
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
		
		mTextPaint = new Paint();
		mTextPaint.setColor(mTextColor);
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
		drawText(canvas);
		drawForeground(canvas);
	}
	
	public void SetDataPoints(List<Point> data) {
		mDataPoints = data;
		invalidate();
	}
	
	public void AppendDataPoints(List<Point> data) {
		mDataPoints.addAll(data);
		invalidate();
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
	
	private void drawForeground(Canvas canvas) {
		// Draw the corner that hides data text
		canvas.drawRect(0, getHeight() - mYPadding, mXPadding, getHeight(), mAxisBackgroundPaint);
	}
	
	private void drawSegments(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		for(float i = mXAxisStart - mod(mXAxisStart, mXSegments); i < mXAxisEnd; i+=mXSegments) {
			Point segmentPoint = new Point(i, h);
			Point newP = dataToCanvas(segmentPoint);
			canvas.drawLine(newP.x, 0, newP.x, h - mYPadding, mSegmentPaint);
		}
		
		for(float i = mYAxisStart - mod(mYAxisStart, mYSegments); i < mYAxisEnd; i+=mYSegments) {
			Point segmentPoint = new Point(mXAxisStart, i);
			Point newP = dataToCanvas(segmentPoint);
			canvas.drawLine(newP.x, newP.y, newP.x + w, newP.y, mSegmentPaint);
		}
	}
	
	private void drawText(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		for(float i = mXAxisStart - mod(mXAxisStart,mXSegments); i < mXAxisEnd; i+=mXSegments) {
			Point segmentPoint = new Point(i, h);
			Point newP = dataToCanvas(segmentPoint);
			canvas.drawText(String.valueOf(i), newP.x, h, mTextPaint);
		}
		
		for(float i = mYAxisStart - mod(mYAxisStart, mYSegments); i < mYAxisEnd; i+=mYSegments) {
			Point segmentPoint = new Point(0, i);
			Point newP = dataToCanvas(segmentPoint);
			canvas.drawText(String.valueOf(i), 0, newP.y, mTextPaint);
		}
	}
	
	private float mod(float x, float y)
	{
	    float result = x % y;
	    if (result < 0)
	    {
	        result += y;
	    }
	    return result;
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
		
		canvas.drawPath(dataPath, mDataPaint);
		
		if(mFillData) {
			for(int i = 1; i < mDataPoints.size(); i++) {	
				Path fillPath = new Path();
				Point currentPoint = mDataPoints.get(i);
				Point prevPoint = mDataPoints.get(i-1);
				Point p = dataToCanvas(currentPoint);
				Point p2 = dataToCanvas(prevPoint);
				
				fillPath.moveTo(p2.x, p2.y);
				fillPath.lineTo(p.x, p.y);
				fillPath.lineTo(p.x, getHeight() - mYPadding);
				fillPath.lineTo(p2.x, getHeight() - mYPadding);
				canvas.drawPath(fillPath, mDataFillPaint);
			}
		}
	}
	
	private Point dataToCanvas(Point point) {
		float xInterval = mXAxisEnd - mXAxisStart;
		float yInterval = mYAxisEnd - mYAxisStart;
		float x;
		float y;
		
		x = (point.x - mXAxisStart) * (getWidth() - mXPadding) / xInterval + mXPadding;
		y = getHeight() - (point.y * ((getHeight() - mYPadding) / yInterval)) - mYPadding;
		
		return new Point(x, y);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mDetector.onTouchEvent(event);

		return result;
	}
	
	private class mListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float scale = (float)(mXAxisEnd - mXAxisStart) / (float)(getWidth() - mXPadding);
			mXAxisEnd += distanceX * scale;
			mXAxisStart += distanceX * scale;
			
			invalidate();
			return true;
		}
	}
	
	public void setXAxisStart(float xAxisStart) {
		mXAxisStart = xAxisStart;
	}
	
	public void setXAxisEnd(float xAxisEnd) {
		mXAxisEnd = xAxisEnd;
	}
	
	public void setYAxisStart(float yAxisStart) {
		mYAxisStart = yAxisStart;
	}
	
	public void setYAxisEnd(float yAxisEnd) {
		mYAxisEnd = yAxisEnd;
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
