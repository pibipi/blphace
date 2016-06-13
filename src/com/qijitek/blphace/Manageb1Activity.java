package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Manageb1Activity extends Activity implements OnClickListener {
	private Button bt1;
	private Button bt2;
	private Button bt3;
	private Button bt4;
	private Button bt5;
	private Button bt6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageb1);
		init();
	}

	private void init() {
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt5 = (Button) findViewById(R.id.bt5);
		bt6 = (Button) findViewById(R.id.bt6);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt1:

			break;
		case R.id.bt2:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			break;
		case R.id.bt3:

			break;
		case R.id.bt4:

			break;
		case R.id.bt5:

			break;
		case R.id.bt6:

			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
