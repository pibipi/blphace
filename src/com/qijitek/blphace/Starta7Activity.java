package com.qijitek.blphace;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

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
