package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Applyd1Activity extends Activity implements OnClickListener {
	private RelativeLayout list_d11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyd1);
		init();
	}

	private void init() {
		list_d11 = (RelativeLayout) findViewById(R.id.list_d11);
		list_d11.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_d11:
			startActivity(new Intent(Applyd1Activity.this,
					Applyd2Activity.class));
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
