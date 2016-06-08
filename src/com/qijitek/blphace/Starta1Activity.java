package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Starta1Activity extends Activity implements OnClickListener {
	private Button next;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_starta1);
		init();
	}

	private void init() {
		next = (Button) findViewById(R.id.next);
		back = (ImageView) findViewById(R.id.back);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next:
			startActivity(new Intent(Starta1Activity.this,
					Starta2Activity.class));
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
