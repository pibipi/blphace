package com.qijitek.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.ProgressBar;

import com.qijitek.blphace.R;

@SuppressLint("NewApi")
public class MyProgressBar extends ProgressBar {
	private Paint mPaint;// 画笔

	public MyProgressBar(Context context) {
		super(context);
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint(attrs);
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint(attrs);
	}

	// @SuppressLint("NewApi")
	// public MyProgressBar(Context context, AttributeSet attrs, int defStyle,
	// int styleRes) {
	// super(context, attrs, defStyle, styleRes);
	// initPaint(attrs);
	// // TODO Auto-generated constructor stub
	// }

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.getProgress() != 0) {
			float r = this.getHeight() / 2f;
			float x = (this.getWidth()) * this.getProgress() * 0.01f;
			float y = (this.getHeight() / 2f);
			canvas.drawCircle(x, y, y, mPaint);
		}

	}

	@Override
	@ExportedProperty(category = "progress")
	public synchronized int getProgress() {
		// TODO Auto-generated method stub
		return super.getProgress();
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		int new_p = (int) (progress * 119f / 130f + 5.5f * 100f / 130f);
		super.setProgress(new_p);
	}

	private void initPaint(AttributeSet attrs) {
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setStrokeWidth(10);

		TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.MyProgressBar);
		int backColor = t.getColor(R.styleable.MyProgressBar_backColor,
				0xffb1dfd1);
		this.mPaint.setColor(backColor);
		// Shader mShader = new LinearGradient(0, 0, 40, 60, new int[] {
		// Color.RED, Color.GREEN, Color.BLUE }, null,
		// Shader.TileMode.REPEAT);
		// this.mPaint.setShader(mShader);
	}

}
