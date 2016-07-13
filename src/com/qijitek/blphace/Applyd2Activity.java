package com.qijitek.blphace;

import com.qijitek.utils.MyUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Applyd2Activity extends Activity implements OnClickListener {
	private String name = "";
	private String content = "";
	private String imgurl = "";
	private ImageView demo_img;
	private TextView content_txt;
	private int width;
	private int height;
	private int dp_hei;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyd2);
		init();

	}

	private void init() {
		//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		dp_hei = (int) (180f / (MyUtils.px2dip(getApplicationContext(), height)) * height);
		//
		name = getIntent().getStringExtra("name");
		content = getIntent().getStringExtra("content");
		imgurl = getIntent().getStringExtra("imgurl");
		demo_img = (ImageView) findViewById(R.id.demo_img);
		content_txt = (TextView) findViewById(R.id.content_txt);
		Picasso.with(getApplicationContext()).load(imgurl).into(demo_img);
		content_txt.setText("\b\b\b\b"+content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
