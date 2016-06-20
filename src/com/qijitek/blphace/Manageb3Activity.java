package com.qijitek.blphace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class Manageb3Activity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageb3);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.start_test:
			showDialog();
			break;
		case R.id.test:
			startActivity(new Intent(Manageb3Activity.this,
					CompareResultActivity.class));
			break;
		default:
			break;
		}
	}

	private void showDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_start_test, null);
		final AlertDialog add_Dialog = new AlertDialog.Builder(
				Manageb3Activity.this).create();
		add_Dialog.show();

		Window window = add_Dialog.getWindow();
		// window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		// window.setWindowAnimations(R.style.mystyle); // 添加动画

		// add_Dialog.getWindow().setContentView(layout);
		add_Dialog.getWindow().setContentView(R.layout.dialog_start_test);
		add_Dialog.setCancelable(true);

		add_Dialog.getWindow().findViewById(R.id.close)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						add_Dialog.dismiss();
					}
				});
	}
}
