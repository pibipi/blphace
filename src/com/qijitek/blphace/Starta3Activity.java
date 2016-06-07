package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Starta3Activity extends Activity implements OnClickListener {
	private Button next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starta3);
		init();
	}
	private void init() {
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			startActivity(new Intent(Starta3Activity.this,
					Starta4Activity.class));
			break;

		default:
			break;
		}
	}
}
