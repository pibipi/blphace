package com.qijitek.blphace;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutUsActivity extends Activity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_about_us);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back:
	    finish();
	    break;

	default:
	    break;
	}
    }
}