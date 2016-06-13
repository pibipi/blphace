package com.qijitek.blphace;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Starta7Activity extends Activity implements OnClickListener {
	private RelativeLayout list_a71;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta7);
		init();
	}

	private void init() {
		list_a71 = (RelativeLayout) findViewById(R.id.list_a71);
		list_a71.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_a71:
			
			break;

		default:
			break;
		}
	}
}
