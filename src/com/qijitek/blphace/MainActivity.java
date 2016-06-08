package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt1:
			startActivity(new Intent(MainActivity.this, Starta1Activity.class));
			break;
		case R.id.bt2:
			startActivity(new Intent(MainActivity.this, Startb1Activity.class));
			break;
		case R.id.bt3:
			startActivity(new Intent(MainActivity.this, Startc1Activity.class));
			break;
		case R.id.bt4:

			break;
		default:
			break;
		}
	}

}
