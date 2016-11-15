package com.qijitek.blphace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qijitek.utils.MyUtils;
import com.qijitek.utils.SharedpreferencesUtil;
import com.umeng.analytics.MobclickAgent;

public class MySkinActivity extends Activity implements OnClickListener {
	private LinearLayout menu_layout;
	private TextView content;
	private TextView title;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private LinearLayout edit;
	private LinearLayout delete;
	private Button skin_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myskin);
		init();
	}

	private void init() {
		skin_edit = (Button) findViewById(R.id.skin_edit);
		skin_edit.setOnClickListener(this);
		edit = (LinearLayout) findViewById(R.id.edit);
		delete = (LinearLayout) findViewById(R.id.delete);
		edit.setOnClickListener(this);
		delete.setOnClickListener(this);
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		content = (TextView) findViewById(R.id.content);
		title = (TextView) findViewById(R.id.title);
		menu_layout = (LinearLayout) findViewById(R.id.menu_layout);
		// content.setText(MyUtils.getSkintypeText(new SharedpreferencesUtil(
		// getApplicationContext()).getSkintype()));
		// title.setText("您的肌肤类型是：" + sharedpreferencesUtil.getSkintypeStr()
		// + "肌肤性肌肤");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.menu:
			if (menu_layout.getVisibility() == View.INVISIBLE) {
				menu_layout.setVisibility(View.VISIBLE);
			} else if (menu_layout.getVisibility() == View.VISIBLE) {
				menu_layout.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.edit:
			new SharedpreferencesUtil(getApplicationContext())
					.saveIsTesting(true);
			Intent intent = new Intent(MySkinActivity.this,
					Starta1Activity.class);
			startActivity(intent);
			break;
		case R.id.delete:
			this.finish();
			new SharedpreferencesUtil(getApplicationContext())
					.saveIsTest(false);
			break;
		case R.id.skin_edit:
			startActivity(new Intent(MySkinActivity.this, Starta1Activity.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		content.setText("\b\b\b\b"
				+ MyUtils.getSkintypeText(new SharedpreferencesUtil(
						getApplicationContext()).getSkintype()));
		title.setText("您的肌肤类型是：" + sharedpreferencesUtil.getSkintypeStr()
				+ "肌肤性肌肤");
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
