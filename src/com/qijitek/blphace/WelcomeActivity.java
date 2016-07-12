package com.qijitek.blphace;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qijitek.blphace.LoginActivity;
import com.qijitek.blphace.MainActivity;
import com.qijitek.blphace.R;
import com.qijitek.utils.SharedpreferencesUtil;

public class WelcomeActivity extends Activity {
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		init();
		final boolean isLogin = new SharedpreferencesUtil(
				getApplicationContext()).getIsLogin();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isLogin||true) {
					startActivity(new Intent(WelcomeActivity.this,
							MainActivity.class));
				} else {
					startActivity(new Intent(WelcomeActivity.this,
							LoginActivity.class));

				}
				finish();
			}
		}, 2000);
	}

	private void init() {
		mHandler = new Handler() {
		};
	}
}
