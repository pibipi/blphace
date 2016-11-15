package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qijitek.utils.SharedpreferencesUtil;
import com.umeng.analytics.MobclickAgent;

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
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("1");
			break;
		case R.id.bt2:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("2");
			break;
		case R.id.bt3:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("3");
			break;
		case R.id.bt4:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("4");
			break;
		case R.id.bt5:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("5");
			break;
		case R.id.bt6:
			startActivity(new Intent(Manageb1Activity.this,
					Manageb2Activity.class));
			new SharedpreferencesUtil(getApplicationContext())
					.saveItemtype("6");
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
