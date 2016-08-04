package com.qijitek.blphace;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
		final boolean IsFirst = new SharedpreferencesUtil(
				getApplicationContext()).getIsFirst1();
		final boolean IsLogin = new SharedpreferencesUtil(
				getApplicationContext()).getIsLogin();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (IsFirst) {
					startActivity(new Intent(WelcomeActivity.this,
							GuideActivity.class));
				} else {
					if (IsLogin) {
						startActivity(new Intent(WelcomeActivity.this,
								MainActivity.class));
					} else {
						startActivity(new Intent(WelcomeActivity.this,
								LoginActivity.class));
					}
				}
				finish();
			}
		}, 2000);
	}

	private void init() {
		mHandler = new Handler() {
		};
		String SDCard = Environment.getExternalStorageDirectory() + "";
		String pathName1 = SDCard + "/alphace/Alphace.apk";// 文件存储路径
		String pathName2 = SDCard + "/alphace";// 文件存储路径
		File file1 = new File(pathName1);
		File file2 = new File(pathName2);
		if (file1.exists()) {
			file1.delete();
		}
		if (file2.exists()) {
			file2.delete();
		}
	}
}
