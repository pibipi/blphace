package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private RelativeLayout tips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		tips = (RelativeLayout) findViewById(R.id.tips);
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		initTips();
	}

	private void initTips() {
		bt1.setClickable(false);
		bt2.setClickable(false);
		bt3.setClickable(false);
		bt4.setClickable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt1:
			startActivity(new Intent(MainActivity.this, Starta1Activity.class));
			break;
		case R.id.bt2:
			startActivity(new Intent(MainActivity.this, Manageb1Activity.class));
			break;
		case R.id.bt3:
			startActivity(new Intent(MainActivity.this, Socialc1Activity.class));
			break;
		case R.id.bt4:
			startActivity(new Intent(MainActivity.this, Applyd1Activity.class));
			break;
		case R.id.tips:
			tips.setVisibility(View.INVISIBLE);
			bt1.setClickable(true);
			bt2.setClickable(true);
			bt3.setClickable(true);
			bt4.setClickable(true);
			break;
		default:
			break;
		}
	}

}
