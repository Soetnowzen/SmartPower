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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class for rendering a graph from a list of data points.
 *
 * @author Andreas Arvidsson
 */
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
	private int mXPadding = 40;
	private int mYPadding = 40;
	private GestureDetector mDetector = new GestureDetector(GraphView.this.getContext(), new mListener());
	
	public GraphView(Context context) {
		super(context);
		mContext = context;
		init();
	}

    /**
     * Constructor for XML creation.
     *
     * @param context The application context.
     * @param attrs The XML attributes.
     */
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.GraphView);
        if(attributes != null) {
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
		    mFillColor = attributes.getColor(R.styleable.GraphView_fillDataColor, Color.BLACK);
		    mAxisBackgroundColor = attributes.getColor(R.styleable.GraphView_axisBackgroundColor, Color.BLACK);
		    mDataBackgroundColor = attributes.getColor(R.styleable.GraphView_dataBackgroundColor, Color.BLACK);
		    mTextColor = attributes.getColor(R.styleable.GraphView_textColor, Color.BLACK);
		    attributes.recycle();

            init();
        }
	}

    /**
     * Initiates the paints used to draw the view.
     */
	private void init() {
		mDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDataPaint.setColor(mDataColor);
		mDataPaint.setStyle(Paint.Style.STROKE);
		mDataPaint.setStrokeWidth(4f);
		
		mDataFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

    /**
     * Sets the data points to be used in the graphview.
     * This method invalidates the view, so that it redraws itself automatically.
     *
     * @param data The data points to be rendered.
     */
	public void setDataPoints(List<Point> data) {
		mDataPoints = data;
		invalidate();
	}

    /**
     * Adds data points to the end of the data point list.
     *
     * @param data The data points to be appended.
     */
	public void appendDataPoints(List<Point> data) {
		mDataPoints.addAll(data);
		invalidate();
	}

    /**
     * Draws the axis of the graph on the provided canvas.
     *
     * @param canvas The canvas which should be drawn on.
     */
	private void drawAxis(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		
		int startX = mXPadding;
		int stopX = w;
		int startY = h - mYPadding;
		int stopY = h - mYPadding;

        // Draw those sexy axis
		canvas.drawRect(0, 0, mXPadding, h, mAxisBackgroundPaint);
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
		
		startX = mXPadding;
		stopX = mXPadding;
		startY = h - mYPadding;
		stopY = 0;
		
		canvas.drawRect(0, h - mYPadding, w, h, mAxisBackgroundPaint);
		canvas.drawLine(startX, startY, stopX, stopY, mAxisPaint);
	}

    /**
     * Draws the background on the provided canvas.
     *
     * @param canvas The canvas which should be drawn on.
     */
	private void drawBackground(Canvas canvas) {
		canvas.drawRect(0, 0, getWidth(), getHeight(), mDataBackgroundPaint);
	}

    /**
     * Draws the foreground on the provided canvas.
     *
     * @param canvas The canvas which should be drawn on.
     */
	private void drawForeground(Canvas canvas) {
		// Draw the corner that hides data text
		canvas.drawRect(0, getHeight() - mYPadding, mXPadding, getHeight(), mAxisBackgroundPaint);
	}

    /**
     * Draws the segment lines on the provided canvas.
     *
     * @param canvas The canvas which should be drawn on.
     */
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

    /**
     * Draws the text on the provided canvas. The text is shown on the axis of the graph.
     *
     * @param canvas The canvas which should be drawn on.
     */
	private void drawText(Canvas canvas) {
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

    /**
     * A better modulo function to replace the shitty java one.
     *
     * @param x The first parameter.
     * @param y The mod parameter.
     * @return The first parameter modulo the mod parameter.
     */
	private float mod(float x, float y)
	{
	    float result = x % y;
	    if (result < 0)
	    {
	        result += y;
	    }
	    return result;
	}

    /**
     * Draws the data from the internal data points on the provided canvas.
     *
     * @param canvas The canvas which should be drawn on.
     */
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

    /**
     * Converts a data point to coordinates on the canvas.
     *
     * @param point The point to be converted.
     * @return The converted point, which can be used as canvas coordinates.
     */
	private Point dataToCanvas(Point point) {
		float xInterval = mXAxisEnd - mXAxisStart;
		float yInterval = mYAxisEnd - mYAxisStart;
		float x;
		float y;
		
		x = (point.x - mXAxisStart) * (getWidth() - mXPadding) / xInterval + mXPadding;
		y = getHeight() - (point.y * ((getHeight() - mYPadding) / yInterval)) - mYPadding;
		
		return new Point(x, y);
	}

    /**
     * Triggered when the view is touched.
     *
     * @param event The touch event.
     * @return True if the touch is handeled, otherwise false.
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mDetector.onTouchEvent(event);
	}

    /**
     * A simple touch listener that can detect scrolling.
     */
	private class mListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float scale = (mXAxisEnd - mXAxisStart) / (float)(getWidth() - mXPadding);
			mXAxisEnd += distanceX * scale;
			mXAxisStart += distanceX * scale;

            // Limit the scrolling to the minimum X value
            if(mDataPoints.size() > 0 && mXAxisStart < mDataPoints.get(0).x) {
                mXAxisEnd = mXAxisEnd + Math.abs(mXAxisStart);
                mXAxisStart = mDataPoints.get(0).x;
            }
			
			invalidate();
			return true;
		}
	}

    /**
     * Sets the X axis start value.
     *
     * @param xAxisStart The X axis start value.
     */
	public void setXAxisStart(float xAxisStart) {
		mXAxisStart = xAxisStart;
	}

    /**
     * Sets the X axis end value.
     * @param xAxisEnd The X axis end value.
     */
	public void setXAxisEnd(float xAxisEnd) {
		mXAxisEnd = xAxisEnd;
	}

    /**
     * Sets the Y axis start value.
     *
     * @param yAxisStart The Y axis start value.
     */
	public void setYAxisStart(float yAxisStart) {
		mYAxisStart = yAxisStart;
	}

    /**
     * Sets the Y axis end value.
     *
     * @param yAxisEnd The Y axis end value.
     */
	public void setYAxisEnd(float yAxisEnd) {
		mYAxisEnd = yAxisEnd;
	}

    /**
     * Sets the spacing between segments on the X axis.
     *
     * @param xSegments The segment spacing.
     */
	public void setXSegments(int xSegments) {
		mXSegments = xSegments;
	}

    /**
     * Sets the spacing between segments on the Y axis.
     *
     * @param ySegments The segment spacing.
     */
	public void setYSegments(int ySegments) {
		mYSegments = ySegments;
	}

    /**
     * Class to represent a data point.
     */
	public static class Point {
		public float x;
		public float y;
		
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
