package com.qijitek.blphace;

import com.qijitek.view.RoundProgressBar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CompareResultActivity extends Activity implements OnClickListener {
	private RoundProgressBar water_progress1;
	private RoundProgressBar water_progress2;
	private RoundProgressBar oil_progress1;
	private RoundProgressBar oil_progress2;
	private RoundProgressBar color_progress1;
	private RoundProgressBar color_progress2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_result_activity);
		init();
		water_progress1.setProgress(55);
		water_progress2.setProgress(60);
		oil_progress1.setProgress(65);
		oil_progress2.setProgress(70);
		color_progress1.setProgress(75);
		color_progress2.setProgress(80);
	}

	private void init() {
		water_progress1 = (RoundProgressBar) findViewById(R.id.water_progress1);
		water_progress2 = (RoundProgressBar) findViewById(R.id.water_progress2);
		oil_progress1 = (RoundProgressBar) findViewById(R.id.oil_progress1);
		oil_progress2 = (RoundProgressBar) findViewById(R.id.oil_progress2);
		color_progress1 = (RoundProgressBar) findViewById(R.id.color_progress1);
		color_progress2 = (RoundProgressBar) findViewById(R.id.color_progress2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}
	}
}
