package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Applyd1Activity extends Activity implements OnClickListener {
	private RelativeLayout list_d11;
	private TextView days;
	private TextView days2;
	private TextView days3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applyd1);
		init();
	}

	private void init() {
		days = (TextView) findViewById(R.id.days);
		days2 = (TextView) findViewById(R.id.days2);
		days3 = (TextView) findViewById(R.id.days3);
		list_d11 = (RelativeLayout) findViewById(R.id.list_d11);
		list_d11.setOnClickListener(this);
		//
		SpannableStringBuilder builder = new SpannableStringBuilder(days
				.getText().toString());
		ColorStateList redColors = ColorStateList.valueOf(0xfff19393);
		builder.setSpan(new TextAppearanceSpan(null, 0, 60, redColors, null),
				2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		days.setText(builder);
		days2.setText(builder);
		days3.setText(builder);
		//
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
